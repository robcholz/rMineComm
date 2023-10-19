package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class DeviceCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("add").then(CommandManager.literal("device")
                        .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                .executes(DeviceCommand::addDevice))))
                .then(CommandManager.literal("remove").then(CommandManager.literal("device")
                        .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                .executes(DeviceCommand::removeDevice))))
        );
    }

    private static int addDevice (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int removeDevice (CommandContext<ServerCommandSource> context) {
        return 1;
    }
}
