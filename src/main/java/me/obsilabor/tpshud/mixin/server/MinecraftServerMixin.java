package me.obsilabor.tpshud.mixin.server;

import me.obsilabor.tpshud.mixininterface.IMinecraftServer;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import java.util.function.BooleanSupplier;

/**
 * Not the best method to track tps server side
 */
@Mixin(MinecraftServer.class)
public abstract class MinecraftServerMixin implements IMinecraftServer {
    private double tps;

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(BooleanSupplier booleanSupplier, CallbackInfo ci) {
        tps++;
    }

    @Override
    public double getTps() {
        return tps;
    }

    @Override
    public void setTPS(double tps) {
        this.tps = tps;
    }
}
