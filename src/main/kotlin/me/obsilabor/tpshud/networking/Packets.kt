package me.obsilabor.tpshud.networking

import net.minecraft.util.Identifier

object Packets {
    val HANDSHAKE = Identifier("tpshud", "handshake")
    const val HANDSHAKE_STRING = "tpshud:handshake"
    val TPS = Identifier("tpshud", "tps")
    const val TPS_STRING = "tpshud:tps"
}