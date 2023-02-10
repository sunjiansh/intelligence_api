package co.yixiang.modules.device.mqtt;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

/**
 * @author sunjian
 * @Date 2023/1/8
 * @license 版权所有，非经许可请勿使用本代码
 */
public class PushCallback implements MqttCallback {

    public void connectionLost(Throwable cause) {
        // 链接丢失后，通常在这里面进行重连
        System.out.println("链接断开，能够作重连");
    }

    public void deliveryComplete(IMqttDeliveryToken token) {
        System.out.println("deliveryComplete---------" + token.isComplete());
    }

    public void messageArrived(String topic, MqttMessage message) throws Exception {
        // subscribe后获得的消息会执行到这里面
        System.out.println("接收消息主题 : " + topic);
        System.out.println("接收消息Qos : " + message.getQos());
        System.out.println("接收消息内容 : " + new String(message.getPayload()));
    }


}
