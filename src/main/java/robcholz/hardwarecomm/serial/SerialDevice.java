package robcholz.hardwarecomm.serial;

import purejavacomm.*;
import robcholz.hardwarecomm.comm.DeviceException;
import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceInterface;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class SerialDevice extends AbstractCommDevice {
    public final int DEFAULT_BAUD_RATE = 9600;
    private int baudRate;
    private int dataBit;
    private int stopBit;
    private int parity;
    private final byte[] buffer = new byte[1024];
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;

    public SerialDevice (String name, String port) {
        super(name, port);
        setName(name);
        setBaudRate(DEFAULT_BAUD_RATE);
        setParams(SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

    public void setBaudRate (int baudRate) {
        this.baudRate = baudRate;
    }

    public void setParams (int dataBit, int stopBit, int parity) {
        this.dataBit = dataBit;
        this.stopBit = stopBit;
        this.parity = parity;
    }

    @Override
    public int getDeviceType () {
        return CommDeviceInterface.SERIAL_DEVICE;
    }

    @Override
    public void connect () throws DeviceException.DeviceUnopenedException, DeviceException.BadDeviceConfigException, DeviceException.DeviceInUseException {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(getID());
            this.serialPort = (SerialPort) portIdentifier.open(getName(), 2000);
            this.inputStream = serialPort.getInputStream();
            this.outputStream = serialPort.getOutputStream();
        } catch (NoSuchPortException e) {
            throw new DeviceException.DeviceUnopenedException(this, "No such port");
        } catch (PortInUseException e) {
            throw new DeviceException.DeviceInUseException(this, "Port is in use");
        } catch (IOException e) {
            throw new DeviceException.DeviceUnopenedException(this, e.getMessage());
        }
        try {
            serialPort.setSerialPortParams(this.baudRate, this.dataBit, this.stopBit, this.parity);
        } catch (UnsupportedCommOperationException e) {
            throw new DeviceException.BadDeviceConfigException(this, "Unsupported serial port configuration");
        }
    }

    @Override
    public void disconnect () throws DeviceException.DeviceUnclosedException {
        try {
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new DeviceException.DeviceUnclosedException(this);
        }
        serialPort.close();
    }

    @Override
    public void write (String message) throws DeviceException.BadWriteException {
        try {
            outputStream.write(message.getBytes());
            outputStream.flush();
            setSentSuccessfullyFlag(true);
        } catch (IOException e) {
            setSentSuccessfullyFlag(false);
            throw new DeviceException.BadWriteException(this);
        }
    }

    @Override
    public void onUpdate () {
        readToBuffer();
    }

    private void readToBuffer () {
        try {
            addMessage(new String(buffer, 0, inputStream.read(buffer, 0, inputStream.available())));
        } catch (IOException ignored) {}
    }
}
