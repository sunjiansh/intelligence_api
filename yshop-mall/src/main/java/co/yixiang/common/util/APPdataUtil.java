package co.yixiang.common.util;

import co.yixiang.common.enums.TumbleCmdEnum;
import co.yixiang.modules.device.tumbledatarecords.domain.DTumbleDataRecords;
import org.junit.Test;

/**
 * @author sunjian
 * @Date 2023/2/2
 * @license 版权所有，非经许可请勿使用本代码
 */
public class APPdataUtil {


    public static String getCmd(String content){
        //example regeist AA550D06180D1001046006074220719301101089860620220076129639EF
        //      heart beat      String content = "AA550D021402C0003C1801AAAA4E9F700D54805301060005113C";
        String cmd = content.substring(6,8);
        return cmd;
    }

    public static String getData(String content){
       // String content = "AA550D021402C0003C1801AAAA4E9F700D54805301060005113C";
        String dataLength = content.substring(8,10);
        int length = Integer.parseInt(dataLength, 16);//长度单位是 字节
        String data = content.substring(10,10 + length * 2);
        return data;
    }

    /**
    * 判断上报的数据是否是报警类型
    * @param content
    * @return
    */
    public static boolean isAlarm(String content){
         String cmd = getCmd(content);
        return TumbleCmdEnum.ALARM.getValue().equals(cmd);
    }



    /**
     * 处理注册类型的书据，组装entity
     * @param records
     * @param appData
     * @return
     */
    public static void parseRegisterData(DTumbleDataRecords records,String appData){
        //appData = "AA550D06180D1001046006074220719301101089860620220076129639EF";
        String data = getData(appData);//
        String IMSI = data.substring(6,6+16);//0460060742207193
        String ICCID =data.substring(28,28+20);//89860620220076129639
        records.setImsi(IMSI);
        records.setIccid(ICCID);
    }

    /**
     * 处理报警类型的书据，组装entity
     * @param records
     * @param appData
     * @return
     */
    public static void parseAlarmData(DTumbleDataRecords records,String appData){
        //DTumbleDataRecords D = new DTumbleDataRecords();
        //String appData = "AA550D030A0AC0003C1801AAAA1400A1";
        String data = getData(appData);//0AC0003C1801AAAA1400
        String status = data.substring(0,4);//状态为2字节，需要高低字节调换后再转换成二进制字符来解析
        String senser_params = data.substring(4,4+8);//传感器参数（4字节),需要高低字节调换后再解析
        String battery_info  = data.substring(12,12+4);//电池电量（2字节）,需要高低字节调换后再解析,AAAA   表示设备外部供电
        String reserve_info = data.substring(16,16+4);//保留(2字节),低字节表示跌倒报警时长，比如05=5秒

        String reverse_status = HexUtil.reverseHex(status);
        String reverse_status_binary = HexUtil.hexToBinaryString(reverse_status);

        String reverse_senser_params =  HexUtil.reverseHex(senser_params);//01183C00
        String param_height_hex = reverse_senser_params.substring(0,4);//长度2字节
        String param_linger_time_hex  = reverse_senser_params.substring(4,4+2);
        String param_no_person_time_hex= reverse_senser_params.substring(6,6+2);


        String CSQ = reserve_info.substring(0,2);
        String fall_down_time_hex = reserve_info.substring(2,2+2);

        //为了方便解析二进制从左到右的顺序，将reverse_status_binary再reverse
        StringBuffer sb = new StringBuffer(reverse_status_binary);
        StringBuffer reverse_reverse_status_binary = sb.reverse();

        records.setIsPersonSwitch(String.valueOf(reverse_reverse_status_binary.charAt(15)));//有人报警开关
        records.setNoPersonSwitch(String.valueOf(reverse_reverse_status_binary.charAt(14)));//无人报警开关
        records.setFallDownSwitch(String.valueOf(reverse_reverse_status_binary.charAt(13)));//跌倒报警开关
        records.setLingerSwitch(String.valueOf(reverse_reverse_status_binary.charAt(12)));//驻留报警开关

        records.setLingerAlarm(String.valueOf(reverse_reverse_status_binary.charAt(5)));//驻留报警
        records.setFallDownAlarm(String.valueOf(reverse_reverse_status_binary.charAt(4)));//跌倒报警
        records.setIsPersonAlarm(String.valueOf(reverse_reverse_status_binary.charAt(3)));//有人报警
        records.setNoPersonAlarm(String.valueOf(reverse_reverse_status_binary.charAt(2)));//无人报警
        records.setExistPerson(String.valueOf(reverse_reverse_status_binary.charAt(1)));//无人/有人
        records.setIsError(String.valueOf(reverse_reverse_status_binary.charAt(0)));//故障




        records.setParamHight(String.valueOf(HexUtil.hexToDec(param_height_hex)));
        records.setParamLingerTime(String.valueOf(HexUtil.hexToDec(param_linger_time_hex)));
        records.setParamNoPersonTime(String.valueOf(HexUtil.hexToDec(param_no_person_time_hex)));
        records.setParamFallDownTime(String.valueOf(HexUtil.hexToDec(fall_down_time_hex)));//跌倒报警时长
        records.setParamBattery(battery_info);


        System.out.println(records.toString());
    }


