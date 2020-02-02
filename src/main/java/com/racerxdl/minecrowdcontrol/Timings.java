package com.racerxdl.minecrowdcontrol;

import java.util.HashMap;
import java.util.Map;

public class Timings {
    public static final int NO_STOP = -1;

    public static final Map<String, Integer> StopTimingMap = new HashMap<String, Integer>() {{
        put("KILL", NO_STOP);
        put("TAKE_HEART", NO_STOP);
        put("GIVE_HEART", NO_STOP);
        put("SET_FIRE", NO_STOP);
        put("SPAWN_CREEPER", NO_STOP);
        put("SET_TIME_NIGHT", NO_STOP);
        put("SET_TIME_DAY", NO_STOP);
        put("TAKE_FOOD", NO_STOP);
        put("GIVE_FOOD", NO_STOP);
        put("SEND_PLAYER_TO_SPAWN_POINT", NO_STOP);
        put("TAKE_ALL_HEARTS_BUT_HALF", NO_STOP);
        put("FILL_HEARTS", NO_STOP);
        put("SPAWN_ENDERMAN", NO_STOP);
        put("SPAWN_ENDERDRAGON", NO_STOP);
        put("INVERT_MOUSE", 2 * 60);
        put("DISABLE_JUMP", 60);
        put("TAKE_ALL_FOOD", NO_STOP);
        put("FILL_FOOD", NO_STOP);
        put("MAKE_IT_RAIN", 60);
        put("GOTTA_GO_FAST", 60);
    }};

    public static int GetStopTiming(String cmd) {
        cmd = cmd.toUpperCase();

        if (StopTimingMap.get(cmd) != null) {
            return StopTimingMap.get(cmd);
        }

        return NO_STOP;
    }
}
