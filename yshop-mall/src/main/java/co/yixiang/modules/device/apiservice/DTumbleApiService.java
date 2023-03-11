package co.yixiang.modules.device.apiservice;

import co.yixiang.common.enums.TumbleCmdEnum;
import co.yixiang.common.util.HexUtil;
import co.yixiang.utils.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.ctg.ag.sdk.biz.AepDeviceCommandClient;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandRequest;
import com.ctg.ag.sdk.biz.aep_device_command.CreateCommandResponse;
import org.springframework.stereotype.Service;

/**
 * @author sunjian
 * @Date 2023/2/8
 * @license 版权所有，非经许可请勿使用本代码
 */
@Service
public class DTumbleApiService {


    public static String appKey = "dyg2BzXqigf";

    public static String appSecret = "nl8mvgEFeu";

    public static String masterKey = "2e0c19abd7414b8e95b96928d68c9cf8";


    /**
     * 注册设备到AEP平台
     */
    public JSONObject registerDevice(String imei,String iccid,String deviceId,Integer productId) throws Exception{
            //AA550D06180D1001046006074220719301101089860620220076129639EF
            String registerCmdTemplate = "0D06180D1001{IMEI}011010{ICCID}";
            //String IMEI = "860387067588889";
            //String ICCID = "8986112223800283848";
            String FULL_IMEI = HexUtil.padLeft(imei,16);
            String FULL_ICCID = HexUtil.padLeft(iccid,20);
            String register_cmd =  registerCmdTemplate.replace("{IMEI}",FULL_IMEI).replace("{ICCID}",FULL_ICCID).toUpperCase();
            String check = HexUtil.makeChecksum(register_cmd).toUpperCase();

            String cmd = TumbleCmdEnum.CONTENT_HEAD + register_cmd + check;


            AepDeviceCommandClient client = AepDeviceCommandClient.newClient().appKey(appKey).appSecret(appSecret).build();
            CreateCommandRequest request = new CreateCommandRequest();
            request.setParamMasterKey(masterKey);
          //  ##### 请求包内容描述：
//        {
//            "content": {},
//            "deviceId": "string",
//                "operator": "string",
//                "productId": 0,
//                "ttl": 7200,
//                "deviceGroupId": 100,
//                "level": 1
//        }
//
//        deviceId: 设备ID，（当指令级别为设备级时必填，为设备组级时则不填）
//        operator: 操作者，必填
//        productId: 产品ID，必填
//        ttl: 设备指令缓存时长，选填。单位：秒，取值范围：0-864000。
//        不携带则默认值：7200。如不需缓存请填0。
//        level:指令级别，1或2为设备级别,3为设备组级别，选填。不填默认设备级。
//        deviceGroupId：设备组ID，选填，当指令级别为设备级，
//        deviceId不为空，deviceGroupId为空；
//        当指令级别为设备组级，deviceId为空，deviceGroupId不为空。


//        content: 指令内容，必填，格式为Json。
//
//        TCP和LWM2M协议透传的content内容：
//        {
//            payload:指令内容,数据格式为十六进制时需要填十六进制字符串,
//                    dataType:数据类型：1字符串，2十六进制
//        }
//        样例：
//        {
//            "dataType":1,
//                "payload": "5365ab32d"
//        }


             JSONObject jsonBody = new JSONObject();
             JSONObject jsonContent = new JSONObject();

                jsonContent.put("dataType",1);
                jsonContent.put("payload",cmd);

                //jsonBody.put("deviceId","8a940782d52d493581770cc64cdfcba2");
                jsonBody.put("deviceId",deviceId);
                jsonBody.put("operator","admin");
                //jsonBody.put("productId",15518212);
                jsonBody.put("productId",productId);
                jsonBody.put("deviceGroupId",null);
                jsonBody.put("ttl",0);
                jsonBody.put("level",1);
                jsonBody.put("content",jsonContent);


            request.setBody(jsonBody.toJSONString().getBytes());
            CreateCommandResponse response = client.CreateCommand(request);
            JSONObject reponse_body = JSONObject.parseObject(new String(response.getBody()));
            if (0 != reponse_body.getInteger("code")){
                new RuntimeException(reponse_body.getString("msg"));
            }
            return  reponse_body;
    }


