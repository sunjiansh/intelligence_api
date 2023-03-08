package co.yixiang.modules.device.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttTopic;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.util.concurrent.ScheduledExecutorService;

/**
 * @author sunjian
 * @Date 2023/1/8
 * @license 版权所有，非经许可请勿使用本代码
 */
public class ClientMQTT {

    public static final String HOST = "tcp://mq.jsktzx.com:1883";
    public static final String TOPIC = "javatopic";
    private static final String clientid = "java client";
    private MqttClient client;
    private MqttConnectOptions options;
    private String userName = "";
    private String passWord = "";

    private ScheduledExecutorService scheduler;

    private void start() {
        try {
            // host为主机名，clientid即链接MQTT的客户端ID，通常以惟一标识符表示，MemoryPersistence设置clientid的保存形式，默认为之内存保存
            client = new MqttClient(HOST, clientid, new MemoryPersistence());
            // MQTT的链接设置
            options = new MqttConnectOptions();
            // 设置是否清空session,这里若是设置为false表示服务器会保留客户端的链接记录，这里设置为true表示每次链接到服务器都以新的身份链接
            options.setCleanSession(true);
            // 设置链接的用户名
            options.setUserName(userName);
            // 设置链接的密码
            options.setPassword(passWord.toCharArray());
            // 设置超时时间 单位为秒
            options.setConnectionTimeout(10);
            // 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并无重连的机制
            options.setKeepAliveInterval(20);
            // 设置回调
            client.setCallback(new PushCallback());
            MqttTopic topic = client.getTopic(TOPIC);
            // setWill方法，若是项目中须要知道客户端是否掉线能够调用该方法。设置最终端口的通知消息
            options.setWill(topic, "close".getBytes(), 2, true);

            client.connect(options);
            // 订阅消息
            int[] Qos = { 1 };
            String[] topic1 = { TOPIC };
            client.subscribe(topic1, Qos);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws MqttException {
        ClientMQTT client = new ClientMQTT();
        client.start();
    }


}
