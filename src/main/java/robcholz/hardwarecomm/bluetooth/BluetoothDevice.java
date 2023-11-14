package robcholz.hardwarecomm.bluetooth;

import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.hardwarecomm.comm.DeviceException;

public class BluetoothDevice extends AbstractCommDevice {
    public BluetoothDevice (String name, String ID) {
        super(name, ID);
    }

    @Override
    public int getDeviceType () {
        return CommDeviceInterface.BLUETOOTH_DEVICE;
    }

    @Override
    public void connect () throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException {

    }

    @Override
    public void disconnect () throws DeviceException.DeviceUnclosedException {

    }

    @Override
    public void write (String message) throws DeviceException.BadWriteException {

    }

    @Override
    public void onUpdate () {

    }
}
