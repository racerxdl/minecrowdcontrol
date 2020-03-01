using System;
using System.Collections.Generic;
using CrowdControl.Common;
using CrowdControl.Games.Packs;
using ConnectorType = CrowdControl.Common.ConnectorType;

public class Minecraft : SimpleTCPPack
{
    public override string Host => "127.0.0.1";

    public override ushort Port => 58430;

    public Minecraft(IPlayer player, Func<CrowdControlBlock, bool> responseHandler, Action<object> statusUpdateHandler) : base(player, responseHandler, statusUpdateHandler) { }

    public override Game Game => new Game(53, "Minecraft", "minecraft", "PC", ConnectorType.SimpleTCPConnector);

    public override List<Effect> Effects => new List<Effect>
    {
        new Effect("Kill the Player", "kill"),
        new Effect("Take one Heart", "take_heart"),
        new Effect("Give one Heart", "give_heart"),
        new Effect("Set Fire", "set_fire"),
        new Effect("Set Time Night", "set_time_night"),
        new Effect("Set Time Day", "set_time_day"),
        new Effect("Take one food", "take_food"),
        new Effect("Give one food", "give_food"),
        new Effect("Send player to Spawn Point", "send_player_to_spawn_point"),
        new Effect("Take all hearts and leave half heart", "take_all_hearts_but_half"),
        new Effect("Fill all hearts", "fill_hearts"),
		new Effect("Invert Mouse (2 minutes)", "invert_mouse"),
		new Effect("Disable Jump (1 minute)", "disable_jump"),
		new Effect("Make it hungry", "take_all_food"),
		new Effect("Food blessing", "fill_food"),
		new Effect("Make it rain (1 minute)", "make_it_rain"),
		new Effect("Gotta go fast (1 minute)", "gotta_go_fast"),
		new Effect("Drunk Mode (1 minute)", "drunk_mode"),
		new Effect("Destroy Selected Item", "destroy_selected_item"),
		new Effect("Drop Selected Item", "drop_selected_item"),
		new Effect("Repair Selected Item", "repair_selected_item"),
		new Effect("Explode Player", "explode_player"),


        new Effect("Spawn Creeper", "spawn_creeper"),
        new Effect("Spawn Enderman", "spawn_enderman"),
        new Effect("Spawn Enderdragon", "spawn_enderdragon"),
        new Effect("Spawn Blaze", "spawn_blaze"),
        new Effect("Spawn Cave Spider", "spawn_cavespider"),
        new Effect("Spawn Spider", "spawn_spider"),
        new Effect("Spawn Witch", "spawn_witch"),
        new Effect("Spawn Bee", "spawn_bee"),
        new Effect("Spawn Horse", "spawn_horse"),
        new Effect("Spawn Skeleton Horse", "spawn_skeletonhorse"),
        new Effect("Spawn Zombie Horse", "spawn_zombiehorse"),
        new Effect("Spawn Zombie", "spawn_zombie"),
        new Effect("Spawn Cow", "spawn_cow"),
        new Effect("Spawn Chicken", "spawn_chicken"),
        new Effect("Spawn Sheep", "spawn_sheep"),
        new Effect("Spawn Villager", "spawn_villager"),
        new Effect("Spawn Wither", "spawn_wither"),
        new Effect("Spawn Slime", "spawn_slime"),
        new Effect("Spawn Silver Fish", "spawn_silverfish"),
        new Effect("Spawn Ravager", "spawn_ravager"),
        new Effect("Spawn Phantom", "spawn_phantom"),
        new Effect("Spawn Vex", "spawn_vex"),
        new Effect("Spawn Fish", "spawn_tropicalfish"),

         new Effect("Create Leather", "create_leather"),
         new Effect("Create Stone", "create_stone"),
         new Effect("Create Iron Ingot", "create_ironingot"),
         new Effect("Create Gold Ingot", "create_goldingot"),
         new Effect("Create Diamon", "create_diamond"),
         new Effect("Create Stone Pick Axe", "create_stonepickaxe"),
         new Effect("Create Stone Sword", "create_stonesword"),
         new Effect("Create Stone Axe", "create_stoneaxe"),
         new Effect("Create Stone Shovel", "create_stoneshovel"),
         new Effect("Create Diamond Pick Axe", "create_diamondpickaxe"),
         new Effect("Create Diamond Sword", "create_diamondsword"),
         new Effect("Create Diamond Axe", "create_diamondaxe"),
         new Effect("Create Diamon Horse Armor", "create_diamondhorsearmor"),
         new Effect("Create Diamon Shovel", "create_diamondshovel"),
         new Effect("Create Ender Pearl", "create_enderpearl"),

         new Effect("Set Difficult to Peaceful", "set_difficult_peaceful"),
         new Effect("Set Difficult to Easy", "set_difficult_easy"),
         new Effect("Set Difficult to Normal", "set_difficult_normal"),
         new Effect("Set Difficult to Hard", "set_difficult_hard"),
    };
}
