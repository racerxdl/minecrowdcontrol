package com.racerxdl.minecrowdcontrol;

import java.io.IOException;
import java.io.InputStreamReader;

public class Tools {
    public final static int NIGHT = 13000;
    public final static int DAY = 1000;

    public final static int MAX_HEALTH = 20;
    public final static int MAX_FOOD = 20;

    public final static double MAX_FOV = 90;
    public final static double MIN_FOV = 30;
    public final static double FOV_STEP = 0.5;
    public final static long DRUNK_REFRESH_INTERVAL = 10; // ms

    public static String ReadUntilNull(InputStreamReader reader) throws IOException {
        StringBuilder sb = new StringBuilder();
        char[] results = new char[1];
        int bytes_read = reader.read(results);
        while (results[0] != 0x00 && bytes_read == 1) {
            sb.append(results[0]);
            bytes_read = reader.read(results);
        }

        return sb.toString();
    }
}
