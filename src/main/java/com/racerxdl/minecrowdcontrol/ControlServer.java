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
    boolean enablePlayerMessages;
    AtomicBoolean running;

    public ControlServer(MinecraftServer server) {
        this.server = server;
        this.started = false;
        this.enablePlayerMessages = true;
        this.running = new AtomicBoolean(false);
    }

    public void Start() {
        if (!running.compareAndSet(false, true)) {
            Log.info("Starting server");
            Thread t = new Thread(this::serverLoop);
            t.start();
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
                String result = ParseCommand(data, "");
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

    public String ParseCommand(String command, String name) {
        switch (command.toUpperCase()) {
            case "KILL":
                KillPlayers(name);
                return "OK";
            case "TAKE_HEART":
                TakeHeart(name);
                return "OK";
            case "GIVE_HEART":
                GiveHeart(name);
                return "OK";
            case "SET_FIRE":
                SetFire(name);
                return "OK";
            case "SPAWN_CREEPER":
                SpawnCreeper(name);
                return "OK";
            case "SET_TIME_NIGHT":
                SetTimeNight(name);
                return "OK";
            case "SET_TIME_DAY":
                SetTimeDay(name);
                return "OK";
            case "TAKE_FOOD":
                TakeFood(name);
                return "OK";
            case "GIVE_FOOD":
                GiveFood(name);
                return "OK";
        }

        // If not, try JSON commands
        return ParseJSON(command);
    }

    public String ParseJSON(String data) {
        Response res = new Response();
        try {
            Request req = Request.FromJSON(data);
            res.id = req.id;
            res.status = EffectResult.Success;
            res.message = "Test";

            if (req.type == RequestType.Start) {
                String cmdResult = ParseCommand(req.code, req.viewer);
                res.status = cmdResult.equals("OK") ? EffectResult.Success : EffectResult.Unavailable;
                Log.info("Effect Result: " + cmdResult);
                res.message = "Effect " + req.code + " started.";
            }

            return res.ToJSON();
        } catch (JsonSyntaxException e) {
            res.message = e.getMessage();
            return res.ToJSON();
        }
    }

    public void RunOnPlayers(PlayerRunnable runnable) {
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (PlayerEntity player : players) {
            runnable.run(player);
        }
    }

    public void SendPlayerMessage(String msg) {
        if (enablePlayerMessages) {
            player.sendStatusMessage(new StringTextComponent(msg), false);
        }
    }

    public void SendPlayerMessage(String msg, Object... params) {
        if (enablePlayerMessages) {
            player.sendStatusMessage(new StringTextComponent(MessageFormat.format(msg, params)), false);
        }
    }

    // region Commands
    public void SetTimeNight(String viewer) {
        Log.info(Messages.ServerSetTimeNight, viewer);
        SendPlayerMessage(Messages.ClientSetTimeNight, viewer);
        world.setDayTime(Tools.NIGHT);
    }

    public void SetTimeDay(String viewer) {
        Log.info(Messages.ServerSetTimeDay, viewer);
        SendPlayerMessage(Messages.ClientSetTimeDay, viewer);
        world.setDayTime(Tools.DAY);
    }

    public void SpawnCreeper(String viewer) {
        BlockPos pos = player.getPosition();
        Log.info(Messages.ServerSpawnCreeper, viewer);
        SendPlayerMessage(Messages.ClientSpawnCreeper, viewer);

        Entity e = EntityType.CREEPER.create(world);
        e.setPositionAndRotation(pos.getX() + 2, pos.getY() + 2, pos.getZ(), 0, 0);

        world.addEntity(e);
    }

    public void TakeFood(String viewer) {
        RunOnPlayers((player -> {
            Log.info(Messages.ServerTakeFood, viewer, player.getName().getString());
            SendPlayerMessage(Messages.ClientTakeFood, viewer);
            FoodStats fs = player.getFoodStats();
            fs.setFoodLevel(fs.getFoodLevel() - 2);
        }));
    }

    public void GiveFood(String viewer) {
        RunOnPlayers((player -> {
            Log.info(Messages.ServerGiveFood, viewer, player.getName().getString());
            SendPlayerMessage(Messages.ClientGiveFood, viewer);
            FoodStats fs = player.getFoodStats();
            fs.setFoodLevel(fs.getFoodLevel() + 2);
        }));
    }


    public void TakeHeart(String viewer) {
        RunOnPlayers((player -> {
            Log.info(Messages.ServerTakeHeart, viewer, player.getName().getString());
            SendPlayerMessage(Messages.ClientTakeHeart, viewer);
            player.setHealth(player.getHealth() - 2);
        }));
    }

    public void GiveHeart(String viewer) {
        RunOnPlayers((player -> {
            Log.info(Messages.ServerGiveHeart, viewer, player.getName().getString());
            SendPlayerMessage(Messages.ClientGiveHeart, viewer);
            player.setHealth(player.getHealth() + 2);
        }));
    }

    public void SetFire(String viewer) {
        RunOnPlayers((player -> {
            Log.info(Messages.ServerSetFire, viewer, player.getName().getString());
            SendPlayerMessage(Messages.ClientSetFire, viewer);
            player.setFire(5);
        }));
    }

    public void KillPlayers(String viewer) {
        RunOnPlayers((player -> {
            Log.info(Messages.ServerKill, viewer, player.getName().getString());
            SendPlayerMessage(Messages.ClientKill, viewer);
            player.setHealth(0);
        }));
    }
    // endregion
}
