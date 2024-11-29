package org.losttribe.losttribeParkour;

import org.bukkit.entity.Player;

public class ParkourPlayerData {
    private boolean hasStarted;
    private int checkpointsPassed;

    public ParkourPlayerData() {
        this.hasStarted = false;
        this.checkpointsPassed = 0;
    }

    public int getCheckpointsPassed() {
        return checkpointsPassed;
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public void startParkour() {
        this.hasStarted = true;
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
