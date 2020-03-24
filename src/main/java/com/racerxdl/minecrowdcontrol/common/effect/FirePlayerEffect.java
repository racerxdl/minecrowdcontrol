package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;

public class FirePlayerEffect extends BaseEffect
{
    public FirePlayerEffect(String name)
    {
        super(name);
    }

    @Override
    public boolean exec(EffectContext ctx)
    {
        ctx.getPlayer().setFire(10);
        return true;
    }
}
