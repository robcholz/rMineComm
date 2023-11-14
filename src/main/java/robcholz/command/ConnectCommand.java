package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import robcholz.hardwarecomm.comm.DeviceException;
import robcholz.manager.CommDeviceManager;

public class ConnectCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("connect")
                        .then(CommandManager.argument("deviceName", StringArgumentType.string())
                                .executes(ConnectCommand::connect)))
                .then(CommandManager.literal("disconnect")
                        .then(CommandManager.argument("deviceName", StringArgumentType.string())
                                .executes(ConnectCommand::disconnect)))
        );
    }

    private static int connect (CommandContext<ServerCommandSource> context) {
        String deviceName = StringArgumentType.getString(context, "deviceName");
        Text message = new LiteralText("Connection established at device: " + deviceName);
        message.getStyle().setColor(Formatting.GRAY).setItalic(true);
        Style errorStyle = new Style().setColor(Formatting.RED).setItalic(false);
        try {
            CommDeviceManager.getInstance().connect(deviceName);
        } catch (DeviceException.BadDeviceConfigException e) {
            message = new LiteralText("Cannot configure device: " + deviceName);
            message.setStyle(errorStyle);
        } catch (DeviceException.DeviceInUseException e) {
            message = new LiteralText("Device is in use: " + deviceName);
            message.setStyle(errorStyle);
        } catch (Exception e) {
            message = new LiteralText("Cannot open connection at device: " + deviceName);
            message.setStyle(errorStyle);
        }
        context.getSource().sendFeedback(message,false);
        return 1;
    }

    private static int disconnect (CommandContext<ServerCommandSource> context) {
        String deviceName = StringArgumentType.getString(context, "deviceName");
        Text message = new LiteralText("Connection closed at device: " + deviceName);
        message.getStyle().setColor(Formatting.GRAY).setItalic(true);
        try {
            CommDeviceManager.getInstance().disconnect(deviceName);
        } catch (DeviceException.DeviceUnclosedException e) {
            message = new LiteralText("Cannot close connection at device: " + deviceName);
            message.getStyle().setColor(Formatting.RED).setItalic(false);
        }
        context.getSource().sendFeedback(message,false);
        return 1;
    }
}
