package robcholz.hardwarecomm.mqtt;

import robcholz.hardwarecomm.comm.CommScannerInterface;

import java.util.List;

public class MQTTScanner implements CommScannerInterface<MQTTDevice> {
    @Override
    @Deprecated
    public List<MQTTDevice> scan () {
        return null;
    }
}
