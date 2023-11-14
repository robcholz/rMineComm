package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.util.math.BlockPos;
import robcholz.manager.CommBlockManager;
import robcholz.state.CommBlockInterface;
import robcholz.state.CommBlockState;

public class BlockCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("add")
                        .then(CommandManager.literal("pos")
                                .then(CommandManager.argument("name", StringArgumentType.string())
                                        .then(CommandManager.argument("x", IntegerArgumentType.integer())
                                                .then(CommandManager.argument("y", IntegerArgumentType.integer())
                                                        .then(CommandManager.argument("z", IntegerArgumentType.integer())
                                                                .executes(BlockCommand::addBlock))
                                                )
                                        )
                                )
                        ))
                .then(CommandManager.literal("remove")
                        .then(CommandManager.literal("pos")
                                .then(CommandManager.argument("name", StringArgumentType.string()))
                        )
                )
        );
    }

    private static int addBlock (CommandContext<ServerCommandSource> context) {
        BlockPos pos = getBlockPos(context);
        String name = StringArgumentType.getString(context, "name");
        CommBlockInterface block = new CommBlockState(name, context.getSource().getWorld().getDimension().getType().getRawId(), pos);
        String format;
        if (CommBlockManager.getInstance().add(block))
            format = "Block %s %s is added at [%d,%d,%d]";
        else
            format = "Block %s %s at [%d,%d,%d] can't be added since it has been in the list.";
        context.getSource().sendFeedback(new LiteralText(String.format(format,
                block.getBlockName().asFormattedString(),
                block.getInternalType().asFormattedString(),
                pos.getX(),
                pos.getY(),
                pos.getZ()
        )), false);
        return 1;
    }

    private static int removeBlock (CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        CommBlockInterface block = CommBlockManager.getInstance().getPos(name);
        String format;
        if (CommBlockManager.getInstance().remove(name)) //successful
            format = "Block %s %s at [%d,%d,%d] is removed from the list.";
        else // unsuccessful
            format = "Block %s %s at [%d,%d,%d] can't be removed since it has not been added to the list.";
        context.getSource().sendFeedback(new LiteralText(String.format(format,
                block.getBlockName().asFormattedString(),
                block.getInternalType().asFormattedString(),
                block.getBlockPos().getX(),
                block.getBlockPos().getY(),
                block.getBlockPos().getZ()
        )), false);
        return 1;
    }

    private static BlockPos getBlockPos (CommandContext<ServerCommandSource> context) {
        return new BlockPos(
                IntegerArgumentType.getInteger(context, "x"),
                IntegerArgumentType.getInteger(context, "y"),
                IntegerArgumentType.getInteger(context, "z")
        );
    }
}
