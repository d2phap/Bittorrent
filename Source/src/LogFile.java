/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Nhật ký chương trình
 * @author Duong Dieu Phap
 */
public class LogFile {
    
    private static String _filename = "./log.txt";
    
    /**
     * Lấy đường dẫn file log
     * @return 
     */
    public static String getFileName()
    {
        return _filename;
    }
    
    /**
     * Gán đường dẫn file log
     * @param filename 
     */
    public static void setFileName(String filename)
    {
        _filename = filename;
    }
    
    /**
     * Xoá nội dung file log
     * @return true nếu xoá thành công, ngược lại false
     */
    public static boolean ClearAll()
    {
        File f = new File(_filename);
        if(f.exists())
        {
            return f.delete();
        }
        
        return false;
    }
    
    public static void Write(String text) 
    {
        String thoigian = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(Calendar.getInstance().getTime());
        thoigian += " > ";
        thoigian += text + "\r\n";
        
        //Them vao log
        Bittorrent.logString += thoigian;
    }
    
    /**
     * Ghi file log
     * @param text nội dung cần ghi
     */
    public static void WriteToFile(String text) 
    {
        File f = new File(_filename);
        
        try {
            
            //Đọc tất cả nội dung của file log
            List _dsLines = new ArrayList();
            if(f.exists())
            {
                BufferedReader reader = Files.newBufferedReader(f.toPath(), StandardCharsets.UTF_8);
                String line = null;
                while((line = reader.readLine()) != null)
                {
                    _dsLines.add(line);
                }
                reader.close();
            }
            _dsLines.add(text);
            String kq = "";
            
            //Ghi nội dung file log
            FileWriter writer = new FileWriter(f);
            
            for(int i = 0 ; i< _dsLines.size(); i++)
            {
                kq += _dsLines.get(i).toString() + "\r\n";
            }
            
            writer.write(kq);
            writer.flush();
            writer.close();
            
        } catch (Exception ex) {
            System.err.print(ex.getMessage());
        }
    }
    
}
