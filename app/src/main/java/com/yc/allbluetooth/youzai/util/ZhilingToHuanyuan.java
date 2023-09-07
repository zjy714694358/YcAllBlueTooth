package com.yc.allbluetooth.youzai.util;

import com.yc.allbluetooth.utils.StringUtils;

/**
 * Date:2023/8/5 10:47
 * author:jingyu zheng
 * 通过指令还原成原展示数据
 */
public class ZhilingToHuanyuan {
    /**
     * 量程
     * @param zhiling
     * @return
     */
    public static String lc(String zhiling){
        String str = "";
        if(StringUtils.isEquals("0A00",zhiling)){
            str = "10Ω(2.0A)";
        }else if(StringUtils.isEquals("1400",zhiling)){
            str = "20Ω(1.0A)";
        }else if(StringUtils.isEquals("2800",zhiling)){
            str = "40Ω(0.5A)";
        }else if(StringUtils.isEquals("6400",zhiling)){
            str = "100Ω(0.2A)";
        }
        return str;
    }

    /**
     * 时长
     * 120ms档为2400（6009）；320ms档为6400（0019）
     * @param zhiling
     * @return
     */
    public static String sc(String zhiling){
        String str = "";
        if(StringUtils.isEquals("6009",zhiling)){
            str = "120ms";
        }else if(StringUtils.isEquals("0019",zhiling)){
            str = "320ms";
        }
        return str;
    }

    /**
     * 连接方式
     * YN为0x00，Y为0x01，Δ为0x02
     * @param zhiling
     * @return
     */
    public static String ljfs(String zhiling){
        String str = "";
        if(StringUtils.isEquals("0000",zhiling)){
            str = "YN";
        }else if(StringUtils.isEquals("0100",zhiling)){
            str = "Y";
        }else if(StringUtils.isEquals("0200",zhiling)){
            str = "Δ";
        }
        return str;
    }
    /**
     * 测试相数
     * C相为0x0101，B相为0x0201，BC相为0x0302，A相为0x0401，AC相为0x0502，AB相为0x0602，ABC相为0x0703。
     * @param zhiling
     * @return
     */
    public static String csxs(String zhiling){
        String str = "";
        if(StringUtils.isEquals("0101",zhiling)){
            str = "C";
        }else if(StringUtils.isEquals("0102",zhiling)){
            str = "B";
        }else if(StringUtils.isEquals("0203",zhiling)){
            str = "BC";
        }else if(StringUtils.isEquals("0104",zhiling)){
            str = "A";
        }else if(StringUtils.isEquals("0205",zhiling)){
            str = "AC";
        }else if(StringUtils.isEquals("0206",zhiling)){
            str = "AB";
        }else if(StringUtils.isEquals("0307",zhiling)){
            str = "ABC";
        }
        return str;
    }
}
