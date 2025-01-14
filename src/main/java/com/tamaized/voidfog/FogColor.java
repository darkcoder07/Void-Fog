package com.tamaized.voidfog;

import com.tamaized.voidfog.api.Voidable;

import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;

public class FogColor {

    private double brightness;

    public double getFogBrightness(ClientWorld world, Entity entity, float delta) {
        if (entity.hasVehicle()) {
            entity = entity.getRootVehicle();
        }

        double prevBrightness = brightness;
        brightness = computeBrightness(world, entity, delta);
        return MathHelper.lerp(delta / (brightness > prevBrightness ? 10 : 2), prevBrightness, brightness);
    }

    private double computeBrightness(ClientWorld world, Entity entity, float delta) {

        if (!VoidFog.config.enabled) {
            return 1;
        }

        Voidable voidable = Voidable.of(world);

        if (voidable.isVoidFogDisabled(entity, world)) {
            return 1;
        }

        double yPosition = MathHelper.lerp(delta, entity.prevY, entity.getY());
        double brightness = yPosition * world.getLevelProperties().getHorizonShadingRatio();

        float light = entity.world.getLightLevel(LightType.SKY, entity.getBlockPos()) / 15F;

        brightness *= light;

        if (brightness >= 1) {
            return 1;
        }

        return Math.pow(Math.max(0, brightness), 3);
    }
}
