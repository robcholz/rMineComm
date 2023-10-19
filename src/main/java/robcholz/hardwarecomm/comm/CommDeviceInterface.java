package robcholz.hardwarecomm.comm;

public interface CommDeviceInterface {
    boolean isAvailable ();

    String getName ();

    boolean isPrevDataSentSuccessfully ();

    void connect () throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException;

    void disconnect () throws DeviceException.DeviceUnclosedException;

    void write (String message) throws DeviceException.BadWriteException;

    String read () throws DeviceException.BadReadException;
}
