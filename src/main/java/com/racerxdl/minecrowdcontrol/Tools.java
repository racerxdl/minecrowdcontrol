package com.racerxdl.minecrowdcontrol;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Tools {
    public final static int NIGHT = 13000;
    public final static int MIDNIGHT = 18000;
    public final static int DAY = 1000;

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
