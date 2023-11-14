package robcholz.gui.entry;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.util.Util;
import robcholz.gui.entry.DeviceListEntry;

@Environment(EnvType.CLIENT)
public class ScanningEntry extends AlwaysSelectedEntryListWidget.Entry<DeviceListEntry> {
    private final MinecraftClient client = MinecraftClient.getInstance();

    public ScanningEntry() {
    }

    public void render(int i, int j, int k, int l, int m, int n, int o, boolean bl, float f) {
        int var10000 = j + m / 2;
        this.client.textRenderer.getClass();
        int p = var10000 - 9 / 2;
        this.client.textRenderer.draw("Scanning for devices", (float) (this.client.currentScreen.width / 2 - this.client.textRenderer.getStringWidth("Scanning for devices") / 2), (float) p, 16777215);
        String string;
        switch ((int) (Util.getMeasuringTimeMs() / 300L % 4L)) {
            case 0:
            default:
                string = "O o o";
                break;
            case 1:
            case 3:
                string = "o O o";
                break;
            case 2:
                string = "o o O";
        }

        TextRenderer var12 = this.client.textRenderer;
        float var10002 = (float) (this.client.currentScreen.width / 2 - this.client.textRenderer.getStringWidth(string) / 2);
        this.client.textRenderer.getClass();
        var12.draw(string, var10002, (float) (p + 9), 8421504);
    }
}