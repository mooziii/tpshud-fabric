package me.obsilabor.tpshud.hud;

import me.obsilabor.tpshud.TpsTracker;
import me.obsilabor.tpshud.config.ConfigManager;
import me.obsilabor.tpshud.config.TpsHudConfig;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class TpsWidget {

    public static void render(GuiGraphicsExtractor context, DeltaTracker tickCounter) {
        TpsHudConfig config = ConfigManager.getConfig();
        if (config == null || !config.isEnabled) return;

        Minecraft minecraft = Minecraft.getInstance();

        var pose = context.pose();
        pose.pushMatrix();
        pose.scale(config.scale, config.scale, pose);

        if (config.backgroundEnabled) {
            fillBackground(context, config.x, config.y,
                config.x + getWidth(), config.y + minecraft.font.lineHeight,
                config.backgroundColor, config.backgroundOpacity);
        }

        // Get text and value, for MSPT or TPS
        TextAndValue textAndValue = getTextAndValue();

        // Render it
        int widthPartOne = minecraft.font.width(textAndValue.text);
        context.text(minecraft.font, textAndValue.text, config.x, config.y, config.textColor, config.textShadow);
        context.text(minecraft.font, textAndValue.value, config.x + widthPartOne, config.y, config.valueTextColor, config.textShadow);

        pose.popMatrix();
    }

    private static TextAndValue getTextAndValue() {
        TpsHudConfig config = ConfigManager.getConfig();
        if (config == null) return new TextAndValue("TPS: ", "0.00");

        float rawValue = TpsTracker.INSTANCE.getTickTime();
        String text;
        float value;

        if (config.displayModeTps) {
            text = "TPS: ";
            value = convertToTps(rawValue);
        } else {
            text = "MSPT: ";
            value = rawValue;
        }

        if (!config.customText.isEmpty()) {
            text = config.customText;
        }

        // Fallback in case of NaN or weird values
        float safeValue = Float.isNaN(value) || Float.isInfinite(value) ? 0.0f : value;

        String valueStr;
        if (config.satisfyTpsCount) {
            valueStr = String.valueOf(Math.round(safeValue));
        } else {
            valueStr = String.valueOf(BigDecimal.valueOf(safeValue).setScale(2, RoundingMode.HALF_UP).floatValue());
        }

        return new TextAndValue(text, valueStr);
    }

    private static float convertToTps(float mspt) {
        return Math.min(1000f / mspt, 20f);
    }

    public static int getWidth() {
        Minecraft minecraft = Minecraft.getInstance();

        TextAndValue textAndValue = getTextAndValue();
        return minecraft.font.width(textAndValue.text) +
               minecraft.font.width(textAndValue.value);
    }

    private static void fillBackground(GuiGraphicsExtractor guiGraphics, int x1, int y1, int x2, int y2, int color, float alpha) {
        Color rgb = new Color(color);
        int alphaValue = Math.round(alpha * 255);
        Color withAlpha = new Color(rgb.getRed(), rgb.getGreen(), rgb.getBlue(), alphaValue);
        guiGraphics.fill(x1, y1, x2, y2, withAlpha.getRGB());
    }

    private record TextAndValue(String text, String value) {}
}
