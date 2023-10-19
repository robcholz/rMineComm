package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ScanCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("scan")
                        .executes(ScanCommand::scanAllDevices)
                        .then(CommandManager.literal("usb")
                                .executes(ScanCommand::scanSerialDevices)
                                .then(CommandManager.literal("on").executes(ScanCommand::scanSetSerialOn))
                                .then(CommandManager.literal("off").executes(ScanCommand::scanSetSerialOff)))
                        .then(CommandManager.literal("wan")
                                .executes(ScanCommand::scanMQTTDevices)
                                .then(CommandManager.literal("on").executes(ScanCommand::scanSetMQTTOn))
                                .then(CommandManager.literal("off").executes(ScanCommand::scanSetMQTTOff)))
                        .then(CommandManager.literal("ble")
                                .executes(ScanCommand::scanBluetoothDevices)
                                .then(CommandManager.literal("on").executes(ScanCommand::scanSetBluetoothOn))
                                .then(CommandManager.literal("off").executes(ScanCommand::scanSetBluetoothOff)))
                )
                .then(CommandManager.literal("on").executes(ScanCommand::autoScanOn))
                .then(CommandManager.literal("off").executes(ScanCommand::autoScanOff))
        );
    }

    private static int autoScanOn(CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int autoScanOff(CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanAllDevices (CommandContext<ServerCommandSource> context) {

        return 1;
    }

    private static int scanSerialDevices (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanMQTTDevices (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanBluetoothDevices (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetSerialOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetSerialOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetMQTTOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetMQTTOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetBluetoothOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetBluetoothOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }
}
