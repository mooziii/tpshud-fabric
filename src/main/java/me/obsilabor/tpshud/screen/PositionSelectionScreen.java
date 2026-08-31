package me.obsilabor.tpshud.screen;

import me.obsilabor.tpshud.config.ConfigManager;
import me.obsilabor.tpshud.config.TpsHudConfig;
import me.obsilabor.tpshud.hud.TpsWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class PositionSelectionScreen extends Screen {
    private final Screen parent;
    private boolean selected = false;
    private int selectedRelX = 0;
    private int selectedRelY = 0;
    private double currentMouseX = 0.0;
    private double currentMouseY = 0.0;

    public PositionSelectionScreen(Screen parent) {
        super(Component.literal("Position Selection"));
        this.parent = parent;
    }


    @Override
    public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a) {
        TpsHudConfig config = ConfigManager.getConfig();
        int x = config.x;
        int y = config.y;

        TpsWidget.render(graphics, null);
        var pose = graphics.pose();
        pose.pushMatrix();
        pose.scale(config.scale, config.scale, pose);
        drawRect(graphics, x - 1, y - 1, x + TpsWidget.getWidth(), y + font.lineHeight, 0xFFFFFFFF);
        pose.popMatrix();
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        if (selected) {
            TpsHudConfig config = ConfigManager.getConfig();
            config.x = (int) (mouseX - selectedRelX);
            config.y = (int) (mouseY - selectedRelY);
        }
        currentMouseX = mouseX;
        currentMouseY = mouseY;
    }

    @Override
    public void onClose() {
        super.onClose();
        minecraft.setScreenAndShow(parent);
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent event, boolean doubleClick) {
        TpsHudConfig config = ConfigManager.getConfig();
        int x = config.x;
        int y = config.y;
        Minecraft minecraft = Minecraft.getInstance();

        if (currentMouseX >= x && currentMouseX <= x + TpsWidget.getWidth() &&
                currentMouseY >= y && currentMouseY <= y + minecraft.font.lineHeight) {
            selected = true;
            selectedRelX = (int) (currentMouseX - x);
            selectedRelY = (int) (currentMouseY - y);
        }
        return false;
    }

    @Override
    public boolean mouseReleased(@NonNull MouseButtonEvent event) {
        selected = false;
        ConfigManager.saveConfig();
        return super.mouseReleased(event);
    }

    private void drawRect(GuiGraphicsExtractor context, int x1, int y1, int x2, int y2, int color) {
        context.horizontalLine(x1, x2, y1, color);
        context.horizontalLine(x1, x2, y2, color);
        context.verticalLine(x1, y1, y2, color);
        context.verticalLine(x2, y1, y2, color);
    }
}
