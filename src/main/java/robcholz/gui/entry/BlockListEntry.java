package robcholz.gui.entry;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.AlwaysSelectedEntryListWidget;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import robcholz.gui.BadgeRenderer;
import robcholz.gui.widget.BlockListWidget;
import robcholz.setting.data.BlockData;

import java.util.Collection;

public class BlockListEntry extends AlwaysSelectedEntryListWidget.Entry<BlockListEntry> {
    private final BlockData blockData;
    private final MinecraftClient client;
    private final BadgeRenderer badgeRenderer;
    private final BadgeRenderer bindBadgeRenderer;
    private boolean connectionStatus;

    BlockListWidget list;
    Screen screen;

    public BlockListEntry(String name, int dimension, int type, int blockID, Collection<Integer> pos, Screen screen, BlockListWidget list) {
        this.blockData = new BlockData(name, dimension, type, blockID, pos);
        this.client = MinecraftClient.getInstance();
        this.screen = screen;
        this.list = list;
        this.badgeRenderer = new BadgeRenderer(this);
        this.bindBadgeRenderer = BadgeRenderer.BIND;
    }

    public BlockData getBlockData() {
        return blockData;
    }

    protected void draw(int x, int y, Identifier textureId) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.client.getTextureManager().bindTexture(textureId);
        RenderSystem.enableBlend();
        DrawableHelper.blit(x, y, 0.0F, 0.0F, 32, 32, 32, 32);
        RenderSystem.disableBlend();
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
        /*
        int t = mouseX - x;
        int u = mouseY - y;
        int nameLength = this.client.textRenderer.getStringWidth(this.blockData.getName());
        int idLength = this.client.textRenderer.getStringWidth(this.blockData.getID());
        int badgeWidth = this.badgeRenderer.getWidth();
        if (t >= rowWidth - 15 && t <= rowWidth - 5 && u >= 0 && u <= 8) {
            this.screen.setTooltip(this.connectionStatus ? "Connected" : "no connection");
        } // connection level
        else if ((t >= 32 + 3 && t <= 32 + 3 + nameLength) && (u >= 0 && u <= 8)) {
            this.screen.setTooltip(this.blockData.getName());
        } // device name
        else if ((t >= 32 + 3 && t <= 32 + 3 + idLength) && (y + 20 < mouseY && mouseY < y + 20 + 8)) {
            this.screen.setTooltip(this.blockData.getID());
        } // deviceID
        else if ((t >= 32 + 3 + 80 + 3 && t <= 32 + 3 + 80 + 3 + badgeWidth) && (u >= 0 && u <= 8)) {
            this.screen.setTooltip(this.badgeRenderer.getText());
        }

         */
    }

    private void renderGuiItemIcon(ItemStack stack, int x, int y) {
        this.renderGuiItemModel(stack, x, y, MinecraftClient.getInstance().getItemRenderer().getHeldItemModel(stack, (World) null, (LivingEntity) null));
    }

    private void renderGuiItemModel(ItemStack stack, int x, int y, BakedModel model) {
        RenderSystem.pushMatrix();
        MinecraftClient.getInstance().getTextureManager().bindTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX);
        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEX).setFilter(false, false);
        RenderSystem.enableRescaleNormal();
        RenderSystem.enableAlphaTest();
        RenderSystem.defaultAlphaFunc();
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        float zOffset = 0;
        RenderSystem.translatef((float) x, (float) y, 100.0F + zOffset);
        RenderSystem.translatef(8.0F, 8.0F, 0.0F);
        RenderSystem.scalef(1.0F, -1.0F, 1.0F);
        RenderSystem.scalef(16.0F, 16.0F, 16.0F);
        MatrixStack matrixStack = new MatrixStack();
        matrixStack.scale(1.5f, 1.5f, 1.0f);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformation.Mode.GUI, false, matrixStack, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);
        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }
        RenderSystem.disableAlphaTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

    @Override
    public void render(int index, int y, int x, int rowWidth, int rowHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
        ItemStack itemStack = new ItemStack(Registry.BLOCK.get(blockData.getID()).asItem());
        itemStack.setCount(1);
        renderGuiItemIcon(itemStack, x + 9, y + 7);
        drawTrimmedText(y + 1, x + 32 + 3, 72, blockData.getName(), 0xFFFFFF);
        String transKey = Registry.BLOCK.get(blockData.getID()).getTranslationKey();
        drawTrimmedText(y + 11, x + 32 + 3, rowWidth - 32 - 3, I18n.translate(transKey), 0x808080);
        drawTrimmedText(y + 21, x + 32 + 3, rowWidth - 32 - 3, transKey, 0x808080);
        this.badgeRenderer.draw(rowWidth + this.list.getRowLeft() - this.badgeRenderer.getWidth(), y + 1);
        if (bindBadgeRenderer != null)
            this.bindBadgeRenderer.draw(rowWidth + this.list.getRowLeft() - this.bindBadgeRenderer.getWidth(), y + 21);
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
