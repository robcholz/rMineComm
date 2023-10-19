package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class StatusCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("available")
                        .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                .executes(StatusCommand::available)))
        );
    }

    private static int available (CommandContext<ServerCommandSource> context) {
        return 1;
    }
}
