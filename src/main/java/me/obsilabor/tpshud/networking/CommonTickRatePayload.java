package me.obsilabor.tpshud.networking;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;

public record CommonTickRatePayload(double tickRate, boolean convertedFromTps) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CommonTickRatePayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.parse(Packets.TPS));

    public static final StreamCodec<RegistryFriendlyByteBuf, CommonTickRatePayload> CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.DOUBLE, CommonTickRatePayload::tickRate,
                    ByteBufCodecs.BOOL, CommonTickRatePayload::convertedFromTps,
                    CommonTickRatePayload::new
            );

    @Override
    public @NonNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
