package robcholz.hardwarecomm.mqtt;

import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import robcholz.hardwarecomm.comm.AbstractCommDevice;
import robcholz.hardwarecomm.comm.CommDeviceInterface;
import robcholz.hardwarecomm.comm.DeviceException;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @mqtt send: {clientID}/topic receive: {clientID}/topic/response
 */
public class MQTTDevice extends AbstractCommDevice implements CommDeviceInterface {
    private final String brokerURL;
    private final MemoryPersistence memoryPersistence;
    private final MqttConnectOptions connectOptions;
    private MqttClient mqttClient;
    public final int DEFAULT_QOS = 0;
    private int qos;

    public MQTTDevice (String clientID, String brokerURL) {
        this.brokerURL = brokerURL;
        this.memoryPersistence = new MemoryPersistence();
        this.connectOptions = new MqttConnectOptions();
        this.connectOptions.setCleanSession(true);
        setClientID(clientID);
        setName(clientID);
        setQos(DEFAULT_QOS);
        setTopic(clientID + "/topic");
        setSentSuccessfullyFlag(false);
    }

    public void setQos (int qos) {
        this.qos = qos;
    }

    public int getQos () {return this.qos;}

    public String getBrokerURL () {return this.brokerURL;}

    @Override
    public boolean isAvailable () {
        return this.mqttClient.isConnected();
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
    public String read () {
        if (!isMessageBufferEmpty()) {
            return getMessage();
        }
        return "";
    }
}
