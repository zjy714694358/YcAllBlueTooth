package com.yc.allbluetooth.config;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Date:2022/11/3 15:30
 * author:jingyu zheng
 */
public class Config {
    /**
     * 定位权限动态申请状态
     */
    public static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    /**
     * 定位权限手动打开状态
     */
    public static final int REQUEST_CODE_LOCATION_SETTINGS = 2;
    /**
     * 蓝牙搜索权限
     */
    public static final int REQUEST_CODE_BLUETOOTH_SCAN = 3;
    /**
     * 蓝牙连接权限
     */
    public static final int REQUEST_CODE_BLUETOOTH_CONNECT = 4;
    /**
     * 蓝牙搜索完成，按钮变成开始搜索
     */
    public static final int BLUETOOTH_SEARCH_END = 10;
    /**
     * 接收到数据
     */
    public static final int BLUETOOTH_GETDATA = 100;
    /**
     * 数据发送超时
     */
    public static final int BLUETOOTH_SEND_CHAOSHI = 0;
    /**
     * 蓝牙连接断开
     */
    public static final int BLUETOOTH_LIANJIE_DUANKAI = -1;
    /**
     * 1:中文；2：英文
     */
    public static int CN_US_TYPE = 1;
    /**
     * 中、英文====》zh、en
     */
    public static String zyType = "zh";
    /**
     * 仪器设置：试品编号==》仪器编号
     */
    public static String spbh = "";
    /**
     * 折算温度：默认75
     */
    public static String zswd = "75";
    /**
     * 绕组材质 0:铜(235)；1：铝(225)
     */
    public static int rzczInt = 235;
    /**
     * 绕组材质 铜、铝
     */
    public static String rzczStr = "08";
    /**
     * 测试温度---三通道
     */
    public static String cswd = "20";
    /**
     * 测试温度---直阻单通道
     */
    public static String cswd2 = "25";
    /**
     * 分接位置
     */
    public static String fjwz = "02";
    /**
     * 数据管理每次获取数据的条数状态值，获取一次，记录一次，下次加1
     * 默认从0开始
     */
    public static int getSjglListType = 0;
    /**
     * 数据管理每次获取数据的条数ID值，获取一次，记录一次，下次加1
     * 默认从0开始 ID
     */
    public static int getSjglListId = 100;
    /**
     * 所有已添加过Fragment集合
     */
    public static List<Fragment>fragmentList = new ArrayList<>();
    /**
     * YN测试A0电阻值
     */
    public static String ynA0Csdz = "";
    /**
     * YN测试B0电阻值
     */
    public static String ynB0Csdz = "";
    /**
     * YN测试C0电阻值
     */
    public static String ynC0Csdz = "";
    /**
     * Dy测试Ab电阻值
     */
    public static String dyAbCsdz = "";
    /**
     * Dy测试Bc电阻值
     */
    public static String dyBcCsdz = "";
    /**
     * Dy测试Ca电阻值
     */
    public static String dyCaCsdz = "";
    /**
     * YN测试A0电阻值单位
     */
    public static String ynA0CsdzDw = "";
    /**
     * YN测试B0电阻值单位
     */
    public static String ynB0CsdzDw = "";
    /**
     * YN测试C0电阻值单位
     */
    public static String ynC0CsdzDw = "";
    /**
     * Dy测试Ab电阻值单位
     */
    public static String dyAbCsdzDw = "";
    /**
     * Dy测试Bc电阻值单位
     */
    public static String dyBcCsdzDw = "";
    /**
     * Dy测试Ca电阻值单位
     */
    public static String dyCaCsdzDw = "";
    /**
     * YN测试A0折算电阻值
     */
    public static String ynA0CsZsdz = "";
    /**
     * YN测试B0折算电阻值
     */
    public static String ynB0CsZsdz = "";
    /**
     * YN测试C0折算电阻值
     */
    public static String ynC0CsZsdz = "";
    /**
     * Dy测试Ab折算电阻值
     */
    public static String dyAbCsZsdz = "";
    /**
     * Dy测试Bc折算电阻值
     */
    public static String dyBcCsZsdz = "";
    /**
     * Dy测试Ca折算电阻值
     */
    public static String dyCaCsZsdz = "";
    /**
     * YN测试A0折算电阻值单位
     */
    public static String ynA0CsZsdzDw = "";
    /**
     * YN测试B0折算电阻值单位
     */
    public static String ynB0CsZsdzDw = "";
    /**
     * YN测试C0折算电阻值单位
     */
    public static String ynC0CsZsdzDw = "";
    /**
     * Dy测试Ab折算电阻值单位
     */
    public static String dyAbCsZsdzDw = "";
    /**
     * Dy测试Bc折算电阻值单位
     */
    public static String dyBcCsZsdzDw = "";
    /**
     * Dy测试Ca折算电阻值单位
     */
    public static String dyCaCsZsdzDw = "";
    /**
     * 蓝牙连接地址（mac）
     */
    public static String lyAddress = "";
    /**
     * int btnType = 0;//点击保存、打印、停止按钮对应的状态；保存：1；打印：2；停止：3;换相：4
     */
    public static int btnType = 0;
    /**
     * 不平衡率是否开启的状态，默认0不开启，1开启
     */
    public static int bphlType = 0;
    /**
     * 测试选择的电流值，直阻测试电流状态
     */
    public static String zzCsDlType = "";
    /**
     * 默认0没进过，或者第一次；第二次改为1
     * 第一次进入零线测试不展示B、C两相电流值，第一次走完0改为1
     */
    public static int lxType = 0;
    /******************************************************************不平衡率**************************************************************************/
    /**
     * A0电阻值
     */
    public static String A0Value = "";
    /**
     * B0电阻值
     */
    public static String B0Value = "";
    /**
     * C0电阻值
     */
    public static String C0Value = "";
    /**
     * AB电阻值
     */
    public static String ABValue = "";
    /**
     * BC电阻值
     */
    public static String BCValue = "";
    /**
     * CA电阻值
     */
    public static String CAValue = "";
    /**
     * A电阻单位
     */
    public static String AValueDw = "";
    /**
     * B电阻值单位
     */
    public static String BValueDw = "";
    /**
     * C电阻值单位
     */
    public static String CValueDw = "";
    /**
     * 绕组（保存）
     */
    public static String RzValue = "";
    /**
     * 分接位置（保存）
     */
    public static String FjValue = "";
    /**
     * 页面跳转Type（A0orAB）
     * 0:A0;1:AB;
     */
    public static int TzType = 0;
    /**
     * 当前测试电流值
     */
    public static String CsDlValue = "";
    /**
     * 当前测试电流单位
     */
    public static String CsDlValueDw = "";
    /**
     * 跳转前的最后一个测试相位(相别)
     */
    public static String CsXb = "";
    /**********************************************************************短路阻抗***************************************************************************/
    public static int Clms = 0;
    /**
     * 施加电压Ab
     */
    public static String sjdyAb = "";
    /**
     * 施加电压Bc
     */
    public static String sjdyBc = "";
    /**
     * 施加电压Ca
     */
    public static String sjdyCa = "";
    /**
     * 施加电流Ab
     */
    public static String sjdlAb = "";
    /**
     * 施加电流Bc
     */
    public static String sjdlBc = "";
    /**
     * 施加电流Ca
     */
    public static String sjdlCa = "";
    /**
     * 测量相角Ab
     */
    public static String clxjAb = "";
    /**
     * 测量相角Bc
     */
    public static String clxjBc = "";
    /**
     * 测量相角Ca
     */
    public static String clxjCa = "";
    /**
     * 有功功率Ab
     */
    public static String ygglAb = "";
    /**
     * 有功功率Bc
     */
    public static String ygglBc = "";
    /**
     * 有功功率Ca
     */
    public static String ygglCa = "";
    /**
     * 短路阻抗Ab
     */
    public static String dlzkAb = "";
    /**
     * 短路阻抗Bc
     */
    public static String dlzkBc = "";
    /**
     * 短路阻抗Ca
     */
    public static String dlzkCa = "";
    /**
     * 短路感抗Ab
     */
    public static String dlgkAb = "";
    /**
     * 短路感抗Bc
     */
    public static String dlgkBc = "";
    /**
     * 短路感抗Ca
     */
    public static String dlgkCa = "";
    /**
     * 绕组电感Ab
     */
    public static String rzdgAb = "";
    /**
     * 绕组电感Bc
     */
    public static String rzdgBc = "";
    /**
     * 绕组电感Ca
     */
    public static String rzdgCa = "";
    /**
     * 阻抗电压Ab
     */
    public static String zkdyAb = "";
    /**
     * 阻抗电压Bc
     */
    public static String zkdyBc = "";
    /**
     * 阻抗电压Ca
     */
    public static String zkdyCa = "";
    /**
     * 阻抗电压ZK%
     */
    public static String zkdyZk = "";
    /**
     * 阻抗误差DZk%
     */
    public static String zkwcDZk = "";
    /**
     * 短路阻抗，数据处理中避免与上一次发送同一条数据，做记录对比
     */
    public static String sjclSendStr = "";
    /**
     * 记录铭牌阻抗输入的值，后期计算三相汇总的阻抗误差
     */
    public static String mpzk = "";
    /**
     * 总阻抗电压百分数
     */
    public static String allZkdyBfs = "";
    /**
     * 总阻抗电压百分数(保留十二位)
     */
    public static String allZkdyBfs2 = "";
    /**
     * 施加电压A0
     */
    public static String sjdyA0 = "";
    /**
     * 施加电压B0
     */
    public static String sjdyB0 = "";
    /**
     * 施加电压C0
     */
    public static String sjdyC0 = "";
    /**
     * 施加电流A0
     */
    public static String sjdlA0 = "";
    /**
     * 施加电流B0
     */
    public static String sjdlB0 = "";
    /**
     * 施加电流C0
     */
    public static String sjdlC0 = "";
    /**
     * 测量阻抗A0
     */
    public static String clzkA0 = "";
    /**
     * 测量阻抗B0
     */
    public static String clzkB0 = "";
    /**
     * 测量阻抗C0
     */
    public static String clzkC0 = "";
    /**
     * 测量电抗A0
     */
    public static String cldkA0 = "";
    /**
     * 测量电抗B0
     */
    public static String cldkB0 = "";
    /**
     * 测量电抗C0
     */
    public static String cldkC0 = "";
    /**
     * 漏电感A0
     */
    public static String ldgA0 = "";
    /**
     * 漏电感B0
     */
    public static String ldgB0 = "";
    /**
     * 漏电感C0
     */
    public static String ldgC0 = "";
    /**
     * 阻抗电压A0
     */
    public static String zkdyA0 = "";
    /**
     * 阻抗电压B0
     */
    public static String zkdyB0 = "";
    /**
     * 阻抗电压C0
     */
    public static String zkdyC0 = "";
    /**
     * 记录铭牌阻抗输入的值，后期计算三相汇总的阻抗误差
     */
    public static String mpzkDx = "";
    /**
     * 总阻抗电压百分数
     */
    public static String allDxZkdyBfs = "";
    /**
     * 总阻抗电压百分数(保留十二位)
     */
    public static String allDxZkdyBfs2 = "";


