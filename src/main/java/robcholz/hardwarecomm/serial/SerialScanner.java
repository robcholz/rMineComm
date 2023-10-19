package robcholz.hardwarecomm.serial;

import robcholz.hardwarecomm.comm.CommScannerInterface;
import purejavacomm.CommPortIdentifier;

import java.util.ArrayList;
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
            SerialDevice device = new SerialDevice(port.getName(), "scanner" + index);
            deviceList.add(device);
            index++;
        }
        return deviceList;
    }
}
