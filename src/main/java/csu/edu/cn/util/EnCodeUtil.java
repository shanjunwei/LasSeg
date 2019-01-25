package csu.edu.cn.util;

import java.io.UnsupportedEncodingException;

public class EnCodeUtil {
    /**
     * 将byte[] 转换成 十六进制字符串
     */
    public static String bytesToHexStr(byte[] bytes) {
        char[] HEX_CHAR = {'0', '1', '2', '3', '4',
                '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        int index = 0;
        char[] hexChar = new char[bytes.length * 2];
        for (int i = 0; i < bytes.length; i++) {
            hexChar[index++] = HEX_CHAR[bytes[i] >> 4 & 0xF];
            hexChar[index++] = HEX_CHAR[bytes[i] & 0xF];
        }
        return new String(hexChar);
    }

    /**
     * 将byte[] 转换成 十六进制字符串
     */
    public static String ToHexStr(String unicodeStr) {
        byte[] bytes = unicodeStr.getBytes();
        return bytesToHexStr(bytes);
    }

    // 16进制字符串转 数组
    public static String deCodeHexString(String hexString) {
        try {
            return new String(toBytes(hexString), "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将16进制字符串转换为byte[]
     *
     * @param str
     * @return
     */
    public static byte[] toBytes(String str) {
        if(str == null || str.trim().equals("")) {
            return new byte[0];
        }

        byte[] bytes = new byte[str.length() / 2];
        for(int i = 0; i < str.length() / 2; i++) {
            String subStr = str.substring(i * 2, i * 2 + 2);
            bytes[i] = (byte) Integer.parseInt(subStr, 16);
        }

        return bytes;
    }


    public static void main(String[] args) {
      /*  String hexStr = bytesToHexStr("བོད");
        String fromStr = deCodeHexString(hexStr);

        //System.out.println(bytesToHexStr("上海"));   //\xe4\xb8\x8a\xe6\xb5\xb7
        // System.out.println(decodeUnicode("ཀྱི"));
        System.out.println(hexStr + "======" + fromStr);*/

       String  text  =  FileUtil.readFileToString(Constans.CaCasePath);
       System.out.println(ToHexStr(text));
    }

}
