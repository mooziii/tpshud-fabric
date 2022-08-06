package me.obsilabor.tpshud.hud

import com.mojang.blaze3d.systems.RenderSystem
import me.obsilabor.tpshud.TpsTracker
import me.obsilabor.tpshud.config.ClothConfigManager
import me.obsilabor.tpshud.minecraft
import net.minecraft.client.gui.DrawableHelper
import net.minecraft.client.render.*
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Matrix4f

object TpsWidget : DrawableHelper() {
    fun render(matrices: MatrixStack) {
        val config = ClothConfigManager.config ?: return
        if(!config.isEnabled) return
        matrices.push()
        matrices.scale(ClothConfigManager.config?.scale?:1f, ClothConfigManager.config?.scale?:1f, 0f)
        if(config.backgroundEnabled) {
            RenderSystem.disableDepthTest()
            fillBackground(matrices, config.x.toFloat(), config.y.toFloat(), config.x+width.toFloat(), config.y+minecraft.textRenderer.fontHeight+1f, config.backgroundColor, config.backgroundOpacity)
            RenderSystem.enableDepthTest()
        }
        val widthPartOne = minecraft.textRenderer.getWidth("TPS: ")
        if(config.textShadow) {
            minecraft.textRenderer.drawWithShadow(matrices, "TPS: ", config.x.toFloat(), config.y.toFloat(), config.textColor)
            minecraft.textRenderer.drawWithShadow(matrices, removeDot(TpsTracker.INSTANCE.tickRate), config.x+widthPartOne.toFloat(), config.y.toFloat(), config.valueTextColor)+1
        } else {
            minecraft.textRenderer.draw(matrices, "TPS: ", config.x.toFloat(), config.y.toFloat(), config.textColor)
            minecraft.textRenderer.draw(matrices, removeDot(TpsTracker.INSTANCE.tickRate), config.x+widthPartOne.toFloat(), config.y.toFloat(), config.valueTextColor)+1
        }
        matrices.pop()
    }

    private fun removeDot(tps: Float): String {
        var copy = tps
        if(copy >= 19.79 && ClothConfigManager.config?.satisfyTpsCount == true) { // show 20 to satisfy the user
            copy = 20.0f
        }
        return if(tps.toString().contains(".")) {
            copy.toString().split(".")[0]
        } else {
            copy.toString()
        }
    }

    val width: Int
        get() = minecraft.textRenderer.getWidth("TPS: ")+minecraft.textRenderer.getWidth(removeDot(TpsTracker.INSTANCE.tickRate))

    private fun fillBackground(matrices: MatrixStack, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, alpha: Float) {
        fill(matrices.peek().positionMatrix, x1, y1, x2, y2, color, alpha)
    }

    private fun fill(matrix: Matrix4f, x1: Float, y1: Float, x2: Float, y2: Float, color: Int, alpha: Float) {
        val r = (color shr 16 and 255).toFloat() / 255.0f
        val g = (color shr 8 and 255).toFloat() / 255.0f
        val b = (color and 255).toFloat() / 255.0f
        val var10000 = Tessellator.getInstance()
        val bufferBuilder = var10000.buffer
        RenderSystem.setShader { GameRenderer.getPositionColorShader() }
        RenderSystem.enableBlend()
        RenderSystem.disableTexture()
        RenderSystem.defaultBlendFunc()
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR)
        bufferBuilder.vertex(matrix, x1, y2, 0.0f).color(r, g, b, alpha).next()
        bufferBuilder.vertex(matrix, x2, y2, 0.0f).color(r, g, b, alpha).next()
        bufferBuilder.vertex(matrix, x2, y1, 0.0f).color(r, g, b, alpha).next()
        bufferBuilder.vertex(matrix, x1, y1, 0.0f).color(r, g, b, alpha).next()
        val buffer = bufferBuilder.end()
        BufferRenderer.drawWithShader(buffer)
        RenderSystem.enableTexture()
        RenderSystem.disableBlend()
    }
}