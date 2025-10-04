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
    var showCompatibilityToast: Boolean? = true,
    var customText: String,
    var displayModeTps: Boolean = false
) {
    companion object {
        val DEFAULT = TpsHudConfig(
            true,
            0,
            0,
            -1,
            -16261400,
            1f,
            -1,
            0.5f,
            true,
            true,
            true,
            true,
            false,
            true,
            "",
            true
        )
    }
}