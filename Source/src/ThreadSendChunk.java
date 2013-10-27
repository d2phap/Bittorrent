
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketTimeoutException;
import java.nio.ByteBuffer;

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
            
            //Tinh kich thuoc header = 52 bytes
            int soByteHeader = 52;

            //So byte cua chunk duoc gui
            int soByteGui = 1024 - soByteHeader;
            

            //Thực hiện gửi dữ liệu
            //Cho đến khi hết dữ liệu
            while (lengthData != 0) {

                //Nếu đây là lần gửi cuối cùng
                if (lengthData < soByteGui) {
                    b = new byte[data.length - (n * soByteGui) + 52];
                    lengthData = 0; // để thoát khỏi while
                } 
                //Cắt dữ liệu thành từng gói 1024
                else {
                    b = new byte[1024];
                    lengthData = lengthData - soByteGui; // mỗi lần trừ đi đúng 1024 byte - 52 bytes header
                }
                
                
                //Them 972 byte file chunk vao goi du lieu
                for (int i = soByteHeader; i < b.length; i++) {
                    b[i] = data[i + (n * soByteGui) - soByteHeader];
                }
                
                byte[] hashData = new byte[b.length - soByteHeader];
                System.arraycopy(b, soByteHeader, hashData, 0, hashData.length);
                
                //Chen 52 bytes header vao goi tin 
                //4 bytes PKG LENGTH
                byte[] pkgLength = ByteBuffer.allocate(4).putInt(b.length).array();

                //40 bytes CHECKSUM
                byte[] checksum = ThongTinTapTin.generateHashCode(hashData).getBytes();
                
                //4 bytes SEQ: Vi tri byte dau tien
                byte[] seq = ByteBuffer.allocate(4).putInt(n * soByteGui).array();

                //4 bytes ACK: Vi tri byte cuoi cung da nhan, = -1 neu chua nhan
                byte[] ack = ByteBuffer.allocate(4).putInt((n * soByteGui) - 1).array();

                
                //Them header vao goi tin
                System.arraycopy(pkgLength, 0, b, 0, pkgLength.length);
                System.arraycopy(checksum, 0, b, pkgLength.length, checksum.length);
                System.arraycopy(seq, 0, b, pkgLength.length + checksum.length, seq.length);
                System.arraycopy(ack, 0, b, pkgLength.length + checksum.length + seq.length, ack.length);
                

                n++;

                //Đóng gói dữ liệu
                sendPacket = new DatagramPacket(b, b.length, rcvPacket.getAddress(), rcvPacket.getPort());
                socket.send(sendPacket); // gui 1024 bytes
                LogFile.Write("Lan gui #" + n + ": " + b.length
                        + " bytes, IP = " + rcvPacket.getAddress()
                        + ", port = " + rcvPacket.getPort());

                //Nhận dữ liệu phản hồi
                socket.receive(rcvPacket); // nhận ok
            }
            //Gửi tín hiệu kết thúc
            sendPacket = new DatagramPacket("END".getBytes(), "END".getBytes().length, rcvPacket.getAddress(), rcvPacket.getPort());
            socket.send(sendPacket);
            LogFile.Write("Lan gui #END" + ": "
                    + " IP = " + rcvPacket.getAddress()
                    + ", port = " + rcvPacket.getPort());

        } catch (Exception e) {
        }

        return true;
    }

    public void run() {
        send();
    }
}
