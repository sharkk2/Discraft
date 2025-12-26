package org.discraft;

import java.util.HashMap;
import java.util.UUID;

public class PlayTimer {
    private static final HashMap<UUID, Long> joinTimes = new HashMap<>();
    public static final long startTime = System.currentTimeMillis();

    public void addTimer(UUID playeruuid) {
       joinTimes.put(playeruuid, System.currentTimeMillis());
    }

    public void removeTimer(UUID playeruuid) {
        if (joinTimes.containsKey(playeruuid)) {
            joinTimes.remove(playeruuid);
        }
    }

    public long getPlaytime(UUID playeruuid) {
        return joinTimes.getOrDefault(playeruuid, System.currentTimeMillis());
    }

    public String getPlayTimeFormatted(UUID playeruuid) {
        long joinTime = getPlaytime(playeruuid);
        long currentTime = System.currentTimeMillis();
        long playTimeMillis = currentTime - joinTime;

        long seconds = (playTimeMillis / 1000) % 60;
        long minutes = (playTimeMillis / (1000 * 60)) % 60;
        long hours = (playTimeMillis / (1000 * 60 * 60)) % 24;

        String playtimeFormatted = (hours > 0 ? hours + "h " : "") +
                (minutes > 0 ? minutes + "m " : "") + seconds + "s";


        return playtimeFormatted;
    }

    public String getPlayTimeFormatted(long joinTime) {
        long currentTime = System.currentTimeMillis();
        long playTimeMillis = currentTime - joinTime;

        long seconds = (playTimeMillis / 1000) % 60;
        long minutes = (playTimeMillis / (1000 * 60)) % 60;
        long hours = (playTimeMillis / (1000 * 60 * 60)) % 24;

        String playtimeFormatted = (hours > 0 ? hours + "h " : "") +
                (minutes > 0 ? minutes + "m " : "") + seconds + "s";


        return playtimeFormatted;
    }


}
