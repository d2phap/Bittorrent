
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author OPTIMUS dùng để yêu cầu startDownload 1 chunk
 */
public class ThreadDownloadChunk extends Thread {

    public ThongTinTapTin file;
    public PeerInfo peer;
    public int thuTuChunk; // thứ tự chunk đang tải về
    private byte[] buffer;
    private int port = 1;
    private ThongTinChunk chunkInfo;
    private DatagramSocket socket;
    private DatagramPacket sendPacket, rcvPacket;
    private volatile boolean isRunning = true;
    private transient Vector listeners;

    public boolean startDownload() {
        try {
            String tenChunk = file.getTenfile() + "_" + thuTuChunk;

            // if we have no listeners, do nothing...
            if (listeners != null && !listeners.isEmpty()) {
                Vector targets;
                synchronized (this) {
                    targets = (Vector) listeners.clone();
                }
                // create the event object to send
                CustomEventObject event = new CustomEventObject();
                event._object1 = tenChunk + ".chunk"; //Lưu tên tập tin


                //Phát sinh sự kiện START
                Enumeration e = targets.elements();
                while (e.hasMoreElements()) {
                    CustomEventListener l = (CustomEventListener) e.nextElement();
                    l.onStart(event);
                }

                /////////////////////////////////////////////
                //TOI UU HOA VIEC LUA CHON PEER DE TAI CHUNK

                //Danh sach peer chua chunk
                List<Integer> dsPeer = new ArrayList<>();

                //Kiem tra chunk dang down o peer nao
                for (int i = 0; i < Bittorrent.danhSachPeer.size(); i++) {

                    //Neu chunk nam trong peer(i)
                    if (Bittorrent.danhSachPeer.get(i).getDanhSachChunk().indexOf(thuTuChunk) != -1) {
                        //Peer(i) chua chunk
                        dsPeer.add(i);
                    }
                }

                //Neu khong co peer nao chua chunk nay
                if (dsPeer.size() <= 0) {
                    LogFile.Write("Chunk #" + thuTuChunk + ": Khong tim thay!");

                    //Phát sinh sự kiện FINISH
                    e = targets.elements();
                    event._errorMessage = "Chunk '" + tenChunk + ".chunk': không tìm thấy";
                    while (e.hasMoreElements()) {
                        CustomEventListener l = (CustomEventListener) e.nextElement();
                        l.onError(event);
                    }

                    return false;
                }

                //Tim peer ranh roi nhat
                int minPeer_soChunks = dsPeer.get(0);

                //Dieu phoi downloading chunk
                //Tim peer nao dang down it chunk nhat
                for (int i = 1; i < dsPeer.size(); i++) {
                    int vPeer_soChunk = dsPeer.get(i);

                    if (Bittorrent.danhSachPeer.get(minPeer_soChunks).getDanhSachChunkDangDown().size()
                            > Bittorrent.danhSachPeer.get(vPeer_soChunk).getDanhSachChunkDangDown().size()) {
                        minPeer_soChunks = vPeer_soChunk;
                    }
                }
                ////////////////////////////////////////////

                try {

                    String _ip = Bittorrent.danhSachPeer.get(minPeer_soChunks).getIpAddresss().toString();
                    int soByteHeader = 52;

                    buffer = new byte[1024];

                    socket = new DatagramSocket();
                    socket.setSoTimeout(5000);

                    sendPacket = new DatagramPacket("Down_File".getBytes(), "Down_File".getBytes().length,
                            Bittorrent.danhSachPeer.get(minPeer_soChunks).getIpAddresss(), Bittorrent.portlisten);

                    socket.send(sendPacket); //gui Yêu Cầu down_file 
                    LogFile.Write("Gui yeu cau Down_File toi IP: " + _ip);

                    rcvPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(rcvPacket); // Nhận ok
                    LogFile.Write("Nhan OK tu IP: " + rcvPacket.getAddress().toString());

                    String rc = new String(rcvPacket.getData(), 0, rcvPacket.getLength());

                    sendPacket = new DatagramPacket(tenChunk.getBytes(), tenChunk.getBytes().length,
                            rcvPacket.getAddress(), rcvPacket.getPort());

                    socket.send(sendPacket); //gui tenfile_sochunk
                    LogFile.Write("Gui ten tap tin chunk: " + tenChunk + " den IP: " + _ip);

                    socket.receive(rcvPacket); // nhận 1024 byte
                    LogFile.Write("Nhan 'OK' tu IP: " + rcvPacket.getAddress().toString());

                    sendPacket = new DatagramPacket("OK".getBytes(), "OK".getBytes().length,
                            rcvPacket.getAddress(), rcvPacket.getPort());
                    // kiểm tra xem có file đó ko
                    rc = new String(rcvPacket.getData(), 0, rcvPacket.getLength());

                    byte[] data = new byte[(int) file.kichThuocChunk];// lưu chunk có kích thước 512 kb
                    int n = 0; // số lần startDownload
                    int size = 0;

                    while (rc.compareTo("END") != 0) {

                        //Tạm dừng nếu nhận lệnh isRunning = false
                        while (!isRunning) {
                            //Lặp cho đến khi nhận được lệnh isRunning = true
                        }


                        //Phát sinh sự kiện OCCUR
                        e = targets.elements();
                        while (e.hasMoreElements()) {
                            CustomEventListener l = (CustomEventListener) e.nextElement();
                            l.onOccur(event);
                        }


                        //Chen 52 bytes header vao goi tin 
                        //4 bytes PKG LENGTH
                        byte[] pkgLength = new byte[4];
                        System.arraycopy(rcvPacket.getData(), 0, pkgLength, 0, pkgLength.length);

                        //40 bytes CHECKSUM
                        byte[] checksum = new byte[40];
                        System.arraycopy(rcvPacket.getData(), pkgLength.length, checksum, 0, checksum.length);

                        //4 bytes SEQ: Vi tri byte dau tien
                        byte[] seq = new byte[4];
                        System.arraycopy(rcvPacket.getData(), pkgLength.length + checksum.length, seq, 0, seq.length);

                        //4 bytes ACK: Vi tri byte cuoi cung da nhan, = -1 neu chua nhan
                        byte[] ack = new byte[4];
                        System.arraycopy(rcvPacket.getData(), pkgLength.length + checksum.length + seq.length, ack, 0, ack.length);

                        int kichThuocGoiTin = ByteBuffer.wrap(pkgLength).getInt();
                        String maHash = new String(checksum);
                        int seqNumber = ByteBuffer.wrap(seq).getInt();
                        int ackNumber = ByteBuffer.wrap(ack).getInt();

                        //Lay kich thuoc goi tin
                        int numbyte = kichThuocGoiTin - soByteHeader;
                        
                        byte[] hashData = new byte[numbyte];
                        System.arraycopy(rcvPacket.getData(), soByteHeader, hashData, 0, hashData.length);
                        
                        LogFile.Write("Kiem tra lan nhan #" + n + ": "
                                + "pkgSize = " + kichThuocGoiTin + ", "
                                + "Checksum = " + maHash + ", "
                                + "SHA1 = " + ThongTinTapTin.generateHashCode(hashData) + ", "
                                + "SEQ = " + seqNumber + ", "
                                + "ACK = " + ackNumber);


                        //copy du lieu nhan dc vao mang data
                        System.arraycopy(rcvPacket.getData(), soByteHeader, data, n * (1024 - soByteHeader), numbyte);
                        size += numbyte; //tong kich thuoc da nhan
                        n++;

                        socket.send(sendPacket); //Gui yeu cau da nhan 'OK'
                        socket.receive(rcvPacket); //Nhan du lieu

                        LogFile.Write("Lan nhan #" + n + ": Chunk #" + thuTuChunk + ", so byte nhan = "
                                + numbyte + ", tong byte da nhan = " + size);

                        rc = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
                    }

                    LogFile.Write("Lan nhan #END: Chunk #" + thuTuChunk);

                    byte[] newData = new byte[size];
                    System.arraycopy(data, 0, newData, 0, size);
                    boolean ghi = file.ghichunk(thuTuChunk, newData);


                    if (ghi == true) {
                        //Phát sinh sự kiện FINISH
                        e = targets.elements();
                        while (e.hasMoreElements()) {
                            CustomEventListener l = (CustomEventListener) e.nextElement();
                            l.onFinish(event);
                        }

                    }

                    socket.close();

                } catch (Exception ex) {
                    LogFile.Write("Chunk #" + thuTuChunk + ": Time out!");

                    //Phát sinh sự kiện FINISH
                    e = targets.elements();
                    event._errorMessage = "'" + tenChunk + ".chunk': time out. ";
                    while (e.hasMoreElements()) {
                        CustomEventListener l = (CustomEventListener) e.nextElement();
                        l.onError(event);
                    }
                    return false;
                }

            }

        } catch (Exception e) {
        }
        return true;
    }

    public void run() {
        startDownload();
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
     * Tạm dừng tien trình
     *
     * @throws InterruptedException
     */
    public void pauseThread() throws InterruptedException {
        isRunning = false;
    }

    /**
     * Phục hồi tiến trình
     */
    public void resumeThread() {
        isRunning = true;
    }
}
