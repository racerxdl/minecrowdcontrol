package com.racerxdl.minecrowdcontrol.common.effect;

import com.racerxdl.minecrowdcontrol.common.effect.api.EffectContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

public class SpawnEntityEffect extends BaseEffect
{

    private SummonableEntities summon;

    public SpawnEntityEffect(String name, SummonableEntities entities)
    {
        super(name);
        this.summon = entities;
    }

    @Override
    public boolean exec(EffectContext ctx)
    {
        PlayerEntity player = ctx.getPlayer();
        switch (summon)
        {
            case BEE: summonEntity(player, summon.getId());
            case COW: summonEntity(player, summon.getId());
            case PIG: summonEntity(player, summon.getId());
            case VEX: summonEntity(player, summon.getId());
            case BLAZE: summonEntity(player, summon.getId());
            case HORSE: summonEntity(player, summon.getId());
            case SHEEP: summonEntity(player, summon.getId());
            case SLIME: summonEntity(player, summon.getId());
            case WITCH: summonEntity(player, summon.getId());
            case SPIDER: summonEntity(player, summon.getId());
            case WITHER: summonEntity(player, summon.getId());
            case ZOMBIE: summonEntity(player, summon.getId());
            case CHICKEN: summonEntity(player, summon.getId());
            case CREEPER: summonEntity(player, summon.getId());
            case PHANTOM: summonEntity(player, summon.getId());
            case RAVAGER: summonEntity(player, summon.getId());
            case ENDERMAN: summonEntity(player, summon.getId());
            case VILLAGER: summonEntity(player, summon.getId());
            case SILVERFISH: summonEntity(player, summon.getId());
            case CAVE_SPIDER: summonEntity(player, summon.getId());
            case ENDER_DRAGON: summonEntity(player, summon.getId());
            case ZOMBIE_HORSE: summonEntity(player, summon.getId());
            case TROPICAL_FISH: summonEntity(player, summon.getId());
            case SKELETON_HORSE: summonEntity(player, summon.getId());
        }
        return false;
    }

    private boolean summonEntity(PlayerEntity player, ResourceLocation location)
    {
        World world = player.getEntityWorld();
        BlockPos pos = player.getPosition();
        IForgeRegistry<EntityType<?>> entityRegistry = ForgeRegistries.ENTITIES;

        if (!world.isRemote)
        {
            EntityType<?> entityType = entityRegistry.getValue(location);
            Entity entity = entityType.create(world);
            if (entity != null)
            {
                entity.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
                world.addEntity(entity);
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    public enum SummonableEntities
    {
        CREEPER("creeper"),
        ENDERMAN("enderman"),
        ENDER_DRAGON("ender_dragon"),
        BLAZE("blaze"),
        CAVE_SPIDER("cave_spider"),
        SPIDER("spider"),
        WITCH("witch"),
        BEE("bee"),
        HORSE("horse"),
        SKELETON_HORSE("skeleton_horse"),
        ZOMBIE_HORSE("zombie_horse"),
        ZOMBIE("zombie"),
        COW("cow"),
        CHICKEN("chicken"),
        PIG("pig"),
        SHEEP("sheep"),
        VILLAGER("villager"),
        WITHER("wither"),
        SLIME("slime"),
        SILVERFISH("silverfish"),
        RAVAGER("ravager"),
        PHANTOM("phantom"),
        VEX("vex"),
        TROPICAL_FISH("tropical_fish");

        private String id;

        SummonableEntities(String id)
        {
            this.id = id;
        }

        public String getName()
        {
            return this.id;
        }

        public ResourceLocation getId()
        {
            return new ResourceLocation("minecraft:" + id);
        }
    }
}
