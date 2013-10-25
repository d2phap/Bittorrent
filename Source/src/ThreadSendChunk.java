
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author OPTIMUS
 */
public class ThreadSendChunk extends Thread {

    //public int port = 1;
    public PeerInfo peer;
    private byte[] buffer;
    private DatagramSocket socket;
    private DatagramPacket sendPacket, rcvPacket;

    public boolean send() { // dùng để send trở lại
        try {
            //socket = new DatagramSocket(Bittorent_Like.portlisten + port);
            socket = new DatagramSocket(); // random tự lấy port 
            buffer = new byte[1024];
            
            sendPacket = new DatagramPacket("OK".getBytes(), "OK".getBytes().length, peer.getIpAddresss(), peer.getPort());
            rcvPacket = new DatagramPacket(buffer, buffer.length);
            socket.send(sendPacket); //gui ok

            socket.receive(rcvPacket); // nhan tenfile_sochunk

            String rc = new String(rcvPacket.getData(), 0, rcvPacket.getLength());

            String[] str = rc.split("_");
            int countChunk = Integer.parseInt(str[str.length - 1]); // số lượng chunk
            String fileName = rc.substring(0, rc.lastIndexOf("_")); //Lay ten file
            

            ThongTinTapTin file = new ThongTinTapTin();
            file.setTenfile(fileName);

            //Doc file chunk thanh mang bytes[]
            byte[] data = file.readchunk(countChunk); // chứa 512kb đầy đủ
            int lengthData = data.length;

            byte[] b; //mang data se gui di
            int n = 0; //Vi tri cua data da gui
            
            //Thực hiện gửi dữ liệu
            //Cho đến khi hết dữ liệu
            while (lengthData != 0) {
                
                //Nếu đây là lần gửi cuối cùng
                if (lengthData < 1024) {
                    b = new byte[data.length - n * 1024];
                    lengthData = 0; // để thoát khỏi while
                } 
                //Cắt dữ liệu thành từng gói 1024
                else {
                    b = new byte[1024];
                    lengthData = lengthData - 1024; // mỗi lần trừ đi đúng 512kb
                }
                
                //Cắt dữ liệu thành từng gói 1024
                for (int i = 0; i < b.length; i++) {
                    b[i] = data[i + n * 1024];
                }

                n++;

                //Đóng gói dữ liệu
                sendPacket = new DatagramPacket(b, b.length, rcvPacket.getAddress(), rcvPacket.getPort());
                socket.send(sendPacket); // gui 1024  byte
                LogFile.Write("Lan gui #" + n + ": " + b.length + 
                        "bytes, dia chi = " + rcvPacket.getAddress() + 
                        "port = " + rcvPacket.getPort());
                
                //Nhận dữ liệu phản hồi
                socket.receive(rcvPacket); // nhận ok
            }
            //Gửi tín hiệu kết thúc
            sendPacket = new DatagramPacket("END".getBytes(), "END".getBytes().length, rcvPacket.getAddress(), rcvPacket.getPort());
            socket.send(sendPacket);
            LogFile.Write("Lan gui END" + ": " + 
                        "dia chi = " + rcvPacket.getAddress() + 
                        "port = " + rcvPacket.getPort());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return true;
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

    public void run() {
        send();
    }
}
