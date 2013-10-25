
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * dùng lưu thông tin các máy trong file node
 *
 * @author OPTIMUS
 */
public class PeerInfo {

    private int id;
    private InetAddress ipAdrress;
    private int port;
    private boolean status;
    private ArrayList<PeerInfo> listPeer;

    public PeerInfo() {
        try {
            listPeer = new ArrayList();
            loadListPeer();
        } catch (Exception ex) {
        }

    }

    public PeerInfo(int _id, String _ip, int _port, boolean _status) {
        this.id = _id;
        setIpAddress(_ip);
        this.port = _port;
        this.status = _status;
    }

    public PeerInfo(PeerInfo peer) {
        this.id = peer.id;
        this.ipAdrress = peer.ipAdrress;
        this.port = peer.port;
        this.status = peer.status;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return the ipaddresss
     */
    public InetAddress getIpAddresss() {
        return ipAdrress;
    }

    /**
     * @param ipAddresss the ipaddresss to set
     */
    public void setIpAddresss(InetAddress ipAddresss) {
        this.ipAdrress = ipAddresss;
    }

    public void setIpAddress(String name) {
        try {
            this.ipAdrress = InetAddress.getByName(name);
        } catch (UnknownHostException ex) {
            Logger.getLogger(PeerInfo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * @return the port
     */
    public int getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }
    

    /**
     * @return the lspeer
     */
    public PeerInfo getPeerItem(int index) {
        return listPeer.get(index);
    }

    public int countListPeer() {
        return listPeer.size();
    }

    /**
     * @param lspeer the lspeer to set
     */
    public void addPeer(PeerInfo peer) {
        listPeer.add(peer);
    }

    /**
     * @return the status
     */
    public boolean getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(boolean status) {
        this.status = status;
    }

    //load peer
    public void loadListPeer() throws IOException {
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("./Map/Nodes.map");
            Scanner input = new Scanner(fis);
            int n = input.nextInt();
            for (int i = 0; i < n; i++) {
                int _id = input.nextInt();
                String _ip = input.next();
                int _port = input.nextInt();
                if (_ip.compareTo(InetAddress.getLocalHost().getHostAddress()) == 0) {
                    this.id = _id;
                    setIpAddress(_ip);
                    this.port = _port;
                    this.status = false;
                } else {
                    addPeer(new PeerInfo(_id, _ip, _port, false));
                }
            }
        } catch (IOException ex) {
        } finally {
            fis.close();
        }
    }
}
