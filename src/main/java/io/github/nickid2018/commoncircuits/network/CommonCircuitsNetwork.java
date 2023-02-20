package io.github.nickid2018.commoncircuits.network;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.resources.ResourceLocation;

public class CommonCircuitsNetwork {

    public static final ResourceLocation WIRE_CONNECTOR_UPDATE = new ResourceLocation("commoncircuits", "wire_connector_update");

    public static void registerNetworkServer() {
        ServerPlayNetworking.registerGlobalReceiver(WIRE_CONNECTOR_UPDATE, (server, player, handler, buf, responseSender) -> {

        });
    }
}
