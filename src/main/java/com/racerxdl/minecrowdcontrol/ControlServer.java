package com.racerxdl.minecrowdcontrol;

import com.google.gson.JsonSyntaxException;
import com.racerxdl.minecrowdcontrol.CrowdControl.EffectResult;
import com.racerxdl.minecrowdcontrol.CrowdControl.Request;
import com.racerxdl.minecrowdcontrol.CrowdControl.RequestType;
import com.racerxdl.minecrowdcontrol.CrowdControl.Response;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.monster.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class ControlServer {
    private static final Logger Log = LogManager.getLogger();

    MinecraftServer server;
    World world;
    PlayerEntity player;
    boolean started;
    AtomicBoolean running;

    public ControlServer(MinecraftServer server) {
        this.server = server;
        this.started = false;
        this.running = new AtomicBoolean(false);
        Commands.SetEnablePlayerMessages(true);
    }

    public void Start() {
        if (running.compareAndSet(false, true)) {
            Log.info("Starting server");
            Thread t = new Thread(this::serverLoop);
            t.start();
        } else {
            Log.error("Already running");
        }
    }

    public void Stop() {
        running.set(false);
    }

    public void SetWorld(World world) {
        if (world.isRemote) {
            return;
        }
        Log.info("Setting world to " + world.toString());
        this.world = world;
    }

    public void SetPlayer(PlayerEntity player) {
        Log.info("Setting player to " + player.toString());
        this.player = player;
    }

    private void serverLoop() {
        Log.info("Server loop started");
        while (running.get()) {
            try {
                Log.info("Trying to connect to Crowd Control");
                Socket s = new Socket("localhost", 58430);
                Log.info("Connected to crowd control!");
                clientLoop(s);
                Log.info("Disconnected from crowd control");
            } catch (Exception e) {
                Log.error("Socket error: " + e.getMessage());
            }
        }
        Log.info("Server loop ended");
    }

    public void clientLoop(Socket client) {
        try {
            InputStreamReader inFromClient = new InputStreamReader(client.getInputStream());
            DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
            while (running.get()) {
                String data = Tools.ReadUntilNull(inFromClient);
                Log.debug("Received: " + data);
                String result = ParseJSON(data);
                Log.debug("Sending: " + result);
                byte[] tmp = result.getBytes();
                byte[] outData = new byte[tmp.length + 1];

                System.arraycopy(tmp, 0, outData, 0, tmp.length);

                outData[outData.length - 1] = 0x00;

                outToClient.write(outData);
            }
        } catch (Exception e) {
            Log.error("Error handling client data: " + e.getMessage());
        }
    }

    public EffectResult RunCommand(String command, String viewer) {
        if (Commands.CommandList.get(command.toUpperCase()) != null) {
            return Commands.CommandList.get(command.toUpperCase()).Run(player, server, viewer);
        }

        return EffectResult.Unavailable;
    }

    public String ParseJSON(String data) {
        Response res = new Response();
        try {
            Request req = Request.FromJSON(data);
            res.id = req.id;
            res.status = EffectResult.Success;
            res.message = "Test";

            if (req.type == RequestType.Start) {
                res.status = RunCommand(req.code, req.viewer);
                res.message = "Effect " + req.code + ": " + res.status;
            }

            return res.ToJSON();
        } catch (JsonSyntaxException e) {
            res.message = e.getMessage();
            return res.ToJSON();
        }
    }
}
