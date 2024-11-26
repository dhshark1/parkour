package org.losttribe.losttribeParkour;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashMap;
import java.util.Map;

public class ParkourListener implements Listener {

    private final LostTribeParkour plugin;

    // Track player progress: maps player to their parkour data
    private final Map<Player, ParkourPlayerData> playerData = new HashMap<>();

    // Define the pressure plates
    private final Material startPlate = Material.STONE_PRESSURE_PLATE;
    private final Material oakPlate = Material.OAK_PRESSURE_PLATE;
    private final Material darkOakPlate = Material.DARK_OAK_PRESSURE_PLATE;
    private final Material sprucePlate = Material.SPRUCE_PRESSURE_PLATE;
    private final Material endPlate = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;

    public ParkourListener(LostTribeParkour plugin) {
        this.plugin = plugin;
    }

    // Player steps on a pressure plate
    @EventHandler
    public void onPressurePlateStep(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Material plateMaterial = event.getClickedBlock().getType();
        Player player = event.getPlayer();
        ParkourPlayerData data = playerData.get(player);

        // If the player is not part of the parkour, ignore the pressure plates
        if (data == null || !data.hasStarted()) {
            return;
        }

        // Handle start plate (stone)
        if (plateMaterial == startPlate) {
            startParkour(player, data);
        }

        // Handle checkpoints (oak, dark oak, spruce)
        if (plateMaterial == oakPlate && !data.hasPassedCheckpoint(1)) {
            passCheckpoint(player, data, 1, "Oak");
        } else if (plateMaterial == darkOakPlate && !data.hasPassedCheckpoint(2)) {
            passCheckpoint(player, data, 2, "Dark Oak");
        } else if (plateMaterial == sprucePlate && !data.hasPassedCheckpoint(3)) {
            passCheckpoint(player, data, 3, "Spruce");
        }

        // Handle end plate (golden)
        if (plateMaterial == endPlate && data.hasCompletedAllCheckpoints()) {
            endParkour(player, data);
        }
    }

    // Handle player starting the parkour
    private void startParkour(Player player, ParkourPlayerData data) {
        data.startParkour();
        player.sendMessage("Parkour started! Reach the checkpoints in order.");
    }

    // Handle player passing a checkpoint
    private void passCheckpoint(Player player, ParkourPlayerData data, int checkpointId, String checkpointName) {
        data.passCheckpoint();
        player.sendMessage("You have reached the " + checkpointName + " checkpoint!");
    }

    // Handle player reaching the end plate (winning)
    private void endParkour(Player player, ParkourPlayerData data) {
        player.sendMessage("Congratulations, you completed the parkour!");
    }

    // Handle player falling below Y=50 (losing)
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        ParkourPlayerData data = playerData.get(player);

        // If the player is in parkour and falls below Y=50, reset their progress
        if (data != null && data.hasStarted() && player.getLocation().getY() < 50) {
            data.resetProgress();
            player.sendMessage("You fell below Y=50! Your parkour progress has been reset.");
        }
    }

    // Handle player quitting the game (remove their progress)
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerData.remove(event.getPlayer());
    }
}

