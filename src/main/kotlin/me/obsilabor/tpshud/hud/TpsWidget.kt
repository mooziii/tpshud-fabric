@file:Suppress("unused", "UNUSED_PARAMETER")

package me.obsilabor.tpshud.hud

import com.mojang.blaze3d.systems.RenderSystem
import me.obsilabor.tpshud.TpsTracker
import me.obsilabor.tpshud.config.ConfigManager
import me.obsilabor.tpshud.minecraft
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import org.joml.Matrix4f
import java.awt.Color
import kotlin.math.roundToInt

object TpsWidget {
    fun render(context: DrawContext) {
        val config = ConfigManager.config ?: return
        if(!config.isEnabled) return
        context.matrices.push()
        context.matrices.scale(ConfigManager.config?.scale?:1f, ConfigManager.config?.scale?:1f, 0f)
        if(config.backgroundEnabled) {
            RenderSystem.disableDepthTest()
            fillBackground(context, config.x.toFloat(), config.y.toFloat(), config.x+width.toFloat(), config.y+minecraft.textRenderer.fontHeight+1f, config.backgroundColor, config.backgroundOpacity)
            RenderSystem.enableDepthTest()
        }
        val text = ConfigManager.config?.text ?: "TPS: "
        val widthPartOne = minecraft.textRenderer.getWidth(text)
        context.drawText(minecraft.textRenderer, text, config.x, config.y, config.textColor, config.textShadow)
        context.drawText(minecraft.textRenderer, removeDot(TpsTracker.INSTANCE.tickRate), config.x+widthPartOne, config.y, config.valueTextColor, config.textShadow)
        context.matrices.pop()
    }

    fun renderLivePreview(context: DrawContext, x: Int, y: Int) {
        val config = ConfigManager.config ?: return
        if(!config.isEnabled) return
        context.matrices.push()
        context.matrices.scale(ConfigManager.config?.scale?:1f, ConfigManager.config?.scale?:1f, 0f)
        if(config.backgroundEnabled) {
            RenderSystem.disableDepthTest()
            fillBackground(context, x.toFloat(), y.toFloat(), x+width.toFloat()+7f, y+ minecraft.textRenderer.fontHeight+1f, config.backgroundColor, config.backgroundOpacity)
            RenderSystem.enableDepthTest()
        }
        val text = ConfigManager.config?.text ?: "TPS: "
        val widthPartOne = minecraft.textRenderer.getWidth(text)
        context.drawText(minecraft.textRenderer, text, x, y, config.textColor, config.textShadow)
        context.drawText(minecraft.textRenderer, removeDot(19.89f), x+widthPartOne, y, config.valueTextColor, config.textShadow)
        context.matrices.pop()
    }

    private fun removeDot(tps: Float): String {
        var copy = tps
        if(copy >= 19.79 && ConfigManager.config?.satisfyTpsCount == true) { // show 20 to satisfy the user
            copy = 20.0f
        }
        return if(tps.toString().contains(".")) {
            copy.toString().split(".")[0]
        } else {
            copy.toString()
        }
    }

    val width: Int
        get() = minecraft.textRenderer.getWidth(ConfigManager.config?.text ?: "TPS: ")+minecraft.textRenderer.getWidth(removeDot(TpsTracker.INSTANCE.tickRate))

    private fun fillBackground(drawContext: DrawContext, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, alpha: Float) {
        // Doesn't work / only works without alpha / looks ugly - Due to removal of RenderSystem.disableTexture
        //fill(matrices.peek().positionMatrix, x1, y1, x2, y2, color, alpha)
        val rgb = Color(color)
        drawContext.fill(x1.toInt(), y1.toInt(), x2.toInt(), y2.toInt(), Color(rgb.red, rgb.blue, rgb.green, (alpha*255).roundToInt()).rgb)
    }

    private fun fill(matrix: Matrix4f, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, alpha: Float) {
        val r = (color shr 16 and 255).toFloat() / 255.0f
        val g = (color shr 8 and 255).toFloat() / 255.0f
        val b = (color and 255).toFloat() / 255.0f
        val var10000 = Tessellator.getInstance()
        val bufferBuilder = var10000.buffer
        RenderSystem.setShader { GameRenderer.getPositionTexProgram() }
        RenderSystem.enableBlend()
        //RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        bufferBuilder.vertex(matrix, x1, y2, 0.0f).color(r, g, b, alpha).next()
        bufferBuilder.vertex(matrix, x2, y2, 0.0f).color(r, g, b, alpha).next()
        bufferBuilder.vertex(matrix, x2, y1, 0.0f).color(r, g, b, alpha).next()
        bufferBuilder.vertex(matrix, x1, y1, 0.0f).color(r, g, b, alpha).next()
        val buffer = bufferBuilder.end()
        BufferRenderer.drawWithGlobalProgram(buffer)
        //RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }

}