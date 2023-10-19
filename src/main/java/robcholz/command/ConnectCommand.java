package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class ConnectCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("connect")
                        .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                .executes(ConnectCommand::connect)))
                .then(CommandManager.literal("disconnect")
                        .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                .executes(ConnectCommand::disconnect)))
        );
    }

    private static int connect (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int disconnect (CommandContext<ServerCommandSource> context) {
        return 1;
    }
}
