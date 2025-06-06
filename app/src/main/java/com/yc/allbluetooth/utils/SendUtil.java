package com.yc.allbluetooth.utils;

import android.util.Log;

import androidx.fragment.app.Fragment;

import com.yc.allbluetooth.config.Config;
import com.yc.allbluetooth.crc.CrcUtil;
import com.yc.allbluetooth.std.entity.TestSet;
import com.yc.allbluetooth.std.util.Zhiling;
import com.yc.allbluetooth.std.util.ZzCsStartCs;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.List;

/**
 * Date:2022/11/17 16:16
 * author:jingyu zheng
 */
public class SendUtil {
    /**************************************************************三通道*******************************************************************/
    /**
     *
     * @param zzcsType
     * @param fragment
     * @param zhiling 指令：0x6b 测试命令，0x69 停止测试，0x6d保存数据，0x6c 打印命令 0x6a换相测试，0x6e 召唤历史数据
     * @return
     */
    public static String send(int zzcsType, Fragment fragment,String zhiling){
        Zhiling zhilingSave = new Zhiling();
        String csfsZlSave = zhilingSave.ceshifangfa(zzcsType+"");

        List<TestSet> listSave = ZzCsStartCs.getChildData(fragment,zzcsType);

        String xwSave = listSave.get(0).getXw();
        String zcSave = listSave.get(0).getZc();
        String dlSave = listSave.get(0).getDl();
        String dzSave = listSave.get(0).getDz();

        String xwZlStdSave = zhilingSave.ceshixiangwei(xwSave);
        String zcZlStdSave = zhilingSave.zhuciqidong(zcSave);
        String dlZlStdSave = zhilingSave.ceshidianliu(dlSave);
        String dzZlStdSave = zhilingSave.zhuciqidong(dzSave);
//        Log.e("dyDl==",listSave.toString());
//        Log.e("dyDl==",csfsZlSave+","+dlSave+","+xwSave+","+dzSave+","+zcSave);

        String strStdCsAllStdSave = "6886"+zhiling+csfsZlSave+xwZlStdSave+zcZlStdSave+dlZlStdSave+"00"+"00"+"0000";//+"00"+"00"+"0000"
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        Log.e("fasong:",CrcUtil.getTableCRC(bytesStdSave));
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        return sendAllYnSave;
    }

