package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.FoodStats;

public class FoodEffect extends BaseEffect
{
    public enum FoodMode {ADD, SUBTRACT};

    private FoodMode mode;

    public FoodEffect(String name, FoodMode mode)
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
            case ADD: add(player);
            case SUBTRACT: sub(player);
        }

        return false;
    }

    private boolean sub(PlayerEntity player)
    {
        FoodStats fs = player.getFoodStats();
        float hunger = fs.getFoodLevel();
        if (hunger != 0)
        {
            player.setHealth(hunger - 2);
            return true;
        }
        return false;
    }

    private boolean add(PlayerEntity player)
    {
        FoodStats fs = player.getFoodStats();
        float hunger = fs.getFoodLevel();
        if (hunger != 20)
        {
            player.setHealth(hunger + 2);
            return true;
        }
        return false;
    }

}
