package io.github.nickid2018.commoncircuits.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.nickid2018.commoncircuits.inventory.SemiconductorBenchMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class SemiconductorBenchScreen extends AbstractContainerScreen<SemiconductorBenchMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("commoncircuits", "textures/gui/container/semiconductor_bench.png");

    public SemiconductorBenchScreen(SemiconductorBenchMenu menu, Inventory inventory) {
        this(menu, inventory, inventory.getDisplayName());
    }

    public SemiconductorBenchScreen(SemiconductorBenchMenu menu, Inventory inventory, Component component) {
        super(menu, inventory, component);
        imageWidth = 229;
        imageHeight = 218;
        inventoryLabelX = 35;
        inventoryLabelY = 125;
    }

    @Override
    protected void renderBg(PoseStack poseStack, float delta, int mx, int my) {
        //#if MC>=11701
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        //#else
        //$$ RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //$$ minecraft.getTextureManager().bind(TEXTURE);
        //#endif
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;
        blit(poseStack, x, y, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(PoseStack poseStack, int mx, int my, float delta) {
        renderBackground(poseStack);
        super.render(poseStack, mx, my, delta);
        renderTooltip(poseStack, mx, my);
    }

    @Override
    protected void init() {
        super.init();
        titleLabelX = (imageWidth - font.width(title)) / 2;
    }
}
