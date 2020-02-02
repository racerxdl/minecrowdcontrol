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
        new Effect("Spawn Creeper", "spawn_creeper"),
        new Effect("Set Time Night", "set_time_night"),
        new Effect("Set Time Day", "set_time_day"),
        new Effect("Take one food", "take_food"),
        new Effect("Give one food", "give_food"),
        new Effect("Send player to Spawn Point", "send_player_to_spawn_point"),
        new Effect("Take all hearts and leave half heart", "take_all_hearts_but_half"),
        new Effect("Fill all hearts", "fill_hearts"),
        new Effect("Spawn Enderman", "spawn_enderman"),
        new Effect("Spawn Enderdragon", "spawn_enderdragon"),
		new Effect("Invert Mouse (2 minutes)", "invert_mouse"),
		new Effect("Disable Jump (1 minute)", "disable_jump"),
		new Effect("Make it hungry", "take_all_food"),
		new Effect("Food blessing", "fill_food"),
		new Effect("Make it rain (1 minute)", "make_it_rain"),
		new Effect("Gotta go fast (1 minute)", "gotta_go_fast"),
    };
}