    /**
     * 消磁功能测试
     * @param zhiling 72:开始消磁；69：停止消磁；
     * @param xwStr 消磁相位：00=》A0；01=》B0；02=》C0；03=》ab；04=》bc；05=》ca；
     * @return
     */
    public static String xcSend(String zhiling,String xwStr){
        String strStdCsAllStdSave = "6886"+zhiling+"00"+xwStr+"06"+"00"+"00"+"00"+"0000"+"00";//+00+
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("xcfasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 下发仪器编号三通道
     * @param zhiling   仪器指令 70
     * @param yqbhStr 试品编号
     * @return
     */
    public static String yiqibianhaoSend_std(String zhiling,String yqbhStr){
        String strStdCsAllStdSave = "";
        if(StringUtils.isEmpty(yqbhStr)){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffffffffff"+"00";
        }else if(yqbhStr.length()==1){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffffffff"+"00";
        }else if(yqbhStr.length()==2){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffffff"+"00";
        }else if(yqbhStr.length()==3){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffff"+"00";
        }else if(yqbhStr.length()==4){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffff"+"00";
        }else if(yqbhStr.length()==5){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffff"+"00";
        }else if(yqbhStr.length()==6){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffff"+"00";
        }else if(yqbhStr.length()==7){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ff"+"00";
        }else if(yqbhStr.length()==8){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"00";
        }
        //strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr);
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("仪器编号fasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**************************************************************10C直阻*******************************************************************/
    /**
     *
     * @param zhiling 指令：0x6a 直阻页面，0x71 开启不平衡率，0x72 关闭不平衡率，0x73 直阻测试1返回，0x86 保存，
     *               0x87 打印，0x88 停止,0x6e 数据界面返回（记录查询）
     * @return
     */
    public static String initSend(String zhiling){

        String strStdCsAllStdSave = "6886"+zhiling+"000000000000"+"0000"+"00";//+00+
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("fasong:",sendAllYnSave);
        return sendAllYnSave;
    }

    /**
     * 启动测试
     * @param zhiling 70
     *
     * @param dlStr 消磁相位：00=》10A；01=》5A；02=》1A；03=》100mA；04=》自动；05=》10mA；
     * @return
     */
    public static String startCsSend(String zhiling,String dlStr){
        String strStdCsAllStdSave = "6886"+zhiling+"000000"+dlStr+"0000"+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("startCsfasong:",sendAllYnSave);
        return sendAllYnSave;
    }

    /**
     * 测试结果页的变换
     * @param zhiling   0x80 绕组变换；  0x81 相别变换；  0x82  分节变换（分接）
     * 0x83  绕组材料变换
     * 0x84  测试温度
     * 0x85  折算温度
     * @param rzStr
     * 0x06高压绕组，0x07中压绕组，0x08低压绕组
     * 0x00 AO，0x01 BO，0x02 CO，0x03 AB，0x04 BC，0x05 AC（测试相位）
     * 0x xx  测试温度，折算温度
     * 0x09   铜，0x0a  铝
     * 0x xx分节位置
     * @return
     */
    public static String bianhuanSend(String zhiling,String rzStr){
        String strStdCsAllStdSave = "6886"+zhiling+rzStr+"0000"+"00"+"0000"+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("rzfasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 记录查询 6b
     * @param zhiling
     * @param type
     * @return
     */
    public static String jiluchaxunSend(String zhiling,int type){
        String strStdCsAllStdSave = "";
        if(type==0){//00   16~20
            strStdCsAllStdSave = "6886"+zhiling+"00000000"+"000"+type+"0000";
        }else{
            //Config.getSjglListType+=1;
            if(type<10){
                strStdCsAllStdSave = "6886"+zhiling+"00000000"+"000"+type+"0000";
            }else if(type>=10&&type<16){
                strStdCsAllStdSave = "6886"+zhiling+"00000000"+"000"+ ShiOrShiliu.toHexString(type)+"0000";
            }else if(type>=16&&type<=255){
                strStdCsAllStdSave = "6886"+zhiling+"00000000"+"00"+ShiOrShiliu.toHexString(type)+"0000";
            }else if(type>255&&type<4096){
                strStdCsAllStdSave = "6886"+zhiling+"00000000"+"0"+ShiOrShiliu.toHexString(type)+"0000";
            } else if (type>=4096&&type<=65535) {
                strStdCsAllStdSave = "6886"+zhiling+"00000000"+ShiOrShiliu.toHexString(type)+"0000";
            }
        }
        //strStdCsAllStdSave = "6886"+zhiling+"0000000000"+sjStr+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("jlcxfasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 记录查询 6b
     * @param zhiling
     * @param type
     * @return
     */
    public static String jiluchaxunSendBb(String zhiling,int type){
        String strStdCsAllStdSave = "";
        if(type==0){//00   16~20
            strStdCsAllStdSave = "6886"+zhiling+"000"+type+"000000000000";
        }else{
            //Config.getSjglListType+=1;
            if(type<10){
                strStdCsAllStdSave = "6886"+zhiling+"000"+type+"000000000000";
            }else if(type>=10&&type<16){
                strStdCsAllStdSave = "6886"+zhiling+"000"+ ShiOrShiliu.toHexString(type)+"000000000000";
            }else if(type>=16&&type<=255){
                strStdCsAllStdSave = "6886"+zhiling+"00"+ShiOrShiliu.toHexString(type)+"000000000000";
            }else if(type>255&&type<4096){
                strStdCsAllStdSave = "6886"+zhiling+"0"+ShiOrShiliu.toHexString(type)+"000000000000";
            } else if (type>=4096&&type<=65535) {
                strStdCsAllStdSave = "6886"+zhiling+ShiOrShiliu.toHexString(type)+"00000000"+"0000";
            }
        }
        //strStdCsAllStdSave = "6886"+zhiling+"0000000000"+sjStr+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("jlcxfasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 下发仪器编号
     * @param zhiling   仪器指令 6c
     * @param yqbhStr 仪器编号
     * @return
     */
    public static String yiqibianhaoSend(String zhiling,String yqbhStr){
        String strStdCsAllStdSave = "";
        if(StringUtils.isEmpty(yqbhStr)){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffffffffff";
        }else if(yqbhStr.length()==1){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffffffff";
        }else if(yqbhStr.length()==2){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffffff";
        }else if(yqbhStr.length()==3){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffffff";
        }else if(yqbhStr.length()==4){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffffff";
        }else if(yqbhStr.length()==5){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffffff";
        }else if(yqbhStr.length()==6){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ffff";
        }else if(yqbhStr.length()==7){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr)+"ff";
        }else if(yqbhStr.length()==8){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(yqbhStr);
        }
        //strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr);
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("仪器编号fasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 下发时间
     * @param zhiling   仪器指令 6c
     * @param sjStr 时间
     * @return
     */
    public static String shijianSend(String zhiling,String sjStr){
        String strStdCsAllStdSave = "6886"+zhiling+sjStr+"0000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("时间fasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**************************************************************短路阻抗*******************************************************************/
    /**
     * 短路阻抗参数设置，四个字节的(需要高低位转换（HexUtil.reverseHex）)
     * @param zhiling
     * @param csStr
     * @return
     */
    public static String dlzkCanshuShuruSizijieSend(String zhiling,String csStr){
        //float f=Float.parseFloat(csStr);
        //Log.e("---", Integer.toHexString(Float.floatToRawIntBits(Float.parseFloat(csStr))));
        //Integer.toHexString(Float.floatToIntBits(f))
        String strStdCsAllStdSave = "6886"+zhiling+ HexUtil.reverseHex(HexUtil.fToHex(Float.parseFloat(csStr)))+"00000000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("参数fasong:",sendAllYnSave);
        return sendAllYnSave;
    } /**
     * 参数设置，四个字节的(不需要高低位转换（HexUtil.reverseHex）)
     * @param zhiling
     * @param csStr
     * @return
     */
    public static String ShuruSizijieSend(String zhiling,String csStr){
        //float f=Float.parseFloat(csStr);
        //Log.e("---", Integer.toHexString(Float.floatToRawIntBits(Float.parseFloat(csStr))));
        //Integer.toHexString(Float.floatToIntBits(f))
        String strStdCsAllStdSave = "6886"+zhiling+ HexUtil.fToHex(Float.parseFloat(csStr))+"00000000";
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("参数fasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 短路阻抗参数设置，单字节的
     * @param zhiling
     * @param csStr
     * @return
     */
    public static String dlzkCanshuShuruDanzijieSend(String zhiling,String csStr){
        //String strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"00000000000000";
        String strCsAllSave = "6886"+zhiling+ csStr+"00000000000000";
        byte[] bytesStdSave = new BigInteger(strCsAllSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllSave = strCsAllSave + crcStdSave;
        Log.e("参数fasong:",sendAllSave);
        return sendAllSave;
    }
    /**
     * 短路阻抗参数设置，八字节的
     * @param zhiling
     * @param csStr
     * @return
     */
    public static String dlzkCanshuShuruBazijieSend(String zhiling,String csStr){
        //float f=Float.parseFloat(csStr);
        //Log.e("---", Integer.toHexString(Float.floatToRawIntBits(Float.parseFloat(csStr))));
        String strStdCsAllStdSave = "";
        if(StringUtils.isEmpty(csStr)){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffffffffffffffff";
        }else if(csStr.length()==1){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffffffffffffff";
        }else if(csStr.length()==2){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffffffffffff";
        }else if(csStr.length()==3){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffffffffff";
        }else if(csStr.length()==4){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffffffff";
        }else if(csStr.length()==5){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffffff";
        }else if(csStr.length()==6){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ffff";
        }else if(csStr.length()==7){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr)+"ff";
        }else if(csStr.length()==8){
            strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr);
        }
        //strStdCsAllStdSave = "6886"+zhiling+ BytesToHexString.str2HexStr(csStr);
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("参数fasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 短路阻抗参数设置，六个字节的
     * @param zhiling
     * @param csStr
     * @return
     */
    public static String dlzkCanshuShuruLiuzijieSend(String zhiling,String csStr){
        String strStdCsAllStdSave = "";
        if(StringUtils.isEmpty(csStr)){
            strStdCsAllStdSave = "6886"+zhiling+ "ffffffffffff"+"0000";
        }else{
            try {
                String uniStr = ShiOrShiliu.enUnicode(csStr);
                if(uniStr.length()/2==1){
                    strStdCsAllStdSave = "6886"+zhiling+uniStr+"ffffffffff"+"0000";
                }else if(uniStr.length()/2==2){
                    strStdCsAllStdSave = "6886"+zhiling+uniStr+"ffffffff"+"0000";
                }else if(uniStr.length()/2==3){
                    strStdCsAllStdSave = "6886"+zhiling+uniStr+"ffffff"+"0000";
                }else if(uniStr.length()/2==4){
                    strStdCsAllStdSave = "6886"+zhiling+uniStr+"ffff"+"0000";
                }else if(uniStr.length()/2==5){
                    strStdCsAllStdSave = "6886"+zhiling+uniStr+"ff"+"0000";
                }else if(uniStr.length()/2==6){
                    strStdCsAllStdSave = "6886"+zhiling+uniStr+"0000";
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();

        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("参数fasong:",sendAllYnSave);
        return sendAllYnSave;
    }
    /**
     * 短路阻抗数据处理 89 (单字节)
     * @param zhiling
     * @param type 第几条数据
     * @return
     */
    public static String dlzkShujuSend(String zhiling,int type){
        String strStdCsAllStdSave = "";
        if(type<16){
            strStdCsAllStdSave = "6886"+zhiling+"0"+ShiOrShiliu.toHexString(type)+"0000000000"+"0000";
        }else if(type>=16&&type<=99){
            strStdCsAllStdSave = "6886"+zhiling+ ShiOrShiliu.toHexString(type)+"0000000000"+"0000";
        }
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("数据处理fasong:",sendAllYnSave);
        Log.e("数据处理config:", Config.sjclSendStr);
        if(StringUtils.isEquals(Config.sjclSendStr,sendAllYnSave)){
            return "";
        }else{
            Config.sjclSendStr = sendAllYnSave;
        }
        return sendAllYnSave;
    }
    //******************************************************************三通道**************************************************************************
    /**
     * 三通道通用
     * @param zhiling
     * @return
     */
    public static String initSendStd(String zhiling){

        String strStdCsAllStdSave = "6886"+zhiling+"000000000000"+"0000"+"00";//+00+
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllYnSave = strStdCsAllStdSave + crcStdSave;
        Log.e("fasong:",sendAllYnSave);
        return sendAllYnSave;
    }

    /**
     * 三通道后续添加：测试温度、折算温度、分接、绕组材料
     * @param zhiling
     * @param type
     * @return
     */
    public static String initSendStdNew(String zhiling,String type){
        String strStdCsAllStdSave = "6886"+zhiling+"00000000"+type+"00"+"0000"+"00";//+00+
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllSave = strStdCsAllStdSave + crcStdSave;
        Log.e("fasong:",sendAllSave);
        return sendAllSave;
    }
    /**
     * 变比后续添加：输入八字节
     * @param zhiling
     * @param type
     * @return
     */
    public static String initSendBianbiNew8(String zhiling,String type){
        String strStdCsAllStdSave = "6886"+zhiling+type;
        byte[] bytesStdSave = new BigInteger(strStdCsAllStdSave, 16).toByteArray();
        String crcStdSave = CrcUtil.getTableCRC(bytesStdSave);
        String sendAllSave = strStdCsAllStdSave + crcStdSave;
        Log.e("fasong:",sendAllSave);
        return sendAllSave;
    }
}
