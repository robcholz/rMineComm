package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import robcholz.monitor.BlockStateMonitor;
import robcholz.monitor.MonitoredBlockState;

public class BlockCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("add").then(CommandManager.literal("pos")
                        .then(CommandManager.argument("x", IntegerArgumentType.integer())
                                .then(CommandManager.argument("y", IntegerArgumentType.integer())
                                        .then(CommandManager.argument("z", IntegerArgumentType.integer())
                                                .executes(BlockCommand::addBlock))))))
                .then(CommandManager.literal("remove").then(CommandManager.literal("pos")
                        .then(CommandManager.argument("x", IntegerArgumentType.integer())
                                .then(CommandManager.argument("y", IntegerArgumentType.integer())
                                        .then(CommandManager.argument("z", IntegerArgumentType.integer())
                                                .executes(BlockCommand::removeBlock))))))
        );
    }

    private static int addBlock (CommandContext<ServerCommandSource> context) {
        BlockPos pos = Command.getBlockPos(context);
        MonitoredBlockState state = new MonitoredBlockState(context.getSource().getWorld(), pos);
        String format;
        if (BlockStateMonitor.getInstance().addBlock(state))
            format = "Block %s %s is added at [%d,%d,%d]";
        else
            format = "Block %s %s at [%d,%d,%d] can't be added since it has been in the list.";
        context.getSource().sendFeedback(new LiteralText(String.format(format,
                state.getBlockName().asFormattedString(),
                state.getInternalType().asFormattedString(),
                pos.getX(),
                pos.getY(),
                pos.getZ()
        )), false);
        return 1;
    }

    private static int removeBlock (CommandContext<ServerCommandSource> context) {
        BlockPos pos = Command.getBlockPos(context);
        MonitoredBlockState state = new MonitoredBlockState(context.getSource().getWorld(), pos);
        String format;
        if (BlockStateMonitor.getInstance().removeBlock(state)) //successful
            format = "Block %s %s at [%d,%d,%d] is removed from the list.";
        else // unsuccessful
            format = "Block %s %s at [%d,%d,%d] can't be removed since it has not been added to the list.";
        context.getSource().sendFeedback(new LiteralText(String.format(format,
                state.getBlockName().asFormattedString(),
                state.getInternalType().asFormattedString(),
                pos.getX(),
                pos.getY(),
                pos.getZ()
        )), false);
        return 1;
    }
}
