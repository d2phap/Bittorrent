
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Duong Dieu Phap
 */
public class ThreadDownloadTorrent extends Thread implements EventListener {

    private transient Vector listeners;
    private volatile boolean isRunning = true;
    private int numberFinished = 0;
    private int sochunk = 0;
    public File torrentFile = null;
    public PeerInfo peer = null;
    private List<ThreadDownloadChunk> listThreadDownload = new ArrayList<>();

    public void run() {
        StartDownload();
    }

    /**
     * Đăng ký sự kiện
     */
    synchronized public void addCustomListener(CustomEventListener l) {
        if (listeners == null) {
            listeners = new Vector();
        }
        listeners.addElement(l);
    }

    /**
     * Xoá sự kiện
     */
    synchronized public void removeCustomListener(CustomEventListener l) {
        if (listeners == null) {
            listeners = new Vector();
        }
        listeners.removeElement(l);
    }

    /**
     * Tạm dừng tiến trình
     *
     * @throws InterruptedException
     */
    public void pauseThread() throws InterruptedException {
        isRunning = false;
        
        //Tam dung tat ca tien trinh download chunk
        for (int j = 0; j < listThreadDownload.size(); j++) 
        {
            try 
            {
                listThreadDownload.get(j).pauseThread();
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadDownloadTorrent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }

    /**
     * Phục hồi tiến trình
     */
    public void resumeThread() {
        isRunning = true;
        
        //Phuc hoi cac tien trinh download chunk
        for (int j = 0; j < listThreadDownload.size(); j++) 
        {
            listThreadDownload.get(j).resumeThread();
        }
    }

    private void StartDownload() {

        if (torrentFile == null || peer == null) {
            return;
        }

        FileInputStream fin = null;
        sochunk = 0;
        try {
            fin = new FileInputStream(torrentFile);
            Scanner input = new Scanner(fin);
            sochunk = input.nextInt();
        } catch (Exception ex) {
            return;
        }

        String ten = torrentFile.getName().substring(0, torrentFile.getName().lastIndexOf(".torrent"));

        final ThongTinTapTin fi = new ThongTinTapTin();
        fi.setTenfile(ten);
        fi.setSochunk(sochunk);

        // if we have no listeners, do nothing...
        if (listeners != null && !listeners.isEmpty()) {
            // make a copy of the listener list in case
            //   anyone adds/removes listeners
            final Vector targets;
            synchronized (this) {
                targets = (Vector) listeners.clone();
            }
            // create the event object to send
            final CustomEventObject event = new CustomEventObject(0, sochunk);
            event._object1 = ten; //Lưu tên tập tin
            

            //Phát sinh sự kiện START
            Enumeration e = targets.elements();
            while (e.hasMoreElements()) {
                CustomEventListener l = (CustomEventListener) e.nextElement();
                l.onStart(event);
            }
            
            //Gui broadcast tim kiem vi tri chunk
            ThreadSendRequest gui = new ThreadSendRequest();
            gui.peer = Bittorrent.peer;
            gui.func = ThreadSendRequest.TenPhuongThuc.kiemTraFileChunk;
            gui.tenFile = ten;
            gui.soChunk = sochunk;
            gui.start();
            
            try {
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ThreadDownloadTorrent.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            //Danh sach cac thread download chunk
            //Nham quan ly viec tam dung download
            listThreadDownload = new ArrayList<>();
            
            //Lan luot tao thread de tai cac chunk dong thoi
            for (int i = 0; i < sochunk; i++) {
                
                File f = new File(ThongTinChunk.duongDanChunk + ten + "/" + ten + "_" + (i + 1 + ".chunk"));
                if (!f.exists()) { //Neu tap tin chunk ko ton tai thi download về
                    
                    // lúc này yêu cầu gửi
                    ThreadDownloadChunk down = new ThreadDownloadChunk();
                    down.file = fi;
                    down.peer = peer;
                    down.thuTuChunk = i + 1;
                    down.addCustomListener(new CustomEventListener() {

                        @Override
                        public void onStart(CustomEventObject e) {
                        }

                        @Override
                        public void onOccur(CustomEventObject e) {
                            //Phát sinh sự kiện OCCUR
                            event._value = numberFinished;
                            event._object1 = fi.getTenfile();
                            event._object2 = e._object1; //Ten chunk dang down
                            Enumeration ev = targets.elements();
                            while (ev.hasMoreElements()) {
                                CustomEventListener l = (CustomEventListener) ev.nextElement();
                                l.onOccur(event);
                            }
                        }

                        @Override
                        public void onFinish(CustomEventObject e) {
                            numberFinished++;
                            
                            if(numberFinished == sochunk)
                            {
                                //Phát sinh sự kiện FINISH
                                Enumeration ev2 = targets.elements();
                                while (ev2.hasMoreElements()) {
                                    CustomEventListener l = (CustomEventListener) ev2.nextElement();
                                    l.onFinish(event);
                                }
                            }
                        }
                        
                        @Override
                        public void onError(CustomEventObject e) {
                            numberFinished++;
                            
                            //Phát sinh sự kiện ERROR
                            event._errorMessage = e._errorMessage;
                            Enumeration ev2 = targets.elements();
                            while (ev2.hasMoreElements()) {
                                CustomEventListener l = (CustomEventListener) ev2.nextElement();
                                l.onError(event);
                            }
                            
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                Logger.getLogger(ThreadDownloadTorrent.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });
                    
                    listThreadDownload.add(down);
                    
                    down.start();
                }
                else
                {
                    //Neu tap tin chunk đã tồn tại thì tăng kq
                    numberFinished++;
                    
                    //Phát sinh sự kiện OCCUR
                    event._value = numberFinished;
                    event._object1 = fi.getTenfile();
                    Enumeration ev = targets.elements();
                    while (ev.hasMoreElements()) {
                        CustomEventListener l = (CustomEventListener) ev.nextElement();
                        l.onOccur(event);
                    }
                    
                    if (numberFinished == sochunk) {
                        //Phát sinh sự kiện FINISH
                        Enumeration ev2 = targets.elements();
                        while (ev2.hasMoreElements()) {
                            CustomEventListener l = (CustomEventListener) ev2.nextElement();
                            l.onFinish(event);
                        }
                    }
                }//end if file có tồn tại ko?
            }//end for
            
        }//end if

    }
}
