package com.racerxdl.minecrowdcontrol;

public class PlayerStates {
    private boolean jumpDisabled;
    private boolean gottaGoFast;
    private boolean drunkMode;
    private double originalFOV;

    public PlayerStates() {
        jumpDisabled = false;
        gottaGoFast = false;
        drunkMode = false;
        originalFOV = 70;
    }

    public boolean getJumpDisabled() {
        return jumpDisabled;
    }

    public boolean getGottaGoFast() {
        return gottaGoFast;
    }

    public boolean getDrunkMode() {
        return drunkMode;
    }

    public double getOriginalFOV() {
        return originalFOV;
    }

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

    public PlayerStates setDrunkMode(boolean d) {
        PlayerStates n = this.Clone();
        n.drunkMode = d;
        return n;
    }

    public PlayerStates setOriginalFOV(double d) {
        PlayerStates n = this.Clone();
        n.originalFOV = d;
        return n;
    }

    public PlayerStates Clone() {
        PlayerStates n = new PlayerStates();

        n.jumpDisabled = jumpDisabled;
        n.gottaGoFast = gottaGoFast;
        n.drunkMode = drunkMode;
        n.originalFOV = originalFOV;

        return n;
    }
}