    /**
     * 初始化配置跌倒报警器
     * @param deviceId
     * @param productId
     * @return
     */
    public static JSONObject configDevice(String deviceId,String productId,String fallDwonTime) throws Exception{
        //AA550D04090908E001050000000011
        //帧头解析
        //帧头（2字节）:      AA55
        //产品类型（1字节）:  0D    NB人体存在
        //命令位（1字节）：   04    平台设置指令
        //数据长度（1字节）:  09  (9个字节)
        //数据包：
        //设置类型(1字节)：   09   人体存在设置指令
        //数据（4字节）：     08E00105=0x0501E008
        //                    Byte0=0X08 无人报警时长8:8小时无人报警
        //                    Byte1=0XE0  :设置状态开关   BIT15=1 固定1   BIT14=1  有人报警开启  BIT13=1:无人报警关闭  BIT12=0:跌倒报警关闭  BIT11=0:驻留报警关闭
        //                    Byte2=0X01  :驻留时长1： 1分钟
        //                    Byte3=0X05  :跌倒时长:  5秒
        //保留（4字节）：     00000000
        //校验1字节：         11
        String cmdTemplate = "0D040909{PARAM}00000000";
        String params_byte0 = "08";//无人报警时长8:8小时无人报警
        String params_byte1 = "E0";//设置状态开关   BIT15=1 固定1   BIT14=1  有人报警开启  BIT13=1:无人报警关闭  BIT12=0:跌倒报警关闭  BIT11=0:驻留报警关闭
        String params_byte2 = "01";//驻留时长1： 1分钟
        String params_byte3 = "08";//默认跌倒时长:  8秒 (可设置5-180s) 1E为30秒

        if(StringUtils.isNotEmpty(fallDwonTime)){
            params_byte3 = fallDwonTime;
        }

        //有人报警开启\无人报警关闭\跌倒报警开启\驻留报警关闭(BIT15=1 固定1   BIT14=1  有人报警开启  BIT13=1:无人报警关闭  BIT12=0:跌倒报警关闭  BIT11=0:驻留报警关闭  )
        String new_params_byte1 = HexUtil.binary2HexString("11010000");

        //重新拼接参数字节，应为发给设备要高低字节换序，这里直接反过来拼接
        StringBuffer param = new StringBuffer();
        param.append(params_byte0).append(new_params_byte1).append(params_byte2).append(params_byte3);

        String config_cmd = cmdTemplate.replace("{PARAM}",param).toUpperCase();

        String check = HexUtil.makeChecksum(config_cmd).toUpperCase();

        String  cmd = TumbleCmdEnum.CONTENT_HEAD + config_cmd + check;


        AepDeviceCommandClient client = AepDeviceCommandClient.newClient().appKey(appKey).appSecret(appSecret).build();
        CreateCommandRequest request = new CreateCommandRequest();
        request.setParamMasterKey(masterKey);

        JSONObject jsonBody = new JSONObject();
        JSONObject jsonContent = new JSONObject();

        jsonContent.put("dataType",1);
        jsonContent.put("payload",cmd);

        //jsonBody.put("deviceId","8a940782d52d493581770cc64cdfcba2");
        jsonBody.put("deviceId",deviceId);
        jsonBody.put("operator","admin");
        //jsonBody.put("productId",15518212);
        jsonBody.put("productId",productId);
        jsonBody.put("deviceGroupId",null);
        jsonBody.put("ttl",0);
        jsonBody.put("level",1);
        jsonBody.put("content",jsonContent);


        request.setBody(jsonBody.toJSONString().getBytes());
        CreateCommandResponse response = client.CreateCommand(request);
        JSONObject reponse_body = JSONObject.parseObject(new String(response.getBody()));
        if (0 != reponse_body.getInteger("code")){
            new RuntimeException(reponse_body.getString("msg"));
        }
        return  reponse_body;
    }


