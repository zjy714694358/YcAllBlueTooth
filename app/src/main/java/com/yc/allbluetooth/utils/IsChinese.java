package com.yc.allbluetooth.utils;

/**
 * Date:2023/4/21 14:42
 * author:jingyu zheng
 */
public class IsChinese {
    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */
    public boolean ischinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        // 4E00-9FBF：CJK 统一表意符号
        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                //F900-FAFF：CJK 兼容象形文字
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                //3400-4DBF：CJK 统一表意符号扩展 A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                //2000-206F：常用标点
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                //3000-303F：CJK 符号和标点
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                //FF00-FFEF：半角及全角形式
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
            return true;
        }
        return false;
    }
    /**
     * 检测string是否全是中文
     *
     * @param name
     * @return
     */
    public boolean isAllchese(String name) {
        boolean res = true;
        char[] ctemp = name.toCharArray();
        for (int i = 0; i < name.length(); i++) {
            if (!ischinese(ctemp[i])) {
                res = false;
                break;
            }
        }
        return res;
    }
}
