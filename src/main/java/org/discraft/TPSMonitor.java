package org.discraft;

public class TPSMonitor implements Runnable {
    private static long lastTick = System.currentTimeMillis();
    private static double tps = 20.0;

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        long diff = now - lastTick;
        lastTick = now;

        double tickTime = diff / 50.0;
        tps = Math.min(20.0, 20.0 / Math.max(1.0, tickTime));
    }

    public static double getTPS() {
        return tps;
    }
}