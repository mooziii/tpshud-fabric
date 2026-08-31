package me.obsilabor.tpshud.networking;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record CommonHandshakePayload() implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<CommonHandshakePayload> TYPE =
        new CustomPacketPayload.Type<>(Identifier.parse(Packets.HANDSHAKE));

    public static final StreamCodec<FriendlyByteBuf, CommonHandshakePayload> CODEC =
        StreamCodec.of(
            (buf, payload) -> {},
            buf -> new CommonHandshakePayload()
        );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
