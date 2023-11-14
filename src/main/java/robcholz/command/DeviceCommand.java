package robcholz.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.LiteralText;
import robcholz.hardwarecomm.device.AbstractCommDevice;
import robcholz.hardwarecomm.device.CommDeviceFactory;
import robcholz.manager.CommDeviceManager;

public class DeviceCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("add").then(CommandManager.literal("device")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .then(CommandManager.argument("deviceID", StringArgumentType.string())
                                        .executes(DeviceCommand::addDevice)))))
                .then(CommandManager.literal("remove").then(CommandManager.literal("device")
                        .then(CommandManager.argument("name", StringArgumentType.string())
                                .executes(DeviceCommand::removeDevice))))
        );
    }

    private static int addDevice (CommandContext<ServerCommandSource> context) {
        String deviceID = StringArgumentType.getString(context, "deviceID");
        String name = StringArgumentType.getString(context, "name");
        String format;
        if (CommDeviceManager.getInstance().add(deviceID)) { //successful
            format = "Device %s %s is added to the list.";
            CommDeviceManager.getInstance().rename(name,deviceID);
        }
        else // unsuccessful
            format = "Device %s %s can't be added.";
        context.getSource().sendFeedback(new LiteralText(String.format(format, name, deviceID)), false);
        return 1;
    }

    private static int removeDevice (CommandContext<ServerCommandSource> context) {
        String name = StringArgumentType.getString(context, "name");
        String format;
        if (CommDeviceManager.getInstance().remove(name))
            format = "Device %s is removed from the list.";
        else
            format = "Device %s can't be removed.";
        context.getSource().sendFeedback(new LiteralText(String.format(format, name)), false);
        return 1;
    }
}
