package csu.edu.cn.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CaseRecognizeUtil {
    //   属格助词识别
    public static boolean CaCaseRecognition(String segment) {
        // 属格助词正则验证
        String regEx = "(" +
                "(e0bd91e0bc8b|e0bd96e0bc8b|e0bda6e0bc8b)e0bd80e0beb1e0bdb2" +"|"+
                "(e0bd82e0bc8b|e0bd84e0bc8b)e0bd82e0bdb2"  +"|"+
                "(e0bd93e0bc8b|e0bd98e0bc8b|e0bda2e0bc8b|e0bda3e0bc8b)e0bd82e0beb1e0bdb2"+
                ")";    //  \xe0\xbd\x91\xe0\xbc\x8b\xe0\xbd\x96\xe0\xbc\x8b\xe0\xbd\xa6\xe0\xbc\x8b
        // 编译正则表达式
        Pattern pattern = Pattern.compile(regEx);
        Matcher matcher = pattern.matcher(segment);
        boolean result = false;
        while (matcher.find()) {
            result = true;
            System.out.println(matcher.group());
        }
        // return matcher.find();  // 字符串是否与正则表达式相匹配
        return result;
    }

    public static void main(String[] args) {
        System.out.println(CaCaseRecognition(EnCodeUtil.ToHexStr(FileUtil.readFileToString(Constans.inputFilePath))));
    }
}
