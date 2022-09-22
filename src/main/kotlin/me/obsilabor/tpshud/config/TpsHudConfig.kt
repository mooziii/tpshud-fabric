package me.obsilabor.tpshud.config

import kotlinx.serialization.Serializable

@Serializable
data class TpsHudConfig(
    var isEnabled: Boolean,
    var x: Int,
    var y: Int,
    var textColor: Int,
    var valueTextColor: Int,
    var scale: Float,
    var backgroundColor: Int,
    var backgroundOpacity: Float,
    var backgroundEnabled: Boolean,
    var satisfyTpsCount: Boolean = true,
    var textShadow: Boolean = true,
    var useServerProvidedData: Boolean? = true,
    var askedForServerProvidedData: Boolean? = false,
    var showCompatibilityToast: Boolean? = true
) {
    companion object {
        val DEFAULT = TpsHudConfig(
            true,
            0,
            0,
            16777215,
            8904424,
            1f,
            16777215,
            0.5f,
            true,
            true,
            true,
            true,
            false,
            true
        )
    }
}