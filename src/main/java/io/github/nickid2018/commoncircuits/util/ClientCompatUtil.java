package io.github.nickid2018.commoncircuits.util;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class ClientCompatUtil {

    public static Button buildButton(int x, int y, int width, int height, Component component, Button.OnPress onPress) {
        //#if MC>=11904
        return new Button.Builder(component, onPress).bounds(x, y, width, height).build();
        //#else
        //$$ return new Button(x, y, width, height, component, onPress);
        //#endif
    }
}
