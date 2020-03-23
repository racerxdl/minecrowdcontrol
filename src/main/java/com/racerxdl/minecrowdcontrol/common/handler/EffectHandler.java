package com.racerxdl.minecrowdcontrol.common.handler;

import com.racerxdl.minecrowdcontrol.common.effect.*;
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
        registerEffect(new FirePlayerEffect("fireplayer"));
        registerEffect(new TimeEffect("setday", TimeEffect.DayTimes.DAY));
        registerEffect(new TimeEffect("setnoon", TimeEffect.DayTimes.NOON));
        registerEffect(new TimeEffect("setnight", TimeEffect.DayTimes.NIGHT));
        registerEffect(new FoodEffect("addfood", FoodEffect.FoodMode.ADD));
        registerEffect(new FoodEffect("subtractfood", FoodEffect.FoodMode.ADD));
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
