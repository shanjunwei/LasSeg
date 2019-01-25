package csu.edu.cn.util;

import csu.edu.cn.pojo.Term;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 文本工具类
 */
public class TextUtility {
    /**
     * 将异常转为字符串
     *
     * @param e
     * @return
     */
    public static String exceptionToString(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        return sw.toString();
    }

    /**
     *  识别中文字符
     */
    public static boolean isChineseStr(String text) {   // 可以识别繁体字
        return Pattern.compile("[\\u4e00-\\u9fa5]+").matcher(text).matches();
    }

    /**
     * 反转字符串
     */
    public static String reverseString(String str) {
        return new StringBuilder(str).reverse().toString();
    }

    /**
     * 标准化偏移位置插值,目的是为了让插入的值的长度大小固定
     * TODO: 这里假设句子不够长,最长不过100个字
     */
    public static String getShiftStandardPosition(int left, int right) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(getTriNum(left)+"->"+getTriNum(right));
        return stringBuilder.toString();
    }


    public static String getTriNum(int num) {
        StringBuilder triNumStr = new StringBuilder();
        String str = String.valueOf(num);
        if (str.length() == 1) {
            triNumStr.append("00").append(str);
        } else if (str.length() == 2) {
            triNumStr.append("0").append(str);
        } else {
            triNumStr.append(str);
        }
        return triNumStr.toString();
    }

    public static void main(String[] args) {
        System.out.println(getTriNum(2));
    }

    /**
     * 利用抽取出来的词对原句进行切分处理   这是词语抽取向分词转变的过程
     * 传进来的exactWords是按顺序来的
     */
    public static String handleSentenceWithExtractWords(String sentence, List<Term> exactWords, boolean right) {
        StringBuilder stringBuilder = new StringBuilder(sentence);
        int shift = 0;  // 偏移量
        for (Term term : exactWords) {
            stringBuilder.insert(term.rightBound + shift, getShiftStandardPosition(term.leftBound, term.rightBound));  // 因为插值的原因,位置发生偏移
            if (!right) shift = shift + 8;
        }
        sentence = stringBuilder.toString();
        for (Term word : exactWords) {
            String hatWord = word.getSeg() + getShiftStandardPosition(word.leftBound, word.rightBound);  // 带边界的词
            sentence = sentence.replaceAll(hatWord, " " + word.getSeg() + " ");    // 这样做有一定隐患 比如 5 25 ;孙少平 少平
        }
        //System.out.println("处理完后的元句子-->" + sentence);
        sentence = sentence.trim();  // 去首尾空格
        sentence = sentence.replaceAll("\\s{1,}", " ");  // 去连续空格
        return sentence;
    }

    public static List<String> handleSentenceWithExtractWordsToList(String sentence, List<Term> exactWords, boolean right) {
        String str = handleSentenceWithExtractWords(sentence, exactWords, right);
        String[] array = str.split(" ");
        return Arrays.asList(array);
    }


}
