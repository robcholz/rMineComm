package robcholz;


import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import robcholz.block.ModBlock;
import robcholz.command.Command;
import robcholz.item.ModItem;
import robcholz.manager.CommBlockManager;
import robcholz.manager.CommDeviceManager;
import robcholz.setting.SettingLoader;


public class RMineComm implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(Reference.MOD_ID);
    private static final CommBlockManager blockStateMonitor = CommBlockManager.getInstance();
    private static final CommDeviceManager commDeviceManager = CommDeviceManager.getInstance();

    @Override
    public void onInitialize () {
        registerCommand();
        registerEvents();
        registerGamePlay();
        SettingLoader.getInstance().loadDevices();
        LOGGER.info("rMineComm Initialized.");
    }

    private static void registerCommand () {
        CommandRegistrationCallback.EVENT.register(Command::register);
    }

    private static void registerEvents () {
        ClientTickEvents.END_WORLD_TICK.register((world -> {
            blockStateMonitor.update();
            commDeviceManager.update();
        }));
        ServerWorldEvents.LOAD.register((server, world) -> {
            SettingLoader.getInstance().loadBlocks();
        });
    }

    private static void registerGamePlay(){
        ModBlock.register();
        ModItem.register();
    }
}