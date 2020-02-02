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
    public static final String ServerKill = "Viewer {} just killed {}";
    public static final String ServerSendPlayerToSpawnPoint = "Viewer {} just sent {} to his spawn point";
    public static final String ServerTakeAllHeartsButHalf = "Viewer {} just took {} hearts";
    public static final String ServerFillAllHearts = "Viewer {} just filled all {} hearts";
    public static final String ServerSpawn = "Viewer {} just spawned a {}";
    public static final String ServerInvertMouse = "Viewer {} just inverted {} mouse";
    public static final String ServerRestoreInvertMouse = "{} mouse inversion restored";
    // endregion

    // region Client Messages
    public static final String ClientGiveHeart = TextFormatting.GREEN + "Viewer {0} just gave an heart";
    public static final String ClientTakeHeart = TextFormatting.RED + "Viewer {0} just took an heart";
    public static final String ClientGiveFood = TextFormatting.GREEN + "Viewer {0} just gave an food";
    public static final String ClientTakeFood = TextFormatting.RED + "Viewer {0} just took an food";
    public static final String ClientSetTimeDay = TextFormatting.GOLD + "Viewer {0} just set time to day";
    public static final String ClientSetTimeNight = TextFormatting.GOLD + "Viewer {0} just set time to night";
    public static final String ClientSetFire = TextFormatting.RED + "Viewer {0} just set fire";
    public static final String ClientKill = TextFormatting.RED + "Viewer {0} just killed you";
    public static final String ClientSendPlayerToSpawnPoint = TextFormatting.GOLD + "Viewer {0} just send you to spawn point";
    public static final String ClientTakeAllHeartsButHalf = TextFormatting.RED + "Viewer {0} just took all your hearts but half";
    public static final String ClientFillAllHearts = TextFormatting.GREEN + "Viewer {0} just filled all your hearts";
    public static final String ClientSpawn = TextFormatting.RED + "Viewer {0} just spawn a {1}";
    public static final String ClientInvertMouse = TextFormatting.RED + "Viewer {0} just inverted your mouse";
    public static final String ClientRestoreInvertMouse = TextFormatting.GREEN + "Your mouse inversion has been restored";
    // endregion
}
