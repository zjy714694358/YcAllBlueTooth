package com.yc.allbluetooth.youzai.util;

import android.util.Log;
import android.widget.TextView;

import com.yc.allbluetooth.utils.BytesToHexString;
import com.yc.allbluetooth.utils.ShiOrShiliu;
import com.yc.allbluetooth.utils.StringUtils;


/**
 * Date:2023/6/7 17:11
 * author:jingyu zheng
 * 对参数设置中，切换
 */
public class CsszQiehuan {
    /**
     * 量程切换
     * @param str   当前显示的量程值（2A、1A、0.2A）
     * @param textView  要显示的TV
     */
    public static String  liangcheng2A(String str, TextView textView){
        String lcZhiling = "";
        if(StringUtils.isEquals("10Ω(2.0A)",str)){
            lcZhiling = "1400";
            textView.setText("20Ω(1.0A)");
        }else if(StringUtils.isEquals("20Ω(1.0A)",str)){
            textView.setText("100Ω(0.2A)");
            lcZhiling = "6400";
        }else if(StringUtils.isEquals("100Ω(0.2A)",str)){
            textView.setText("10Ω(2.0A)");
            lcZhiling = "0A00";
        }
        Log.e("lc==",lcZhiling);
        return lcZhiling;
    }
    /**
     * 量程切换
     * @param str   当前显示的量程值（1A、0.5A、0.2A）
     * @param textView  要显示的TV
     */
    public static String  liangcheng1A(String str, TextView textView){
        String lcZhiling = "";
        if(StringUtils.isEquals("20Ω(1.0A)",str)){
            lcZhiling = "2800";
            textView.setText("40Ω(0.5A)");
        }else if(StringUtils.isEquals("40Ω(0.5A)",str)){
            textView.setText("100Ω(0.2A)");
            lcZhiling = "6400";
        }else if(StringUtils.isEquals("100Ω(0.2A)",str)){
            textView.setText("20Ω(1.0A)");
            lcZhiling = "1400";
        }
        Log.e("lc==",lcZhiling);
        return lcZhiling;
    }
    /**
     * 量程切换
     * @param str   当前显示的时长值（120.0ms\320.0ms）
     * @param textView  要显示的TV
     */
    public static String  shichang(String str, TextView textView){
        String scZhiling = "";
        if(StringUtils.isEquals("120.0ms",str)){
//          Log.e("==========", ShiOrShiliu.toHexString(6400));
//          Log.e("==========2", HexUtil.reverseHex(ShiOrShiliu.toHexString(6400)));
//          String hexStr = ShiOrShiliu.toHexString(2400);
            scZhiling = "0019";//6400==>1900==>0019
            textView.setText("320.0ms");
        }else if(StringUtils.isEquals("320.0ms",str)){
            textView.setText("120.0ms");
            scZhiling = "6009";//2400==>960==>0960==>6009
        }
        Log.e("sc==",scZhiling);
        return scZhiling;
    }

    /**
     * 灵敏度
     * @param str
     * @param textView
     * @param type 左（减少）0；右（增加）1
     * @return
     */
    public static String lingmindu(String str,TextView textView,int type){
        String lmdZhiling = "";
        int strInt = 0;
        strInt = StringUtils.strToInt(str);
        if(type==0 && strInt!=2){//减少
            textView.setText(strInt-1+"");
        }else if(type==1 && strInt!=33){//增加
            textView.setText(strInt+1+"");
        }
        String tvStr = textView.getText().toString();
        int tvStrInt = 0;
        tvStrInt = StringUtils.strToInt(tvStr);
        if(tvStrInt<16){
            lmdZhiling = "0"+ShiOrShiliu.toHexString(tvStrInt)+"00";
        }else{
            lmdZhiling = ShiOrShiliu.toHexString(tvStrInt)+"00";
        }
        Log.e("lmd==",lmdZhiling);
        return lmdZhiling;
    }

