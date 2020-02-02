package com.racerxdl.minecrowdcontrol;

public class PlayerStates {
    boolean jumpDisabled;

    public PlayerStates() {
        jumpDisabled = false;
    }

    public boolean getJumpDisabled() {
        return jumpDisabled;
    }

    public PlayerStates setJumpDisabled(boolean d) {
        PlayerStates n = this.Clone();
        n.jumpDisabled = d;
        return n;
    }

    public PlayerStates Clone() {
        PlayerStates n = new PlayerStates();

        n.jumpDisabled = jumpDisabled;

        return n;
    }
}
