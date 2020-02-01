package com.racerxdl.minecrowdcontrol;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("minecrowdcontrol")
public class MineCrowdControl {
    // Directly reference a log4j logger.
    private static final Logger Log = LogManager.getLogger();

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

    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        // do something that can only be done on the client
//        Log.info("Got game settings {}", event.getMinecraftSupplier().get().gameSettings);
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        Log.info("Server started. Creating Control Server");
        cs = new ControlServer(event.getServer());
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStoppingEvent event) {
        Log.info("Server stopping. Stopping Control Server");
        cs.Stop();
        cs = null;
    }

    @SubscribeEvent
    public void onWorldEntry(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            Log.info("Player in. Starting CrowdControl");
            cs.SetWorld(event.getWorld());
            cs.SetPlayer((PlayerEntity) event.getEntity());
            cs.Start();
        }
    }

}
