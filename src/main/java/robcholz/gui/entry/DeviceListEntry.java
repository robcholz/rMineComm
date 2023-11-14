package robcholz.gui.entry;

import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import robcholz.gui.BadgeRenderer;
import robcholz.gui.screen.GUIConfigScreen;
import robcholz.gui.widget.DeviceListWidget;
import robcholz.hardwarecomm.comm.DeviceException;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.manager.CommDeviceManager;
import robcholz.setting.data.DeviceData;

@Environment(EnvType.CLIENT)
public class DeviceListEntry extends AlwaysSelectedEntryListWidget.Entry<DeviceListEntry> {
    private final DeviceData deviceData;
    private final MinecraftClient client;
    private static final char STATUS_NOT_FOUND = 0;
    private static final char STATUS_IN_USE = 1;
    private static final char STATUS_CONNECTED = 2;
    private char connectionStatus;
    private final BadgeRenderer badgeRenderer;

    DeviceListWidget list;
    GUIConfigScreen screen;

    public DeviceListEntry(String name, int deviceType, String ID, GUIConfigScreen screen, DeviceListWidget list) {
        this.deviceData = new DeviceData(name, deviceType, ID);
        this.client = MinecraftClient.getInstance();
        this.screen = screen;
        this.list = list;
        this.badgeRenderer = new BadgeRenderer(this);
        this.connectionStatus = STATUS_NOT_FOUND;
    }

    public DeviceData getDeviceData() {
        return deviceData;
    }

    public char connect() {
        try {
            CommDeviceManager.getInstance().connect(this.deviceData.getID());
            this.connectionStatus = STATUS_CONNECTED;
        } catch (DeviceException.DeviceInUseException e) {
            this.connectionStatus = STATUS_IN_USE;
        } catch (Exception e) {
            this.connectionStatus = STATUS_NOT_FOUND;
        }
        return this.connectionStatus;
    }

    public boolean disconnect() {
        try {
            CommDeviceManager.getInstance().disconnect(this.deviceData.getID());
            this.connectionStatus = STATUS_NOT_FOUND;
        } catch (DeviceException.DeviceUnclosedException e) {
            return false;
        }
        return true;
    }

    public boolean ping() {
        if (connect() == STATUS_CONNECTED)
            return disconnect();
        return false;
    }

    public boolean isConnected() {
        return this.connectionStatus == STATUS_CONNECTED;
    }

    protected void draw(int x, int y, Identifier textureId) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(textureId);
        RenderSystem.enableBlend();
        DrawableHelper.blit(x, y, 0.0F, 0.0F, 32, 32, 32, 32);
        RenderSystem.disableBlend();
    }

    private void drawIcon(int x, int y, int deviceType) {
        switch (deviceType) {
            case CommDeviceInterface.BLUETOOTH_DEVICE:
                this.draw(x, y, DeviceListWidget.BLE_DEVICE_ICON);
                break;
            case CommDeviceInterface.MQTT_DEVICE:
                this.draw(x, y, DeviceListWidget.MQTT_DEVICE_ICON);
                break;
            case CommDeviceInterface.SERIAL_DEVICE:
                this.draw(x, y, DeviceListWidget.SERIAL_DEVICE_ICON);
                break;
            default:
                this.draw(x, y, DeviceListWidget.UNKNOWN_DEVICE_ICON);
                break;
        }
    }

    private void setSignalIndicatorLevel(int index, int y, int x, int rowWidth, boolean scanMode, int level) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int r, s;
        if (!scanMode) {
            if (level < 0)
                level = 5;
            else if (level <= 5)
                level = 5 - level;
            else
                level = 0;
            r = 0;
            s = level;
        } else {
            r = 1;
            s = (int) (Util.getMeasuringTimeMs() / 100L + (long) (index * 2L) & 7L);
            if (s > 4)
                s = 8 - s;
        }
        this.client.getTextureManager().bindTexture(DrawableHelper.GUI_ICONS_LOCATION);
        DrawableHelper.blit(x + rowWidth - 15, y, (float) (r * 10), (float) (176 + s * 8), 10, 8, 256, 256);
    }

    private void drawTrimmedText(int y, int x, int maxNameWidth, String ID, int color) {
        TextRenderer font = this.client.textRenderer;
        String trimmedID = ID;
        if (font.getStringWidth(ID) > maxNameWidth) {
            trimmedID = font.trimToWidth(ID, maxNameWidth - font.getStringWidth("...")) + "...";
        }
        font.draw(trimmedID, x, y, color);
    }

    private void updateTooltip(int y, int x, int rowWidth, int mouseX, int mouseY) {
        int t = mouseX - x;
        int u = mouseY - y;
        int nameLength = this.client.textRenderer.getStringWidth(this.deviceData.getName());
        int idLength = this.client.textRenderer.getStringWidth(this.deviceData.getID());
        int badgeWidth = this.badgeRenderer.getWidth();
        if (t >= rowWidth - 15 && t <= rowWidth - 5 && u >= 0 && u <= 8) {
            if (this.connectionStatus == STATUS_NOT_FOUND)
                this.screen.setTooltip("Device Not Found");
            else if (this.connectionStatus == STATUS_CONNECTED)
                this.screen.setTooltip("Connected");
            else
                this.screen.setTooltip("no connection");
        } // connection level
        else if ((t >= 32 + 3 && t <= 32 + 3 + nameLength) && (u >= 0 && u <= 8)) {
            this.screen.setTooltip(this.deviceData.getName());
        } // device name
        else if ((t >= 32 + 3 && t <= 32 + 3 + idLength) && (y + 20 < mouseY && mouseY < y + 20 + 8)) {
            this.screen.setTooltip(this.deviceData.getID());
        } // deviceID
        else if ((t >= 32 + 3 + 80 + 3 && t <= 32 + 3 + 80 + 3 + badgeWidth) && (u >= 0 && u <= 8)) {
            this.screen.setTooltip(this.badgeRenderer.getText());
        }
    }

    @Override
    public void render(int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        this.drawIcon(x, y, deviceData.getType());
        drawTrimmedText(y + 1, x + 32 + 3, 72, deviceData.getName(), 0xFFFFFF);
        drawTrimmedText(y + 20, x + 32 + 3, rowWidth - 32 - 3, this.deviceData.getID(), 0x808080);
        this.badgeRenderer.draw(x + 32 + 3 + 80 + 3, y + 1);
        if (this.connectionStatus == STATUS_NOT_FOUND)
            setSignalIndicatorLevel(index, y, x, rowWidth, false, 0);
        else if (this.connectionStatus == STATUS_CONNECTED)
            setSignalIndicatorLevel(index, y, x, rowWidth, false, 5);
        else
            setSignalIndicatorLevel(index, y, x, rowWidth, true, 0);
        updateTooltip(y, x, rowWidth, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double v, double v1, int i) {
        this.list.select(this);
        return true;
    }

    public int getXOffset() {
        return 0;
    }
}
