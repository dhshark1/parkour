package org.losttribe.losttribeParkour;

import org.bukkit.plugin.java.JavaPlugin;

public class LostTribeParkour extends JavaPlugin {

    @Override
    public void onEnable() {
        getLogger().info("LostTribeParkour Plugin Enabled!");
        // Register event listeners
        getServer().getPluginManager().registerEvents(new ParkourListener(this), this);
    }

    @Override
    public void onDisable() {
        getLogger().info("LostTribeParkour Plugin Disabled!");
    }
}

