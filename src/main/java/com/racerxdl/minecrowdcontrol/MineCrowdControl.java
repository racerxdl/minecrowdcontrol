package com.racerxdl.minecrowdcontrol;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.PlayerList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jline.utils.Log;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("minecrowdcontrol")
public class MineCrowdControl {
    // Directly reference a log4j logger.
    private static final Logger LOGGER = LogManager.getLogger();

    private ControlServer cs;

    public MineCrowdControl() {
        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // some preinit code
        LOGGER.info("HELLO FROM PREINIT");
        LOGGER.info("DIRT BLOCK >> {}", Blocks.DIRT.getRegistryName());
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
        LOGGER.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        // do something when the server starts
        LOGGER.info("HELLO from server starting");

        cs = new ControlServer(event.getServer());

        try {
            cs.Start();
        } catch (Exception e) {
            Log.error("Cannot start server: " + e.getLocalizedMessage());
        }

//        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
//
//        executorService.schedule((Runnable) () -> {
//            LOGGER.info("Executing Kill Task");
//            cs.KillPlayers();
//        }, 10000, TimeUnit.MILLISECONDS);

    }

//    @SubscribeEvent
//    public void sendAlert(EntityJoinWorldEvent event) {
//        if (!(event.getEntity() instanceof CreeperEntity)) {
//            return;
//        }
//
//        List players = event.getEntity().world.getPlayers();
//
//        for (int i = 0; i < players.size(); i++) {
//            PlayerEntity player = (PlayerEntity) players.get(i);
//            if (event.getWorld().isRemote) {
//                player.sendStatusMessage(new StringTextComponent(TextFormatting.GREEN + "A creeper has spawned!"), false);
//            }
//        }
//    }

    @SubscribeEvent
    public void onWorldEntry(EntityJoinWorldEvent event){
        if(event.getEntity() instanceof PlayerEntity){
            LOGGER.info("Got World!");
            cs.SetWorld(event.getWorld());
            cs.SetPlayer((PlayerEntity)event.getEntity());
        }
    }

}
