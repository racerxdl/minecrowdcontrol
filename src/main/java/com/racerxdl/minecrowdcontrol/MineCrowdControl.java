package com.racerxdl.minecrowdcontrol;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(ModData.MODID)
public class MineCrowdControl {
    // Directly reference a log4j logger.
    private static final Logger Log = LogManager.getLogger();

    private ControlServer cs;
    private Minecraft client;
    private CrowdControlModConfig modconfig;

    public MineCrowdControl() {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        modconfig = new CrowdControlModConfig(modContainer);
        modContainer.addConfig(modconfig);

        Log.debug("Config file: " + modconfig.getFileName());

        // Register the setup method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        // Register the doClientStuff method for modloading
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigReload);

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onConfigLoad);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
//        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, CCConfig.spec);
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        Log.info("Got Client");
        this.client = event.getMinecraftSupplier().get();
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {
        Log.info("Server started. Creating Control Server");
        cs = new ControlServer(event.getServer());
    }

    @SubscribeEvent
    public void onServerStopping(FMLServerStoppingEvent event) {
        Log.info("Server stopping. Stopping Control Server");
        cs.Stop();
        cs = null;
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onJump(LivingEvent.LivingJumpEvent event) {
        if (cs.GetStates().getJumpDisabled()) {
            Vector3d motion = event.getEntity().getMotion();
            event.getEntity().setMotion(motion.getX(), 0, motion.getZ());
        }
    }

    @SubscribeEvent
    public void onWorldEntry(EntityJoinWorldEvent event) {
        if (event.getEntity() instanceof PlayerEntity) {
            if (CrowdControlModConfig.ModEnabled.get()) {
                Log.info("Player in. Starting CrowdControl");
                cs.SetClient(client);
                cs.SetPlayer((PlayerEntity) event.getEntity());

                Commands.SetEnablePlayerMessages(CrowdControlModConfig.ShowEffectMessages.get());

                cs.Start();
            } else {
                Commands.SendPlayerSystemMessage((PlayerEntity) event.getEntity(), TextFormatting.RED + "Crowd Control is disabled");
            }
        }
    }

    @SubscribeEvent
    public void onConfigReload(ModConfig.Reloading configEvent) {
        ModConfig config = configEvent.getConfig();
        Log.info("Reloaded config for mod {}", config.getModId());
        if (config.getModId().equals(ModData.MODID)) {
            Commands.SetEnablePlayerMessages(CrowdControlModConfig.ShowEffectMessages.get());
        }
    }

    @SubscribeEvent
    public void onConfigLoad(ModConfig.Loading configEvent) {
        ModConfig config = configEvent.getConfig();
        Log.info("Loading config for mod {}", config.getModId());
        if (config.getModId().equals(ModData.MODID)) {
            Commands.SetEnablePlayerMessages(CrowdControlModConfig.ShowEffectMessages.get());
        }
    }
}
