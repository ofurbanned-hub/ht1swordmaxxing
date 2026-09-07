package com.ht1client.module;

import com.ht1client.HT1Config;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.MathHelper;

import java.util.Random;

public class AimModule {

    private static final Random RANDOM = new Random();

    // Small per-tick jitter so the camera doesn't move with robotic precision
    private static final float JITTER_DEGREES = 0.4f;

    public static void updateAim(ClientPlayerEntity player, Entity target) {
        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        // Aim slightly below eye height, real players rarely aim at the exact eye point
        double dy = (target.getEyeY() - 0.15) - player.getEyeY();
        double horizontalDist = Math.sqrt(dx * dx + dz * dz);

        float desiredYaw = (float) Math.toDegrees(Math.atan2(dz, dx)) - 90f;
        float desiredPitch = (float) -Math.toDegrees(Math.atan2(dy, horizontalDist));

        float yawDelta = MathHelper.wrapDegrees(desiredYaw - player.getYaw());
        float pitchDelta = desiredPitch - player.getPitch();

        yawDelta = MathHelper.clamp(yawDelta, -HT1Config.maxDegreesPerTick, HT1Config.maxDegreesPerTick);
        pitchDelta = MathHelper.clamp(pitchDelta, -HT1Config.maxDegreesPerTick, HT1Config.maxDegreesPerTick);

        float jitterYaw = (RANDOM.nextFloat() - 0.5f) * JITTER_DEGREES;
        float jitterPitch = (RANDOM.nextFloat() - 0.5f) * JITTER_DEGREES;

        player.setYaw(player.getYaw() + yawDelta * HT1Config.turnSmoothing + jitterYaw);
        player.setPitch(MathHelper.clamp(
                player.getPitch() + pitchDelta * HT1Config.turnSmoothing + jitterPitch,
                -90f, 90f
        ));
    }
}
