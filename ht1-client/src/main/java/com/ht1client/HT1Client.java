package com.ht1client;

import com.ht1client.gui.HT1Screen;
import com.ht1client.module.CombatModule;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class HT1Client implements ClientModInitializer {

    private static KeyBinding toggleUiKey;

    @Override
    public void onInitializeClient() {
        // Right Shift opens/closes the toggle panel. Chosen because it's not
        // bound to anything by default and doesn't collide with movement/hotbar keys.
        toggleUiKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.ht1client.toggle_ui",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT,
                "category.ht1client.main"
        ));

        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
    }

    private void onTick(MinecraftClient client) {
        // Handle the keybind first so opening the UI doesn't get delayed by combat logic
        while (toggleUiKey.wasPressed()) {
            if (client.currentScreen instanceof HT1Screen) {
                client.setScreen(null);
            } else if (client.currentScreen == null) {
                client.setScreen(new HT1Screen());
            }
        }

        CombatModule.onClientTick(client);
    }
}
