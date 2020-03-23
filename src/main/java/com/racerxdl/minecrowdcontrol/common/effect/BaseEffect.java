package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.IEffect;

public abstract class BaseEffect implements IEffect
{
    private String name;

    public BaseEffect(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
}