    /**
     * 处理心跳类型的书据，组装entity
     * @param records
     * @param appData
     * @return
     */
    public static void parseHeartBeatData(DTumbleDataRecords records,String appData){
        //DTumbleDataRecords records= new DTumbleDataRecords();
       // String appData = "AA550D021402C0003C1801AAAA4E9F700D54805301060005113C";
        String data = getData(appData);
        String status = data.substring(0,4);//状态为2字节，需要高低字节调换后再转换成二进制字符来解析
        String senser_params = data.substring(4,4+8);//传感器参数（4字节),需要高低字节调换后再解析
        String battery_info  = data.substring(12,12+4);//电池电量（2字节）,需要高低字节调换后再解析,AAAA   表示设备外部供电
        String CELL_ID = data.substring(16,16+8);//CELL ID(4字节)，,需要高低字节调换后再解析
        String RSRP = data.substring(24,24+4);//RSRP(2字节)，,需要高低字节调换后再解析
        String PCI = data.substring(28,28+4);//PCI(2字节)，,需要高低字节调换后再解析
        String SINR = data.substring(32,32+4);//SINR(2字节)，,需要高低字节调换后再解析
        String ECL = data.substring(36,36+2);//ECL(1字节)
        String CSQ = data.substring(38,38+2);//CSQ(1字节)0AC008011801AAAA1605

        String reverse_status = HexUtil.reverseHex(status);
        String reverse_status_binary = HexUtil.hexToBinaryString(reverse_status);

        String reverse_senser_params =  HexUtil.reverseHex(senser_params);//01183C00
        String param_height_hex = reverse_senser_params.substring(0,4);//长度2字节
        String param_linger_time_hex = reverse_senser_params.substring(4,4+2);//驻留时长分钟
        String param_no_person_time_hex = reverse_senser_params.substring(6,6+2);//无人报警时长小时



        //为了方便解析二进制从左到右的顺序，将reverse_status_binary再reverse
        StringBuffer sb = new StringBuffer(reverse_status_binary);
        StringBuffer reverse_reverse_status_binary = sb.reverse();

        records.setIsPersonSwitch(String.valueOf(reverse_reverse_status_binary.charAt(15)));//有人报警开关
        records.setNoPersonSwitch(String.valueOf(reverse_reverse_status_binary.charAt(14)));//无人报警开关
        records.setFallDownSwitch(String.valueOf(reverse_reverse_status_binary.charAt(13)));//跌倒报警开关
        records.setLingerSwitch(String.valueOf(reverse_reverse_status_binary.charAt(12)));//驻留报警开关

        records.setLingerAlarm(String.valueOf(reverse_reverse_status_binary.charAt(5)));//驻留报警
        records.setFallDownAlarm(String.valueOf(reverse_reverse_status_binary.charAt(4)));//跌倒报警
        records.setIsPersonAlarm(String.valueOf(reverse_reverse_status_binary.charAt(3)));//有人报警
        records.setNoPersonAlarm(String.valueOf(reverse_reverse_status_binary.charAt(2)));//无人报警
        records.setExistPerson(String.valueOf(reverse_reverse_status_binary.charAt(1)));//无人/有人
        records.setIsError(String.valueOf(reverse_reverse_status_binary.charAt(0)));//故障




        records.setParamHight(String.valueOf(HexUtil.hexToDec(param_height_hex)));
        records.setParamLingerTime(String.valueOf(HexUtil.hexToDec(param_linger_time_hex)));
        records.setParamNoPersonTime(String.valueOf(HexUtil.hexToDec(param_no_person_time_hex)));
        records.setParamFallDownTime(String.valueOf(HexUtil.hexToDec(ECL)));//跌倒报警时长
        records.setParamBattery(battery_info);

        System.out.println(records.toString());
    }




//    public static void main(String[] args) {
//        DTumbleDataRecords records= new DTumbleDataRecords();
//         String appData = "AA550D021402000005F000AAAA51545304538012000200051268";
//        String data = getData(appData);
//        String status = data.substring(0,4);//状态为2字节，需要高低字节调换后再转换成二进制字符来解析
//        String senser_params = data.substring(4,4+8);//传感器参数（4字节),需要高低字节调换后再解析
//        String battery_info  = data.substring(12,12+4);//电池电量（2字节）,需要高低字节调换后再解析,AAAA   表示设备外部供电
//        String CELL_ID = data.substring(16,16+8);//CELL ID(4字节)，,需要高低字节调换后再解析
//        String RSRP = data.substring(24,24+4);//RSRP(2字节)，,需要高低字节调换后再解析
//        String PCI = data.substring(28,28+4);//PCI(2字节)，,需要高低字节调换后再解析
//        String SINR = data.substring(32,32+4);//SINR(2字节)，,需要高低字节调换后再解析
//        String ECL = data.substring(36,36+2);//ECL(1字节)
//        String CSQ = data.substring(38,38+2);//CSQ(1字节)
//
//        String reverse_status = HexUtil.reverseHex(status);
//        String reverse_status_binary = HexUtil.hexToBinaryString(reverse_status);
//
//        String reverse_senser_params =  HexUtil.reverseHex(senser_params);//01183C00
//        String param_height_hex = reverse_senser_params.substring(0,4);//长度2字节
//        String param_linger_time_hex = reverse_senser_params.substring(4,4+2);
//        String param_no_person_time_hex = reverse_senser_params.substring(6,6+2);
//
//
//        //为了方便解析二进制从左到右的顺序，将reverse_status_binary再reverse
//        StringBuffer sb = new StringBuffer(reverse_status_binary);
//        StringBuffer reverse_reverse_status_binary = sb.reverse();
//
//        records.setIsPersonSwitch(String.valueOf(reverse_reverse_status_binary.charAt(15)));//有人报警开关
//        records.setNoPersonSwitch(String.valueOf(reverse_reverse_status_binary.charAt(14)));//无人报警开关
//        records.setFallDowmSwitch(String.valueOf(reverse_reverse_status_binary.charAt(13)));//跌倒报警开关
//        records.setLingerSwitch(String.valueOf(reverse_reverse_status_binary.charAt(12)));//驻留报警开关
//
//        records.setLingerAlarm(String.valueOf(reverse_reverse_status_binary.charAt(5)));//驻留报警
//        records.setFallDownAlarm(String.valueOf(reverse_reverse_status_binary.charAt(4)));//跌倒报警
//        records.setIsPersonAlarm(String.valueOf(reverse_reverse_status_binary.charAt(3)));//有人报警
//        records.setNoPersonAlarm(String.valueOf(reverse_reverse_status_binary.charAt(2)));//无人报警
//        records.setExistPerson(String.valueOf(reverse_reverse_status_binary.charAt(1)));//无人/有人
//        records.setIsError(String.valueOf(reverse_reverse_status_binary.charAt(0)));//故障
//
//
//
//
//        records.setParamHight(String.valueOf(HexUtil.hexToDec(param_height_hex)));
//        records.setParamLingerTime(String.valueOf(HexUtil.hexToDec(param_linger_time_hex)));
//        records.setParamNoPersonTime(String.valueOf(HexUtil.hexToDec(param_no_person_time_hex)));
//        records.setParamFallDowmTime(String.valueOf(HexUtil.hexToDec(ECL)));//跌倒报警时长
//        records.setParamBattery(battery_info);
//
//        System.out.println(records.toString());
//
//    }


    public static void main(String[] args) throws Exception{
        String appData = "AA550D06180D1001046006074220719301101089860620220076129639EF";
        String data = getData(appData);//0D1001046006074220719301101089860620220076129639

        String MISI = data.substring(6,6+16);//0460060742207193
        String ICCID =data.substring(28,28+20);//89860620220076129639

    }

}
