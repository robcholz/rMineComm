package robcholz.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.prospector.modmenu.util.HardcodedUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.NarratorManager;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import robcholz.Reference;
import robcholz.gui.SelectAction;
import robcholz.gui.entry.DeviceListEntry;
import robcholz.gui.screen.GUIConfigScreen;
import robcholz.hardwarecomm.device.CommDeviceInterface;
import robcholz.setting.data.DeviceData;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class DeviceListWidget extends AlwaysSelectedEntryListWidget<DeviceListEntry> implements AutoCloseable {
    public static final Identifier UNKNOWN_DEVICE_ICON;
    public static final Identifier SERIAL_DEVICE_ICON;
    public static final Identifier BLE_DEVICE_ICON;
    public static final Identifier MQTT_DEVICE_ICON;
    private final SelectAction<DeviceListWidget> selectAction;
    private final GUIConfigScreen parent;
    private final List<DeviceListEntry> entries;

    static {
        UNKNOWN_DEVICE_ICON = new Identifier(Reference.MOD_ID, "textures/misc/unknown_device.png");
        SERIAL_DEVICE_ICON = new Identifier(Reference.MOD_ID, "textures/misc/serial_device.png");
        BLE_DEVICE_ICON = new Identifier(Reference.MOD_ID, "textures/misc/ble_device.png");
        MQTT_DEVICE_ICON = new Identifier(Reference.MOD_ID, "textures/misc/mqtt_device.png");
    }

    public DeviceListWidget(GUIConfigScreen parent, int width, int height, int top, int bottom, int itemHeight, SelectAction<DeviceListWidget> selectAction) {
        super(MinecraftClient.getInstance(), width, height, top, bottom, itemHeight);
        this.selectAction = selectAction;
        this.parent = parent;
        this.entries = new LinkedList<>();
    }

    public void select(DeviceListEntry entry) {
        this.setSelected(entry);
        if (entry != null) {
            DeviceData deviceData = entry.getDeviceData();
            NarratorManager.INSTANCE.narrate(new TranslatableText("narrator.select", HardcodedUtil.formatFabricModuleName(deviceData.getName())).getString());
        }
    }

    public void ping(){
        this.entries.forEach(DeviceListEntry::ping);
    }

    @Override
    public void setSelected(DeviceListEntry entry) {
        super.setSelected(entry);
        this.selectAction.onSelected(this);
    }

    @Override
    public int addEntry(DeviceListEntry entry) {
        this.entries.add(entry);
        return super.addEntry(entry);
    }

    @Override
    protected int getScrollbarPosition() {
        return this.getRowLeft() + this.width - 12;
    }

    @Override
    public int getRowWidth() {
        return this.width - (Math.max(0, this.getMaxPosition() - (this.bottom - this.top - 4)) > 0 ? 18 : 12);
    }

    @Override
    protected int getRowLeft() {
        return this.left + 6;
    }

    public void addEntry(CommDeviceInterface entry) {
        addEntry(new DeviceListEntry(entry.getName(), entry.getDeviceType(), entry.getID(), this.parent, this));
    }

    public void clear() {
        this.clearEntries();
    }

    @Override
    public void close() throws Exception {
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderList(int x, int y, int mouseX, int mouseY, float delta) {
        int itemCount = this.getItemCount();
        Tessellator tessellator_1 = Tessellator.getInstance();
        BufferBuilder buffer = tessellator_1.getBuffer();

        for (int index = 0; index < itemCount; ++index) {
            int entryTop = this.getRowTop(index) + 2;
            int entryBottom = this.getRowTop(index) + this.itemHeight;
            if (entryBottom >= this.top && entryTop <= this.bottom) {
                int entryHeight = this.itemHeight - 4;
                DeviceListEntry entry = this.getEntry(index);
                int rowWidth = this.getRowWidth();
                int entryLeft;
                if (this.renderSelection && this.isSelectedItem(index)) {
                    entryLeft = getRowLeft() - 2 + entry.getXOffset();
                    int selectionRight = x + rowWidth + 2;
                    RenderSystem.disableTexture();
                    float float_2 = this.isFocused() ? 1.0F : 0.5F;
                    RenderSystem.color4f(float_2, float_2, float_2, 1.0F);
                    buffer.begin(7, VertexFormats.POSITION);
                    buffer.vertex((double) entryLeft, (double) (entryTop + entryHeight + 2), 0.0D).next();
                    buffer.vertex((double) selectionRight, (double) (entryTop + entryHeight + 2), 0.0D).next();
                    buffer.vertex((double) selectionRight, (double) (entryTop - 2), 0.0D).next();
                    buffer.vertex((double) entryLeft, (double) (entryTop - 2), 0.0D).next();
                    tessellator_1.draw();
                    RenderSystem.color4f(0.0F, 0.0F, 0.0F, 1.0F);
                    buffer.begin(7, VertexFormats.POSITION);
                    buffer.vertex((double) (entryLeft + 1), (double) (entryTop + entryHeight + 1), 0.0D).next();
                    buffer.vertex((double) (selectionRight - 1), (double) (entryTop + entryHeight + 1), 0.0D).next();
                    buffer.vertex((double) (selectionRight - 1), (double) (entryTop - 1), 0.0D).next();
                    buffer.vertex((double) (entryLeft + 1), (double) (entryTop - 1), 0.0D).next();
                    tessellator_1.draw();
                    RenderSystem.enableTexture();
                }

                entryLeft = this.getRowLeft();
                entry.render(index, entryTop, entryLeft, rowWidth, entryHeight, mouseX, mouseY, this.isMouseOver((double) mouseX, (double) mouseY) && Objects.equals(this.getEntryAtPos((double) mouseX, (double) mouseY), entry), delta);
            }
        }
    }

    public final DeviceListEntry getEntryAtPos(double x, double y) {
        int int_5 = MathHelper.floor(y - (double) this.top) - this.headerHeight + (int) this.getScrollAmount() - 4;
        int index = int_5 / this.itemHeight;
        return x < (double) this.getScrollbarPosition() && x >= (double) getRowLeft() && x <= (double) (getRowLeft() + getRowWidth()) && index >= 0 && int_5 >= 0 && index < this.getItemCount() ? this.children().get(index) : null;
    }
}
