package robcholz.hardwarecomm.mqtt;

import io.moquette.broker.Server;
import net.minecraft.util.Pair;

import java.io.IOException;

public interface MQTTServerInterface {
    void start () throws IOException;

    void stop ();

    Server getServer ();

    String getURL();

    void addMessage (String message, int status);

    Pair<String, Integer> getMessage ();
}
