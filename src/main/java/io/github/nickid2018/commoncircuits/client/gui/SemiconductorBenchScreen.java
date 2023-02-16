package io.github.nickid2018.commoncircuits.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.nickid2018.commoncircuits.inventory.SemiconductorBenchMenu;
import io.github.nickid2018.commoncircuits.util.CompatUtil;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

public class SemiconductorBenchScreen extends AbstractContainerScreen<SemiconductorBenchMenu> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("commoncircuits", "textures/gui/container/semiconductor_bench.png");

    public static final int[][][] BUTTONS_TEX_POS = new int[][][]{
            {{0, 219}, {0, 237}, {230, 4}},
            {{18, 219}, {18, 237}, {230, 22}},
            {{36, 219}, {36, 237}, {230, 40}},
            {{54, 219}, {54, 237}, {230, 58}}
    };

    public static final int[][] BUTTONS_POS = new int[][]{
            {71, 22}, {71, 50}, {71, 78}, {71, 106}
    };

    public static final String[] BUTTONS_TEXT = new String[]{
            "gui.commoncircuits.semiconductor_bench.diode", "gui.commoncircuits.semiconductor_bench.bjt",
            "gui.commoncircuits.semiconductor_bench.jfet", "gui.commoncircuits.semiconductor_bench.mosfet"
    };

    public static final int[][] CRAFT_BUTTON_TEX_POS = new int[][] {
            {72, 219}, {159, 219}
    };

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
        blit(poseStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        int level = menu.getBlazeLevel();
        if (level > 0)
            blit(poseStack, leftPos + 143, topPos + 113, 230, 0, Mth.clamp((18 * level + 20 - 1) / 20, 0, 18) + 1, 4);

        int parsedX = mx - leftPos;
        int parsedY = my - topPos;
        int isIn = -1;
        for (int i = 0; i < 4; i++) {
            if ((menu.getMode() & 0b11) == i)
                blit(poseStack, leftPos + BUTTONS_POS[i][0], topPos + BUTTONS_POS[i][1], BUTTONS_TEX_POS[i][1][0], BUTTONS_TEX_POS[i][1][1], 18, 18);
            else if (parsedX >= BUTTONS_POS[i][0] && parsedX <= BUTTONS_POS[i][0] + 18 &&
                    parsedY >= BUTTONS_POS[i][1] && parsedY <= BUTTONS_POS[i][1] + 18) {
                blit(poseStack, leftPos + BUTTONS_POS[i][0], topPos + BUTTONS_POS[i][1], BUTTONS_TEX_POS[i][2][0], BUTTONS_TEX_POS[i][2][1], 18, 18);
                isIn = i;
            } else
                blit(poseStack, leftPos + BUTTONS_POS[i][0], topPos + BUTTONS_POS[i][1], BUTTONS_TEX_POS[i][0][0], BUTTONS_TEX_POS[i][0][1], 18, 18);
        }

        if (isIn != -1)
            renderTooltip(poseStack, CompatUtil.translated(BUTTONS_TEXT[isIn]), mx, my);
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

    @Override
    public boolean mouseClicked(double mx, double my, int i) {
        int isIn = -1;
        mx -= leftPos;
        my -= topPos;
        for (int index = 0; index < 4; index++) {
            if (mx >= BUTTONS_POS[index][0] && mx <= BUTTONS_POS[index][0] + 18 &&
                    my >= BUTTONS_POS[index][1] && my <= BUTTONS_POS[index][1] + 18) {
                isIn = index;
                break;
            }
        }
        if (isIn != -1 && menu.clickMenuButton(minecraft.player, isIn)) {
            minecraft.gameMode.handleInventoryButtonClick(menu.containerId, isIn);
            return true;
        }
        return super.mouseClicked(mx, my, i);
    }
}
