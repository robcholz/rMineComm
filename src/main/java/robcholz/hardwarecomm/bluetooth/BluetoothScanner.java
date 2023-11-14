package robcholz.hardwarecomm.bluetooth;

import robcholz.hardwarecomm.scanner.CommScannerInterface;

import java.util.Collections;
import java.util.List;

public class BluetoothScanner implements CommScannerInterface<BluetoothDevice> {
    @Override
    public List<BluetoothDevice> scan () {
        return Collections.emptyList();
    }
}
