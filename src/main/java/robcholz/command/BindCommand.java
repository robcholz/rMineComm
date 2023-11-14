package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class BindCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("bind")
                        .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                .then(CommandManager.argument("posName", StringArgumentType.string())
                                                        .executes(BindCommand::bind)
                                                )))
                .then(CommandManager.literal("unbind")
                        .then(CommandManager.argument("posName", StringArgumentType.string())
                                .executes(BindCommand::unbind)))
        );
    }

    private static int bind (CommandContext<ServerCommandSource> context) {

        return 1;
    }

    private static int unbind (CommandContext<ServerCommandSource> context) {

        return 1;
    }
}
