package com.yc.allbluetooth.utils;

import android.util.Log;

import java.io.UnsupportedEncodingException;

public class BytesToHexString {

    /**
     * byte[]转字符串
     *
     * @param src
     * @return
     */
    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * byte[]转字符串
     *
     * @param b
     * @return
     */
    public static String bytes2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = "0" + hex;
            }
            ret += hex;
        }
        return ret;
    }

    /**
     * byte[]转字符串
     *
     * @param buffer
     * @return
     */
    public static String toHex(byte[] buffer) {
        StringBuffer sb = new StringBuffer(buffer.length * 2);
        for (int i = 0; i < buffer.length; i++) {
            sb.append(Character.forDigit((buffer[i] & 240) >> 4, 16));
            sb.append(Character.forDigit(buffer[i] & 15, 16));
        }
        return sb.toString();
    }

    /**
     * 字符串转换为十六进制（无需Unicode码）
     * @param str
     * @return
     */
    public static String str2HexStr(String str) {
        char[] chars = "0123456789ABCDEF".toCharArray(); //16进制能用到的所有字符 0-15
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;
        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
        }
        return sb.toString().trim();
    }

    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hex  Byte字符串(Byte之间无分隔符
     * @return 对应的字符串
     * 可用 GBK ASCII
     */
    public static String hexStr2Str(String hex) {
        String hexStr = "";
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        for(int i=0;i<hex.length();i++){
            String s = hex.substring(i, i+1);
            if(s.equals("a")||s.equals("b")||s.equals("c")||s.equals("d")||s.equals("e")||s.equals("f")){
                s=s.toUpperCase().substring(0, 1);
            }
            hexStr+=s;
        }

        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        String name = "";
        try {
            name = new String(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return name;
    }
    /**
     * 16进制直接转换成为字符串(无需Unicode解码)
     * @param hex  Byte字符串(Byte之间无分隔符
     * @return 对应的字符串
     * 可用 GBK ASCII
     */
    public static String hexStr2Str2(String hex) {
        String hexStr = "";
        String str = "0123456789ABCDEF"; //16进制能用到的所有字符 0-15
        for(int i=0;i<hex.length();i++){
            String s = hex.substring(i, i+1);
            if(s.equals("a")||s.equals("b")||s.equals("c")||s.equals("d")||s.equals("e")||s.equals("f")){
                s=s.toUpperCase().substring(0, 1);
            }
            hexStr+=s;
        }

        char[] hexs = hexStr.toCharArray();//toCharArray() 方法将字符串转换为字符数组。
        int length = (hexStr.length() / 2);//1个byte数值 -> 两个16进制字符
        byte[] bytes = new byte[length];
        int n;
        for (int i = 0; i < bytes.length; i++) {
            int position = i * 2;//两个16进制字符 -> 1个byte数值
            n = str.indexOf(hexs[position]) * 16;
            n += str.indexOf(hexs[position + 1]);
            // 保持二进制补码的一致性 因为byte类型字符是8bit的  而int为32bit 会自动补齐高位1  所以与上0xFF之后可以保持高位一致性
            //当byte要转化为int的时候，高的24位必然会补1，这样，其二进制补码其实已经不一致了，&0xff可以将高的24位置为0，低8位保持原样，这样做的目的就是为了保证二进制数据的一致性。
            bytes[i] = (byte) (n & 0xff);
        }
        String name = "";
        try {
            name = new String(bytes,"GBK");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return name;
    }

    /**
     * 试品编号转GBK前，去掉结尾“00”
     * @param str
     * @return
     */
    public static String gbkQuLing(String str){
        String gbkStr = "";
        if(StringUtils.isEquals("00",StringUtils.subStrStartToEnd(str,0,2))){
            //Log.e("-----","null!");
            gbkStr = "";
        }else{
            for(int i=0;i<str.length()/2;i++){
                //Log.e("--",i*2+","+(i*2+2));
                String strSub = StringUtils.subStrStartToEnd(str,i*2,(i*2+2));
                if(StringUtils.isEquals(strSub,"00")&&!StringUtils.isEquals("00",StringUtils.subStrStartToEnd(str,(i-1)*2,i*2))){
                    //Log.e("-----",StringUtils.subStrStartToEnd(str,0,(i-1)*2));
                    //Log.e("------",BytesToHexString.hexStr2Str(StringUtils.subStrStartToEnd(str,0,(i-1)*2)));
                    gbkStr = BytesToHexString.hexStr2Str2(StringUtils.subStrStartToEnd(str,0,i*2));
                }
            }
        }
        return gbkStr;
    }

    /**
     * 如“1A2B”转“2B1A”。
     * @param hex
     * @return
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
    public static String rev(String ox){
        String s = ox.substring(2);
        byte b[] = s.getBytes();
        byte result[] = new byte[b.length];
        for (int i= b.length-1, j=0; i>=0;i--,j++)
            result[j]= b[i];
        return new String(result);
    }
    public static Long rev2(){
        // Java读取16进制数据
        int javaReadInt = 0xFE7AF939 ;
        // 将每个字节取出来
        byte byte4 = (byte) (javaReadInt & 0xff);
        byte byte3 = (byte) ((javaReadInt & 0xff00) >> 8);
        byte byte2 = (byte) ((javaReadInt & 0xff0000) >> 16);
        byte byte1 = (byte) ((javaReadInt & 0xff000000) >> 24);

        // 拼装成 "高字节在后，低字节在前"的格式
        long realint = (byte1& 0xff)<<0  |(byte2& 0xff)<<8 | (byte3& 0xff)<< 16| (byte4& 0xff)<<24 ;
        System.out.printf("new2:"+realint+"\n");
        return realint;
    }
    public static String GbkToUtf8(String gbkStr) throws UnsupportedEncodingException {
        String utfStr = "";
        //String strGBK = "这是一段GBK编码的字符串";
        byte[] bytes = gbkStr.getBytes("GBK");
        utfStr = new String(bytes,"UTF-8");
        //System.out.println(strUTF);
        return utfStr;
    }
}
