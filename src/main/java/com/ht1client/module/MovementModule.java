package com.ht1client.module;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;

public class MovementModule {

    private static final double STOP_DISTANCE = 2.5;
    private static final double STRAFE_RADIUS = 3.0;

    public static void updateMovement(MinecraftClient client, Entity target) {
        ClientPlayerEntity player = client.player;
        if (player == null || client.world == null) return;

        double dx = target.getX() - player.getX();
        double dz = target.getZ() - player.getZ();
        double dist = Math.sqrt(dx * dx + dz * dz);

        boolean shouldApproach = dist > STOP_DISTANCE;
        boolean shouldStrafe = dist <= STRAFE_RADIUS;

        // Drive the same input fields real keypresses populate, so movement
        // looks like normal player input rather than a position teleport.
        player.input.movementForward = shouldApproach ? 1f : 0f;
        player.input.movementSideways = shouldStrafe
                ? (float) Math.sin(client.world.getTime() * 0.1)
                : 0f;

        // Small periodic jump to clear ledges/slabs and look like a real player
        // reacting to terrain rather than gliding through it.
        if (player.horizontalCollision && client.world.getTime() % 20 == 0) {
            player.jump();
        }
    }

    public static void stop(ClientPlayerEntity player) {
        player.input.movementForward = 0f;
        player.input.movementSideways = 0f;
    }
}
