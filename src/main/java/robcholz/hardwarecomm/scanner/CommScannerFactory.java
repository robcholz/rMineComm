package robcholz.hardwarecomm.scanner;

import robcholz.hardwarecomm.bluetooth.BluetoothDevice;
import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.hardwarecomm.serial.SerialDevice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CommScannerFactory {
    private static CommScannerFactory instance;

    public static CommScannerFactory getInstance () {
        if (instance == null)
            instance = new CommScannerFactory();
        return instance;
    }

    private static class SerialScanner extends robcholz.hardwarecomm.serial.SerialScanner {
        private static SerialScanner instance;

        public static SerialScanner getInstance () {
            if (instance == null)
                instance = new SerialScanner();
            return instance;
        }
    }

    private static class BluetoothScanner extends robcholz.hardwarecomm.bluetooth.BluetoothScanner {
        private static BluetoothScanner instance;

        public static BluetoothScanner getInstance () {
            if (instance == null)
                instance = new BluetoothScanner();
            return instance;
        }
    }

    public List<CommDeviceInterface> scan(int deviceType) {
        if (deviceType == CommDeviceInterface.SERIAL_DEVICE) {
            List<SerialDevice> serialDevices = SerialScanner.getInstance().scan();
            return new ArrayList<>(serialDevices);
        }
        if (deviceType == CommDeviceInterface.MQTT_DEVICE) {
            List<BluetoothDevice> bluetoothDevices = BluetoothScanner.getInstance().scan();
            return new ArrayList<>(bluetoothDevices);
        }
        return Collections.emptyList();
    }


}
