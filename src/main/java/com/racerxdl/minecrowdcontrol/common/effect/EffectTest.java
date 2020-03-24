package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.text.StringTextComponent;


public class EffectTest extends BaseEffect
{
    public EffectTest()
    {
        super("testeff");
    }


    @Override
    public boolean exec(EffectContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        player.sendMessage(new StringTextComponent("Bees!"));

        return true;
    }
}
