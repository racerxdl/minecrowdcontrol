package com.racerxdl.minecrowdcontrol;

import com.racerxdl.minecrowdcontrol.CrowdControl.EffectResult;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Commands {
    private static final Logger Log = LogManager.getLogger();
    private static boolean enablePlayerMessages = false;

    public static final Map<String, MinecraftCommand> CommandList = new HashMap<String, MinecraftCommand>() {{
        put("KILL", Commands::KillPlayers);
        put("TAKE_HEART", Commands::TakeHeart);
        put("GIVE_HEART", Commands::GiveHeart);
        put("SET_FIRE", Commands::SetFire);
        put("SPAWN_CREEPER", Commands::SpawnCreeper);
        put("SET_TIME_NIGHT", Commands::SetTimeNight);
        put("SET_TIME_DAY", Commands::SetTimeDay);
        put("TAKE_FOOD", Commands::TakeFood);
        put("GIVE_FOOD", Commands::GiveFood);
    }};

    public static void SetEnablePlayerMessages(boolean status) {
        enablePlayerMessages = status;
    }

    // Run command on all players and returns if any of them returned true
    public static boolean RunOnPlayers(MinecraftServer server, PlayerRunnable runnable) {
        boolean result = false;
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (PlayerEntity player : players) {
            result |= runnable.run(player);
        }
        return result;
    }

    public static void SendPlayerMessage(PlayerEntity player, String msg, Object... params) {
        if (enablePlayerMessages) {
            player.sendStatusMessage(new StringTextComponent(MessageFormat.format(msg, params)), false);
        }
    }

    public static EffectResult SetTimeNight(PlayerEntity player, MinecraftServer server, String viewer) {
        World world = player.getEntityWorld();
        if (world.getDayTime() < 13000 || world.getDayTime() > 23000) {
            Log.info(Messages.ServerSetTimeNight, viewer);
            // Send message to all players
            RunOnPlayers(server, (p) -> {
                SendPlayerMessage(p, Messages.ClientSetTimeNight, viewer);
                p.getEntityWorld().setDayTime(Tools.NIGHT);
                return true;
            });
            return EffectResult.Success;
        }

        return EffectResult.Unavailable;
    }

    public static EffectResult SetTimeDay(PlayerEntity player, MinecraftServer server, String viewer) {
        World world = player.getEntityWorld();
        if (world.getDayTime() > 6000) {
            Log.info(Messages.ServerSetTimeDay, viewer);
            RunOnPlayers(server, (p) -> {
                SendPlayerMessage(p, Messages.ClientSetTimeDay, viewer);
                p.getEntityWorld().setDayTime(Tools.DAY);
                return true;
            });
            world.setDayTime(Tools.DAY);
            return EffectResult.Success;
        }

        return EffectResult.Unavailable;
    }

    public static EffectResult SpawnCreeper(PlayerEntity unused, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (player) -> {
            BlockPos pos = player.getPosition();

            Log.info(Messages.ServerSpawnCreeper, viewer);
            SendPlayerMessage(player, Messages.ClientSpawnCreeper, viewer);

            Entity e = EntityType.CREEPER.create(player.getEntityWorld());
            e.setPositionAndRotation(pos.getX() + 2, pos.getY() + 2, pos.getZ(), 0, 0);

            player.getEntityWorld().addEntity(e);

            return true;
        });

        return result ? EffectResult.Success : EffectResult.Retry;
    }

    public static EffectResult TakeFood(PlayerEntity player, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (p -> {
            FoodStats fs = p.getFoodStats();
            if (fs.getFoodLevel() > 0) {
                Log.info(Messages.ServerTakeFood, viewer, p.getName().getString());
                SendPlayerMessage(p, Messages.ClientTakeFood, viewer);
                fs.setFoodLevel(fs.getFoodLevel() - 2);
                return true;
            }

            return false;
        }));

        return result ? EffectResult.Success : EffectResult.Retry;
    }

    public static EffectResult GiveFood(PlayerEntity unused, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (player -> {
            FoodStats fs = player.getFoodStats();
            if (fs.getFoodLevel() < Tools.MAX_FOOD) {
                Log.info(Messages.ServerGiveFood, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientGiveFood, viewer);
                fs.setFoodLevel(fs.getFoodLevel() + 2);

                return true;
            }

            return false;
        }));

        return result ? EffectResult.Success : EffectResult.Retry;
    }


    public static EffectResult TakeHeart(PlayerEntity unused, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (player -> {
            if (player.getHealth() > 2) {
                Log.info(Messages.ServerTakeHeart, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientTakeHeart, viewer);
                player.setHealth(player.getHealth() - 2);

                return true;
            }

            return false;
        }));

        return result ? EffectResult.Success : EffectResult.Retry;
    }

    public static EffectResult GiveHeart(PlayerEntity unused, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (player -> {
            if (player.getHealth() < Tools.MAX_HEALTH) {
                Log.info(Messages.ServerGiveHeart, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientGiveHeart, viewer);
                player.setHealth(player.getHealth() + 2);
                return true;
            }

            return false;
        }));

        return result ? EffectResult.Success : EffectResult.Retry;
    }

    public static EffectResult SetFire(PlayerEntity unused, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (player -> {
            if (player.getFireTimer() == -20) {
                Log.info(Messages.ServerSetFire, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientSetFire, viewer);

                player.setFire(5);
                return true;
            }
            return false;
        }));

        return result ? EffectResult.Success : EffectResult.Retry;
    }

    public static EffectResult KillPlayers(PlayerEntity unused, MinecraftServer server, String viewer) {
        boolean result = RunOnPlayers(server, (player -> {
            float health = player.getHealth();
            if (health != 0) {
                Log.info(Messages.ServerKill, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientKill, viewer);
                player.setHealth(0);
                return true;
            }

            return false;
        }));

        return result ? EffectResult.Success : EffectResult.Unavailable;
    }
}
