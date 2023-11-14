package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robcholz.manager.CommDeviceManager;

public class StatusCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("available")
                        .then(CommandManager.argument("deviceName", StringArgumentType.string())
                                .executes(StatusCommand::available)))
        );
    }

    private static int available (CommandContext<ServerCommandSource> context) {
        String deviceName = StringArgumentType.getString(context, "deviceName");
        String format;
        if (CommDeviceManager.getInstance().isAvailable(deviceName))
            format = "Device %s is available";
        else
            format = "Device %s is unavailable";
        Text text = new LiteralText(String.format(format, deviceName));
        text.getStyle().setColor(Formatting.GRAY).setItalic(true);
        context.getSource().sendFeedback(text,false);
        return 1;
    }
}
