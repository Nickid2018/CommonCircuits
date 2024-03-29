package io.github.nickid2018.commoncircuits.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import io.github.nickid2018.commoncircuits.inventory.WireConnectorMenu;
import io.github.nickid2018.commoncircuits.util.ClientCompatUtil;
import io.github.nickid2018.commoncircuits.util.CompatUtil;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;

import java.util.List;

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

    public static final Component LABEL_I = CompatUtil.literal("I");
    public static final Component LABEL_O = CompatUtil.literal("O");
    public static final Component LABEL_C = CompatUtil.literal("C");

    public static final Component[] LABEL_NUMBER = new Component[]{
            CompatUtil.literal("1"), CompatUtil.literal("2"), CompatUtil.literal("3"), CompatUtil.literal("4"),
            CompatUtil.literal("5"), CompatUtil.literal("6"), CompatUtil.literal("7"), CompatUtil.literal("8")
    };

    public static final Component LABEL_CONNECTIONS = CompatUtil.translated("gui.commoncircuits.wire_connector.connections");

    public static final Component[] LABEL_DIRECTIONS = new Component[] {
            CompatUtil.translated("gui.commoncircuits.wire_connector.direction.down"),
            CompatUtil.translated("gui.commoncircuits.wire_connector.direction.up"),
            CompatUtil.translated("gui.commoncircuits.wire_connector.direction.north"),
            CompatUtil.translated("gui.commoncircuits.wire_connector.direction.south"),
            CompatUtil.translated("gui.commoncircuits.wire_connector.direction.west"),
            CompatUtil.translated("gui.commoncircuits.wire_connector.direction.east")
    };

    public static final float[][] CONNECTION_COLOR_LIST = new float[][] {
            {0.0F, 0.0F, 1.0F},
            {0.0F, 1.0F, 0.0F},
            {0.0F, 1.0F, 1.0F},
            {1.0F, 0.0F, 0.0F},
            {1.0F, 0.0F, 1.0F},
            {1.0F, 1.0F, 0.0F},
            {0.5F, 0.0F, 0.0F},
            {0.0F, 0.5F, 0.0F},
            {0.0F, 0.0F, 0.5F},
            {0.5F, 0.5F, 0.5F},
            {0.5F, 0.5F, 0.0F},
            {0.5F, 0.0F, 0.5F},
            {0.0F, 0.5F, 0.5F},
            {0.5F, 0.5F, 1.0F},
            {0.5F, 1.0F, 0.5F},
            {1.0F, 0.5F, 0.5F},
            {0.5F, 1.0F, 1.0F},
            {1.0F, 0.5F, 1.0F},
            {1.0F, 1.0F, 0.5F}
    };

    private int labelIx;
    private int labelOx;
    private int labelCx;
    private int labelConnectionsX;
    private int labelDirectionX;

    private Direction nowDirection = Direction.DOWN;
    private int startIndex = 0;
    private boolean scrolling;
    private float scrollOffs;

    public WireConnectorScreen(WireConnectorMenu abstractContainerMenu, Inventory inventory, Component component) {
        super(abstractContainerMenu, inventory, component);
        imageWidth = 230;
        imageHeight = 219;
    }

    @Override
    protected void init() {
        super.init();

        labelIx = 46 - font.width("I") / 2;
        labelOx = 70 - font.width("O") / 2;
        labelCx = 95 - font.width("C") / 2;
        labelConnectionsX = 166 - font.width(LABEL_CONNECTIONS) / 2;
        labelDirectionX = imageWidth / 2 - font.width(LABEL_DIRECTIONS[0]) / 2;

        Button ok = ClientCompatUtil.buildButton(leftPos + 118, topPos + 184, 40, 20,
                CompatUtil.translated("gui.commoncircuits.wire_connector.ok"), button -> {
                    menu.confirmAndClose();
                    minecraft.setScreen(null);
                });
        Button cancel = ClientCompatUtil.buildButton(leftPos + 174, topPos + 184, 40, 20,
                CompatUtil.translated("gui.commoncircuits.wire_connector.cancel"), button -> minecraft.setScreen(null));
        //#if MC>=11701
        addRenderableWidget(ok);
        addRenderableWidget(cancel);
        //#else
        //$$ addButton(ok);
        //$$ addButton(cancel);
        //#endif
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

        // Scroll bar
        blit(poseStack, leftPos + 201, (int) (topPos + 52 + scrollOffs * 107), 230, isScrollBarActive() ? 52 : 67, 12, 15);

        // Link slots
        WireConnectorMenu.DisplayChannelEntry[] entries = menu.getDisplayChannelEntries().get(nowDirection);
        for (int i = 0; i < entries.length; i++) {
            WireConnectorMenu.DisplayChannelEntry entry = entries[i];
            blit(poseStack, leftPos + 42, topPos + 52 + 20 * i, entry.input ? 230 : 238, 0, 8, 8);
            blit(poseStack, leftPos + 66, topPos + 52 + 20 * i, entry.output ? 230 : 238, 0, 8, 8);
        }
        for (int i = 0; i < entries.length; i++) {
            WireConnectorMenu.DisplayChannelEntry entry = entries[i];

            int connection = entry.connectionIndex;
            if (connection == -1)
                continue;
            int colorIndex = connection % CONNECTION_COLOR_LIST.length;

            //#if MC>=11701
            RenderSystem.setShaderColor(CONNECTION_COLOR_LIST[colorIndex][0], CONNECTION_COLOR_LIST[colorIndex][1], CONNECTION_COLOR_LIST[colorIndex][2], 1.0F);
            //#else
            //$$ RenderSystem.color4f(CONNECTION_COLOR_LIST[colorIndex][0], CONNECTION_COLOR_LIST[colorIndex][1], CONNECTION_COLOR_LIST[colorIndex][2], 1.0F);
            //#endif
            blit(poseStack, leftPos + 91, topPos + 52 + 20 * i, 230, 44, 8, 8);
        }

        // Connection slots
        List<WireConnectorBlockEntity.ConnectEntry> connectEntries = menu.getDataAccess();
        for (int i = Math.max(0, Math.min(startIndex, connectEntries.size() - 1)), j = 0; i < connectEntries.size() && j < 5; i++, j++) {
            int colorIndex = i % CONNECTION_COLOR_LIST.length;
            //#if MC>=11701
            RenderSystem.setShaderColor(CONNECTION_COLOR_LIST[colorIndex][0], CONNECTION_COLOR_LIST[colorIndex][1], CONNECTION_COLOR_LIST[colorIndex][2], 1.0F);
            //#else
            //$$ RenderSystem.color4f(CONNECTION_COLOR_LIST[colorIndex][0], CONNECTION_COLOR_LIST[colorIndex][1], CONNECTION_COLOR_LIST[colorIndex][2], 1.0F);
            //#endif
            blit(poseStack, leftPos + 119, topPos + 52 + 22 * j, 0, 219, 79, 22);
        }
    }

    @Override
    protected void renderLabels(PoseStack poseStack, int i, int j) {
        font.draw(poseStack, title, titleLabelX, titleLabelY, 4210752);
        font.draw(poseStack, LABEL_DIRECTIONS[nowDirection.ordinal()], labelDirectionX, 16, 4210752);
        font.draw(poseStack, LABEL_I, labelIx, 35, 4210752);
        font.draw(poseStack, LABEL_O, labelOx, 35, 4210752);
        font.draw(poseStack, LABEL_C, labelCx, 35, 4210752);
        font.draw(poseStack, LABEL_CONNECTIONS, labelConnectionsX, 35, 4210752);
        for (int index = 0; index < 8; index++)
            font.draw(poseStack, LABEL_NUMBER[index], 22, 52 + 20 * index, 4210752);
        List<WireConnectorBlockEntity.ConnectEntry> connectEntries = menu.getDataAccess();
        for (int index = Math.max(0, Math.min(startIndex, connectEntries.size() - 1)), nowIndex = 0; index < connectEntries.size() && nowIndex < 5; index++, nowIndex++) {
            Component info = CompatUtil.literal(String.format("I: %d, O: %d",
                    connectEntries.get(index).inputs.size(), connectEntries.get(index).outputs.size()));
            font.draw(poseStack, info, 124, 58 + 22 * nowIndex, 4210752);
        }
    }

    @Override
    public boolean mouseClicked(double mx, double my, int i) {
        scrolling = false;
        int parsedMouseX = (int) mx - leftPos;
        int parsedMouseY = (int) my - topPos;
        if (parsedMouseX >= BUTTON_SWITCH_X && parsedMouseX <= BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH
                && parsedMouseY >= BUTTON_SWITCH_Y && parsedMouseY <= BUTTON_SWITCH_Y + BUTTON_SWITCH_HEIGHT) {
            if (nowDirection != Direction.DOWN) {
                nowDirection = Direction.values()[nowDirection.ordinal() - 1];
                labelDirectionX = imageWidth / 2 - font.width(LABEL_DIRECTIONS[nowDirection.ordinal()]) / 2;
                return true;
            }
        } else if (parsedMouseX >= BUTTON_SWITCH_X + BUTTON_SWITCH_WIDTH && parsedMouseX <= BUTTON_SWITCH_X + 2 * BUTTON_SWITCH_WIDTH
                && parsedMouseY >= BUTTON_SWITCH_Y && parsedMouseY <= BUTTON_SWITCH_Y + BUTTON_SWITCH_HEIGHT) {
            if (nowDirection != Direction.EAST) {
                nowDirection = Direction.values()[nowDirection.ordinal() + 1];
                labelDirectionX = imageWidth / 2 - font.width(LABEL_DIRECTIONS[nowDirection.ordinal()]) / 2;
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
        int connectionIndex = -1;
        for (int j = 0; j < 8; j++) {
            if (parsedMouseX >= 91 && parsedMouseX <= 99 && parsedMouseY >= 52 + 20 * j && parsedMouseY <= 60 + 20 * j) {
                connectionIndex = j;
                break;
            }
        }
        if (connectionIndex != -1) {
            if (hasShiftDown())
                return menu.removeConnection(nowDirection, connectionIndex);
            if (hasControlDown())
                return menu.createConnection(nowDirection, connectionIndex);
            return menu.nextConnection(nowDirection, connectionIndex);
        }
        if (parsedMouseX >= 201 && parsedMouseX <= 212 && parsedMouseY >= 52 && parsedMouseY <= 174)
            scrolling = true;
        return super.mouseClicked(mx, my, i);
    }

    public boolean mouseDragged(double d, double e, int i, double f, double g) {
        if (scrolling && isScrollBarActive()) {
            int j = topPos + 52;
            int k = j + 122;
            scrollOffs = ((float) e - (float) j - 7.5F) / ((float) (k - j) - 15.0F);
            scrollOffs = Mth.clamp(scrollOffs, 0.0F, 1.0F);
            startIndex = (int) (scrollOffs * getOffscreenRows() + 0.5);
            return true;
        }
        return super.mouseDragged(d, e, i, f, g);
    }

    public boolean mouseScrolled(double d, double e, double f) {
        if (isScrollBarActive()) {
            int i = getOffscreenRows();
            float g = (float)f / (float)i;
            this.scrollOffs = Mth.clamp(scrollOffs - g, 0.0F, 1.0F);
            this.startIndex = (int)(scrollOffs * i + 0.5);
        }
        return true;
    }

    private boolean isScrollBarActive() {
        return menu.getDataAccess().size() > 5;
    }

    protected int getOffscreenRows() {
        return menu.getDataAccess().size() - 5;
    }
}
