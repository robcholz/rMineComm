package robcholz;


import robcholz.command.Command;
import robcholz.monitor.BlockStateMonitor;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class RMineComm implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger("rminecomm");
    private static final BlockStateMonitor blockStateMonitor = BlockStateMonitor.getInstance();

    @Override
    public void onInitialize () {
        registerCommand();
        registerEvents();
        LOGGER.info("rMineComm Initialized.");
    }

    public static void registerCommand () {
        CommandRegistrationCallback.EVENT.register(Command::register);
    }

    public static void registerEvents () {
        ClientTickEvents.END_WORLD_TICK.register((world -> blockStateMonitor.update()));
    }
}