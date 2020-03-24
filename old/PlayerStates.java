package com.racerxdl.minecrowdcontrol;

public class PlayerStates {
    private boolean jumpDisabled;
    private boolean gottaGoFast;
    public String gottaGoFastViewer;
    private boolean drunkMode;
    private double originalFOV;

    public PlayerStates() {
        jumpDisabled = false;
        gottaGoFast = false;
        drunkMode = false;
        gottaGoFastViewer = "";
        originalFOV = 70;
    }

    public boolean getJumpDisabled() {
        return jumpDisabled;
    }

    public boolean getGottaGoFast() {
        return gottaGoFast;
    }

    public String getGottaGoFastViewer() {
        return gottaGoFastViewer;
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

    public PlayerStates setGottaGoFastViewer(String d) {
        PlayerStates n = this.Clone();
        n.gottaGoFastViewer = d;
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
        n.gottaGoFastViewer = gottaGoFastViewer;
        n.drunkMode = drunkMode;
        n.originalFOV = originalFOV;

        return n;
    }
}
