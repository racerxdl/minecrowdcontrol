package com.racerxdl.minecrowdcontrol.common.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;

public class CommandHandler
{
    public CommandHandler(CommandDispatcher<CommandSource> dist)
    {
        dist.register(
                LiteralArgumentBuilder.<CommandSource>literal("crowdcontrol")
                        .then(TestEffectCMD.register())
        );
    }
}
