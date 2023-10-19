package robcholz.hardwarecomm.comm;

import java.io.IOException;

public class DeviceException {
    public static class DeviceUnopenedException extends IOException {
        public DeviceUnopenedException (CommDeviceInterface device) {
            super("Cannot open device on " + device.getName());
            Comm.println("Cannot open device on " + device.getName());
        }

        public DeviceUnopenedException (CommDeviceInterface device, String reason) {
            super(reason + " on " + device.getName());
            Comm.println(reason + " on " + device.getName());
        }
    }

    public static class DeviceUnclosedException extends IOException {
        public DeviceUnclosedException (CommDeviceInterface device) {
            super("Cannot close device on " + device.getName());
            Comm.println("Cannot close device on " + device.getName());
        }
    }

    public static class BadWriteException extends IOException {
        public BadWriteException (CommDeviceInterface device) {
            super("Cannot write to device: " + device.getName());
            Comm.println("Cannot write to device: " + device.getName());
        }

        public BadWriteException (CommDeviceInterface device, String reason) {
            super("Cannot write to device: " + device.getName() + " Error: " + reason);
            Comm.println("Cannot write to device: " + device.getName() + " Error: " + reason);
        }
    }

    public static class BadReadException extends IOException {
        public BadReadException (CommDeviceInterface device) {
            super("Cannot read from device: " + device.getName());
            Comm.println("Cannot read from device: " + device.getName());
        }
    }

    public static class BadDeviceConfigException extends Exception {
        public BadDeviceConfigException (CommDeviceInterface device) {
            super("Device is opened but cannot set on " + device.getName());
            Comm.println("Device is opened but cannot set on " + device.getName());
        }

        public BadDeviceConfigException (CommDeviceInterface device, String reason) {
            super(reason + " on " + device.getName());
            Comm.println(reason + " on " + device.getName());
        }
    }
}
