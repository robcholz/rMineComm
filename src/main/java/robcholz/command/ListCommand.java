package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.manager.CommBlockManager;
import robcholz.manager.CommDeviceManager;
import robcholz.state.CommBlockInterface;

import java.util.List;
import java.util.Set;

public class ListCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("list")
                        .then(CommandManager.literal("device").executes(ListCommand::showDeviceList))
                        .then(CommandManager.literal("pos").executes(ListCommand::showPosList))
                        .then(CommandManager.literal("connection").executes(ListCommand::showConnectionList))
                )
        );
    }

    private static int showDeviceList (CommandContext<ServerCommandSource> context) {
        Set<AbstractCommDevice> devices = CommDeviceManager.getInstance().getMarkedElements();
        Text text = new LiteralText("Added Devices:\n").setStyle(new Style().setColor(Formatting.GRAY));
        int count = 1;
        for (CommDeviceInterface device : devices) {
            text.append(new LiteralText(String.format("%d. ", count++)))
                    .append(new LiteralText(device.getName()).setStyle(new Style().setItalic(true)))
                    .append(" ")
                    .append(new LiteralText(device.getID()).setStyle(new Style().setColor(Formatting.GRAY))).append("\n");
        }
        context.getSource().sendFeedback(text, false);
        return 1;
    }

    private static int showPosList (CommandContext<ServerCommandSource> context) {
        Set<CommBlockInterface> blockSet = CommBlockManager.getInstance().getMarkedElements();
        Text text = new LiteralText("Marked Position at:\n").setStyle(new Style().setColor(Formatting.GRAY));
        int count = 1;
        for (CommBlockInterface state : blockSet) {
            Text blockTypeText = new LiteralText(state.getBlockType().toString()).setStyle(new Style().setItalic(true));
            text.append(new LiteralText(String.format("  %d. %s ", count++, state.getName())))
                    .append(blockTypeText)
                    .append(new LiteralText(String.format(" at [%d,%d,%d] %d\n",
                            state.getBlockPos().getX(),
                            state.getBlockPos().getY(),
                            state.getBlockPos().getZ(),
                            state.getPower()
                    )));
        }
        context.getSource().sendFeedback(text, false);
        return 1;
    }

    private static int showConnectionList (CommandContext<ServerCommandSource> context) {
        List<AbstractCommDevice> connections = CommDeviceManager.getInstance().getConnectionList();
        Text text = new LiteralText("Established Connections:\n").setStyle(new Style().setColor(Formatting.GRAY));
        int count = 1;
        for (AbstractCommDevice connection : connections) {
            text.append(new LiteralText(String.format("%d. ", count++)))
                    .append(new LiteralText(connection.getName()).setStyle(new Style().setItalic(true)))
                    .append(" ")
                    .append(new LiteralText(connection.getID()).setStyle(new Style().setColor(Formatting.GRAY))).append("\n");
        }
        context.getSource().sendFeedback(text, false);
        return 1;
    }
}
