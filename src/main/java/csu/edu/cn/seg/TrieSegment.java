package csu.edu.cn.seg;

import csu.edu.cn.trie.bintrie.BinTrie;
import csu.edu.cn.util.Constans;

import java.io.*;
import java.util.TreeMap;

/**
 * @program: LasSeg    @author: shan junwei
 * @description
 * @create: 2019-01-24 11:03
 **/
public class TrieSegment {
    public BinTrie trie = new BinTrie();
    public int MAX_WORD_LEN = 1;   // 字典中词的最大长度

    public TrieSegment() {
        initDict(Constans.lasDicPath);
    }
  /*  static {
        initDict(Constans.lasDicPath);
    }*/
    /**
     * 从文件初始化字典
     *
     * @param dicPath
     */
    public void initDict(String dicPath) {
        TreeMap<String, Integer> treeMap = new TreeMap<>();
        try {
            // 以utf-8读取文件
            FileInputStream fis = new FileInputStream(dicPath);
            InputStreamReader reader = new InputStreamReader(fis, "UTF-8");
            BufferedReader br = new BufferedReader(reader);
            String str = null;
            while ((str = br.readLine()) != null) {
                treeMap.put(str, 0);
                MAX_WORD_LEN = Math.max(str.split("-").length, MAX_WORD_LEN);  // 更新字典里词的最长字数
            }
            br.close();
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        trie.build(treeMap);
    }

}
