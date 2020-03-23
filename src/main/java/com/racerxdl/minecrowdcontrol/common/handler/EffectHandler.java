package com.racerxdl.minecrowdcontrol.common.handler;

import com.racerxdl.minecrowdcontrol.common.effect.BaseEffect;
import com.racerxdl.minecrowdcontrol.common.effect.EffectTest;
import com.racerxdl.minecrowdcontrol.common.effect.HealthEffect;
import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import com.racerxdl.minecrowdcontrol.common.effect.api.EffectStatus;
import com.racerxdl.minecrowdcontrol.common.effect.api.IEffect;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;

public class EffectHandler
{
    private HashMap<String, IEffect> effectRegistry = new HashMap<>();

    public EffectHandler()
    {
        registerEffect(new EffectTest());
        registerEffect(new HealthEffect("addhealth", HealthEffect.HealthMode.ADD));
        registerEffect(new HealthEffect("subtracthealth", HealthEffect.HealthMode.SUBTRACT));
    }

    private void registerEffect(BaseEffect effect)
    {
       this.effectRegistry.put(effect.getName(), effect);
    }

    public boolean executeEffect(String effect, PlayerEntity player, String viewer, EffectStatus status)
    {
        return this.effectRegistry.get(effect).exec(new EffectContext(player, viewer, status));
    }

    public HashMap<String, IEffect> getEffectRegistry()
    {
        return this.effectRegistry;
    }

}
