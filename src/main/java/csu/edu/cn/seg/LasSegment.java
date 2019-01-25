package csu.edu.cn.seg;

import csu.edu.cn.pojo.Term;
import csu.edu.cn.util.TextUtility;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @program: LasSeg    @author: shan junwei
 * @description: 藏语机械分词
 * @create: 2019-01-24 10:06
 **/
public class LasSegment extends TrieSegment {
    public int LineNum = 1;

    public List<String> LasFMM(String text) {
        List<String> result = new ArrayList<>();
        String[] arrays = text.split("-");
        int tempLen = Math.min(arrays.length, MAX_WORD_LEN);
        int p = 0;
        int q = tempLen;   // 剩余的最大长度
        while (p < arrays.length) {
            while (q > 0 && p + q <= arrays.length) {  // 控制取词的长度
                String strChar = getSubTerm(arrays, p, p + q);
                if (trie.containsKey(strChar)) {
                    result.add(strChar);
                    p = p + q - 1;
                    tempLen = (arrays.length - 1) - (p + q);  // 更新剩余长度
                    if (tempLen > MAX_WORD_LEN) q = MAX_WORD_LEN;
                    else if (tempLen <= MAX_WORD_LEN) {
                        q = arrays.length - (p + 1);
                    }
                    break;
                }
                if (strChar.split("-").length == 1) {
                    String temp = strChar.substring(0, strChar.indexOf("-"));   // 去掉后缀 -以后的字符
                    if (TextUtility.isChineseStr(temp)) {
                        result.addAll(FMM(temp));  //  对中文串进行单独分词处理
                    } else {
                        //wordsNoninDic.add(strChar + "," + LineNum);  // 加上行号
                        result.add(strChar);
                        p = p + q - 1;
                        tempLen = (arrays.length - 1) - (p + q);  // 更新剩余长度
                        if (tempLen > MAX_WORD_LEN) q = MAX_WORD_LEN;
                        else if (tempLen <= MAX_WORD_LEN) {
                            q = arrays.length - (p + 1);
                        }
                    }
                    break;
                }
                q--;
            }
            p++;
        }
        return result;
    }


    // 逆向最大匹配分词
    public List<String> LasDMM(String text) {
        List<String> result = new ArrayList<>();
        String[] arrays = text.split("-");
        int temp_max_len;  // 每次取小于或者等于最大字典长度的子串进行匹配
        int q = 0;
        int p = arrays.length;
        while (p > 0) {
            while (q < p) {  // 控制取词的长度
                if (p - q < 0) break;
                String strChar = getSubTerm(arrays, q, p);   // 取词串 q->p  [q,p)
                if (trie.containsKey(strChar)) {
                    result.add(strChar);
                    p = q + 1;
                    temp_max_len = q;
                    if (temp_max_len > MAX_WORD_LEN) q = p - MAX_WORD_LEN;
                    else if (temp_max_len <= MAX_WORD_LEN) {
                        q = 0;
                    }
                    break;
                }
                if (strChar.split("-").length == 1) {
                    String temp = strChar.substring(0, strChar.indexOf("-"));   // 去掉后缀 -以后的字符
                    if (TextUtility.isChineseStr(temp)) {
                        result.addAll(DMM(temp));  //  对中文串进行单独分词处理
                    } else {
                        result.add(strChar);
                        temp_max_len = q;
                        if (temp_max_len > MAX_WORD_LEN) q = p - MAX_WORD_LEN;
                        else if (temp_max_len <= MAX_WORD_LEN) {
                            q = 0;
                        }
                    }
                    break;
                }
                q++;
            }
            p--;
        }
        Collections.reverse(result);   // 反转链表
        return result;
    }


    public String getSubTerm(String[] array, int begin, int end) {
        StringBuilder stringBuilder = new StringBuilder();
        // 下标检查
        if (begin < 0 || end > array.length) return stringBuilder.toString();
        int index = begin;
        while (index < end) {
            stringBuilder.append(array[index] + "-");
            index++;
        }
        return stringBuilder.toString();
    }


    // 逆向最大匹配分词
    public List<String> DMM(String text) {
        List<Term> result = new ArrayList<>();
        int temp_max_len;  // 每次取小于或者等于最大字典长度的子串进行匹配
        int q = 0;
        int p = text.length();
        while (p > 0) {
            while (q < p) {  // 控制取词的长度
                if (p - q < 0) break;
                String strChar = text.substring(q, p);  // 取词串 q->p  [q,p)
                if (trie.containsKey(strChar)) {
                    Term term = new Term(strChar, q, p);
                    result.add(term);
                    p = q + 1;
                    temp_max_len = q;
                    if (temp_max_len > MAX_WORD_LEN) q = p - MAX_WORD_LEN;
                    else if (temp_max_len <= MAX_WORD_LEN) {
                        q = 0;
                    }
                    break;
                }
                if (strChar.length() == 1) {
                    temp_max_len = q;
                    if (temp_max_len > MAX_WORD_LEN) q = p - MAX_WORD_LEN;
                    else if (temp_max_len <= MAX_WORD_LEN) {
                        q = 0;
                    }
                    break;
                }
                q++;
            }
            p--;
        }
        return TextUtility.handleSentenceWithExtractWordsToList(text, result, true);
    }

