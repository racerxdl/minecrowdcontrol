package com.racerxdl.minecrowdcontrol.common.command;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.racerxdl.minecrowdcontrol.MineCrowdControl;
import com.racerxdl.minecrowdcontrol.common.effect.api.EffectStatus;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.player.PlayerEntity;

public class TestEffectCMD
{
    private static final SuggestionProvider<CommandSource> EFFECTS = (ctx, builder) ->
            ISuggestionProvider.suggest(MineCrowdControl.INSTANCE.EFFECT_HANDLER.getEffectRegistry().keySet().stream(),
                    builder);


    public static ArgumentBuilder<CommandSource, ?> register()
    {
        return Commands.literal("effects")
                .requires(cs->cs.hasPermissionLevel(0))
                .then(Commands.argument("effect", StringArgumentType.string()).suggests(EFFECTS))
                .executes(ctx -> {
                    System.out.println(ctx.getInput());
                    exec(ctx.getInput(), ctx.getSource().asPlayer());
                    return 0;
                });
    }

    private static void exec(String effect, PlayerEntity player)
    {
        MineCrowdControl.INSTANCE.EFFECT_HANDLER.executeEffect(effect, player, "mctest", EffectStatus.TEST);
    }
}