    /*********************************************三通道新增****************************************************/
    /**
     * 跳转判断，不然一直跳，0跳转，1不跳
     */
    public static int tiaozhuan = 0;
    /**
     * 三通道不平衡率
     */
    public static String bphl = "";
    /**
     * 进入页面赋值，或者onResume赋值，必须保证当前页面
     */
    public static String ymType = "";
    /**
     * 记录仪器类型（三通道的型号）仪器类型 0x31三通道直阻，0x32单通道直阻（10c）0x33短路阻抗
     * 31==>10A；34==>20A；35==>40A；36==>50A；有载：37===>1A;38====>2A
     */
    public static String yqlx = "";
    /**
     * 测试电流（代传，传到第二个页面显示在顶部）
     */
    public static String csdlStr = "";
    /**
     * 是否点击直阻测试，看直阻测试返回值；0未点击，1点击
     * 只在三通道直阻测试中使用，避免返回第一条指令后，一直进入测试的尴尬
     */
    public static int zzcsBtnType = 0;
    public static int yemianType2 = 0;
    /**
     * 放电弹窗次数
     */
    public static int fdTcCishu = 0;
    /**
     * 保存弹窗次数
     */
    public static int bcTcCishu = 0;
    /**
     * 打印弹窗次数
     */
    public static int dyTcCishu = 0;
    /**
     * 三通道零线电阻，0未启用，1启用
     */
    public static int isStdLingxian = 0;
    /**
     * 三通道测试指令，不带零线电阻的
     */
    public static String stdNoLingxianZhiling = "";
    /**
     * 1：三通道，2：YN，3：DY
     */
    public static int homeQiehuan = 1;
    /*********************************************有载****************************************************/
    /**
     * 本机地址：04-->1A;05-->2A;
     */
    public static String yzBenjiAddress = "04";
    /**
     * 是否添加CRC校验？默认0：不添加；1：添加
     */
    public static int yzCrcTYpe = 0;
    /***************************************************************************************************/
    /**
     * 搜索\查询的UUID：fff0
     * 服务UUID:0xFFE0
     * 透传\读写UUID：0xFFE1
     */
    public static String sousuoUuid = "0000fff0-0000-1000-8000-00805f9b34fb";
}
