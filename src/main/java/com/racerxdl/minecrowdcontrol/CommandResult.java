package com.racerxdl.minecrowdcontrol;

import com.racerxdl.minecrowdcontrol.CrowdControl.EffectResult;

public class CommandResult {
    private EffectResult effectResult;
    private PlayerStates newStates;

    public CommandResult() {
        effectResult = EffectResult.Failure;
        newStates = new PlayerStates();
    }

    public CommandResult(PlayerStates states) {
        this();
        newStates = states;
    }

    public CommandResult SetEffectResult(EffectResult result) {
        CommandResult r = this.Clone();
        r.effectResult = result;
        return r;
    }

    public CommandResult SetNewStates(PlayerStates states) {
        CommandResult r = this.Clone();
        r.newStates = states;
        return r;
    }

    public EffectResult GetEffectResult() {
        return effectResult;
    }

    public PlayerStates GetPlayerStates() {
        return newStates.Clone();
    }

    public CommandResult Clone() {
        CommandResult r = new CommandResult();
        r.effectResult = this.effectResult;
        r.newStates = this.newStates.Clone();
        return r;
    }
}
