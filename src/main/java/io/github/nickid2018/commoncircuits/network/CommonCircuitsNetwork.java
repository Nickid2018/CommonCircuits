package io.github.nickid2018.commoncircuits.network;

import io.github.nickid2018.commoncircuits.block.entity.WireConnectorBlockEntity;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class CommonCircuitsNetwork {

    public static final ResourceLocation WIRE_CONNECTOR_UPDATE = new ResourceLocation("commoncircuits", "wire_connector_update");

    public static void registerNetworkServer() {
        ServerPlayNetworking.registerGlobalReceiver(WIRE_CONNECTOR_UPDATE,
                (server, player, handler, buf, responseSender) -> WireConnectorBlockEntity.update(server, player.level, buf));
    }
}
