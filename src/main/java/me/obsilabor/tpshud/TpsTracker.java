package me.obsilabor.tpshud;

import me.obsilabor.tpshud.config.ConfigManager;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.game.ClientboundSetTimePacket;
import java.util.Arrays;

public class TpsTracker {
    public static final TpsTracker INSTANCE = new TpsTracker();

    private final float[] tickTimesMilli = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate = -1;
    private long timeGameJoined;

    public float serverProvidedTps = -1;

    public static void init() {
        // Register join event
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            INSTANCE.onGameJoined();
        });
    }

    public void onPacketReceive(ClientboundSetTimePacket packet) {
        long now = System.currentTimeMillis();
        long timeElapsed = now - timeLastTimeUpdate;

        // Packet is sent 1 time per second, so divide per 20 to get MSPT
        tickTimesMilli[nextIndex] = timeElapsed / 20.0f;
        nextIndex = (nextIndex + 1) % tickTimesMilli.length;
        timeLastTimeUpdate = now;
    }

    public void onGameJoined() {
        serverProvidedTps = -1;
        Arrays.fill(tickTimesMilli, 0);
        nextIndex = 0;
        timeGameJoined = timeLastTimeUpdate = System.currentTimeMillis();
    }

    // Return milliseconds per tick (MSPT)
    public float getTickTime() {
        // get server side TPS if enabled and available
        if (serverProvidedTps != -1 && ConfigManager.getConfig().useServerProvidedData) {
            return serverProvidedTps;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null) return 0;
        if (System.currentTimeMillis() - timeGameJoined < 4000) return 20;

        // Calculate average tick time from network using the last 20 estimated tick times
        int numTicks = 0;
        float sumTickTimes = 0;
        for (float tickTime : tickTimesMilli) {
            if (tickTime > 0) {
                sumTickTimes += tickTime;
                numTicks++;
            }
        }
        return numTicks > 0 ? sumTickTimes / numTicks : 20;
    }
}
