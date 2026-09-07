package com.ht1client.gui;

import com.ht1client.HT1Config;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

/**
 * Lightweight overlay-style screen. Doesn't pause the game (see HT1Client
 * where it's opened) so you can pop it up mid-recording without freezing
 * the world, tweak toggles, and close it again with Right Shift.
 */
public class HT1Screen extends Screen {

    public HT1Screen() {
        super(Text.literal("HT1 Client"));
    }

    @Override
    protected void init() {
        int centerX = this.width / 2;
        int startY = this.height / 2 - 70;
        int buttonWidth = 200;
        int buttonHeight = 20;
        int spacing = 24;

        addToggleButton(centerX, startY, buttonWidth, buttonHeight,
                "AI Mode", () -> HT1Config.aiModeEnabled,
                v -> HT1Config.aiModeEnabled = v);

        addToggleButton(centerX, startY + spacing, buttonWidth, buttonHeight,
                "Auto Aim", () -> HT1Config.autoAimEnabled,
                v -> HT1Config.autoAimEnabled = v);

        addToggleButton(centerX, startY + spacing * 2, buttonWidth, buttonHeight,
                "Auto Move", () -> HT1Config.autoMoveEnabled,
                v -> HT1Config.autoMoveEnabled = v);

        addToggleButton(centerX, startY + spacing * 3, buttonWidth, buttonHeight,
                "Auto Attack", () -> HT1Config.autoAttackEnabled,
                v -> HT1Config.autoAttackEnabled = v);

        this.addDrawableChild(ButtonWidget.builder(
                Text.literal("Close"),
                btn -> this.close()
        ).dimensions(centerX - buttonWidth / 2, startY + spacing * 4 + 10, buttonWidth, buttonHeight).build());
    }

    private void addToggleButton(int centerX, int y, int width, int height,
                                  String label, java.util.function.Supplier<Boolean> getter,
                                  java.util.function.Consumer<Boolean> setter) {
        this.addDrawableChild(ButtonWidget.builder(
                labelWithState(label, getter.get()),
                btn -> {
                    boolean newValue = !getter.get();
                    setter.accept(newValue);
                    btn.setMessage(labelWithState(label, newValue));
                }
        ).dimensions(centerX - width / 2, y, width, height).build());
    }

    private Text labelWithState(String label, boolean enabled) {
        String state = enabled ? "ON" : "OFF";
        return Text.literal(label + ": " + state);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // No dark background dim -> stays unobtrusive if you screenshot mid-toggle
        super.render(context, mouseX, mouseY, delta);
        context.drawCenteredTextWithShadow(
                this.textRenderer,
                Text.literal("HT1 Client — Right Shift to close"),
                this.width / 2, this.height / 2 - 95, 0xFFFFFF
        );
    }

    @Override
    public boolean shouldPause() {
        return false; // keep the world running while the panel is open
    }
}
