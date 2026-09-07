package com.ht1client.module;

import com.ht1client.HT1Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.util.Hand;

import java.util.Comparator;
import java.util.Random;

public class CombatModule {

    private static final Random RANDOM = new Random();

    // Tracks the current target so a reaction delay only applies to *new* targets
    private static LivingEntity currentTarget = null;
    private static long targetAcquiredTick = -1;
    private static long reactionDelayTicks = 0;

    public static void onClientTick(MinecraftClient client) {
        if (client.player == null || client.world == null) return;
        if (!HT1Config.aiModeEnabled) {
            resetTargetState();
            return;
        }

        LivingEntity target = findNearestHostile(client);

        if (target == null) {
            resetTargetState();
            MovementModule.stop(client.player);
            return;
        }

        // New target acquired -> roll a human-like reaction delay before reacting
        if (target != currentTarget) {
            currentTarget = target;
            targetAcquiredTick = client.world.getTime();
            reactionDelayTicks = 2 + RANDOM.nextInt(5); // ~100-300ms at 20 tps
        }

        boolean reactionElapsed = (client.world.getTime() - targetAcquiredTick) >= reactionDelayTicks;
        if (!reactionElapsed) {
            return; // haven't "noticed" the target yet
        }

        if (HT1Config.autoAimEnabled) {
            AimModule.updateAim(client.player, target);
        }

        if (HT1Config.autoMoveEnabled) {
            MovementModule.updateMovement(client, target);
        }

        if (HT1Config.autoAttackEnabled) {
            tryAttack(client, target);
        }
    }

    private static void tryAttack(MinecraftClient client, LivingEntity target) {
        float cooldownProgress = client.player.getAttackCooldownProgress(0.5f);
        double distance = client.player.distanceTo(target);

        if (cooldownProgress >= 1.0f && distance <= HT1Config.reachDistance) {
            client.interactionManager.attackEntity(client.player, target);
            client.player.swingHand(Hand.MAIN_HAND);
        }
    }

    private static LivingEntity findNearestHostile(MinecraftClient client) {
        return client.world.getEntitiesByClass(
                HostileEntity.class,
                client.player.getBoundingBox().expand(HT1Config.reachDistance + 5.0),
                e -> e.isAlive() && client.player.canSee(e)
        ).stream()
                .min(Comparator.comparingDouble(e -> e.squaredDistanceTo(client.player)))
                .orElse(null);
    }

    private static void resetTargetState() {
        currentTarget = null;
        targetAcquiredTick = -1;
        reactionDelayTicks = 0;
    }
}
