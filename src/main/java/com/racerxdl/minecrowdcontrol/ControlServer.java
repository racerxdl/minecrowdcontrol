package com.racerxdl.minecrowdcontrol;

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
import java.util.List;

public class ControlServer {
    private static final Logger Log = LogManager.getLogger();

    MinecraftServer server;
    ServerSocket socket;
    World world;
    PlayerEntity player;

    public ControlServer(MinecraftServer server) {
        this.server = server;
    }

    public void Start() throws Exception {
        socket = new ServerSocket(6789);
        Thread t = new Thread(this::serverLoop);
        t.start();
    }

    public void Stop() throws Exception {
        socket.close();
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
        try {
            Log.info("Server listening on port 6789");
            while (true) {
                Log.info("Waiting for connection");
                Socket connectionSocket = socket.accept();
                Log.info("Got connection from " + connectionSocket.getRemoteSocketAddress().toString());
                Thread t = new Thread(() -> clientLoop(connectionSocket));
                t.start();
            }
        } catch (Exception e) {
            Log.error("Socket error: " + e.getMessage());
        }
    }

    public void clientLoop(Socket client) {
        try {
            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(client.getInputStream()));
            DataOutputStream outToClient = new DataOutputStream(client.getOutputStream());
            while (true) {
                String clientSentence = inFromClient.readLine();
                Log.info("Received: " + clientSentence);
                String result = ParseCommand(clientSentence);
                outToClient.writeBytes(result + "\n");
            }
        } catch (Exception e) {
            Log.error("Error handling client data: " + e.getMessage());
        }
    }

    public String ParseCommand(String command) {
        command = command.toUpperCase();

        switch (command) {
            case "KILL":
                KillPlayers();
                return "OK";
            case "TAKE_HEART":
                TakeHeart();
                return "OK";
            case "GIVE_HEART":
                GiveHeart();
                return "OK";
            case "SET_FIRE":
                SetFire();
                return "OK";
            case "SPAWN_CREEPER":
                SpawnCreeper();
                return "OK";
            case "SET_TIME_NIGHT":
                SetTimeNight();
                return "OK";
            case "SET_TIME_DAY":
                SetTimeDay();
                return "OK";
            case "TAKE_FOOD":
                TakeFood();
                return "OK";
            case "GIVE_FOOD":
                GiveFood();
                return "OK";
        }

        return "Invalid Command";
    }

    public void SetTimeNight() {
        SendPlayerMessage("The night is now!");
        world.setDayTime(Tools.NIGHT);
    }

    public void SetTimeDay() {
        SendPlayerMessage("The day is now!");
        world.setDayTime(Tools.DAY);
    }

    public void RunOnPlayers(PlayerRunnable runnable) {
        List<ServerPlayerEntity> players = server.getPlayerList().getPlayers();
        for (PlayerEntity player : players) {
            runnable.run(player);
        }
    }

    public void SpawnCreeper() {
        BlockPos pos = player.getPosition();
        Log.info("Spawining Creeper!! " + pos.toString());
        SendPlayerMessage(TextFormatting.RED + "Spawining creeper!");

        Entity e = EntityType.CREEPER.create(world);
        e.setPositionAndRotation(pos.getX() + 2, pos.getY() + 2, pos.getZ(), 0, 0);

        world.addEntity(e);
    }

    public void SendPlayerMessage(String msg) {
        player.sendStatusMessage(new StringTextComponent(msg), false);
    }

    public void TakeFood() {
        RunOnPlayers((player -> {
            Log.info("Taking one food from " + player.getName().getString());
            SendPlayerMessage(TextFormatting.RED + "Taking food");
            FoodStats fs = player.getFoodStats();
            fs.setFoodLevel(fs.getFoodLevel() - 2);
        }));
    }

    public void GiveFood() {
        RunOnPlayers((player -> {
            Log.info("Giving one food from " + player.getName().getString());
            SendPlayerMessage(TextFormatting.GREEN + "Giving food");
            FoodStats fs = player.getFoodStats();
            fs.setFoodLevel(fs.getFoodLevel() + 2);
        }));
    }


    public void TakeHeart() {
        RunOnPlayers((player -> {
            Log.info("Taking one heart from " + player.getName().getString());
            SendPlayerMessage(TextFormatting.RED + "Taking one heart");
            player.setHealth(player.getHealth() - 2);
        }));
    }

    public void GiveHeart() {
        RunOnPlayers((player -> {
            Log.info("Giving one heart from " + player.getName().getString());
            SendPlayerMessage(TextFormatting.GREEN + "Giving one heart");
            player.setHealth(player.getHealth() + 2);
        }));
    }

    public void SetFire() {
        RunOnPlayers((player -> {
            SendPlayerMessage(TextFormatting.RED + "Setting fire on you");
            Log.info("Setting fire on " + player.getName().getString());
            player.setFire(5);
        }));
    }

    public void KillPlayers() {
        RunOnPlayers((player -> {
            SendPlayerMessage(TextFormatting.RED + "Killing you");
            Log.info("Killing " + player.getName().getString());
            player.setHealth(0);
        }));
    }
}
