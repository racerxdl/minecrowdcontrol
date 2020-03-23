package com.racerxdl.minecrowdcontrol.common.effect.api;

import net.minecraft.entity.player.PlayerEntity;

public class EffectContext
{
    private PlayerEntity player;
    private String       viewer;
    private EffectStatus status;

    public EffectContext(PlayerEntity player, String viewer, EffectStatus status)
    {
        this.player = player;
        this.viewer = viewer;
        this.status = status;
    }

    public PlayerEntity getPlayer()
    {
        return player;
    }

    public String getViewer()
    {
        return viewer;
    }

    public EffectStatus getStatus()
    {
        return status;
    }
}
