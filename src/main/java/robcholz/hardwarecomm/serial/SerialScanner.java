package robcholz.hardwarecomm.serial;

import purejavacomm.CommPortIdentifier;
import robcholz.hardwarecomm.scanner.CommScannerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

public class SerialScanner implements CommScannerInterface<SerialDevice> {
    @Override
    public List<SerialDevice> scan () {
        Enumeration<CommPortIdentifier> portEnum = CommPortIdentifier.getPortIdentifiers();
        List<SerialDevice> deviceList = new ArrayList<>();
        int index = 0;
        while (portEnum.hasMoreElements()) {
            CommPortIdentifier port = portEnum.nextElement();
            SerialDevice device = new SerialDevice("usb_scanner" + index, port.getName());
            deviceList.add(device);
            index++;
        }
        return Collections.unmodifiableList(deviceList);
    }
}
