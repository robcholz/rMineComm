package robcholz.hardwarecomm.bluetooth;

import robcholz.hardwarecomm.comm.AbstractCommDevice;
import robcholz.hardwarecomm.comm.DeviceException;

public class BluetoothDevice extends AbstractCommDevice {
    @Override
    public boolean isAvailable () {
        return false;
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
    public String read () throws DeviceException.BadReadException {
        return null;
    }
}
