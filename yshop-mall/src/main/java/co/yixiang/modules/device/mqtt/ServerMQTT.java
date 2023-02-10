package co.yixiang.modules.device.mqtt;

import com.alibaba.fastjson.JSONObject;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.UnsupportedEncodingException;

/**
 * @author sunjian
 * @Date 2023/1/8
 * @license 版权所有，非经许可请勿使用本代码
 */
public class ServerMQTT {

    // tcp://MQTT安装的服务器地址:MQTT定义的端口号
    public static final String HOST = "tcp://106.15.207.77:1883";
    // 定义一个主题
    public static final String TOPIC = "javatopic";
    // 定义MQTT的ID，能够在MQTT服务配置中指定
    private static final String clientid = "java server";

    public static final String TERMINAL_DATA_TOPIC = "datachannel/";

    public static final String TERMINAL_CMD_TOPIC = "cmdchannel/";



    private static MqttClient client;

    private static MqttTopic topic11;
    //用户名和密码
    private String userName = "";
    private String passWord = "";

    private MqttMessage message;

    /**
     * 构造函数
     *
     * @throws MqttException
     */
    public ServerMQTT() throws MqttException {
        // MemoryPersistence设置clientid的保存形式，默认为之内存保存
        client = new MqttClient(HOST, clientid, new MemoryPersistence());
        connect();
    }

    /**
     * 用来链接服务器
     */
    private void connect() {
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
        options.setUserName(userName);
        options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
            topic11 = client.getTopic(TOPIC);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param topic
     * @param message
     * @throws MqttPersistenceException
     * @throws MqttException
     */
    public void publish(MqttTopic topic, MqttMessage message) throws MqttPersistenceException, MqttException {
        MqttDeliveryToken token = topic.publish(message);
        token.waitForCompletion();
        System.out.println("message is published completely! " + token.isComplete());
    }

    /**
     * 启动入口
     *
     * @param args
     * @throws MqttException
     * @throws UnsupportedEncodingException
     */
    public static void main(String[] args) throws MqttException, UnsupportedEncodingException {
//        ServerMQTT server = new ServerMQTT();
//        server.message = new MqttMessage();
//        /* Qos服务质量等级：
//         * 0，最多一次，无论是否接收到；
//         * 1，最少一次，保证信息将会被至少发送一次给接受者
//         * 2，只一次，确保每一个消息都只被接收到的一次，他是最安全也是最慢的服务等级
//         */
//        server.message.setQos(2);
//        server.message.setRetained(true);
//        server.message.setPayload("test".getBytes("UTF-8"));
//        server.publish(server.topic11, server.message);
//        System.out.println(server.message.isRetained() + "------ratained状态");

//将数据发送到mqtt
        JSONObject msg = new JSONObject();
        msg.put("userId",1);
        msg.put("temperature","26");
        //msg.put("time",entity.getPushTime());
        msg.put("action","TEMPERATURE");
        ServerMQTT.publishTerminalData("356221322018563",msg);

    }


    private static MqttClient getClient() throws Exception{
        if(client == null){
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
        }
        return client;
    }


    private static MqttClient getDataTopicServer(String imei) throws Exception{
        //MqttClient client = new MqttClient(HOST, clientid, new MemoryPersistence());
        MqttClient client = getClient();
        MqttConnectOptions options = new MqttConnectOptions();
        options.setCleanSession(false);
       // options.setUserName(userName);
        //options.setPassword(passWord.toCharArray());
        // 设置超时时间
        options.setConnectionTimeout(10);
        // 设置会话心跳时间
        options.setKeepAliveInterval(20);
        try {
            client.setCallback(new PushCallback());
            client.connect(options);
          //  client.getTopic(TERMINAL_DATA_TOPIC+imei);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return client;
    }


    public  static void publishTerminalData(String imei, JSONObject jsonMsg) throws MqttException {
        MqttClient client = null;
        try {
            client = getDataTopicServer(imei);
            MqttMessage message = new MqttMessage();
            message.setQos(0);// 0最多一次，1 至少一次，2 仅一次
           // message.setRetained(true);
            message.setPayload(jsonMsg.toJSONString().getBytes("UTF-8"));
            client.publish(TERMINAL_DATA_TOPIC+imei,message);
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            client.disconnect();
        }
    }


}
