package io.github.nickid2018.commoncircuits.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.nickid2018.commoncircuits.inventory.WireConnectorMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WireConnectorScreen extends AbstractContainerScreen<WireConnectorMenu> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("commoncircuits", "textures/gui/container/wire_connector.png");

    public WireConnectorScreen(WireConnectorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        imageWidth = 230;
        imageHeight = 219;

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

        int leftPos = this.leftPos;
        int topPos = this.topPos;

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {
    }
}
