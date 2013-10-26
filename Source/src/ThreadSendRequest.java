
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
public class ThreadSendRequest extends Thread {

    public PeerInfo peer;
    public int port = 100;
    private byte[] buffer;
    private DatagramSocket socket;
    private DatagramPacket sendPacket, rcvPacket;
    public TenPhuongThuc func = TenPhuongThuc.gui;
    
    /**
     * Sử dụng cho chức năng kiemTraFileChunk
     */
    public String tenFile = "";
    /**
     * Sử dụng cho chức năng kiemTraFileChunk
     */
    public int soChunk = 0;
    
    public enum TenPhuongThuc
    {
        gui,
        kiemTraFileChunk
    }

    public void run() {
        if(func == TenPhuongThuc.gui)
        {
            gui();
        }
        else if(func == TenPhuongThuc.kiemTraFileChunk)
        {
            kiemTraFileChunk(tenFile, soChunk);
        }
    }
    
    public void gui() {
        try {

            socket = new DatagramSocket(Bittorent.portlisten + port);
            socket.setSoTimeout(3000);

            for (int i = 0; i < peer.countListPeer(); i++) {
                
                String dc = peer.getPeerItem(i).getIpAddresss().getHostAddress();
                sendPacket = new DatagramPacket("ONL".getBytes(), "ONL".getBytes().length, 
                        peer.getPeerItem(i).getIpAddresss(), Bittorent.portlisten);
                
                try {
                    socket.send(sendPacket);
                    buffer = new byte[1024];
                    rcvPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(rcvPacket);
                    
                    String gt = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
                    
                    if (gt != null) {
                        peer.getPeerItem(i).setStatus(true);
                        LogFile.Write(peer.getPeerItem(i).getIpAddresss() + " ONLINE");
                    }
                    
                } catch (IOException ex) {

                    LogFile.Write(peer.getPeerItem(i).getIpAddresss() + " OFFLINE");
                }
            }
            
            socket.close();
        } catch (SocketException ex) {
            socket.close();
            Logger.getLogger(ThreadSendRequest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void kiemTraFileChunk(String tenFile, int soLuongChunkToiDa) {
        try {

            socket = new DatagramSocket(Bittorent.portlisten + port);
            socket.setSoTimeout(3000);

            for (int i = 0; i < peer.countListPeer(); i++) {
                
                String duLieuGui = "GET_CHUNK=>" + tenFile + "=>" + soLuongChunkToiDa;
                String dc = peer.getPeerItem(i).getIpAddresss().getHostAddress();
                sendPacket = new DatagramPacket(duLieuGui.getBytes(), duLieuGui.getBytes().length, 
                        peer.getPeerItem(i).getIpAddresss(), Bittorent.portlisten);
                
                try {
                    socket.send(sendPacket);
                    LogFile.Write("Gui yeu cau vi tri file chunks: " + duLieuGui);
                    
                    buffer = new byte[2048];
                    rcvPacket = new DatagramPacket(buffer, buffer.length);
                    socket.receive(rcvPacket);
                    
                    
                    //Lay so file chunk co trong peer
                    //Dinh dang: 192.168.5.20=>1,3,4,
                    String gt = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
                    LogFile.Write("Nhan vi tri file chunks: " + gt);
                    
                    if (gt != null) {
                        peer.getPeerItem(i).setStatus(true);
                        
                        //Lay dia chi IP
                        String _ip = gt.split("=>")[0];
                        
                        //Lay danh sach chunk id
                        String[] _chunks = gt.split("=>")[1].split(",");
                        
                        //Lay danh sach chunk cua _ip
                        List<Integer> dsChunk = Bittorent.viTriChunk.get(_ip);
                        
                        //Khong ton tai IP _ip trong danh sach
                        if(dsChunk == null)
                        {
                            //Cap nhat danh sach chunk ID
                            dsChunk = new ArrayList<>();                            
                            for (int j = 0; j < _chunks.length; j++) 
                            {
                                try {
                                    int chunkID = Integer.parseInt(_chunks[j]);
                                    dsChunk.add(chunkID);
                                } catch (Exception ex) {
                                }

                            }
                            
                            //Them moi
                            Bittorent.viTriChunk.put(_ip, dsChunk);
                        }
                        //Da ton tai IP _ip
                        else
                        {
                            //Cap nhat danh sach chunk ID
                            dsChunk = new ArrayList<>();                            
                            for (int j = 0; j < _chunks.length; j++) 
                            {
                                int chunkID = Integer.parseInt(_chunks[j]);
                                dsChunk.add(chunkID);
                            }
                            
                            Bittorent.viTriChunk.remove(_ip);
                            //Them moi
                            Bittorent.viTriChunk.put(_ip, dsChunk);
                        }
                        
                    }
                    
                } catch (IOException ex) {
                }
            }
            
            socket.close();
        } catch (SocketException ex) {
            socket.close();
        }
    }
    
    
    private void sendPacket() {
        int timeOut = 500;
        try {
            int flag = 1;
            do {
                socket.setSoTimeout(timeOut);
                socket.send(sendPacket);
                int n = 1;
                while (n < 5) {
                    try {
                        rcvPacket = new DatagramPacket(buffer, buffer.length);
                        socket.receive(rcvPacket);
                        timeOut -= 100;
                        flag = 6;
                        break;
                    } catch (SocketTimeoutException e) {
                        timeOut += 100;
                        socket.setSoTimeout(timeOut);
                        n++;
                        continue;
                    }
                }
                timeOut += 200;
                flag++;
            } while (flag < 5);
            if (flag == 5) {
                socket.close();
                //this.stop();
            }
        } catch (Exception ex) {
            socket.close();
            //this.stop();
        }
    }
}