    /**
     * 配置安装高度
     * @param deviceId
     * @param productId
     * @param height 安装高度（200~300cm）
     * @return
     * @throws Exception
     */
    public static JSONObject configHeight(String deviceId,String productId,Integer height) throws Exception{
            //AA550D04090818010000000000003B
            String cmdTemplate = "0D040908{PARAM}00000000";
            String height_hex = HexUtil.decToHex(height);
            height_hex = HexUtil.padLeft(height_hex,8);//补齐长度为4字节
            String param = HexUtil.reverseHex(height_hex);//发送给设备端要高低换序

            String height_cmd = cmdTemplate.replace("{PARAM}",param).toUpperCase();


        String check = HexUtil.makeChecksum(height_cmd).toUpperCase();

        String  cmd = TumbleCmdEnum.CONTENT_HEAD + height_cmd + check;


        AepDeviceCommandClient client = AepDeviceCommandClient.newClient().appKey(appKey).appSecret(appSecret).build();
        CreateCommandRequest request = new CreateCommandRequest();
        request.setParamMasterKey(masterKey);

        JSONObject jsonBody = new JSONObject();
        JSONObject jsonContent = new JSONObject();

        jsonContent.put("dataType",1);
        jsonContent.put("payload",cmd);

        //jsonBody.put("deviceId","8a940782d52d493581770cc64cdfcba2");
        jsonBody.put("deviceId",deviceId);
        jsonBody.put("operator","admin");
        //jsonBody.put("productId",15518212);
        jsonBody.put("productId",productId);
        jsonBody.put("deviceGroupId",null);
        jsonBody.put("ttl",0);
        jsonBody.put("level",1);
        jsonBody.put("content",jsonContent);


        request.setBody(jsonBody.toJSONString().getBytes());
        CreateCommandResponse response = client.CreateCommand(request);
        JSONObject reponse_body = JSONObject.parseObject(new String(response.getBody()));
        if (0 != reponse_body.getInteger("code")){
            new RuntimeException(reponse_body.getString("msg"));
        }
        return  reponse_body;

    }


    public static void sendCmd(String cmd) throws Exception{

        AepDeviceCommandClient client = AepDeviceCommandClient.newClient().appKey(appKey).appSecret(appSecret).build();
        CreateCommandRequest request = new CreateCommandRequest();
        request.setParamMasterKey(masterKey);

        JSONObject jsonBody = new JSONObject();
        JSONObject jsonContent = new JSONObject();

        jsonContent.put("dataType",1);
        jsonContent.put("payload",cmd);

        jsonBody.put("deviceId","8a940782d52d493581770cc64cdfcba2");
       // jsonBody.put("deviceId",deviceId);
        jsonBody.put("operator","admin");
        jsonBody.put("productId",15518212);
       // jsonBody.put("productId",productId);
        jsonBody.put("deviceGroupId",null);
        jsonBody.put("ttl",0);
        jsonBody.put("level",1);
        jsonBody.put("content",jsonContent);


        request.setBody(jsonBody.toJSONString().getBytes());
        CreateCommandResponse response = client.CreateCommand(request);
        JSONObject reponse_body = JSONObject.parseObject(new String(response.getBody()));
        if (0 != reponse_body.getInteger("code")){
            new RuntimeException(reponse_body.getString("msg"));
        }
    }


    public static void main(String[] args) throws Exception{


        System.out.println(HexUtil.makeChecksum("0D0409081801000000000000"));

        configDevice("8a940782d52d493581770cc64cdfcba2","15518212",null);
       // configHeight("8a940782d52d493581770cc64cdfcba2",15518212,240);


       // sendCmd("AA550D04090818010000000000003B");
    }




}
