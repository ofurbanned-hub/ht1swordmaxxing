package com.ht1client;

/**
 * Central place for every toggle the UI screen and modules read from.
 * Kept as simple static fields so the GUI buttons can flip them directly.
 */
public class HT1Config {

    // Master switch — flips every module off at once regardless of their own state
    public static boolean aiModeEnabled = false;

    // Individual module toggles (only matter while aiModeEnabled == true)
    public static boolean autoAimEnabled = true;
    public static boolean autoMoveEnabled = true;
    public static boolean autoAttackEnabled = true;

    // Tunables exposed so you can nudge them from the UI later if you want
    public static double reachDistance = 3.0;
    public static float turnSmoothing = 0.35f;   // lower = laggier/more human, higher = snappier
    public static float maxDegreesPerTick = 12f;  // caps how far the head can turn in one tick

    private HT1Config() {}
}
