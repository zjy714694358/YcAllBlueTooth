package com.yc.allbluetooth.utils;

/**
 * Date:2022/11/17 10:06
 * author:jingyu zheng
 */
public class HexUtil {
    /**
     * 16进制高低位转换
     * @param hex
     */
    public static String reverseHex(String hex) {
        char[] charArray = hex.toCharArray();
        int length = charArray.length;
        int times = length / 2;
        for (int c1i = 0; c1i < times; c1i += 2) {
            int c2i = c1i + 1;
            char c1 = charArray[c1i];
            char c2 = charArray[c2i];
            int c3i = length - c1i - 2;
            int c4i = length - c1i - 1;
            charArray[c1i] = charArray[c3i];
            charArray[c2i] = charArray[c4i];
            charArray[c3i] = c1;
            charArray[c4i] = c2;
        }
        return new String(charArray);
    }
    /**
     * 16进制字符串转换为float符点型
     *
     * @param s
     * @param radix
     * @return
     * @throws NumberFormatException
     */
    public static long parseLong(String s, int radix) throws NumberFormatException {
        if (s == null) {
            throw new NumberFormatException("null");
        }
        if (radix < Character.MIN_RADIX) {
            throw new NumberFormatException("radix" + radix + "less than Character.MIN_RADIX");
        }
        if (radix > Character.MAX_RADIX) {
            throw new NumberFormatException("radix" + radix + "greater than Character.MAX_RADIX");
        }
        long result = 0;
        boolean negative = false;
        int i = 0, len = s.length();
        long limit = -Long.MAX_VALUE;
        long multmin;
        int digit;
        if (len > 0) {
            char firstChar = s.charAt(0);
            if (firstChar < '0') {// Possible leading "+" or "-"
                if (firstChar == '-') {
                    negative = true;
                    limit = Long.MIN_VALUE;
                } else if (firstChar != '+')
                    throw NumberFormatException.forlnputString(s);
                if (len == 1) // Cannot have lone "+" or "-"
                    throw NumberFormatException.forlnputString(s);
                i++;
            }
            multmin = limit / radix;
            while (i < len) {
                // Accumulating negatively avoids surprises near MAX_VALUE
                digit = Character.digit(s.charAt(i++), radix);
                if (digit < 0) {
                    throw NumberFormatException.forlnputString(s);
                }
                if (result < multmin) {
                    throw NumberFormatException.forlnputString(s);
                }
                result *= radix;
                if (result < limit + digit) {
                    throw NumberFormatException.forlnputString(s);
                }
                result -= digit;
            }
        } else {
            throw NumberFormatException.forlnputString(s);
        }
        return negative ? result : -result;
    }

    /**
     * NuberFormatException
     */
    public static class NumberFormatException extends IllegalAccessException {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        public NumberFormatException(String s) {
            super(s);
        }

        static NumberFormatException forlnputString(String s) {
            return new NumberFormatException("For input string: \"" + s + "\"");
        }
    }
    /**
     * float符点型转换为16进制字符串
     * @param changeData
     * @return
     */
    public static String fToHex(float changeData){
        return Integer.toHexString(Float.floatToIntBits(changeData));
    }

}
