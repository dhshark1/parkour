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

    private final Map<Player, ParkourPlayerData> playerData = new HashMap<>();

    private final Material startPlate = Material.STONE_PRESSURE_PLATE;
    private final Material oakPlate = Material.OAK_PRESSURE_PLATE;
    private final Material darkOakPlate = Material.DARK_OAK_PRESSURE_PLATE;
    private final Material sprucePlate = Material.SPRUCE_PRESSURE_PLATE;
    private final Material endPlate = Material.LIGHT_WEIGHTED_PRESSURE_PLATE;

    public ParkourListener(LostTribeParkour plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPressurePlateStep(PlayerInteractEvent event) {
        if (event.getClickedBlock() == null) return;

        Material plateMaterial = event.getClickedBlock().getType();
        Player player = event.getPlayer();
        ParkourPlayerData data = playerData.computeIfAbsent(player, k -> new ParkourPlayerData());

        if (plateMaterial.equals(startPlate) && !data.hasStarted()) {
            startParkour(player, data);
        }

        if (plateMaterial.equals(oakPlate) && data.getCheckpointsPassed() == 0 && data.hasStarted()) {
            passCheckpoint(player, data,  "Oak");
        } else if (plateMaterial.equals(darkOakPlate) && data.getCheckpointsPassed() == 1) {
            passCheckpoint(player, data,  "Dark Oak");
        } else if (plateMaterial.equals(sprucePlate)  && data.getCheckpointsPassed() == 2) {
            passCheckpoint(player, data,  "Spruce");
        }

        if (plateMaterial.equals(endPlate) && data.hasCompletedAllCheckpoints()) {
            endParkour(player, data);
        }
    }

    private void startParkour(Player player, ParkourPlayerData data) {
        data.startParkour();
        player.sendMessage("Parkour started! Reach the checkpoints in order.");
    }

    private void passCheckpoint(Player player, ParkourPlayerData data, String checkpointName) {
        data.passCheckpoint();
        player.sendMessage("You have reached the " + checkpointName + " checkpoint!");
    }

    private void endParkour(Player player, ParkourPlayerData data) {
        player.sendMessage("Congratulations, you completed the parkour!");
        data.resetProgress();
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        int Y_LOCATION_THRESHOLD_FOR_RESET = 50;
        Player player = event.getPlayer();
        ParkourPlayerData data = playerData.get(player);

        if (data != null && data.hasStarted() && player.getLocation().getY() < Y_LOCATION_THRESHOLD_FOR_RESET) {
            data.resetProgress();
            player.sendMessage("You fell below Y=50! Your parkour progress has been reset.");
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playerData.remove(event.getPlayer());
    }
}

