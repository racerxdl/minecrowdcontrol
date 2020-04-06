package com.racerxdl.minecrowdcontrol;

import net.minecraft.util.text.TextFormatting;

import javax.xml.soap.Text;

public class Messages {

    // region Server Messages
    public static final String ServerGiveHeart = "Viewer {} just gave a heart to {}";
    public static final String ServerTakeHeart = "Viewer {} just took a heart to {}";
    public static final String ServerGiveFood = "Viewer {} just gave a food point to {}";
    public static final String ServerTakeFood = "Viewer {} just took a food point to {}";
    public static final String ServerSetTimeDay = "Viewer {} just set the time to day";
    public static final String ServerSetTimeNight = "Viewer {} just set the time to night";
    public static final String ServerSetFire = "Viewer {} just set fire to {}";
    public static final String ServerKill = "Viewer {} just killed {}";
    public static final String ServerSendPlayerToSpawnPoint = "Viewer {} just sent {} to his spawnpoint";
    public static final String ServerTakeAllHeartsButHalf = "Viewer {} just took {} hearts";
    public static final String ServerFillAllHearts = "Viewer {} just filled all {} hearts";
    public static final String ServerSpawn = "Viewer {} just spawned a {}";
    public static final String ServerInvertMouse = "Viewer {} just inverted {} mouse";
    public static final String ServerRestoreInvertMouse = "{} mouse inversion restored";
    public static final String ServerTakeAllFood = "Viewer {} took all {} food points";
    public static final String ServerFillFood = "Viewer {} filled {} food points";
    public static final String ServerJumpDisabled = "Viewer {} just disabled {} jump";
    public static final String ServerJumpRestored = "{} jump is enabled again";
    public static final String ServerMakeItRain = "Viewer {} just made it rain";
    public static final String ServerRainRestored = "The rain just stopped";
    public static final String ServerGottaGoFast = "Viewer {} just made it fast";
    public static final String ServerGottaGoFastRestored = "The speed is now normal";
    public static final String ServerDrunkModeStarted = "Viewer {} just made the game drunk";
    public static final String ServerDrunkModeRestored = "The game is not drunk anymore";
    public static final String ServerDestroyItem = "Viewer {} just destroyed {} {}";
    public static final String ServerDropItem = "Viewer {} just dropped {} {}";
    public static final String ServerRepairItem = "Viewer {} just repaired {} {}";
    public static final String ServerCreateItem = "Viewer {} just gave {} a {}";
    public static final String ServerSetDifficult = "Viewer {} just set the game's difficulty to {}";
    // endregion

    // region Client Messages
    public static final String ClientGiveHeart = TextFormatting.GREEN + "Viewer {0} just gave a heart";
    public static final String ClientTakeHeart = TextFormatting.RED + "Viewer {0} just took a heart";
    public static final String ClientGiveFood = TextFormatting.GREEN + "Viewer {0} just gave a food point";
    public static final String ClientTakeFood = TextFormatting.RED + "Viewer {0} just took a food point";
    public static final String ClientSetTimeDay = TextFormatting.GOLD + "Viewer {0} just set the time to day";
    public static final String ClientSetTimeNight = TextFormatting.GOLD + "Viewer {0} just set the time to night";
    public static final String ClientSetFire = TextFormatting.RED + "Viewer {0} just set fire";
    public static final String ClientKill = TextFormatting.RED + "Viewer {0} just killed you";
    public static final String ClientSendPlayerToSpawnPoint = TextFormatting.GOLD + "Viewer {0} just sent you to your spawnpoint";
    public static final String ClientTakeAllHeartsButHalf = TextFormatting.RED + "Viewer {0} just took all your hearts but half";
    public static final String ClientFillAllHearts = TextFormatting.GREEN + "Viewer {0} just filled all your hearts";
    public static final String ClientSpawn = TextFormatting.RED + "Viewer {0} just spawned a {1}";
    public static final String ClientInvertMouse = TextFormatting.RED + "Viewer {0} just inverted your mouse";
    public static final String ClientRestoreInvertMouse = TextFormatting.GREEN + "Your mouse inversion has been restored";
    public static final String ClientTakeAllFood = TextFormatting.RED + "Viewer {0} just took all your food points. Now you're hungry!";
    public static final String ClientFillFood = TextFormatting.GREEN + "Viewer {0} just filled all your food points";
    public static final String ClientJumpDisabled = TextFormatting.RED + "Viewer {0} just disabled your jump";
    public static final String ClientJumpRestored = TextFormatting.GREEN + "Your jump is enabled again";
    public static final String ClientMakeItRain = TextFormatting.GOLD + "Viewer {0} just made it rain";
    public static final String ClientRainRestored = TextFormatting.GOLD + "The rain just stopped";
    public static final String ClientGottaGoFast = TextFormatting.YELLOW + "Viewer {0} just made you fast";
    public static final String ClientGottaGoFastRestored = "Your speed is now normal";
    public static final String ClientDrunkModeStarted = "Viewer {0} just made you drunk";
    public static final String ClientDrunkModeRestored = "You're not drunk anymore";
    public static final String ClientDestroyItem = TextFormatting.RED + "Viewer {0} just destroyed your {1}";
    public static final String ClientDropItem = TextFormatting.RED + "Viewer {0} just dropped your {1}";
    public static final String ClientRepairItem = TextFormatting.RED + "Viewer {0} just repaired your {1}";
    public static final String ClientCreateItem = TextFormatting.GREEN + "Viewer {0} just gave you a {1}";
    public static final String ClientSetDifficult = TextFormatting.GOLD + "Viewer {0} just set the game's difficult to {1}";
    // endregion
}