    /**
     * 测试分接
     * @param str1   左tv值
     * @param str2   右tv值
     * @param textView1 左tv
     * @param textView2 右tv
     * @param jtType 左：0； 右：1
     * @return
     */
    public static String csfj(String str1,String str2,TextView textView1,TextView textView2,int jtType){
        String csfjZhiling = "";
        String fj1Zhiling = "";
        String fj2Zhiling = "";
        if(jtType==0){//左
            //tv1===左
            if(StringUtils.isEquals("01",str1)){
                textView1.setText("02");
                fj1Zhiling = "0200";
            }else if(StringUtils.isEquals("9A",str1)){
                textView1.setText("09");
                fj1Zhiling = "0900";
            }else if(StringUtils.isEquals("9B",str1)){
                textView1.setText("9A");
                fj1Zhiling = "9A00";
            }else if(StringUtils.isEquals("9C",str1)){
                textView1.setText("9B");
                fj1Zhiling = "9B00";
            }else if(StringUtils.strToInt(str1)<10&&StringUtils.strToInt(str1)!=1){
                textView1.setText("0"+(StringUtils.strToInt(str1)-1));
                fj1Zhiling = "0"+(StringUtils.strToInt(str1)-1)+"00";
            }else if(StringUtils.strToInt(str1)==10){
                textView1.setText("9C");
                fj1Zhiling = "9C"+"00";
            }else if(StringUtils.strToInt(str1)>10&&StringUtils.strToInt(str1)<=99){
                textView1.setText((StringUtils.strToInt(str1)-1)+"");
                fj1Zhiling = (StringUtils.strToInt(str1)-1)+"00";
            }
            //tv2===右
            if(StringUtils.isEquals("01",str2)){
                //textView1.setText("01");
                textView2.setText("02");
                fj2Zhiling = "0200";
            }else if(StringUtils.isEquals("9A",str2)){
                textView2.setText("09");
                fj2Zhiling = "0900";
            }else if(StringUtils.isEquals("9B",str2)){
                textView2.setText("9A");
                fj2Zhiling = "9A00";
            }else if(StringUtils.isEquals("9C",str2)){
                textView2.setText("9B");
                fj2Zhiling = "9B00";
            }else if(StringUtils.strToInt(str2)<10&&StringUtils.strToInt(str2)!=1){
                textView2.setText("0"+(StringUtils.strToInt(str2)-1));
                fj2Zhiling = "0"+(StringUtils.strToInt(str2)-1)+"00";
            }else if(StringUtils.strToInt(str2)==10){
                textView2.setText("9C");
                fj2Zhiling = "9C"+"00";
            }else if(StringUtils.strToInt(str2)>10&&StringUtils.strToInt(str2)<=99){
                textView2.setText((StringUtils.strToInt(str2)-1)+"");
                fj2Zhiling = (StringUtils.strToInt(str2)-1)+"00";
            }
            csfjZhiling = fj1Zhiling + fj2Zhiling;
        }else{//右
            //tv1===右
            if(StringUtils.isEquals("9A",str1)){
                textView1.setText("9B");
                fj1Zhiling = "9B"+"00";
            }else if(StringUtils.isEquals("9B",str1)){
                textView1.setText("9C");
                fj1Zhiling = "9C"+"00";
            }
            else if(StringUtils.isEquals("9C",str1)){
                textView1.setText("10");
                fj1Zhiling = "10"+"00";
            }else if(StringUtils.strToInt(str1)<9){
                textView1.setText("0"+(StringUtils.strToInt(str1)+1));
                fj1Zhiling = "0"+(StringUtils.strToInt(str1)+1)+"00";
            }else if(StringUtils.strToInt(str1)==9){
                textView1.setText("9A");
                fj1Zhiling = "9A"+"00";
            }else if(StringUtils.strToInt(str1)>=10&&StringUtils.strToInt(str1)<98){
                textView1.setText((StringUtils.strToInt(str1)+1)+"");
                fj1Zhiling = (StringUtils.strToInt(str1)+1)+"00";
            }else if(StringUtils.strToInt(str1)==98){
                textView1.setText("01");
                fj1Zhiling = (StringUtils.strToInt(str1)+1)+"00";
            }
            //tv2====右
            if(StringUtils.isEquals("9A",str2)){
                textView2.setText("9B");
                fj2Zhiling = "9B"+"00";
            }else if(StringUtils.isEquals("9B",str2)){
                textView2.setText("9C");
                fj2Zhiling = "9C"+"00";
            }
            else if(StringUtils.isEquals("9C",str2)){
                textView2.setText("10");
                fj2Zhiling = "10"+"00";
            }else if(StringUtils.strToInt(str2)<9){
                textView2.setText("0"+(StringUtils.strToInt(str2)+1));
                fj2Zhiling = "0"+(StringUtils.strToInt(str2)+1)+"00";
            }else if(StringUtils.strToInt(str2)==9){
                textView2.setText("9A");
                fj2Zhiling = "9A"+"00";
            }else if(StringUtils.strToInt(str2)>=10&&StringUtils.strToInt(str2)<99){
                textView2.setText((StringUtils.strToInt(str2)+1)+"");
                fj2Zhiling = (StringUtils.strToInt(str2)+1)+"00";
            }else if(StringUtils.strToInt(str2)==99){
                textView2.setText("02");
                fj2Zhiling = "02"+"00";
            }
            csfjZhiling = fj1Zhiling + fj2Zhiling;
        }

        return csfjZhiling;
    }
    /**
     * 连接方式
     * @param str   当前显示的连接方式值（YN、Y、△）
     * @param textView  要显示的TV
     * @param type  左：0  右：1
     *@param textView2  受影响的测试相数TV
     * @param type2  判断测试相数目前显示的什么？ABC：0；其余全是1
     */
    public static String  ljfs(String str, TextView textView,int type,TextView textView2,int type2){
        String ljfsZhiling = "";
        if(type==1){//右
            if(StringUtils.isEquals("YN",str)){
                ljfsZhiling = "0100";
                if(type2==0){
                    textView2.setText("AB");
                }
                textView.setText("Y");
            }else if(StringUtils.isEquals("Y",str)){
                textView.setText("△");
                if(type2==0){
                    textView2.setText("AB");
                }
                ljfsZhiling = "0200";
            }else if(StringUtils.isEquals("△",str)){
                textView.setText("YN");
                ljfsZhiling = "0000";
            }
        }else if(type==0){
            if(StringUtils.isEquals("YN",str)){
                textView.setText("△");
                if(type2==0){
                    textView2.setText("AB");
                }
                ljfsZhiling = "0200";
            }else if(StringUtils.isEquals("△",str)){
                ljfsZhiling = "0100";
                textView.setText("Y");
                if(type2==0){
                    textView2.setText("AB");
                }
            }else if(StringUtils.isEquals("Y",str)){
                textView.setText("YN");
                ljfsZhiling = "0000";
            }
        }
        Log.e("连接方式==",ljfsZhiling);
        return ljfsZhiling;
    }
    /**
     * 测试相数
     * @param str   当前显示的测试相数值
     * 测试相数
     * YN：ABC==>AB==AC==BC==A==B==C
     * Y：AB=AC=BC=A=B=C
     * △：AB=AC=BC=A=B=C
     * @param textView  要显示的TV
     * @param type  //连接方式YN:0;连接方式Y、△:1
     * @param zyType 左：0；右：1
     */
    public static String  csxs(String str, TextView textView,int type,int zyType){
        String csxwZhiling = "";
        if(type==0){//连接方式YN
            if(zyType==1){//右
                if(StringUtils.isEquals("ABC",str)){
                    csxwZhiling = "0206";
                    textView.setText("AB");
                }else if(StringUtils.isEquals("AB",str)){
                    textView.setText("AC");
                    csxwZhiling = "0205";
                }else if(StringUtils.isEquals("AC",str)){
                    textView.setText("BC");
                    csxwZhiling = "0203";
                }else if(StringUtils.isEquals("BC",str)){
                    textView.setText("A");
                    csxwZhiling = "0104";
                }else if(StringUtils.isEquals("A",str)){
                    textView.setText("B");
                    csxwZhiling = "0102";
                }else if(StringUtils.isEquals("B",str)){
                    textView.setText("C");
                    csxwZhiling = "0101";
                }else if(StringUtils.isEquals("C",str)){
                    textView.setText("ABC");
                    csxwZhiling = "0307";
                }
            }else{//左
                if(StringUtils.isEquals("ABC",str)){
                    csxwZhiling = "0101";
                    textView.setText("C");
                }else if(StringUtils.isEquals("C",str)){
                    textView.setText("B");
                    csxwZhiling = "0102";
                }else if(StringUtils.isEquals("B",str)){
                    textView.setText("A");
                    csxwZhiling = "0104";
                }else if(StringUtils.isEquals("A",str)){
                    textView.setText("BC");
                    csxwZhiling = "0203";
                }else if(StringUtils.isEquals("BC",str)){
                    textView.setText("AC");
                    csxwZhiling = "0205";
                }else if(StringUtils.isEquals("AC",str)){
                    textView.setText("AB");
                    csxwZhiling = "0206";
                }else if(StringUtils.isEquals("AB",str)){
                    textView.setText("ABC");
                    csxwZhiling = "0307";
                }
            }
        }else{//type==1
            if(zyType==1){//右
                if(StringUtils.isEquals("AB",str)){
                    textView.setText("AC");
                    csxwZhiling = "0205";
                }else if(StringUtils.isEquals("AC",str)){
                    textView.setText("BC");
                    csxwZhiling = "0203";
                }else if(StringUtils.isEquals("BC",str)){
                    textView.setText("A");
                    csxwZhiling = "0104";
                }else if(StringUtils.isEquals("A",str)){
                    textView.setText("B");
                    csxwZhiling = "0102";
                }else if(StringUtils.isEquals("B",str)){
                    textView.setText("C");
                    csxwZhiling = "0101";
                }else if(StringUtils.isEquals("C",str)){
                    textView.setText("AB");
                    csxwZhiling = "0206";
                }
            }else{
                if(StringUtils.isEquals("AB",str)){
                    textView.setText("C");
                    csxwZhiling = "0101";
                }else if(StringUtils.isEquals("C",str)){
                    textView.setText("B");
                    csxwZhiling = "0102";
                }else if(StringUtils.isEquals("B",str)){
                    textView.setText("A");
                    csxwZhiling = "0104";
                }else if(StringUtils.isEquals("A",str)){
                    textView.setText("BC");
                    csxwZhiling = "0203";
                }else if(StringUtils.isEquals("BC",str)){
                    textView.setText("AC");
                    csxwZhiling = "0205";
                }else if(StringUtils.isEquals("AC",str)){
                    textView.setText("AB");
                    csxwZhiling = "0206";
                }
            }

        }
        Log.e("测试相数==",csxwZhiling);
        return csxwZhiling;
    }

