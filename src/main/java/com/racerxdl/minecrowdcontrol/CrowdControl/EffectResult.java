package com.racerxdl.minecrowdcontrol.CrowdControl;

import com.google.gson.annotations.SerializedName;

public enum EffectResult {
    /// <summary>The effect executed successfully.</summary>
    @SerializedName("0")
    Success(0),
    /// <summary>The effect failed to trigger, but is still available for use. Viewer(s) will be refunded.</summary>
    @SerializedName("1")
    Failure(1),
    /// <summary>Same as <see cref="Failure"/> but the effect is no longer available for use.</summary>
    @SerializedName("2")
    Unavailable( 2),
    /// <summary>The effect cannot be triggered right now, try again in a few seconds.</summary>
    @SerializedName("3")
    Retry (3);

    public int v;
    EffectResult(int v) {
        this.v = v;
    }

    public int getValue() {
        return v;
    }
}