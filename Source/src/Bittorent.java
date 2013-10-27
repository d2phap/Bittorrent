
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;

/*
 * To change this template, fc Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author OPTIMUS
 */
public class Bittorent extends javax.swing.JFrame {

    /**
     * Creates new form Bittorent
     */
    public static PeerInfo peer;
    public static int portlisten = 1991;
    public static int portdow = 1001;
    public static ThreadListenner nghe;
    public static ThreadSendRequest gui;
    public static File f;
    private ThreadDownloadTorrent dlTorrent;
    private int isPause = 0;
    /**
     * Danh sách lưu thông tin các peer
     */
    public static ArrayList<PeerInfo> danhSachPeer = new ArrayList<>();
    public static String logString = "";
    

    public Bittorent() {

        LogFile.Write("#START_PROGRAM-----------------------------------------------------");
        
        File dir = new File("./ghepfile");
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        
        dir = new File("./torrent");
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        
        dir = new File("./Chunk");
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        
        dir = new File("./Map");
        if(!dir.exists())
        {
            dir.mkdirs();
        }
        
        //Load peer hien tai
        peer = new PeerInfo();
        //Load danh sach peer trong file Map
        danhSachPeer = PeerInfo.loadListPeer();
        nghe = new ThreadListenner();
        nghe.start();
        
        gui = new ThreadSendRequest();
        gui.peer = peer;
        gui.func = ThreadSendRequest.TenPhuongThuc.gui;
        gui.start();

        initComponents();
        txtCatTapTin.setEditable(false);
        txtNoiFile.setEditable(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel2 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        btnCatTapTin = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        txtCatTapTin = new javax.swing.JTextField();
        btnBrowseDownloadChunk = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        txtNoiFile = new javax.swing.JTextField();
        btnChonFileNoi = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        btnNoiTapTin = new javax.swing.JButton();
        txtTaiTorrent = new javax.swing.JTextField();
        cmbChunk = new javax.swing.JComboBox();
        btnTaiTatCa = new javax.swing.JButton();
        btnTaiChunkDangChon = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        btnThongTin = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        lblStatus = new javax.swing.JLabel();
        progStatus = new javax.swing.JProgressBar();
        btnXemLog = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Torrent downloader");
        setBackground(new java.awt.Color(255, 255, 255));
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        jPanel2.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel4.setText("Nối tập tin:");

        btnCatTapTin.setText("Cắt tập tin");
        btnCatTapTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCatTapTinActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel2.setText("Cắt tập tin:");

        jButton4.setText("Chọn tập tin");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonCatTapTinActionPerformed(evt);
            }
        });

        txtCatTapTin.setName(""); // NOI18N

        btnBrowseDownloadChunk.setText("Chọn tập tin torrent");
        btnBrowseDownloadChunk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBrowseDownloadChunkActionPerformed(evt);
            }
        });

        jLabel5.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        jLabel5.setText("Tải tập tin:");

        txtNoiFile.setName(""); // NOI18N

        btnChonFileNoi.setText("Chọn tập tin");
        btnChonFileNoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChonFileNoiActionPerformed(evt);
            }
        });

        jLabel3.setText("Danh sách chunk:");

        btnNoiTapTin.setText("Nối tập tin");
        btnNoiTapTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoiTapTinActionPerformed(evt);
            }
        });

        txtTaiTorrent.setEditable(false);
        txtTaiTorrent.setText("##");

        btnTaiTatCa.setText("Tải tất cả");
        btnTaiTatCa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiTatCaActionPerformed(evt);
            }
        });

        btnTaiChunkDangChon.setText("Tải chunk đang chọn");
        btnTaiChunkDangChon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaiChunkActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        jLabel1.setText("Torrent downloader");

        btnThongTin.setText("?");
        btnThongTin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThongTinActionPerformed(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(230, 230, 230));

        lblStatus.setText("##");

        btnXemLog.setText("Xem log");
        btnXemLog.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXemLogActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblStatus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnXemLog)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(18, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnXemLog)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(progStatus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(163, 163, 163)
                        .addComponent(btnThongTin))
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnNoiTapTin, javax.swing.GroupLayout.PREFERRED_SIZE, 108, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel2)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addComponent(txtCatTapTin, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(jButton4))
                        .addComponent(jLabel4)
                        .addComponent(btnCatTapTin, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createSequentialGroup()
                            .addGap(18, 18, 18)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(btnTaiChunkDangChon)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(btnTaiTatCa, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(cmbChunk, javax.swing.GroupLayout.PREFERRED_SIZE, 299, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtTaiTorrent, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnBrowseDownloadChunk))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txtNoiFile, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnChonFileNoi)))))
                .addContainerGap(12, Short.MAX_VALUE))
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(btnThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCatTapTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnCatTapTin)
                .addGap(18, 18, 18)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtNoiFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnChonFileNoi))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNoiTapTin)
                .addGap(32, 32, 32)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(btnBrowseDownloadChunk)
                    .addComponent(txtTaiTorrent, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(cmbChunk, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnTaiChunkDangChon)
                    .addComponent(btnTaiTatCa))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 34, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        getContentPane().add(jPanel2);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        lblStatus.setText("");
    }//GEN-LAST:event_formWindowOpened

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        // TODO add your handling code here:
        LogFile.Write("#END_PROGRAM-------------------------------------------------------");
        LogFile.WriteToFile(Bittorent.logString);
    }//GEN-LAST:event_formWindowClosing

    private void btnThongTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThongTinActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(new JFrame(), "Thông tin nhóm thực hiện:\n\nBùi Bá Lộc - 1241363\nDương Diệu Pháp - 1241378");
        
        
        ThongTinTapTin file = new ThongTinTapTin();
        file.setTenfile("test.zip");
        String hash = file.getHashCode(ThongTinChunk.duongDanChunk + file.getTenfile() + 
                "/" + file.getTenfile() + "_" + 27 + ".chunk");
        //Chen header vao goi tin
        //2 bytes PKG LENGTH
        byte[] pkgLength = Integer.valueOf(1024).toString().getBytes();

        //2 bytes CHECKSUM
        byte[] checksum = hash.getBytes();

        //4 bytes SEQ: Vi tri byte dau tien
        byte[] seq = Integer.valueOf(1 * 1024).toString().getBytes();

        //4 bytes ACK: Vi tri byte cuoi cung da nhan, = -1 neu chua nhan
        byte[] ack = Integer.valueOf(1 * 1024 - 1).toString().getBytes();

        //Tinh kich thuoc header
        int soByteHeader = pkgLength.length + checksum.length + seq.length + ack.length;
        LogFile.Write(pkgLength.length + "_" + checksum.length + "_" + seq.length + "_" + ack.length);
    }//GEN-LAST:event_btnThongTinActionPerformed

    /*
     * startDownload chunk chọn trên combox
     */
    private void btnTaiChunkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiChunkActionPerformed
        // TODO add your handling code here:

        String chunk = cmbChunk.getSelectedItem().toString();
        ThreadDownloadChunk down = new ThreadDownloadChunk();
        ThongTinTapTin fo = new ThongTinTapTin();
        
        String tenfile = f.getName().substring(0, f.getName().length() - 8); // cắt bỏ đuôi .tottrent
        fo.setTenfile(tenfile);
        fo.setSochunk(ThongTinTapTin.readChunkNumberFromTorrentFile(f.getAbsolutePath()));
        down.file = fo;
        down.peer = peer;
        
        String[] mang = chunk.split("_");
        String tam = mang[mang.length - 1]; //lay phan tu _ cuoi cung
        tam = tam.substring(0, tam.length() - 6); //loc lay thu tu chunk
        down.thuTuChunk = Integer.parseInt(tam);
        
        down.addCustomListener(new CustomEventListener() {

            @Override
            public void onStart(CustomEventObject e) {
                lblStatus.setText("Đang tải tập tin " + e._object1);
                progStatus.setValue(0);
            }

            @Override
            public void onOccur(CustomEventObject e) {
                
            }

            @Override
            public void onFinish(CustomEventObject e) {
                lblStatus.setText("Tải '" + e._object1 + "' hoàn tất");
                progStatus.setValue(progStatus.getMaximum());
            }
            
            @Override
            public void onError(CustomEventObject e) {
                lblStatus.setText(e._errorMessage);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(Bittorent.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
        
        //Gui broadcast tim kiem vi tri chunk
        gui = new ThreadSendRequest();
        gui.peer = Bittorent.peer;
        gui.func = ThreadSendRequest.TenPhuongThuc.kiemTraFileChunk;
        gui.tenFile = tenfile;
        gui.soChunk = fo.getSochunk();
        lblStatus.setText("Đang tìm vị trí chunk...");
        gui.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            Logger.getLogger(Bittorent.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        down.start();
    }//GEN-LAST:event_btnTaiChunkActionPerformed

   /*
     * chọn file cần startDownload
     */
    private void btnTaiTatCaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaiTatCaActionPerformed
        // TODO add your handling code here:
        
        File torrentFile = new File(txtTaiTorrent.getText().trim());
        
        if (!torrentFile.exists()) {
            //Tap tin ko ton tai
            JOptionPane.showMessageDialog(new JPanel(), "Lỗi\n\nTập tin torrent không tồn tại!");
            isPause = 0;
            btnTaiTatCa.setText("Tải tất cả");
        } 
        else 
        {
            //IsPause = 0: Khong down
            //isPause = 1: Dang down
            //IsPause = 2: Tam Dung
            if(isPause == 1) //Neu dang download
            {
                //Tam dung
                isPause = 2;
                try {
                    dlTorrent.pauseThread();
                    btnTaiTatCa.setText("Phục hồi");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Bittorent.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                return;
            }
            else if(isPause == 2) //Neu dang Tam Dung
            {
                //Thi down tiep
                isPause = 1;
                dlTorrent.resumeThread();
                btnTaiTatCa.setText("Tạm dừng");
                
                return;
            }
            
            dlTorrent = new ThreadDownloadTorrent();
            dlTorrent.torrentFile = torrentFile;
            dlTorrent.peer = peer;
            dlTorrent.addCustomListener(new CustomEventListener() {

                @Override
                public void onStart(CustomEventObject e) {
                    progStatus.setMaximum(e._max);
                    progStatus.setMinimum(e._min);
                    progStatus.setValue(0);
                    lblStatus.setText("Đang tìm vị trí chunk cần tải...");
                    isPause = 1;
                    btnTaiTatCa.setText("Tạm dừng");
                }

                @Override
                public void onOccur(CustomEventObject e) {
                    progStatus.setValue(e._value);
                    
                    Double phanTram = (e._value*1.0 / e._max)*100;
                    NumberFormat nf = new DecimalFormat("0");
                    
                    lblStatus.setText("Đang tải tập tin '" + e._object1 + "' (chunk: '" + 
                            e._object2 + "') " + nf.format(phanTram.doubleValue()) + "%...");
                }

                @Override
                public void onFinish(CustomEventObject e) {
                    
                    if(!e._errorMessage.equals(""))
                    {
                        lblStatus.setText("Hoàn tất! Lỗi: " + e._errorMessage);
                    }
                    
                    isPause = 0;
                    btnTaiTatCa.setText("Tải tất cả");
                }
                
                @Override
                public void onError(CustomEventObject e) {
                    lblStatus.setText(e._errorMessage);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Bittorent.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            
            dlTorrent.start();
            
        }


    }//GEN-LAST:event_btnTaiTatCaActionPerformed

    private void btnChonFileNoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonFileNoiActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);// không cho chọn nhiều file
        fc.setCurrentDirectory(new File("./torrent"));
        fc.setFileFilter(new FileNameExtensionFilter("Torrent file (*.torrent)", "torrent"));

        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            txtNoiFile.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnChonFileNoiActionPerformed

    /**
     * Dow
     * @param evt 
     */
    private void btnBrowseDownloadChunkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBrowseDownloadChunkActionPerformed
        // TODO add your handling code here:

        JFileChooser fc = new JFileChooser();
        fc.setFileFilter(new FileNameExtensionFilter("Torrent file (*.torrent)", "torrent"));
        fc.setMultiSelectionEnabled(false);
        fc.setCurrentDirectory(new File("./torrent"));

        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            if (fc.getSelectedFile() != null) {
                txtTaiTorrent.setText(fc.getSelectedFile().getAbsolutePath());

                FileInputStream fis = null;
                f = fc.getSelectedFile(); // chọn file cần startDownload
                int sochunk = 0;
                try {
                    fis = new FileInputStream(fc.getSelectedFile());
                    Scanner scan = new Scanner(fis);
                    sochunk = scan.nextInt();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                String ten = fc.getSelectedFile().getName();
                ten = ten.substring(0, ten.length() - 8);
                ThongTinTapTin fi = new ThongTinTapTin();
                fi.setTenfile(ten);
                fi.setSochunk(sochunk);
                
                cmbChunk.removeAllItems();
                for (int i = 0; i < sochunk; i++) {
                    File f = new File(ThongTinChunk.duongDanChunk + ten + "/" + ten + "_" + (i + 1 + ".chunk"));

                    if (!f.exists()) {
                        String chunkCanDow = f.getName();
                        cmbChunk.addItem(chunkCanDow);
                    }
                }
            }
        }

    }//GEN-LAST:event_btnBrowseDownloadChunkActionPerformed

    /*
     * down tuần tự từng file
     */
    private void btnChonCatTapTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChonCatTapTinActionPerformed
        // TODO add your handling code here:
        JFileChooser fc = new JFileChooser();
        fc.setMultiSelectionEnabled(false);

        if(fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
        {
            txtCatTapTin.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_btnChonCatTapTinActionPerformed

    private void btnNoiTapTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoiTapTinActionPerformed
        ThreadNoiFile c = new ThreadNoiFile();
        String filename = txtNoiFile.getText().trim();
        File f = new File(filename);
        lblStatus.setText("");
        
        FileInputStream in = null;
        int sochunk = 0;
        try {
            in = new FileInputStream(f);
            Scanner input = new Scanner(in);
            sochunk = input.nextInt(); // đọc số lượng chunks
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(new JPanel(), "Lỗi\n\n" + ex.getMessage());
            return;
        } finally {
            try {
                in.close();
            } catch (IOException ex) {
                Logger.getLogger(Bittorent.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        if (f.exists()) {
            c.fInfo = new ThongTinTapTin(f);
            c.fInfo.setSochunk(sochunk);
            
            progStatus.setMinimum(0);
            progStatus.setMaximum(c.fInfo.getSochunk());
            c.addCustomListener(new CustomEventListener() {

                @Override
                public void onStart(CustomEventObject e) {
                    
                }

                @Override
                public void onOccur(CustomEventObject e) {
                    progStatus.setValue(e._value);
                    
                    Double phanTram = (e._value*1.0 / e._max)*100;
                    NumberFormat nf = new DecimalFormat("0");
                    
                    lblStatus.setText("Đang nối tập tin " + nf.format(phanTram.doubleValue()) + "%...");
                }

                @Override
                public void onFinish(CustomEventObject e) {
                    lblStatus.setText("Nối tập tin hoàn tất (" + e._object1.toString() + ") 100%...");
                }
                
                @Override
                public void onError(CustomEventObject e) {
                    lblStatus.setText("Nối không thành công.");
                }
            });
            
            c.start();
        }
        else
        {
            JOptionPane.showMessageDialog(new JPanel(), "Tập tin không tồn tại!");
            lblStatus.setText("Tập tin không tồn tại!");
        }
    }//GEN-LAST:event_btnNoiTapTinActionPerformed

    private void btnCatTapTinActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCatTapTinActionPerformed
        ThreadCatFile c = new ThreadCatFile();
        String tenfile = txtCatTapTin.getText().trim();
        File f = new File(tenfile);
        lblStatus.setText("");

        if (f.exists()) {
            c.fi = new ThongTinTapTin(f);
            progStatus.setMinimum(0);
            progStatus.setMaximum(c.fi.getSochunk());
            c.addCustomListener(new CustomEventListener() {

                @Override
                public void onStart(CustomEventObject e) {
                    
                }

                @Override
                public void onOccur(CustomEventObject e) {
                    progStatus.setValue(e._value);
                    
                    Double phanTram = (e._value*1.0 / e._max)*100;
                    NumberFormat nf = new DecimalFormat("0");
                    
                    lblStatus.setText("Đang cắt tập tin " + nf.format(phanTram.doubleValue()) + "%...");
                }

                @Override
                public void onFinish(CustomEventObject e) {
                    lblStatus.setText("Cắt tập tin hoàn tất (" + e._max + " chunks)...");
                }
                
                @Override
                public void onError(CustomEventObject e) {
                    lblStatus.setText("Cắt không thành công.");
                }
            });
            
            c.start();
        }
        else
        {
            JOptionPane.showMessageDialog(new JPanel(), "Tập tin không tồn tại!");
            lblStatus.setText("Tập tin không tồn tại!");
        }
    }//GEN-LAST:event_btnCatTapTinActionPerformed

    private void btnXemLogActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXemLogActionPerformed
        // TODO add your handling code here:
        frmLog flog = new frmLog(Bittorent.logString);
        flog.setVisible(true);
    }//GEN-LAST:event_btnXemLogActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Bittorent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Bittorent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Bittorent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Bittorent.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Bittorent().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBrowseDownloadChunk;
    private javax.swing.JButton btnCatTapTin;
    private javax.swing.JButton btnChonFileNoi;
    private javax.swing.JButton btnNoiTapTin;
    private javax.swing.JButton btnTaiChunkDangChon;
    private javax.swing.JButton btnTaiTatCa;
    private javax.swing.JButton btnThongTin;
    private javax.swing.JButton btnXemLog;
    private javax.swing.JComboBox cmbChunk;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JProgressBar progStatus;
    private javax.swing.JTextField txtCatTapTin;
    private javax.swing.JTextField txtNoiFile;
    private javax.swing.JTextField txtTaiTorrent;
    // End of variables declaration//GEN-END:variables
}
