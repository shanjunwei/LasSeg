package csu.edu.cn.util;

import java.io.*;

public class FileUtil {
    // 文件读写
    public static String readFileToString(String fileName) {
        String encoding = "unicode";   // 藏语读取需要使用 unicode
        File file = new File(fileName);
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        Long filelength = file.length();
        byte[] filecontent = new byte[filelength.intValue()];
        try {
            if (!file.exists()) file.createNewFile();
            FileInputStream in = new FileInputStream(file);
            in.read(filecontent);
            in.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            return new String(filecontent, encoding);
        } catch (UnsupportedEncodingException e) {
            System.err.println("The OS does not support " + encoding);
            e.printStackTrace();
            return null;
        }
    }


    public static void main(String[] args) {
        String   text =   readFileToString("D:\\HanLP\\Las\\las.txt");
        System.out.println(text);
    }
}
