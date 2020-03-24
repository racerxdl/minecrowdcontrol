package com.racerxdl.minecrowdcontrol.common.handler;

import com.racerxdl.minecrowdcontrol.common.effect.*;
import com.racerxdl.minecrowdcontrol.common.effect.SpawnEntityEffect.SummonableEntities;
import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import com.racerxdl.minecrowdcontrol.common.effect.api.EffectStatus;
import com.racerxdl.minecrowdcontrol.common.effect.api.IEffect;
import net.minecraft.entity.player.PlayerEntity;

import java.util.HashMap;
import java.util.stream.Stream;

public class EffectHandler
{
    private HashMap<String, IEffect> effectRegistry = new HashMap<>();

    public EffectHandler()
    {
        registerEffect(new EffectTest());
        registerEffect(new HealthEffect("give_heart", HealthEffect.HealthMode.ADD));
        registerEffect(new HealthEffect("take_heart", HealthEffect.HealthMode.SUBTRACT));
        registerEffect(new FirePlayerEffect("set_fire"));
        registerEffect(new TimeEffect("set_time_day", TimeEffect.DayTimes.DAY));
        registerEffect(new TimeEffect("set_time_noon", TimeEffect.DayTimes.NOON));
        registerEffect(new TimeEffect("set_time_night", TimeEffect.DayTimes.NIGHT));
        registerEffect(new FoodEffect("give_food", FoodEffect.FoodMode.ADD));
        registerEffect(new FoodEffect("remove_food", FoodEffect.FoodMode.ADD));
        registerEffect(new KillPlayerEffect("kill"));
        Stream.of(SummonableEntities.values()).forEach(i->registerEffect(new SpawnEntityEffect("spawn_" + i.getName(), i)));
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
