package com.racerxdl.minecrowdcontrol;

import com.racerxdl.minecrowdcontrol.CrowdControl.EffectResult;
import com.racerxdl.minecrowdcontrol.CrowdControl.RequestType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.AbstractOption;
import net.minecraft.entity.AreaEffectCloudEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CCreativeInventoryActionPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.*;

public class Commands {
    private static final Logger Log = LogManager.getLogger();
    private static boolean enablePlayerMessages = false;

    public static final Map<String, MinecraftCommand> CommandList = new HashMap<String, MinecraftCommand>() {{
        put("KILL", Commands::KillPlayers);
        put("TAKE_HEART", Commands::TakeHeart);
        put("GIVE_HEART", Commands::GiveHeart);
        put("SET_FIRE", Commands::SetFire);
        put("SET_TIME_NIGHT", Commands::SetTimeNight);
        put("SET_TIME_DAY", Commands::SetTimeDay);
        put("TAKE_FOOD", Commands::TakeFood);
        put("GIVE_FOOD", Commands::GiveFood);
        put("SEND_PLAYER_TO_SPAWN_POINT", Commands::SendPlayerToSpawnPoint);
        put("TAKE_ALL_HEARTS_BUT_HALF", Commands::TakeAllHeartsButHalf);
        put("FILL_HEARTS", Commands::FillHearts);
        put("INVERT_MOUSE", Commands::SetInvertMouse);
        put("DISABLE_JUMP", Commands::SetJumpDisabled);
        put("TAKE_ALL_FOOD", Commands::TakeAllFood);
        put("FILL_FOOD", Commands::FillFood);
        put("MAKE_IT_RAIN", Commands::SetRaining);
        put("GOTTA_GO_FAST", Commands::GottaGoFast);
        put("DRUNK_MODE", Commands::DrunkMode);
        put("DESTROY_SELECTED_ITEM", Commands::DestroySelectedItem);
        put("DROP_SELECTED_ITEM", Commands::DropSelectedItem);
        put("REPAIR_SELECTED_ITEM", Commands::RepairSelectedItem);
        put("EXPLODE_PLAYER", Commands::ExplodePlayer);
    }};


    private static final List<EntityType> spawnEntities = new ArrayList<>(Arrays.asList(
            EntityType.CREEPER,
            EntityType.ENDERMAN,
            EntityType.ENDER_DRAGON,
            EntityType.BLAZE,
            EntityType.CAVE_SPIDER,
            EntityType.SPIDER,
            EntityType.WITCH,
            EntityType.BEE,
            EntityType.HORSE,
            EntityType.SKELETON_HORSE,
            EntityType.ZOMBIE_HORSE,
            EntityType.ZOMBIE,
            EntityType.COW,
            EntityType.CHICKEN,
            EntityType.PIG
    ));

