package robcholz.hardwarecomm.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import robcholz.hardwarecomm.comm.DeviceException;
import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceInterface;

/**
 * @mqtt send: {clientID}/topic receive: {clientID}/topic/response
 */
public class MQTTDevice extends AbstractCommDevice implements CommDeviceInterface {
    private String topic;
    private String responseTopic;
    private String clientID;
    private final String brokerURL;
    private final MemoryPersistence memoryPersistence;
    private final MqttConnectOptions connectOptions;
    private MqttClient mqttClient;
    public final int DEFAULT_QOS = 0;
    private int qos;

    public MQTTDevice (String name,String ID, String brokerURL) {
        super(name, ID); // name is clientID
        this.brokerURL = brokerURL;
        this.memoryPersistence = new MemoryPersistence();
        this.connectOptions = new MqttConnectOptions();
        this.connectOptions.setCleanSession(true);
        setClientID(ID);
        setQos(DEFAULT_QOS);
        setTopic(clientID + "/topic");
        setSentSuccessfullyFlag(false);
    }

    public void setQos (int qos) {
        this.qos = qos;
    }

    public int getQos () {return this.qos;}

    public String getBrokerURL () {return this.brokerURL;}

    private void setTopic (String topic) {
        this.topic = topic + "/topic";
        this.responseTopic = this.topic + "/response";
    }

    private void setClientID (String clientID) {
        this.clientID = clientID;
    }

    private String getTopic () {
        return topic;
    }

    private String getResponseTopic () {
        return responseTopic;
    }

    private String getClientID () {
        return clientID;
    }

    @Override
    public int getDeviceType () {
        return CommDeviceInterface.MQTT_DEVICE;
    }

    @Override
    public void connect () throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException {
        try {
            this.mqttClient = new MqttClient(getBrokerURL(), getClientID(), this.memoryPersistence);
            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost (Throwable throwable) {}

                @Override
                public void messageArrived (String topic, MqttMessage mqttMessage) throws Exception {
                    if (topic.equals(getResponseTopic())) {
                        addMessage(new String(mqttMessage.getPayload()));
                    }
                }

                @Override
                public void deliveryComplete (IMqttDeliveryToken iMqttDeliveryToken) {
                    setSentSuccessfullyFlag(true);
                }
            });
            mqttClient.connect(this.connectOptions);
            mqttClient.subscribe(getResponseTopic());
        } catch (MqttSecurityException e) {
            throw new DeviceException.BadDeviceConfigException(this, e.getMessage());
        } catch (MqttException e) {
            throw new DeviceException.DeviceUnopenedException(this, e.getMessage());
        }
    }

    @Override
    public void disconnect () throws DeviceException.DeviceUnclosedException {
        try {
            mqttClient.disconnect();
        } catch (MqttException e) {
            throw new DeviceException.DeviceUnclosedException(this);
        }
    }

    @Override
    public void write (String message) throws DeviceException.BadWriteException {
        setSentSuccessfullyFlag(false);
        try {
            mqttClient.publish(getTopic(), message.getBytes(), this.qos, false);
        } catch (MqttException e) {
            throw new DeviceException.BadWriteException(this, e.getMessage());
        }
    }

    @Override
    public void onUpdate () {}
}
