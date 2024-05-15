package com.yc.allbluetooth.utils;

import java.math.BigDecimal;

/**
 * 作者： zjingyu on 2022/9/21 15:11
 *
 * 小数运算：加减乘除
 **/
public class XiaoshuYunsuan {
    /**
     * 字符串转小数
     * @param str
     * @return
     */
    public BigDecimal xiaoshu(String str){
        BigDecimal bigDecimal = new BigDecimal(str);
        return bigDecimal;
    }

    /**
     * 小数加法
     * @param b1
     * @param b2
     * @return
     */
    public BigDecimal xiaoshuJia(BigDecimal b1,BigDecimal b2){
        BigDecimal b3 = b1.add(b2);
        return b3;
    }

    /**
     * 小数减法
     * @param b1
     * @param b2
     * @return
     */
    public BigDecimal xiaoshuJian(BigDecimal b1,BigDecimal b2){
        BigDecimal b3 = b1.subtract(b2);
        return b3;
    }

    /**
     * 小数乘法
     * @param b1
     * @param b2
     * @return
     */
    public double xiaoshuCheng(BigDecimal b1,BigDecimal b2){
        double b3 = b1.multiply(b2).doubleValue();
        return b3;
    }

    /**
     * 小数除法保留4位
     * @param b1
     * @param b2
     * @return
     */
    public BigDecimal xiaoshuChu(BigDecimal b1,BigDecimal b2){
        BigDecimal b3 = b1.divide(b2,4,BigDecimal.ROUND_HALF_UP);
        return b3;
    }
    /**
     * 小数除法保留两位小数
     * @param b1
     * @param b2
     * @return
     */
    public BigDecimal xiaoshuChu2(BigDecimal b1,BigDecimal b2){
        BigDecimal b3 = b1.divide(b2,2,BigDecimal.ROUND_HALF_UP);
        return b3;
    }
    /**
     * 小数除法保留六位
     * @param b1
     * @param b2
     * @return
     */
    public BigDecimal xiaoshuChu6(BigDecimal b1,BigDecimal b2){
        BigDecimal b3 = b1.divide(b2,6,BigDecimal.ROUND_HALF_UP);
        return b3;
    }

    /**
     *小数比较大小
     * @return
     */
    public boolean bijiao(BigDecimal b1,BigDecimal b2){
        boolean b;
        if(b1.compareTo(b2)>0){
            b = true;
        }else{
            b = false;
        }
        return b;
    }
    /**
     *三个小数中最大值与最小值的差
     * @return
     */
    public BigDecimal  bijiaoDaxiaoCha(BigDecimal b1,BigDecimal b2,BigDecimal b3){
        BigDecimal b = null;
        if(b1.compareTo(b2)>0){//1>2
            if(b1.compareTo(b3)>0){//1>3
                if(b2.compareTo(b3)>0){//2>3;1-3
                    b=xiaoshuJian(b1,b3);
                }else{//2<3;1-2
                    b=xiaoshuJian(b1,b2);
                }
            }else{//1<3;3-2
                b=xiaoshuJian(b3,b2);
            }
        }else if(b1.compareTo(b3)>0){//1>3
            if(b1.compareTo(b2)>0){//1>2
                if(b2.compareTo(b3)>0){//2>3;1-3
                    b=xiaoshuJian(b1,b3);
                }else{//2<3;1-2
                    b=xiaoshuJian(b1,b2);
                }
            }else{//1<3;3-2
                b=xiaoshuJian(b2,b3);
            }
        }else if(b2.compareTo(b1)>0){//2>1
            if(b2.compareTo(b3)>0){//2>3
                if(b1.compareTo(b3)>0){//1>3;2-3
                    b=xiaoshuJian(b2,b3);
                }else{
                    b=xiaoshuJian(b2,b1);
                }
            }else{
                b=xiaoshuJian(b3,b1);
            }
        }else if(b2.compareTo(b3)>0){//2>3
            if(b2.compareTo(b1)>0){//2>1
                if(b1.compareTo(b3)>0){//1>3;2-3
                    b=xiaoshuJian(b2,b3);
                }else{
                    b=xiaoshuJian(b2,b1);
                }
            }else{
                b=xiaoshuJian(b1,b3);
            }
        }else if(b3.compareTo(b1)>0){//3>1
            if(b3.compareTo(b2)>0){
                if(b1.compareTo(b2)>0){
                    b = xiaoshuJian(b3,b2);
                }else{
                    b = xiaoshuJian(b3,b1);
                }
            }else{
                b = xiaoshuJian(b2,b1);
            }
        }else if(b3.compareTo(b2)>0){//3>2
            if(b3.compareTo(b1)>0){
                if(b1.compareTo(b2)>0){
                    b = xiaoshuJian(b3,b2);
                }else{
                    b = xiaoshuJian(b3,b1);
                }
            }else{
                b = xiaoshuJian(b1,b2);
            }
        }else{//三相值相等
            b=xiaoshu("0");
        }
        return b;
    }
}
