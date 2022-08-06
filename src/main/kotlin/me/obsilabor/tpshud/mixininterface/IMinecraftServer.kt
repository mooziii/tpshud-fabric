package me.obsilabor.tpshud.mixininterface

/**
 * Not the best method to track tps server side
 */
interface IMinecraftServer {
    val tps: Double

    fun setTPS(tps: Double)
}