    static {
        spawnEntities.forEach((et) -> {
            String entityName = et.getName().getString().toUpperCase().replace(" ", "");
            Log.info("Adding command SPAWN_{}", entityName);
            CommandList.put("SPAWN_" + entityName, (states, u, u1, server, viewer, type) -> SpawnEntity(states, server, viewer, type, et));
        });
    }

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
            SendPlayerSystemMessage(player, msg, params);
        }
    }

    public static void SendPlayerSystemMessage(PlayerEntity player, String msg, Object... params) {
        player.sendStatusMessage(new StringTextComponent(MessageFormat.format(msg, params)), false);
    }

    public static void SendSystemMessage(MinecraftServer server, String msg, Object... params) {
        RunOnPlayers(server, (player) -> {
            SendPlayerSystemMessage(player, msg, params);
            return true;
        });
    }

    public static CommandResult ExplodePlayer(PlayerStates states, PlayerEntity p, Minecraft client, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            float health = player.getHealth();
            if (health != 0) {
                Log.info(Messages.ServerKill, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientKill, viewer);

                player.world.createExplosion(player, player.getPosX(), player.getPosY(), player.getPosZ(), 8, Explosion.Mode.DESTROY);

                player.world.makeFireworks(player.getPosX(), player.getPosY(), player.getPosZ(), 0, 0, 0, null);
                player.setHealth(0);
                return true;
            }

            return false;
        }));

        if (result) {
            client.player.playSound(SoundEvents.ENTITY_ENDERMAN_SCREAM, 6, 0.25f);
            Log.debug("WOLOLO");
            p.world.makeFireworks(p.getPosX(), p.getPosY(), p.getPosZ() + 20, 0, 0, 0, null);
            p.setMotion(2, 2, 2);
        }

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Unavailable);
    }

    public static CommandResult RepairSelectedItem(PlayerStates states, PlayerEntity player, Minecraft client, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        int itemIndex = player.inventory.currentItem;
        ItemStack is = player.inventory.mainInventory.get(itemIndex);

        if (is.isEmpty() || !is.isRepairable() || is.getDamage() == 0 || player.getHealth() == 0) {
            return res.SetEffectResult(EffectResult.Retry);
        }

        RunOnPlayers(server, (p) -> {
            p.inventory.mainInventory.get(itemIndex).setDamage(0);
            return true;
        });

        player.inventory.mainInventory.get(itemIndex).setDamage(0);

        client.player.playSound(SoundEvents.BLOCK_ANVIL_HIT, 1, 1);

        Log.info(Messages.ServerRepairItem, viewer, player.getName().getString(), is.getItem().getName().getString());
        SendPlayerMessage(player, Messages.ClientRepairItem, viewer, is.getItem().getName().getString());

        return res.SetEffectResult(EffectResult.Success);
    }

    public static CommandResult DropSelectedItem(PlayerStates states, PlayerEntity player, Minecraft client, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        int itemIndex = player.inventory.currentItem;
        ItemStack is = player.inventory.mainInventory.get(itemIndex);

        if (is.isEmpty() || player.getHealth() == 0) {
            return res.SetEffectResult(EffectResult.Retry);
        }

        RunOnPlayers(server, (p) -> {
            p.dropItem(is, false);
            p.inventory.mainInventory.set(itemIndex, ItemStack.EMPTY);
            return true;
        });

        player.inventory.mainInventory.set(itemIndex, ItemStack.EMPTY);

        client.player.playSound(SoundEvents.ENTITY_COW_DEATH, 1, 8);

        Log.info(Messages.ServerDropItem, viewer, player.getName().getString(), is.getItem().getName().getString());
        SendPlayerMessage(player, Messages.ClientDropItem, viewer, is.getItem().getName().getString());

        return res.SetEffectResult(EffectResult.Success);
    }

    public static CommandResult DestroySelectedItem(PlayerStates states, PlayerEntity player, Minecraft client, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        int itemIndex = player.inventory.currentItem;
        ItemStack is = player.inventory.mainInventory.get(itemIndex);
        if (is.isEmpty() || player.getHealth() == 0) {
            return res.SetEffectResult(EffectResult.Retry);
        }

        if (is.getCount() > 1) {
            int newCount = is.getCount() - 1;
            is.setCount(newCount);
            RunOnPlayers(server, (p) -> {
                p.inventory.mainInventory.get(itemIndex).setCount(newCount);
                return true;
            });
        } else {
            player.inventory.mainInventory.set(itemIndex, ItemStack.EMPTY);
            RunOnPlayers(server, (p) -> {
                p.inventory.mainInventory.set(itemIndex, ItemStack.EMPTY);
                return true;
            });
        }

        client.player.playSound(SoundEvents.ENTITY_COW_HURT, 1, 8);

        Log.info(Messages.ServerDestroyItem, viewer, player.getName().getString(), is.getItem().getName().getString());
        SendPlayerMessage(player, Messages.ClientDestroyItem, viewer, is.getItem().getName().getString());

        return res.SetEffectResult(EffectResult.Success);
    }

    public static CommandResult DrunkMode(PlayerStates states, PlayerEntity player, Minecraft unused, MinecraftServer unused2, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Start) {
            if (states.getDrunkMode()) {
                return res.SetEffectResult(EffectResult.Retry);
            }

            Log.info(Messages.ServerDrunkModeStarted, viewer);
            SendPlayerMessage(player, Messages.ClientDrunkModeStarted, viewer);

            return res
                    .SetNewStates(states.setDrunkMode(true))
                    .SetEffectResult(EffectResult.Success);
        } else if (type == RequestType.Stop) {
            if (!states.getDrunkMode()) {
                return res.SetEffectResult(EffectResult.Retry);
            }

            Log.info(Messages.ServerDrunkModeRestored);
            SendPlayerMessage(player, Messages.ClientDrunkModeRestored);

            return res
                    .SetNewStates(states.setDrunkMode(false))
                    .SetEffectResult(EffectResult.Success);
        }

        return res.SetEffectResult(EffectResult.Success);
    }

    public static CommandResult GottaGoFast(PlayerStates states, PlayerEntity unused, Minecraft client, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        boolean result = RunOnPlayers(server, (player) -> {
            World w = player.getEntityWorld();
            if (type == RequestType.Start) {
                if (states.getGottaGoFast()) {
                    return false;
                }
                Log.info(Messages.ServerGottaGoFast, viewer);
                SendPlayerMessage(player, Messages.ClientGottaGoFast, viewer);

                player.abilities.setWalkSpeed(1F);
                player.abilities.setFlySpeed(0.5F);

                return true;
            } else if (type == RequestType.Stop) {
                if (!states.getGottaGoFast()) {
                    return false;
                }

                Log.info(Messages.ServerGottaGoFastRestored);
                SendPlayerMessage(player, Messages.ClientGottaGoFastRestored);

                player.abilities.setWalkSpeed(0.1F);
                player.abilities.setFlySpeed(0.05F);

                return true;
            }

            return true;
        });

        if (result) {
            res = res.SetNewStates(
                    res.GetPlayerStates()
                            .setGottaGoFast(type == RequestType.Start)
                            .setGottaGoFastViewer(viewer)
            );
        }

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult SetRaining(PlayerStates states, PlayerEntity unused, Minecraft client, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        boolean result = RunOnPlayers(server, (player) -> {
            World w = player.getEntityWorld();
            if (type == RequestType.Start) {
                if (w.getWorldInfo().isRaining()) {
                    return false;
                }

                Biome b = w.getBiome(player.getPosition());

                if (b.getPrecipitation() == Biome.RainType.NONE) {
                    return false;
                }

                Log.info(Messages.ServerMakeItRain, viewer);
                SendPlayerMessage(player, Messages.ClientMakeItRain, viewer);

                w.getWorldInfo().setRaining(true);

                return true;
            } else if (type == RequestType.Stop) {
                if (!w.getWorldInfo().isRaining()) {
                    return false;
                }

                Log.info(Messages.ServerRainRestored);
                SendPlayerMessage(player, Messages.ClientRainRestored);

                w.getWorldInfo().setRaining(false);

                return true;
            }

            return true;
        });

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult SetInvertMouse(PlayerStates states, PlayerEntity player, Minecraft client, MinecraftServer unused2, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Start) {
            if (client.gameSettings.invertMouse) {
                return res.SetEffectResult(EffectResult.Retry);
            }

            Log.info(Messages.ServerInvertMouse, viewer, player.getName().getString());
            SendPlayerMessage(player, Messages.ClientInvertMouse, viewer);
            AbstractOption.INVERT_MOUSE.set(client.gameSettings, "true");
            return res.SetEffectResult(EffectResult.Success);
        } else if (type == RequestType.Stop) {
            if (!client.gameSettings.invertMouse) {
                return res.SetEffectResult(EffectResult.Retry);
            }

            Log.info(Messages.ServerRestoreInvertMouse, player.getName().getString());
            SendPlayerMessage(player, Messages.ClientRestoreInvertMouse);
            AbstractOption.INVERT_MOUSE.set(client.gameSettings, "false");
            return res.SetEffectResult(EffectResult.Success);
        }

        return res.SetEffectResult(EffectResult.Success);
    }

    public static CommandResult SetJumpDisabled(PlayerStates states, PlayerEntity player, Minecraft client, MinecraftServer unused2, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Start) {
            if (res.GetPlayerStates().getJumpDisabled()) {
                return res.SetEffectResult(EffectResult.Retry);
            }

            Log.info(Messages.ServerJumpDisabled, viewer, player.getName().getString());
            SendPlayerMessage(player, Messages.ClientJumpDisabled, viewer);
            return res
                    .SetEffectResult(EffectResult.Success)
                    .SetNewStates(res.GetPlayerStates().setJumpDisabled(true));
        } else if (type == RequestType.Stop) {
            if (!res.GetPlayerStates().getJumpDisabled()) {
                return res.SetEffectResult(EffectResult.Retry);
            }

            Log.info(Messages.ServerJumpRestored, player.getName().getString());
            SendPlayerMessage(player, Messages.ClientJumpRestored);
            return res
                    .SetEffectResult(EffectResult.Success)
                    .SetNewStates(res.GetPlayerStates().setJumpDisabled(false));
        }

        return res.SetEffectResult(EffectResult.Success);
    }

    public static CommandResult SetTimeNight(PlayerStates states, PlayerEntity player, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        World world = player.getEntityWorld();

        if (world.getDayTime() < 13000 || world.getDayTime() > 23000) {
            Log.info(Messages.ServerSetTimeNight, viewer);
            // Send message to all players
            RunOnPlayers(server, (p) -> {
                SendPlayerMessage(p, Messages.ClientSetTimeNight, viewer);
                p.getEntityWorld().setDayTime(Tools.NIGHT);
                return true;
            });
            return res.SetEffectResult(EffectResult.Success);
        }

        return res.SetEffectResult(EffectResult.Unavailable);
    }

    public static CommandResult SendPlayerToSpawnPoint(PlayerStates states, PlayerEntity entity, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player) -> {
            if (player.getHealth() == 0) {
                return false;
            }
            BlockPos spawnPoint = player.getEntityWorld().getSpawnPoint();

            player.setPositionAndUpdate(spawnPoint.getX(), spawnPoint.getY(), spawnPoint.getZ());

            Log.info(Messages.ServerSendPlayerToSpawnPoint, viewer, player.getName().getString());
            SendPlayerMessage(player, Messages.ClientSendPlayerToSpawnPoint, viewer);

            return true;
        });

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult SetTimeDay(PlayerStates states, PlayerEntity player, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        World world = player.getEntityWorld();
        if (world.getDayTime() > 6000) {
            Log.info(Messages.ServerSetTimeDay, viewer);
            RunOnPlayers(server, (p) -> {
                SendPlayerMessage(p, Messages.ClientSetTimeDay, viewer);
                p.getEntityWorld().setDayTime(Tools.DAY);
                return true;
            });
            world.setDayTime(Tools.DAY);
            return res.SetEffectResult(EffectResult.Success);
        }

        return res.SetEffectResult(EffectResult.Unavailable);
    }

    public static CommandResult SpawnEntity(PlayerStates states, MinecraftServer server, String viewer, RequestType type, EntityType entityType) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player) -> {
            BlockPos pos = player.getPosition();

            Log.info(Messages.ServerSpawn, viewer, entityType.getName().getString());
            SendPlayerMessage(player, Messages.ClientSpawn, viewer, entityType.getName().getString());

            Entity e = entityType.create(player.getEntityWorld());
            e.setPositionAndRotation(pos.getX() + 2, pos.getY() + 2, pos.getZ(), 0, 0);

            player.getEntityWorld().addEntity(e);

            return true;
        });

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult TakeFood(PlayerStates states, PlayerEntity player, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (p -> {
            FoodStats fs = p.getFoodStats();
            if (fs.getFoodLevel() > 0 && player.getHealth() != 0) {
                Log.info(Messages.ServerTakeFood, viewer, p.getName().getString());
                SendPlayerMessage(p, Messages.ClientTakeFood, viewer);
                fs.setFoodLevel(fs.getFoodLevel() - 2);
                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult GiveFood(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            FoodStats fs = player.getFoodStats();
            if (fs.getFoodLevel() < Tools.MAX_FOOD && player.getHealth() != 0) {
                Log.info(Messages.ServerGiveFood, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientGiveFood, viewer);
                fs.setFoodLevel(fs.getFoodLevel() + 2);

                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult TakeAllHeartsButHalf(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            if (player.getHealth() > 1) {
                Log.info(Messages.ServerTakeAllHeartsButHalf, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientTakeAllHeartsButHalf, viewer);
                player.setHealth(1);

                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult TakeAllFood(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            FoodStats fs = player.getFoodStats();
            if (fs.getFoodLevel() > 0 && player.getHealth() != 0) {
                Log.info(Messages.ServerTakeAllFood, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientTakeAllFood, viewer);
                player.getFoodStats().setFoodLevel(0);

                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult FillFood(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            FoodStats fs = player.getFoodStats();
            if (fs.getFoodLevel() < Tools.MAX_FOOD && player.getHealth() != 0) {
                Log.info(Messages.ServerFillFood, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientFillFood, viewer);
                player.getFoodStats().setFoodLevel(Tools.MAX_FOOD);

                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult FillHearts(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            if (player.getHealth() != Tools.MAX_HEALTH && player.getHealth() != 0) {
                Log.info(Messages.ServerFillAllHearts, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientFillAllHearts, viewer);
                player.setHealth(Tools.MAX_HEALTH);

                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult TakeHeart(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            if (player.getHealth() > 2) {
                Log.info(Messages.ServerTakeHeart, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientTakeHeart, viewer);
                player.setHealth(player.getHealth() - 2);

                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult GiveHeart(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            if (player.getHealth() < Tools.MAX_HEALTH && player.getHealth() != 0) {
                Log.info(Messages.ServerGiveHeart, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientGiveHeart, viewer);
                player.setHealth(player.getHealth() + 2);
                return true;
            }

            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult SetFire(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

        boolean result = RunOnPlayers(server, (player -> {
            if (player.getFireTimer() == -20) {
                Log.info(Messages.ServerSetFire, viewer, player.getName().getString());
                SendPlayerMessage(player, Messages.ClientSetFire, viewer);

                player.setFire(5);
                return true;
            }
            return false;
        }));

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Retry);
    }

    public static CommandResult KillPlayers(PlayerStates states, PlayerEntity unused, Minecraft unused2, MinecraftServer server, String viewer, RequestType type) {
        CommandResult res = new CommandResult(states);

        if (type == RequestType.Test) {
            return res.SetEffectResult(EffectResult.Success);
        }

        if (type == RequestType.Stop) {
            return res.SetEffectResult(EffectResult.Unavailable);
        }

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

        return result ? res.SetEffectResult(EffectResult.Success) : res.SetEffectResult(EffectResult.Unavailable);
    }
}
