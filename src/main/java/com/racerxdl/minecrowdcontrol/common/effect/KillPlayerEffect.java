package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.entity.player.PlayerEntity;

public class KillPlayerEffect extends BaseEffect
{
    public KillPlayerEffect(String name)
    {
        super(name);
    }

    @Override
    public boolean exec(EffectContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        if (player.getHealth() != 0)
        {
//            player.inventory.mainInventory.forEach(is -> player.dropItem(is, false));
//            player.inventory.offHandInventory.forEach(is -> player.dropItem(is, false));
//            player.inventory.armorInventory.forEach(is -> player.dropItem(is, false));
            player.setHealth(0);
            return true;
        }
        return false;
    }
}
