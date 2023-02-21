package io.github.nickid2018.commoncircuits.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.nickid2018.commoncircuits.inventory.WireConnectorMenu;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class WireConnectorScreen extends AbstractContainerScreen<WireConnectorMenu> {

    public static final ResourceLocation TEXTURE = new ResourceLocation("commoncircuits", "textures/gui/container/wire_connector.png");

    public static final int[][][] BUTTON_SWITCH_TEX = new int[][][]{
            {{230, 8}, {241, 8}},
            {{230, 20}, {241, 20}},
            {{230, 32}, {241, 32}}
    };

    public static final int BUTTON_SWITCH_WIDTH = 11;
    public static final int BUTTON_SWITCH_HEIGHT = 12;

    public static final int BUTTON_SWITCH_X = 205;
    public static final int BUTTON_SWITCH_Y = 4;

    private Direction nowDirection = Direction.DOWN;

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
        int parsedMouseX = mx - leftPos;
        int parsedMouseY = my - topPos;

        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        // Switch button...
        int leftType = 0;
        if (nowDirection == Direction.DOWN)
            leftType = 1;
        else if (parsedMouseX >= BUTTON_SWITCH_X && parsedMouseX <= BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH
                && parsedMouseY >= BUTTON_SWITCH_Y && parsedMouseY <= BUTTON_SWITCH_Y + BUTTON_SWITCH_HEIGHT)
            leftType = 2;
        blit(poseStack, leftPos + BUTTON_SWITCH_X, topPos + BUTTON_SWITCH_Y,
                BUTTON_SWITCH_TEX[leftType][0][0], BUTTON_SWITCH_TEX[leftType][0][1], BUTTON_SWITCH_WIDTH, BUTTON_SWITCH_HEIGHT);
        int rightType = 0;
        if (nowDirection == Direction.EAST)
            rightType = 1;
        else if (parsedMouseX >= BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH && parsedMouseX <= BUTTON_SWITCH_X + 2 * BUTTON_SWITCH_WIDTH
                && parsedMouseY >= BUTTON_SWITCH_Y && parsedMouseY <= BUTTON_SWITCH_Y + BUTTON_SWITCH_HEIGHT)
            rightType = 2;
        blit(poseStack, leftPos + BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH, topPos + BUTTON_SWITCH_Y,
                BUTTON_SWITCH_TEX[rightType][1][0], BUTTON_SWITCH_TEX[rightType][1][1], BUTTON_SWITCH_WIDTH, BUTTON_SWITCH_HEIGHT);

        // Link slots
        WireConnectorMenu.DisplayChannelEntry[] entries = menu.getDisplayChannelEntries().get(nowDirection);
        for (int i = 0; i < entries.length; i++) {
            WireConnectorMenu.DisplayChannelEntry entry = entries[i];
            blit(poseStack, leftPos + 42, topPos + 52 + 20 * i, entry.input ? 230 : 238, 0, 8, 8);
            blit(poseStack, leftPos + 66, topPos + 52 + 20 * i, entry.output ? 230 : 238, 0, 8, 8);
        }
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {
        font.draw(poseStack, title, titleLabelX, titleLabelY, 4210752);
    }

    @Override
    public void onClose() {
        super.onClose();
        menu.confirmAndClose(); // Temporary
    }

    @Override
    public boolean mouseClicked(double mx, double my, int i) {
        int parsedMouseX = (int) mx - leftPos;
        int parsedMouseY = (int) my - topPos;
        if (parsedMouseX >= BUTTON_SWITCH_X && parsedMouseX <= BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH
                && parsedMouseY >= BUTTON_SWITCH_Y && parsedMouseY <= BUTTON_SWITCH_Y + BUTTON_SWITCH_HEIGHT) {
            if (nowDirection != Direction.DOWN) {
                nowDirection = Direction.values()[nowDirection.ordinal() - 1];
                return true;
            }
        } else if (parsedMouseX >= BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH && parsedMouseX <= BUTTON_SWITCH_X + 2 * BUTTON_SWITCH_WIDTH
                && parsedMouseY >= BUTTON_SWITCH_Y && parsedMouseY <= BUTTON_SWITCH_Y + BUTTON_SWITCH_HEIGHT) {
            if (nowDirection != Direction.EAST) {
                nowDirection = Direction.values()[nowDirection.ordinal() + 1];
                return true;
            }
        }
        int inputIndex = -1;
        int outputIndex = -1;
        for (int j = 0; j < 8; j++) {
            if (parsedMouseX >= 42 && parsedMouseX <= 50 && parsedMouseY >= 52 + 20 * j && parsedMouseY <= 60 + 20 * j) {
                inputIndex = j;
                break;
            }
            if (parsedMouseX >= 66 && parsedMouseX <= 74 && parsedMouseY >= 52 + 20 * j && parsedMouseY <= 60 + 20 * j) {
                outputIndex = j;
                break;
            }
        }
        if (inputIndex != -1)
            return menu.toggleInput(nowDirection, inputIndex);
        if (outputIndex != -1)
            return menu.toggleOutput(nowDirection, outputIndex);
        return super.mouseClicked(mx, my, i);
    }
}
