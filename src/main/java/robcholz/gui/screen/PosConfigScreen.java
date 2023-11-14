package robcholz.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.LiteralText;
import robcholz.gui.widget.BlockListWidget;
import robcholz.manager.CommBlockManager;

import java.util.Objects;

@Environment(EnvType.CLIENT)
public class PosConfigScreen extends Screen {
    private final Screen parent;
    private final CommBlockManager blockManager;
    private BlockListWidget blockListWidget;
    private String tooltipText;

    protected PosConfigScreen(Screen parent) {
        super(new LiteralText("rMineComm-Pos"));
        this.parent = parent;
        this.blockManager = CommBlockManager.getInstance();
    }

    @Override
    protected void init() {
        int paneY = 48;
        int paneWidth = this.width / 2 - 8;
        int rightPaneX = width - paneWidth;
        this.blockListWidget = new BlockListWidget(this.parent, paneWidth, this.height, paneY + 19, this.height - 36, 36, onSelect -> {
        });
        this.blockManager.getMarkedElements().forEach(this.blockListWidget::addEntry);
        this.addButton(new ButtonWidget(this.width / 2 - 100, this.height - 28, 200, 20, I18n.translate("gui.done"),
                button -> Objects.requireNonNull(this.minecraft).openScreen(this.parent))
        );
        super.init();
    }

    @Override
    public void render(int mouseX, int mouseY, float delta) {
        this.tooltipText = null;
        this.renderBackground();
        this.blockListWidget.render(mouseX, mouseY, delta);
        this.drawCenteredString(this.font, this.title.asFormattedString(), this.width / 2, 10, 16777215);
        if (this.tooltipText != null)
            this.renderTooltip(this.tooltipText, mouseX, mouseY);
        super.render(mouseX, mouseY, delta);
    }

    @Override
    public boolean mouseScrolled(double d, double e, double amount) {
        if (this.blockListWidget.isMouseOver(d, e))
            this.blockListWidget.mouseScrolled(d, e, amount);
        return super.mouseScrolled(d, e, amount);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.blockListWidget.mouseClicked(mouseX, mouseY, button);
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char chr, int keyCode) {
        return super.charTyped(chr, keyCode);
    }
}
