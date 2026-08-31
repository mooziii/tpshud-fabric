package me.obsilabor.tpshud.server;

import io.netty.buffer.ByteBufUtil;
import me.obsilabor.tpshud.networking.CommonHandshakePayload;
import me.obsilabor.tpshud.networking.CommonTickRatePayload;
import me.obsilabor.tpshud.networking.Packets;
import net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.ByteBuffer;

public class TpsHudPaperServer extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        // Send TPS updates every second.
        Bukkit.getScheduler().runTaskTimer(this, () -> {
            double mspt = Bukkit.getServer().getTickTimes()[0] / 1000000.0;

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendPluginMessage(this, Packets.TPS, ByteBuffer.allocate(9).putDouble(mspt).array());
            }
        }, 20L, 20L);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        // Send handshake after 1 second.
        Bukkit.getScheduler().runTaskLater(this, () -> {
            player.sendPluginMessage(this, Packets.HANDSHAKE, new byte[0]);
        }, 20L);
    }
}
