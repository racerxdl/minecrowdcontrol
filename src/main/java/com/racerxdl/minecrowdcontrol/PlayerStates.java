package com.racerxdl.minecrowdcontrol;

public class PlayerStates {
    boolean jumpDisabled;
    boolean gottaGoFast;

    public PlayerStates() {
        jumpDisabled = false;
        gottaGoFast = false;
    }

    public boolean getJumpDisabled() {
        return jumpDisabled;
    }
    public boolean getGottaGoFast() { return gottaGoFast; }

    public PlayerStates setJumpDisabled(boolean d) {
        PlayerStates n = this.Clone();
        n.jumpDisabled = d;
        return n;
    }

    public PlayerStates setGottaGoFast(boolean d) {
        PlayerStates n = this.Clone();
        n.gottaGoFast = d;
        return n;
    }

    public PlayerStates Clone() {
        PlayerStates n = new PlayerStates();

        n.jumpDisabled = jumpDisabled;
        n.gottaGoFast = gottaGoFast;

        return n;
    }
}
