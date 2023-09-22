package me.obsilabor.tpshud.screen

import me.obsilabor.tpshud.config.ConfigManager
import me.obsilabor.tpshud.hud.TpsWidget
import me.obsilabor.tpshud.minecraft
import net.minecraft.client.gui.DrawContext
import net.minecraft.client.gui.screen.Screen
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1

class PositionSelectionScreen(private val parent: Screen? = null) : Screen(Text.of("Position Selection")) {
    private var selected = false
    private var selectedRelX = 0
    private var selectedRelY = 0

    override fun render(context: DrawContext, mouseX: Int, mouseY: Int, tickDelta: Float) {
        renderBackground(context, mouseX, mouseY, tickDelta)
        val x = ConfigManager.config?.x ?: 0
        val y = ConfigManager.config?.y ?: 0

        TpsWidget.render(context)
        context.matrices.push()
        context.matrices.scale(ConfigManager.config?.scale ?: 1.0F, ConfigManager.config?.scale ?: 1.0F, 0.0F)
        drawRect(context, x - 1, y - 1, x + TpsWidget.width, y + textRenderer.fontHeight, -1)
        context.matrices.pop()
        super.render(context, mouseX, mouseY, tickDelta)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        if(selected) {
            ConfigManager.config?.x = mouseX.toInt() - selectedRelX
            ConfigManager.config?.y = mouseY.toInt() - selectedRelY
        }
    }

    override fun close() {
        super.close()
        minecraft.setScreen(parent ?: return)
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return when {
            super.mouseClicked(mouseX, mouseY, button) -> true
            button == GLFW_MOUSE_BUTTON_1 -> {
                val x = ConfigManager.config?.x ?: 0
                val y = ConfigManager.config?.y ?: 0
                if (mouseX >= x && mouseX <= x + TpsWidget.width && mouseY >= y && mouseY <= y + minecraft.textRenderer.fontHeight) {
                    selected = true
                    selectedRelX = mouseX.toInt() - x
                    selectedRelY = mouseY.toInt() - y
                }
                false
            }
            else -> false
        }
    }

    override fun mouseReleased(mouseX: Double, mouseY: Double, button: Int): Boolean {
        selected = false
        ConfigManager.saveConfigToFile()
        return super.mouseReleased(mouseX, mouseY, button)
    }

    private fun drawRect(context: DrawContext, x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        context.drawHorizontalLine(x1, x2, y1, color)
        context.drawHorizontalLine(x1, x2, y2, color)
        context.drawVerticalLine(x1, y1, y2, color)
        context.drawVerticalLine(x2, y1, y2, color)
    }
}