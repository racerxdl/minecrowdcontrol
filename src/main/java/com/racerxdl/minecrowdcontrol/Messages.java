package com.racerxdl.minecrowdcontrol;

import net.minecraft.util.text.TextFormatting;

public class Messages {

    // region Server Messages
    public static final String ServerGiveHeart = "Viewer {} just gave an heart to {}";
    public static final String ServerTakeHeart = "Viewer {} just took an heart to {}";
    public static final String ServerGiveFood = "Viewer {} just gave an food to {}";
    public static final String ServerTakeFood = "Viewer {} just took an food to {}";
    public static final String ServerSetTimeDay = "Viewer {} just set time to day";
    public static final String ServerSetTimeNight = "Viewer {} just set time to night";
    public static final String ServerSetFire = "Viewer {} just set fire to {}";
    public static final String ServerSpawnCreeper = "Viewer {} just spawned a creeper";
    public static final String ServerKill = "Viewer {} just killed {}";
    // endregion

    // region Client Messages
    public static final String ClientGiveHeart = TextFormatting.GREEN + "Viewer {0} just gave an heart";
    public static final String ClientTakeHeart = TextFormatting.RED + "Viewer {0} just took an heart";
    public static final String ClientGiveFood = TextFormatting.GREEN + "Viewer {0} just gave an food";
    public static final String ClientTakeFood = TextFormatting.RED + "Viewer {0} just took an food";
    public static final String ClientSetTimeDay = TextFormatting.GOLD + "Viewer {0} just set time to day";
    public static final String ClientSetTimeNight = TextFormatting.GOLD + "Viewer {0} just set time to night";
    public static final String ClientSetFire = TextFormatting.RED + "Viewer {0} just set fire";
    public static final String ClientSpawnCreeper = TextFormatting.RED + "Viewer {0} just spawned a creeper";
    public static final String ClientKill = TextFormatting.RED + "Viewer {0} just killed you";
    // endregion
}