    /**
     * 根据量程内容得到指令
     * @param str
     * @return
     */
    public static String getLiangcheng(String str){
        String lcZl = "";
        if(StringUtils.isEquals("10Ω(2.0A)",str)){
            lcZl = "0A00";

        }else if(StringUtils.isEquals("20Ω(1.0A)",str)){
            lcZl = "1400";

        }else if(StringUtils.isEquals("40Ω(0.5A)",str)){
            lcZl = "2800";

        }else if(StringUtils.isEquals("100Ω(0.2A)",str)){
            lcZl = "6400";

        }
        return lcZl;
    }

    /**
     * 根据时长内容得到指令
     * @param str
     * @return
     */
    public static String getShichang(String str){
        String scZl = "";
        if(StringUtils.isEquals("120.0ms",str)){
            scZl = "6009";
        }else if(StringUtils.isEquals("320.0ms",str)){
            scZl = "0019";
        }
        return scZl;
    }

    /**
     * 根据灵敏度内容得到指令
     * @param str
     * @return
     */
    public static String getLmd(String str){
        String lmdZl = "";
        int strInt = 0;
        strInt = StringUtils.strToInt(str);
        Log.e("===",strInt+"");
        if(strInt<16){
            lmdZl = "0"+ShiOrShiliu.toHexString(strInt)+"00";
            Log.e("==1=",lmdZl);
        }else{
            lmdZl = ShiOrShiliu.toHexString(strInt)+"00";
            Log.e("==2=",lmdZl);
        }
        return lmdZl;
    }
    /**
     * 根据试品编号内容得到指令
     * 试品编号字符串转为十六进制
     * @param str
     * @return
     */
    public static String getSpbh(String str){
        String spbhZl = "";
        if(str.length()==0){
            spbhZl = "000000000000000000000000";
        }else if(str.length()==1){
            spbhZl = BytesToHexString.str2HexStr(str)+"0000000000000000000000";
        }else if(str.length()==2){
            spbhZl = BytesToHexString.str2HexStr(str)+"00000000000000000000";
        }else if(str.length()==3){
            spbhZl = BytesToHexString.str2HexStr(str)+"000000000000000000";
        }else if(str.length()==4){
            spbhZl = BytesToHexString.str2HexStr(str)+"0000000000000000";
        }else if(str.length()==5){
            spbhZl = BytesToHexString.str2HexStr(str)+"00000000000000";
        }else if(str.length()==6){
            spbhZl = BytesToHexString.str2HexStr(str)+"000000000000";
        }else if(str.length()==7){
            spbhZl = BytesToHexString.str2HexStr(str)+"0000000000";
        }else if(str.length()==8){
            spbhZl = BytesToHexString.str2HexStr(str)+"00000000";
        }else if(str.length()==9){
            spbhZl = BytesToHexString.str2HexStr(str)+"000000";
        }else if(str.length()==10){
            spbhZl = BytesToHexString.str2HexStr(str)+"0000";
        }else if(str.length()==11){
            spbhZl = BytesToHexString.str2HexStr(str)+"00";
        }else if(str.length()==12){
            spbhZl = BytesToHexString.str2HexStr(str);
        }
        return spbhZl;
    }
    /**
     * 根据测试分接内容得到指令
     * @param str
     * @return
     */
    public static String getCsfj(String str){
        String csfjZl = "";
        csfjZl = str+"00";
        return csfjZl;
    }
    /**
     * 根据连接方式内容得到指令
     * @param str
     * @return
     */
    public static String getLjfs(String str){
        String ljfsZl = "";
        if(StringUtils.isEquals("YN",str)){
            ljfsZl = "0000";
        }else if(StringUtils.isEquals("Y",str)){
            ljfsZl = "0100";
        }else if(StringUtils.isEquals("△",str)){
            ljfsZl = "0200";
        }
        return ljfsZl;
    }
    /**
     * 根据测试相数内容得到指令
     * @param str
     * @return
     */
    public static String getCsxs(String str){
        String csxsZl = "";
        if(StringUtils.isEquals("ABC",str)){
            csxsZl = "0307";
        }else if(StringUtils.isEquals("C",str)){
            csxsZl = "0101";
        }else if(StringUtils.isEquals("B",str)){
            csxsZl = "0102";
        }else if(StringUtils.isEquals("BC",str)){
            csxsZl = "0203";
        }else if(StringUtils.isEquals("A",str)){
            csxsZl = "0104";
        }else if(StringUtils.isEquals("AC",str)){
            csxsZl = "0205";
        }else if(StringUtils.isEquals("AB",str)){
            csxsZl = "0206";
        }
        return csxsZl;
    }
}
