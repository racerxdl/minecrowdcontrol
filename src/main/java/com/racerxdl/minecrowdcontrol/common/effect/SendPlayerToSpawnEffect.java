package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SendPlayerToSpawnEffect extends BaseEffect
{
    public SendPlayerToSpawnEffect(String name)
    {
        super(name);
    }

    @Override
    public boolean exec(EffectContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        World world = player.getEntityWorld();
        BlockPos pos = world.getSpawnPoint();

        player.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
        return false;
    }
}
