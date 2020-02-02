package com.racerxdl.minecrowdcontrol;

import com.google.gson.JsonSyntaxException;
import com.racerxdl.minecrowdcontrol.CrowdControl.EffectResult;
import com.racerxdl.minecrowdcontrol.CrowdControl.Request;
import com.racerxdl.minecrowdcontrol.CrowdControl.RequestType;
import com.racerxdl.minecrowdcontrol.CrowdControl.Response;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicBoolean;

public class ControlServer {
    private static final Logger Log = LogManager.getLogger();

    private MinecraftServer server;
    private PlayerEntity player;
    private AtomicBoolean running;
    private Minecraft client;
    private PlayerStates states;

    public ControlServer(MinecraftServer server) {
        this.server = server;
        this.running = new AtomicBoolean(false);
        this.states = new PlayerStates();
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

    public PlayerStates GetStates() {
        return this.states.Clone();
    }

    private void SetStates(PlayerStates states) {
        this.states = states.Clone();
    }

    public void SetClient(Minecraft client) {
        Log.info("Setting client!");
        this.client = client;
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

    public CommandResult RunCommand(String command, String viewer, RequestType type) {
        if (Commands.CommandList.get(command.toUpperCase()) != null) {
            return Commands.CommandList.get(command.toUpperCase()).Run(GetStates(), player, client, server, viewer, type);
        }

        return new CommandResult(GetStates()).SetEffectResult(EffectResult.Unavailable);
    }

    public String ParseJSON(String data) {
        Response res = new Response();
        try {
            Request req = Request.FromJSON(data);
            res.id = req.id;
            CommandResult cmdRes = RunCommand(req.code, req.viewer, req.type);
            res.status = cmdRes.GetEffectResult();
            SetStates(cmdRes.GetPlayerStates());
            res.message = "Effect " + req.code + ": " + res.status;

            return res.ToJSON();
        } catch (JsonSyntaxException e) {
            res.message = e.getMessage();
            return res.ToJSON();
        }
    }
}
