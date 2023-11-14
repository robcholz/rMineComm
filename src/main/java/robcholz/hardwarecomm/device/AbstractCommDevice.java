package robcholz.hardwarecomm.device;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractCommDevice implements CommDeviceInterface {
    private String name;
    private final String ID;
    private final Queue<String> messageBuffer = new LinkedList<>();
    private boolean sentSuccessfullyFlag = false;

    protected AbstractCommDevice (String name, String ID) {
        setName(name);
        this.ID = ID;
    }

    public void setName (String name) {
        this.name = name;
    }

    protected void setSentSuccessfullyFlag (boolean flag) {
        this.sentSuccessfullyFlag = flag;
    }

    protected void addMessage (String message) {
        messageBuffer.offer(message);
    }

    protected boolean isMessageBufferEmpty () {
        return messageBuffer.isEmpty();
    }

    protected String getMessage () {
        return messageBuffer.poll();
    }

    @Override
    public String getName () {
        return name;
    }

    @Override
    public String getID () {
        return ID;
    }

    @Override
    public boolean isAvailable () {
        return !isMessageBufferEmpty();
    }

    @Override
    public boolean isPrevDataSentSuccessfully () {return sentSuccessfullyFlag;}

    @Override
    public String read () {
        if (isMessageBufferEmpty())
            return "";
        return getMessage();
    }

    @Override
    public boolean equals (Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        AbstractCommDevice device = (AbstractCommDevice) obj;
        return (getDeviceType() == device.getDeviceType() && getID().equals(device.getID()));
    }

    @Override
    public int hashCode(){
        return getID().hashCode();
    }
}
