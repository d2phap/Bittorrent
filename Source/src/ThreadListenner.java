
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author OPTIMUS
 */
public class ThreadListenner extends Thread {

    public PeerInfo peer;
    public int port = 100;
    private byte[] buffer;
    private DatagramSocket socket;
    private DatagramPacket sendPacket, rcvPacket;
    private String yc;

    /*
     * dùng để nghe toàn bộ yêu cầu các máy vs
     */
    public void nghe() {
        try {
            socket = new DatagramSocket(Bittorent.portlisten);
            buffer = new byte[1024];
            rcvPacket = new DatagramPacket(buffer, buffer.length);
            
            do {
                try {
                    socket.receive(rcvPacket);
                    yc = new String(rcvPacket.getData(), 0, rcvPacket.getLength());
                    
                    //Neu ONLINE
                    if (yc.compareTo("ONL") == 0) {
                        socket.setSoTimeout(5000); // quá 5s mà không ai trả lời hủy
                        
                        for (int i = 0; i < Bittorent.peer.countListPeer(); i++) {
                            //Thong bao Peer(i) da san sang ket noi
                            if (Bittorent.peer.getPeerItem(i).getIpAddresss().getHostAddress()
                                    .compareTo(rcvPacket.getAddress().getHostAddress()) == 0) {
                                
                                Bittorent.peer.getPeerItem(i).setStatus(true);
                                break;
                            }
                        }
                        
                        //Gui tin hieu ket thuc
                        sendPacket = new DatagramPacket("END".getBytes(), "END".getBytes().length, 
                                rcvPacket.getAddress(), rcvPacket.getPort());
                        
                        try {
                            socket.send(sendPacket);
                        } catch (IOException ex) {
                            Logger.getLogger(SendRequest.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } 
                    //Neu nhan duoc tin hieu yeu cau Down file
                    else if (yc.compareTo("Down_File") == 0) {
                        ThreadSendChunk send = new ThreadSendChunk();
                        send.peer = new PeerInfo(1, rcvPacket.getAddress().getHostAddress(), rcvPacket.getPort(), true);
                        send.start();
                    }

                } catch (IOException ex) {

                    Logger.getLogger(ThreadListenner.class.getName()).log(Level.SEVERE, null, ex);
                }


            } while (true);
        } catch (SocketException ex) {

            Logger.getLogger(ThreadListenner.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void run() {
        nghe();
    }
}
