package org.losttribe.losttribeParkour;

import org.bukkit.entity.Player;

public class ParkourPlayerData {
    private boolean hasStarted;
    private int checkpointsPassed;  // Tracks which checkpoint they've passed (0 = none, 1 = oak, 2 = dark oak, 3 = spruce)

    public ParkourPlayerData() {
        this.hasStarted = false;
        this.checkpointsPassed = 0;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void startParkour() {
        this.hasStarted = true;
    }

    public boolean hasPassedCheckpoint(int checkpointId) {
        return checkpointsPassed >= checkpointId;
    }

    public void passCheckpoint() {
        if (checkpointsPassed < 3) {
            checkpointsPassed++;
        }
    }

    public void resetProgress() {
        this.hasStarted = false;
        this.checkpointsPassed = 0;
    }

    public boolean hasCompletedAllCheckpoints() {
        return checkpointsPassed == 3;
    }
}
