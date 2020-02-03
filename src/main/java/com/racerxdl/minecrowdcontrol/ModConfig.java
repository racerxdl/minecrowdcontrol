package com.racerxdl.minecrowdcontrol;

import net.minecraftforge.common.ForgeConfigSpec;

public class ModConfig {
    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final ForgeConfigSpec spec = BUILDER.build();

    public static class General {
        public final ForgeConfigSpec.ConfigValue<Boolean> ModEnabled;
        public final ForgeConfigSpec.ConfigValue<Boolean> ShowEffectMessages;

        public General(ForgeConfigSpec.Builder builder) {
            builder.push("General");
            ModEnabled = builder
                    .comment("Enables/Disables the whole Mod [false/true|default:true]")
                    .translation("enable.crowdcontrol.config")
                    .define("enableMod", true);
            ShowEffectMessages = builder
                    .comment("Enables/Disables showing effect messages [false/true|default:true]")
                    .translation("enable.crowdcontrol.config")
                    .define("showEffectMessages", true);
            builder.pop();
        }
    }
}