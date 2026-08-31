package me.obsilabor.tpshud.config;

public class TpsHudConfig {
    public boolean isEnabled = true;
    public int x = 0;
    public int y = 0;
    public int textColor = -1;
    public int valueTextColor = -16287528;
    public float scale = 1.0f;
    public int backgroundColor = -1;
    public float backgroundOpacity = 0.5f;
    public boolean backgroundEnabled = true;
    public boolean satisfyTpsCount = true;
    public boolean textShadow = true;
    public boolean useServerProvidedData = true;
    public boolean askedForServerProvidedData = false;
    public boolean showCompatibilityToast = true;
    public String customText = "";
    public boolean displayModeTps = true;

    public static TpsHudConfig createDefault() {
        return new TpsHudConfig();
    }
}
