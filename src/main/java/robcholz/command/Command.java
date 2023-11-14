package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.BlockPos;


public class Command {
    public static LiteralCommandNode register(CommandDispatcher<ServerCommandSource> dispatcher,boolean dedicated) {
        BlockCommand.register(dispatcher);
        DeviceCommand.register(dispatcher);
        ScanCommand.register(dispatcher);
        StatusCommand.register(dispatcher);
        ListCommand.register(dispatcher);
        BindCommand.register(dispatcher);
        return ConnectCommand.register(dispatcher);
    }
}
