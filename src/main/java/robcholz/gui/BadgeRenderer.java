package robcholz.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import robcholz.gui.entry.BlockListEntry;
import robcholz.gui.entry.DeviceListEntry;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.setting.data.BlockData;
import robcholz.setting.data.DeviceData;
import robcholz.state.CommBlockInterface;

public class BadgeRenderer {
    private final int width;
    private final Badge badge;
    private final MinecraftClient client;
    public static final BadgeRenderer BIND = new BadgeRenderer(Badge.BIND);

    public BadgeRenderer(DeviceListEntry deviceListEntry) {
        DeviceData deviceData = deviceListEntry.getDeviceData();
        this.client = MinecraftClient.getInstance();
        if (deviceData.getType() == CommDeviceInterface.BLUETOOTH_DEVICE)
            this.badge = Badge.BLE;
        else if (deviceData.getType() == CommDeviceInterface.SERIAL_DEVICE)
            this.badge = Badge.SERIAL;
        else/* if (deviceData.getType() == CommDeviceInterface.MQTT_DEVICE)*/
            this.badge = Badge.MQTT;
        this.width = client.textRenderer.getStringWidth(this.badge.getText()) + 6;
    }

    public BadgeRenderer(BlockListEntry blockListEntry) {
        BlockData blockData = blockListEntry.getBlockData();
        this.client = MinecraftClient.getInstance();
        if (blockData.getType() == CommBlockInterface.COMPLEX)
            this.badge = Badge.COMPLEX;
        else if (blockData.getType() == CommBlockInterface.SIMPLE)
            this.badge = Badge.SIMPLE;
        else if (blockData.getType() == CommBlockInterface.ACCESSIBLE)
            this.badge = Badge.ACCESSIBLE;
        else /*if(blockData.getType()==CommBlockInterface.UNACCESSIBLE)*/
            this.badge = Badge.UNACCESSIBLE;
        this.width = client.textRenderer.getStringWidth(this.getText()) + 6;
    }

    public BadgeRenderer(Badge badge) {
        this.client = MinecraftClient.getInstance();
        this.badge = badge;
        this.width = client.textRenderer.getStringWidth(this.getText()) + 6;
    }

    public int getWidth() {
        return width;
    }

    public String getText() {
        return this.badge.getText();
    }

    public void draw(int badgeX, int badgeY) {
        drawBadge(badgeX, badgeY, this.width, this.badge.getText(), this.badge.getOutlineColor(), this.badge.getFillColor(), 0xCACACA);
    }

    private void drawBadge(int x, int y, int tagWidth, String text, int outlineColor, int fillColor, int textColor) {
        DrawableHelper.fill(x + 1, y - 1, x + tagWidth, y, outlineColor);
        DrawableHelper.fill(x, y, x + 1, y + this.client.textRenderer.fontHeight, outlineColor);
        DrawableHelper.fill(x + 1, y + 1 + this.client.textRenderer.fontHeight - 1, x + tagWidth, y + this.client.textRenderer.fontHeight + 1, outlineColor);
        DrawableHelper.fill(x + tagWidth, y, x + tagWidth + 1, y + this.client.textRenderer.fontHeight, outlineColor);
        DrawableHelper.fill(x + 1, y, x + tagWidth, y + this.client.textRenderer.fontHeight, fillColor);
        this.client.textRenderer.draw(text, (x + 1 + (tagWidth - this.client.textRenderer.getStringWidth(text)) / (float) 2), y + 1, textColor);
    }

    public static class Badge {
        private final String text;
        private final int outlineColor;
        private final int fillColor;
        public static Badge BLE = new Badge("BLE", 0x8810d098, 0x88046146);
        public static Badge SERIAL = new Badge("SERIAL", 0x8810A2A2, 0x880F6262);
        public static Badge MQTT = new Badge("MQTT", 0x887C89A3, 0x88202C43);
        public static Badge ACCESSIBLE = new Badge("ACCESSIBLE", 0x887C89A3, 0x88202C43);
        public static Badge UNACCESSIBLE = new Badge("UNACCESSIBLE", 0x887C89A3, 0x88202C43);
        public static Badge COMPLEX = new Badge("COMPLEX", 0x8810d098, 0x88046146);
        public static Badge SIMPLE = new Badge("SIMPLE", 0x8810A2A2, 0x880F6262);
        public static Badge BIND = new Badge("BIND", 0x880F0A0A, 0x88FF0000);

        public Badge(String text, int outlineColor, int fillColor) {
            this.text = text;
            this.outlineColor = outlineColor;
            this.fillColor = fillColor;
        }

        public String getText() {
            return text;
        }

        public int getOutlineColor() {
            return outlineColor;
        }

        public int getFillColor() {
            return fillColor;
        }
    }
}
