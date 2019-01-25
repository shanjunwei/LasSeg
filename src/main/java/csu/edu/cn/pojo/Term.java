package csu.edu.cn.pojo;

import java.io.Serializable;

/**
 * 存储每个切分 和他们的一些统计信息
 */
public class Term implements Serializable {
    public String seg;   // 切分片段

    public int leftBound;  // 左边界  在原来短句中的起始索引位置
    public int rightBound;  // 右边界

    public Term() {
    }
    public Term(String seg, int leftBound, int rightBound) {
        this.seg = seg;
        this.leftBound = leftBound;
        this.rightBound = rightBound;
    }

    public String getSeg() {
        return seg;
    }

    public void setSeg(String seg) {
        this.seg = seg;
    }

    public int getLeftBound() {
        return leftBound;
    }

    public void setLeftBound(int leftBound) {
        this.leftBound = leftBound;
    }

    public int getRightBound() {
        return rightBound;
    }

    public void setRightBound(int rightBound) {
        this.rightBound = rightBound;
    }


    @Override
    public String toString() {
        return seg;
    }

    public String toTotalString() {
        return "Term{" +
                "seg='" + seg + '\'' +
                ", leftBound=" + leftBound +
                ", rightBound=" + rightBound +
                '}';
    }
}
