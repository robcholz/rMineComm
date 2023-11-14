package robcholz.hardwarecomm.mqtt;

import robcholz.hardwarecomm.scanner.CommScannerInterface;

import java.util.Collections;
import java.util.List;

public class MQTTScanner implements CommScannerInterface<MQTTDevice> {
    @Override
    @Deprecated
    public List<MQTTDevice> scan () {
        return Collections.emptyList();
    }
}
