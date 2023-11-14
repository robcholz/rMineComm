package robcholz.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import robcholz.gui.entry.DeviceListEntry;
import robcholz.gui.widget.DeviceListWidget;
import robcholz.hardwarecomm.device.CommDeviceFactory;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.hardwarecomm.scanner.CommScannerFactory;
import robcholz.manager.CommBlockManager;
import robcholz.manager.CommDeviceManager;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class GUIConfigScreen extends Screen {
    private final Screen parent;
    private final CommDeviceManager deviceManager;
    private final CommBlockManager blockManager;
    private DeviceListWidget myDeviceList;
    private DeviceListWidget otherDeviceList;
    private ButtonWidget connectButton;
    private ButtonWidget disconnectButton;
    private ButtonWidget addButton;
    private ButtonWidget deleteButton;
    private DeviceListEntry selectedMyDevice;
    private DeviceListEntry selectedScannedDevice;
    private String tooltipText;
    private int paneY;
    private int paneWidth;
    private int rightPaneX;

    public GUIConfigScreen(Screen screen) {
        super(new LiteralText(I18n.translate("gui.rminecomm.config_screen.title")));
        this.deviceManager = CommDeviceManager.getInstance();
        this.blockManager = CommBlockManager.getInstance();
        this.parent = screen;
    }

    @Override
    protected void init() {
        paneY = 48;
        paneWidth = this.width / 2 - 8;
        rightPaneX = width - paneWidth;

        this.myDeviceList = new DeviceListWidget(this, paneWidth, this.height, 32, this.height - 64, 36, onSelected -> {
            updateButtonActiveStates();
            this.selectedMyDevice = this.myDeviceList.getSelected();
        });
        this.otherDeviceList = new DeviceListWidget(this, paneWidth, this.height, 32, this.height - 64, 36, onSelected -> {
            updateButtonActiveStates();
            this.selectedScannedDevice = this.otherDeviceList.getSelected();
        });
        this.refreshMyDeviceList();
        this.refreshOtherDeviceList();
        this.myDeviceList.setLeftPos(this.width - this.myDeviceList.getRowWidth() - 19);
        this.otherDeviceList.setLeftPos(0);
        this.connectButton = this.addButton(new ButtonWidget(this.width / 2 - 154, this.height - 52, 100, 20, I18n.translate("gui.rminecomm.connect"), button -> {
           // this.minecraft.openScreen(new ConnectScreen()); // TODO
            this.selectedMyDevice.connect();
        }));
        this.disconnectButton = this.addButton(new ButtonWidget(this.width / 2 - 50, this.height - 52, 100, 20, I18n.translate("gui.rminecomm.disconnect"), button -> {
            this.selectedMyDevice.disconnect();
        }));
        this.addButton = this.addButton(new ButtonWidget(this.width / 2 + 4 + 50, this.height - 52, 100, 20, I18n.translate("gui.rminecomm.add"), button -> {
            if (this.selectedScannedDevice != null) {
              //  this.minecraft.openScreen(new AddDeviceScreen()); // TODO
                this.deviceManager.add(CommDeviceFactory.getInstance().createDevice(this.selectedScannedDevice.getDeviceData()));
                this.refreshMyDeviceList();
                this.refreshOtherDeviceList();
            }
        }));
        this.deleteButton = this.addButton(new ButtonWidget(this.width / 2 - 154, this.height - 28, 70, 20, I18n.translate("selectServer.delete"), button -> {
            if (this.selectedMyDevice != null) {
                if (this.selectedMyDevice.isConnected())
                    this.selectedMyDevice.disconnect();
                this.deviceManager.remove(this.selectedMyDevice.getDeviceData().getName());
                this.refreshMyDeviceList();
                this.refreshOtherDeviceList();
            }
        }));
        this.addButton(new ButtonWidget(this.width / 2 - 74, this.height - 28, 70, 20, I18n.translate("gui.rminecomm.scan"), button -> {
            this.refreshOtherDeviceList();
        }));
        this.addButton(new ButtonWidget(this.width / 2 + 4, this.height - 28, 70, 20, I18n.translate("gui.rminecomm.next_page"),
                button -> Objects.requireNonNull(this.minecraft).openScreen(new PosConfigScreen(this))
        ));
        this.addButton(new ButtonWidget(this.width / 2 + 4 + 76, this.height - 28, 75, 20, I18n.translate("gui.cancel"),
                button -> Objects.requireNonNull(minecraft).openScreen(parent)
        ));
        this.children.add(this.myDeviceList);
        this.children.add(this.otherDeviceList);
        updateButtonActiveStates();
        this.myDeviceList.ping();
        this.otherDeviceList.ping();
        super.init();
    }

    private void refreshMyDeviceList() {
        this.myDeviceList.clear();
        this.deviceManager.getMarkedElements().forEach(this.myDeviceList::addEntry);
        this.myDeviceList.setFocused(null);
        this.myDeviceList.ping();
    }

    private void refreshOtherDeviceList() {
        List<CommDeviceInterface> list = new LinkedList<>();
        this.otherDeviceList.clear();
        list.addAll(CommScannerFactory.getInstance().scan(CommDeviceInterface.BLUETOOTH_DEVICE));
        list.addAll(CommScannerFactory.getInstance().scan(CommDeviceInterface.MQTT_DEVICE));
        list.addAll(CommScannerFactory.getInstance().scan(CommDeviceInterface.SERIAL_DEVICE));
        list.removeAll(this.deviceManager.getMarkedElements());
        list.forEach(this.otherDeviceList::addEntry);
        this.otherDeviceList.setFocused(null);
        this.otherDeviceList.ping();
    }

    private void updateButtonActiveStates() {
        this.connectButton.active = false;
        this.disconnectButton.active = false;
        this.addButton.active = false;
        this.deleteButton.active = false;

        if (this.myDeviceList.getFocused() != null) {
            this.connectButton.active = true;
            this.disconnectButton.active = true;
            this.deleteButton.active = true;
        }
        if (this.otherDeviceList.getFocused() != null) {
            this.addButton.active = true;
        }
    }

    public void setTooltip(String text) {
        this.tooltipText = text;
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.tooltipText = null;
        this.renderBackground();
        this.myDeviceList.render(mouseX, mouseY, delta);
        this.otherDeviceList.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 10, 16777215);
        this.drawCenteredString(this.font, I18n.translate("gui.rminecomm.config_screen.scanned_devices"), this.width / 4, 20, 0xFFFFFF);
        this.drawCenteredString(this.font, I18n.translate("gui.rminecomm.config_screen.my_devices"), this.width / 4 * 3, 20, 0xFFFFFF);
        if (this.tooltipText != null)
            this.renderTooltip(this.tooltipText, mouseX, mouseY);
        super.render(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double amount) {
        if (this.myDeviceList.isMouseOver(d, e))
            this.myDeviceList.mouseScrolled(d, e, amount);
        if (this.otherDeviceList.isMouseOver(d, e))
            this.otherDeviceList.mouseScrolled(d, e, amount);
        return super.mouseScrolled(d, e, amount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.myDeviceList.mouseClicked(mouseX, mouseY, button);
        this.otherDeviceList.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
