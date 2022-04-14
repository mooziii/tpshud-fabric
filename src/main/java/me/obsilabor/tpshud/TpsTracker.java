package me.obsilabor.tpshud;

import me.obsilabor.alert.Listener;
import me.obsilabor.alert.Subscribe;
import me.obsilabor.tpshud.event.GameJoinEvent;
import me.obsilabor.tpshud.event.PacketReceiveEvent;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.packet.s2c.play.WorldTimeUpdateS2CPacket;

import java.util.Arrays;

public class TpsTracker implements Listener {

    public static TpsTracker INSTANCE = new TpsTracker();

    private final float[] tickRates = new float[20];
    private int nextIndex = 0;
    private long timeLastTimeUpdate = -1;
    private long timeGameJoined;

    @Subscribe
    public void onPacketReceive(PacketReceiveEvent event) {
        System.out.println("hola");
        if (event.getPacket() instanceof WorldTimeUpdateS2CPacket) {
            long now = System.currentTimeMillis();
            float timeElapsed = (float) (now - timeLastTimeUpdate) / 1000.0F;
            tickRates[nextIndex] = clamp(20.0f / timeElapsed, 0.0f, 20.0f);
            nextIndex = (nextIndex + 1) % tickRates.length;
            timeLastTimeUpdate = now;
        }
    }

    @Subscribe
    public void onGameJoined(GameJoinEvent event) {
        System.out.println("boo!");
        Arrays.fill(tickRates, 0);
        nextIndex = 0;
        timeGameJoined = timeLastTimeUpdate = System.currentTimeMillis();
    }

    public float getTickRate() {
        if (MinecraftClient.getInstance().player == null) return 0;
        if (System.currentTimeMillis() - timeGameJoined < 4000) return 20;

        int numTicks = 0;
        float sumTickRates = 0.0f;
        for (float tickRate : tickRates) {
            if (tickRate > 0) {
                sumTickRates += tickRate;
                numTicks++;
            }
        }
        return sumTickRates / numTicks;
    }

    private float clamp(float value, float min, float max) {
        if (value < min) return min;
        return Math.min(value, max);
    }

}
