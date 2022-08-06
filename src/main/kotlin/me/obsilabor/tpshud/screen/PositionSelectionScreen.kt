package me.obsilabor.tpshud.screen

import me.obsilabor.tpshud.config.ClothConfigManager
import me.obsilabor.tpshud.hud.TpsWidget
import me.obsilabor.tpshud.minecraft
import net.minecraft.client.gui.screen.Screen
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.text.Text
import org.lwjgl.glfw.GLFW.GLFW_MOUSE_BUTTON_1

class PositionSelectionScreen : Screen(Text.of("Position Selection")) {
    private var selected = false
    private var selectedRelX = 0
    private var selectedRelY = 0

    override fun render(matrices: MatrixStack, mouseX: Int, mouseY: Int, tickDelta: Float) {
        renderBackground(matrices)
        val x = ClothConfigManager.config?.x ?: 0
        val y = ClothConfigManager.config?.y ?: 0

        TpsWidget.render(matrices)
        drawRect(matrices, x - 1, y - 1, x + TpsWidget.width, y + textRenderer.fontHeight, -1)
        super.render(matrices, mouseX, mouseY, tickDelta)
    }

    override fun mouseMoved(mouseX: Double, mouseY: Double) {
        if(selected) {
            ClothConfigManager.config?.x = mouseX.toInt() - selectedRelX
            ClothConfigManager.config?.y = mouseY.toInt() - selectedRelY
        }
    }

    override fun mouseClicked(mouseX: Double, mouseY: Double, button: Int): Boolean {
        return when {
            super.mouseClicked(mouseX, mouseY, button) -> true
            button == GLFW_MOUSE_BUTTON_1 -> {
                val x = ClothConfigManager.config?.x ?: 0
                val y = ClothConfigManager.config?.y ?: 0
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
        ClothConfigManager.saveConfigToFile()
        return super.mouseReleased(mouseX, mouseY, button)
    }

    private fun drawRect(matrices: MatrixStack, x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        drawHorizontalLine(matrices, x1, x2, y1, color)
        drawHorizontalLine(matrices, x1, x2, y2, color)
        drawVerticalLine(matrices, x1, y1, y2, color)
        drawVerticalLine(matrices, x2, y1, y2, color)
    }
}