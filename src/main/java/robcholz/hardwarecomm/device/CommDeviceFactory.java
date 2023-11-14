package robcholz.hardwarecomm.device;

import robcholz.hardwarecomm.bluetooth.BluetoothDevice;
import robcholz.hardwarecomm.mqtt.MQTTDevice;
import robcholz.hardwarecomm.mqtt.MQTTServer;
import robcholz.hardwarecomm.serial.SerialDevice;
import robcholz.setting.data.DeviceData;

public class CommDeviceFactory {
    private static CommDeviceFactory instance;

    private static class CommMQTTServer extends MQTTServer {
        private static CommMQTTServer commMQTTServer;

        public static CommMQTTServer getInstance () {
            if (commMQTTServer == null)
                return commMQTTServer = new CommMQTTServer();
            return commMQTTServer;
        }
    }

    private static class CommSerialPortManager {
        private static CommSerialPortManager commSerialPortManager;

        public static CommSerialPortManager getInstance () {
            if (commSerialPortManager == null)
                return commSerialPortManager = new CommSerialPortManager();
            return commSerialPortManager;
        }

        public String getPort () {
            return ""; // TODO
        }
    }

    public static CommDeviceFactory getInstance () {
        if (instance == null)
            instance = new CommDeviceFactory();
        return instance;
    }

    public AbstractCommDevice createDevice (String name,String id, int deviceType) {
        if (deviceType == CommDeviceInterface.BLUETOOTH_DEVICE)
            return new BluetoothDevice(name,id);
        if (deviceType == CommDeviceInterface.MQTT_DEVICE)
            return new MQTTDevice(name,id, CommMQTTServer.getInstance().getURL());
        return new SerialDevice(name, id);
    }

    public AbstractCommDevice createDevice (DeviceData deviceData) {
        return createDevice(deviceData.getName(),deviceData.getID(),deviceData.getType());
    }
}
