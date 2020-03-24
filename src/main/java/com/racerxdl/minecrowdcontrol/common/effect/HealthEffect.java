package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.entity.player.PlayerEntity;

public class HealthEffect extends BaseEffect
{

    public HealthMode mode;

    public HealthEffect(String name, HealthMode mode)
    {
        super(name);
        this.mode = mode;
    }

    @Override
    public boolean exec(EffectContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();

        switch (mode)
        {
            case SUBTRACT: sub(player);
            case ADD: add(player);
            case HALF: setHalf(player);
            case FILL: setFull(player);
        }

        return false;
    }

    private boolean sub(PlayerEntity player)
    {
        float health = player.getHealth();
        if (health != 0)
        {
            player.setHealth(health - 2);
            return true;
        }
        return false;
    }

    private boolean add(PlayerEntity player)
    {

        float health = player.getHealth();
        if (health != 20)
        {
            player.setHealth(health + 2);
            return true;
        }
        return false;
    }

    private boolean setHalf(PlayerEntity player)
    {
        player.setHealth(10);
        return true;
    }

    private boolean setFull(PlayerEntity player)
    {
        player.setHealth(20);
        return true;
    }

    public enum HealthMode
    {
        ADD, SUBTRACT, HALF, FILL
    }
}
