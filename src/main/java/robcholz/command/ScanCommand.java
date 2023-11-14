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
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.hardwarecomm.scanner.CommScannerFactory;
import robcholz.manager.CommDeviceManager;

import java.util.List;

public class ScanCommand {
    public static LiteralCommandNode register (CommandDispatcher<ServerCommandSource> dispatcher) {
        return dispatcher.register(CommandManager.literal("rmc")
                .then(CommandManager.literal("scan").executes(ScanCommand::scanAllDevices)
                        .then(CommandManager.literal("usb").executes(ScanCommand::scanSerialDevices)
                                .then(CommandManager.literal("on").executes(ScanCommand::scanSetSerialOn))
                                .then(CommandManager.literal("off").executes(ScanCommand::scanSetSerialOff))
                        )
                        .then(CommandManager.literal("wan").executes(ScanCommand::scanMQTTDevices)
                                .then(CommandManager.literal("on").executes(ScanCommand::scanSetMQTTOn))
                                .then(CommandManager.literal("off").executes(ScanCommand::scanSetMQTTOff))
                        )
                        .then(CommandManager.literal("ble").executes(ScanCommand::scanBluetoothDevices)
                                .then(CommandManager.literal("on").executes(ScanCommand::scanSetBluetoothOn))
                                .then(CommandManager.literal("off").executes(ScanCommand::scanSetBluetoothOff))
                        )
                )
                .then(CommandManager.literal("on").executes(ScanCommand::autoScanOn))
                .then(CommandManager.literal("off").executes(ScanCommand::autoScanOff))
        );
    }

    private static int autoScanOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int autoScanOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanAllDevices (CommandContext<ServerCommandSource> context) {
        scanMQTTDevices(context);
        scanBluetoothDevices(context);
        scanSerialDevices(context);
        return 1;
    }

    private static int scanSerialDevices (CommandContext<ServerCommandSource> context) {
        sendScanResult(context,"Scanned USB Devices",CommDeviceInterface.SERIAL_DEVICE);
        return 1;
    }

    private static int scanMQTTDevices (CommandContext<ServerCommandSource> context) {
        Text text = new LiteralText("WAN devices cannot be scanned, please connection them manually!\n ")
                .setStyle(new Style().setColor(Formatting.GRAY));
        context.getSource().sendFeedback(text, false);
        return 1;
    }

    private static int scanBluetoothDevices (CommandContext<ServerCommandSource> context) {
        sendScanResult(context,"Scanned Bluetooth Devices",CommDeviceInterface.BLUETOOTH_DEVICE);
        return 1;
    }

    private static int scanSetSerialOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetSerialOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetMQTTOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetMQTTOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetBluetoothOn (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static int scanSetBluetoothOff (CommandContext<ServerCommandSource> context) {
        return 1;
    }

    private static void sendScanResult(CommandContext<ServerCommandSource> context,String title,int deviceType){
        List<CommDeviceInterface> devices = CommScannerFactory.getInstance().scan(deviceType);
        CommDeviceManager.getInstance().addToCacheSearchMap(devices);
        Text text = new LiteralText(title+":\n").setStyle(new Style().setColor(Formatting.GRAY));
        int count = 1;
        for (CommDeviceInterface device : devices) {
            text.append(new LiteralText(String.format("%d. ", count++)))
                    .append(new LiteralText(device.getName()).setStyle(new Style().setItalic(true)))
                    .append(" ")
                    .append(new LiteralText(device.getID()).setStyle(new Style().setColor(Formatting.GRAY))).append("\n");
        }
        context.getSource().sendFeedback(text, false);
    }
}
