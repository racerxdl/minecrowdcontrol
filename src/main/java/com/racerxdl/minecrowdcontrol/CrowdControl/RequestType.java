package com.racerxdl.minecrowdcontrol.CrowdControl;

import com.google.gson.annotations.SerializedName;

public enum RequestType {
    @SerializedName("0")
    Test(0),
    @SerializedName("1")
    Start(1),
    @SerializedName("2")
    Stop(2);

    public int v;

    RequestType(int v) {
        this.v = v;
    }

    public int getValue() {
        return v;
    }
}