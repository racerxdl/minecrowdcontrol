package com.racerxdl.minecrowdcontrol;

import com.racerxdl.minecrowdcontrol.CrowdControl.EffectResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;

@FunctionalInterface
public interface MinecraftCommand {
    EffectResult Run(PlayerEntity player, MinecraftServer server, String viewer);
}
