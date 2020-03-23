package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.world.World;

public class TimeEffect extends BaseEffect
{

    public enum DayTimes
    {
        DAY(1000),
        NIGHT(13000),
        NOON(6000);

        int time;
        DayTimes(int time)
        {
            this.time = time;
        }

        public int getTime()
        {
            return time;
        }
    }

    private DayTimes times;

    public TimeEffect(String name, DayTimes times)
    {
        super(name);
        this.times = times;
    }

    @Override
    public boolean exec(EffectContext ctx)
    {
        World world = ctx.getPlayer().getEntityWorld();
        switch (times) {
            case DAY: world.setDayTime(DayTimes.DAY.getTime());
            case NOON: world.setDayTime(DayTimes.NOON.getTime());
            case NIGHT: world.setDayTime(DayTimes.NIGHT.getTime());
        }
        return true;
    }
}