    // 正向最大匹配分词
    public List<String> FMM(String text) {
        List<Term> result = new ArrayList<>();
        int tempLen = Math.min(text.length(), MAX_WORD_LEN);
        int p = 0;
        int q = tempLen;   // 剩余的最大长度
        while (p < text.length()) {
            while (q > 0 && p + q <= text.length()) {  // 控制取词的长度
                String strChar = text.substring(p, p + q);   // [p,p+q)   // 取词串  p --> p+q
                // System.out.println(strChar);
                if (trie.containsKey(strChar)) {
                    Term term = new Term(strChar, p, p + q);
                    result.add(term);
                    p = p + q - 1;
                    tempLen = (text.length() - 1) - (p + q);  // 更新剩余长度
                    if (tempLen > MAX_WORD_LEN) q = MAX_WORD_LEN;
                    else if (tempLen <= MAX_WORD_LEN) {
                        q = text.length() - (p + 1);
                    }
                    break;
                }
                if (strChar.length() == 1) {
                    tempLen = (text.length() - 1) - (p + q);  // 更新剩余长度
                    if (tempLen > MAX_WORD_LEN) q = MAX_WORD_LEN;
                    else if (tempLen <= MAX_WORD_LEN) {
                        q = text.length() - (p + 1);   // q= temp_max_len+1;
                    }
                    break;
                }

              /*  while (text.charAt(p+q-1)!='-'){
                    System.out.println(text.charAt(p+q-1));
                    q--;
                }*/
                q--;
            }
            p++;
        }
        return TextUtility.handleSentenceWithExtractWordsToList(text, result, false);
    }

    /**
     * 对文件中的藏文进行分词处理
     */
    public void segFile(String filePath, String outputPath) {
        try {
            // 以utf-8读取文件
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(reader);

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));

            String str = null;
            while ((str = br.readLine()) != null) {
                List<String> result = segment(str);
                System.out.println(str + "--->" + result);
                bw.write(StringUtils.join(result, " "));
                bw.write("\n");
            }
            br.close();
            reader.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 输出不在字典的词
     */
    public void segFileAndOutputWordsNoninDic(String filePath, String outputPath) {
        try {
            // 以utf-8读取文件
            FileInputStream fis = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath), "UTF-8"));
            String str;
            while ((str = br.readLine()) != null) {
                List<String> result = segment(str);
                //bw.write(StringUtils.join(result, " "));                 //bw.write("----->");
                boolean dicContains = false;
                for (String it : result) {
                    if (!trie.containsKey(it)) {
                        dicContains = true;
                        try {
                            bw.write(it + "  ");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (dicContains){
                    bw.write(LineNum+" ");
                    bw.write("\n");
                }
                LineNum++;
            }
            br.close();
            reader.close();
            bw.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 对文件中的藏文进行 合法性处理
     */
    public String preHandle(String text) {
        text = text.trim();
        // 中文字符的处理
        Matcher matcher = Pattern.compile("[\\u4e00-\\u9fa5]+").matcher(text);
        while (matcher.find()) {
            String chineseText = matcher.group();
            text = text.replace(chineseText, "-" + chineseText + "-");
        }
        text = text.replaceAll("-{1,}", "-");  // 去连续空格
        return text;
    }


    /**
     * 结合正向最大匹配和逆向最大匹配算法
     *
     * @param text
     * @return
     */
    public List<String> segment(String text) {
        text = preHandle(text);
        List<String> fmm = LasFMM(text);
        List<String> bmm = LasDMM(text);
        // 如果分词的结果不同，返回长度较小的
        if (fmm.size() != bmm.size()) {
            if (fmm.size() > bmm.size())
                return bmm;
            else
                return bmm;
        }
        // 如果分词的词数相同
        else {
            int fmmSingle = 0, bmmSingle = 0;
            boolean isEqual = true;
            for (int i = 0; i < bmm.size(); i++) {
                if (!fmm.get(i).equals(bmm.get(i))) {
                    isEqual = false;
                }
                if (fmm.get(i).split("-").length <= 1)
                    fmmSingle++;
                if (bmm.get(i).split("-").length <= 1)
                    bmmSingle++;
            }
            // 如果正向、逆向匹配结果完全相等，返回任意结果
            if (isEqual) {
                return fmm;
                // 否则，返回单字数少的匹配方式
            } else if (fmmSingle > bmmSingle)
                return bmm;
            else
                return fmm;
        }
    }


    public static void main(String[] args) {
        LasSegment lasSegment = new LasSegment();
        String text = "a";
        // text = "ny9l9-kh9中文ng9-n9ng9-b9s9d拉了x9d9-d9e9v9e9-  "; //
        //text = "th9e9g9-m9kh9r9-g9l9-b9q19u9d9-g9o9-m9-r9u9d9-v9e9-d9u9-n9s9-y9r9-th9l9-d9ng9-d9u9-zh9i9g9s9-k9i9-y9ng9-v9d9i9g9-s9u9-y9o9n9-d9u9-g9r9-v9d9i9g9-s9u9-";
        //System.out.println(lasSegment.LeftMaxSegment(text));
        //System.out.println(lasSegment.LasFMM(text));
        // System.out.println(lasSegment.getSubTerm(text.split("-"),0,3));
  /*      System.out.println(lasSegment.LasFMM(text));
        System.out.println(lasSegment.LasDMM(text));
        System.out.println(lasSegment.segment(text));*/
        // System.out.println(lasSegment.segment(text));

        // System.out.println(lasSegment.DMM(text));
        // System.out.println(lasSegment.segment(text));
        //lasSegment.segFile("data/las-text.txt", "data/result2.txt");
        lasSegment.segFileAndOutputWordsNoninDic("data/las-text.txt", "data/words-not-in-dic.txt");

        //System.out.println(lasSegment.preHandle(text));
    }

}