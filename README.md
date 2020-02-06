![Minecraft Mod CI](https://github.com/racerxdl/minecrowdcontrol/workflows/Minecraft%20Mod%20CI/badge.svg)

Minecraft Crowd Control Mod
===========================

This is a modification for Minecraft (Java Edition) that can interact with [Crowd Control](https://crowdcontrol.live/)


Features
=========

* Kill the player
* Give / Take one Heart
* Give / Take one Food
* Set fire on player
* Spawn Creeper / Enderman / Enderdragon / Blaze / Cave Spider / Spider / Witch / Bee / Horse / Skeleton Horse / Zombie Horse / Zombie / Cow / Chicken / Pig
* Set Time Day / Night
* Send player back to spawn point
* Take all hearts and leave player with half
* Fill all hearts
* Invert Mouse (2 minutes)
* Disable Jump (1 minute)
* Make it hungry (take all food)
* Food Blessing (fill food)
* Make it rain (1 minute)
* [Gotta go fast (1 minute)](https://www.twitch.tv/racerxdl/clip/AgileSuspiciousCucumberOhMyDog)
* [Drunk Mode (1 minute)](https://www.twitch.tv/racerxdl/clip/SpookyInnocentWrenchANELE)
* Destroy Selected Item
* Drop Selected Item
* Repair Selected Item
* Explode Player
* Drop a item to the player (Create Item)

Installing
==========

For running this mod, you will need the oficial [Minecraft Launcher](https://www.minecraft.net/download/) and Minecraft Forge (1.15.2) installed.

Make sure you have Minecraft installed before doing anything.

##### 1. Get Minecraft Forge
To get the Minecraft Forge, download the [Forge Installer 1.15.2](https://files.minecraftforge.net/maven/net/minecraftforge/forge/1.15.2-31.0.16/forge-1.15.2-31.0.16-installer.jar) and run it:

```bash
java -jar forge-1.15.2-31.0.16-installer.jar
```

Select `Install Client` and let it install on the default path:

![Select Install Client](https://user-images.githubusercontent.com/578310/73618456-a0d0ad80-4606-11ea-8e72-e072c84b1e12.png)

After that, a Minecraft Forge entry should appear in your Minecraft Launcher:

![Forge in Launcher](https://user-images.githubusercontent.com/578310/73618477-d8d7f080-4606-11ea-8c6f-aaffbdb63454.png)

Now you should be ready to install the MineCrowdControl.

#### 2. Installing Minecrowd Control

1. Go to [Releases](https://github.com/racerxdl/minecrowdcontrol/releases) page and download the latest `minecrowdcontrol` jar file.
2. Open Minecraft Launcher and start the Forge version you installed in the early step.
3. Click in ![Mods](https://user-images.githubusercontent.com/578310/73618556-497f0d00-4607-11ea-8ace-df935b1f0db7.png) Button
4. Click in ![Open Mods Folder](https://user-images.githubusercontent.com/578310/73618572-60bdfa80-4607-11ea-9242-2bf3fd552467.png) Button.
5. Copy the `jar` file you downloaded to the folder that will open and restart the game.
6. Go again to the `Mods` page and see if `Minecraft CrowdControl` is visible.


![Minecraft Crowd Control](https://user-images.githubusercontent.com/578310/73618595-83e8aa00-4607-11ea-9856-df537c49b2c6.png)

Changing Configuration
=======================

So far the 1.15.x version of forge has the GUI disabled for config (see https://github.com/MinecraftForge/MinecraftForge/pull/6467)
so if you want to disable the mod or the Crowd Control messages you should edit the file manually.
The config file is stored at:

*   Windows: `%appdata%\.minecraft\config\minecrowdcontrol-common.toml`
*   Linux: `~/.minecraft/config/minecrowdcontrol-common.toml`

The file config is pretty easy, just change to `true` to enable a feature or `false` to disable it:

```toml

[General]
	#Enables/Disables showing effect messages [false/true|default:true]
	showEffectMessages = true
	#Enables/Disables the whole Mod [false/true|default:true]
	enableMod = true
```

The `showEffectMessages` has a hot-reload function and can be changed without restarting the game.

The `enableMod` has a hot-reload function but you need to get back to the game main menu.


Testing
=========

We use the [Crowd Control SDK](https://forum.warp.world/t/how-to-setup-and-use-the-crowd-control-sdk/5121) to test it which mimics the their server. Make sure you got it installed in your machine.

1. Open `Crowd Control EffectPack SDK`
2. Click in ![Load Pack Source](https://user-images.githubusercontent.com/578310/73620150-47b94780-460f-11ea-95fc-acc71e6345e7.png)
3. Select `minecrowdcontrol.cs` (it is provided with releases and on the root of this repository)
4. Select `Minecraft` in the list ![Minecraft](https://user-images.githubusercontent.com/578310/73620196-7fc08a80-460f-11ea-9490-c2690fe05672.png) and click button ![Select Pack](https://user-images.githubusercontent.com/578310/73620201-85b66b80-460f-11ea-9141-7532de617dc2.png)
5. Click in ![Connect](https://user-images.githubusercontent.com/578310/73620231-a7175780-460f-11ea-9b09-f058510b90dc.png) button
6. Open up Minecraft with the installed mod and it should connect automatically.

![Effect List](https://user-images.githubusercontent.com/578310/73620251-ba2a2780-460f-11ea-9acc-f678e7b734b6.png)

Developing
==========

TODO - Please refer to [https://mcforge.readthedocs.io/en/latest/gettingstarted/](https://mcforge.readthedocs.io/en/latest/gettingstarted/)


-------------------------------------------
Source installation information for modders
-------------------------------------------
This code follows the Minecraft Forge installation methodology. It will apply
some small patches to the vanilla MCP source code, giving you and it access
to some of the data and functions you need to build a successful mod.

Note also that the patches are built against "unrenamed" MCP source code (aka
srgnames) - this means that you will not be able to read them directly against
normal code.

Source pack installation information:

Standalone source installation
==============================

See the Forge Documentation online for more detailed instructions:
http://mcforge.readthedocs.io/en/latest/gettingstarted/

Step 1: Open your command-line and browse to the folder where you extracted the zip file.

Step 2: You're left with a choice.
If you prefer to use Eclipse:
1. Run the following command: "gradlew genEclipseRuns" (./gradlew genEclipseRuns if you are on Mac/Linux)
2. Open Eclipse, Import > Existing Gradle Project > Select Folder
   or run "gradlew eclipse" to generate the project.
(Current Issue)
4. Open Project > Run/Debug Settings > Edit runClient and runServer > Environment
5. Edit MOD_CLASSES to show [modid]%%[Path]; 2 times rather then the generated 4.

If you prefer to use IntelliJ:
1. Open IDEA, and import project.
2. Select your build.gradle file and have it import.
3. Run the following command: "gradlew genIntellijRuns" (./gradlew genIntellijRuns if you are on Mac/Linux)
4. Refresh the Gradle Project in IDEA if required.

If at any point you are missing libraries in your IDE, or you've run into problems you can run "gradlew --refresh-dependencies" to refresh the local cache. "gradlew clean" to reset everything {this does not affect your code} and then start the processs again.

Should it still not work,
Refer to #ForgeGradle on EsperNet for more information about the gradle environment.
or the Forge Project Discord discord.gg/UvedJ9m

Forge source installation
=========================
MinecraftForge ships with this code and installs it as part of the forge
installation process, no further action is required on your part.

LexManos' Install Video
=======================
https://www.youtube.com/watch?v=8VEdtQLuLO0&feature=youtu.be

For more details update more often refer to the Forge Forums:
http://www.minecraftforge.net/forum/index.php/topic,14048.0.html
