package com.racerxdl.minecrowdcontrol;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;

@FunctionalInterface
public interface PlayerRunnable {
    boolean run(PlayerEntity player);
}