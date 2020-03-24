package com.racerxdl.minecrowdcontrol;

import com.racerxdl.minecrowdcontrol.CrowdControl.RequestType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface MinecraftCommand {
    CommandResult Run(PlayerStates states, PlayerEntity player, Minecraft client, MinecraftServer server, String viewer, RequestType type);
}
