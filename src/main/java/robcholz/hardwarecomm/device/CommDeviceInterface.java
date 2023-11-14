package robcholz.hardwarecomm.device;

import robcholz.hardwarecomm.comm.DeviceException;

public interface CommDeviceInterface {
    int BLUETOOTH_DEVICE = 0;
    int MQTT_DEVICE = 1;
    int SERIAL_DEVICE = 2;

    boolean isAvailable ();

    String getName ();

    String getID();

    int getDeviceType();

    boolean isPrevDataSentSuccessfully ();

    void connect () throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException, DeviceException.DeviceInUseException;

    void disconnect () throws DeviceException.DeviceUnclosedException;

    void write (String message) throws DeviceException.BadWriteException;

    void onUpdate();

    String read () throws DeviceException.BadReadException;
}
