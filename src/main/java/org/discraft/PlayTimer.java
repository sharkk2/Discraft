package org.discraft;

import java.util.HashMap;
import java.util.UUID;

public class PlayTimer {
    private static final HashMap<UUID, Long> joinTimes = new HashMap<>();

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
}
