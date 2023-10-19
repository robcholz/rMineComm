package robcholz.hardwarecomm.bluetooth;

import robcholz.hardwarecomm.comm.CommScannerInterface;

import java.util.List;

public class BluetoothScanner implements CommScannerInterface<BluetoothDevice> {
    @Override
    public List<BluetoothDevice> scan () {
        return null;
    }
}
