package com.racerxdl.minecrowdcontrol;

import com.racerxdl.minecrowdcontrol.common.command.CommandHandler;
import com.racerxdl.minecrowdcontrol.common.handler.EffectHandler;
import com.racerxdl.minecrowdcontrol.common.util.Constants;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(Constants.MOD_ID)
public class MineCrowdControl
{
    public static MineCrowdControl  INSTANCE;
    private static Logger           LOG = LogManager.getLogger();
    public EffectHandler            EFFECT_HANDLER;

    public MineCrowdControl()
    {

        final IEventBus modBus   = FMLJavaModLoadingContext.get().getModEventBus();
        final IEventBus forgeBus = MinecraftForge.EVENT_BUS;

        forgeBus.addListener(this::setup);
        forgeBus.addListener(this::serverStarting);

        INSTANCE = this;
        EFFECT_HANDLER = new EffectHandler();

    }

    private void setup(FMLCommonSetupEvent event)
    {
    }

    private void serverStarting(FMLServerStartingEvent event)
    {
        new CommandHandler(event.getCommandDispatcher());
    }
}
