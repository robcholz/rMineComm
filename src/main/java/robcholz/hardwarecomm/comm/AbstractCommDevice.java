package robcholz.hardwarecomm.comm;

import java.util.LinkedList;
import java.util.Queue;

public abstract class AbstractCommDevice implements CommDeviceInterface {
    private String topic;
    private String responseTopic;
    private String name;
    private String clientID;
    private final Queue<String> messageBuffer = new LinkedList<>();

    private boolean sentSuccessfullyFlag = false;

    protected void setTopic (String topic) {
        this.topic = topic + "/topic";
        this.responseTopic = this.topic + "/response";
    }

    public void setName (String name) {
        this.name = name;
    }

    public void setClientID (String clientID) {
        this.clientID = clientID;
    }

    protected void setSentSuccessfullyFlag (boolean flag) {
        this.sentSuccessfullyFlag = flag;
    }

    protected void addMessage(String message){
        messageBuffer.offer(message);
    }

    @Override
    public String getName () {
        return name;
    }

    @Override
    public boolean isPrevDataSentSuccessfully () {return sentSuccessfullyFlag;}

    protected String getTopic () {
        return topic;
    }

    protected String getResponseTopic () {
        return responseTopic;
    }

    public String getClientID () {
        return clientID;
    }

    protected boolean isMessageBufferEmpty(){
        return messageBuffer.isEmpty();
    }

    protected String getMessage(){
        return messageBuffer.poll();
    }
}
