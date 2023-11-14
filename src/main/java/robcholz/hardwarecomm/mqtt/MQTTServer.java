package robcholz.hardwarecomm.mqtt;

import io.moquette.broker.Server;
import io.moquette.interception.InterceptHandler;
import io.moquette.interception.messages.*;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.handler.codec.mqtt.MqttMessageBuilders;
import io.netty.handler.codec.mqtt.MqttPublishMessage;
import io.netty.handler.codec.mqtt.MqttQoS;
import net.minecraft.util.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Properties;
import java.util.Queue;


public class MQTTServer implements MQTTServerInterface {
    private static final Logger logger = LogManager.getLogger(Server.class);
    private final Server server;
    private final Queue<Pair<String, Integer>> messageBuffer;

    public MQTTServer () {
        this.server = new Server();
        this.messageBuffer = new LinkedList<>();
    }

    @Override
    public void start () throws IOException {
        Properties properties = new Properties();
        properties.setProperty("port", "8964");
        properties.setProperty("host", "localhost");
        properties.setProperty("log.moquette.stdout", "false");
        properties.setProperty("log.moquette.file", "false");
        server.startServer(properties);
        server.addInterceptHandler(new MessageHandler(this));
    }

    @Override
    public void stop () {
        server.stopServer();
    }

    @Override
    public Server getServer () {
        return this.server;
    }

    @Override
    public String getURL(){
        return ""; // TODO
    }

    @Override
    public void addMessage (String clientID, int status) {
        messageBuffer.offer(new Pair<>(clientID, status));
    }

    @Override
    public Pair<String, Integer> getMessage () {
        return messageBuffer.poll();
    }

    public static class MessageHandler implements InterceptHandler {
        private final MQTTServerInterface mqttServer;

        MessageHandler (MQTTServerInterface mqttServer) {
            this.mqttServer = mqttServer;
        }

        private void publish (String clientId, String topicName, String message) {
            ByteBuf payload = Unpooled.copiedBuffer(message.getBytes());
            MqttPublishMessage mqttMessage = MqttMessageBuilders.publish()
                    .topicName(topicName)
                    .qos(MqttQoS.AT_MOST_ONCE)
                    .payload(payload)
                    .build();
            mqttServer.getServer().internalPublish(mqttMessage, clientId);
        }

        private String getTopic (String clientID) {
            return clientID + "/topic/response";
        }

        @Override
        public String getID () {
            return null;
        }

        @Override
        public Class<?>[] getInterceptedMessageTypes () {
            return Arrays.asList(InterceptConnectMessage.class, InterceptDisconnectMessage.class,
                    InterceptPublishMessage.class, InterceptSubscribeMessage.class, InterceptUnsubscribeMessage.class,
                    InterceptAcknowledgedMessage.class).toArray(new Class[0]);
        }

        @Override
        public void onConnect (InterceptConnectMessage interceptConnectMessage) {
            mqttServer.addMessage(interceptConnectMessage.getClientID(), Status.ON_CONNECT);
        }

        @Override
        public void onDisconnect (InterceptDisconnectMessage interceptDisconnectMessage) {
            mqttServer.addMessage(interceptDisconnectMessage.getClientID(), Status.ON_DISCONNECT);
        }

        @Override
        public void onConnectionLost (InterceptConnectionLostMessage interceptConnectionLostMessage) {
            mqttServer.addMessage(interceptConnectionLostMessage.getClientID(), Status.ON_CONNECTION_LOST);
        }

        @Override
        public void onPublish (InterceptPublishMessage interceptPublishMessage) {}

        @Override
        public void onSubscribe (InterceptSubscribeMessage interceptSubscribeMessage) {
            String clientID = interceptSubscribeMessage.getClientID();
            mqttServer.addMessage(clientID, Status.ON_SUBSCRIBE);
            publish(clientID, getTopic(clientID), "Server accepts your subscription.");
        }

        @Override
        public void onUnsubscribe (InterceptUnsubscribeMessage interceptUnsubscribeMessage) {
            String clientID = interceptUnsubscribeMessage.getClientID();
            mqttServer.addMessage(clientID, Status.ON_UNSUBSCRIBE);
            publish(clientID, getTopic(clientID), "You have unsubscribed from server.");
        }

        @Override
        public void onMessageAcknowledged (InterceptAcknowledgedMessage interceptAcknowledgedMessage) {}
    }
}
