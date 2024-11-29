package org.losttribe.losttribeParkour;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Bukkit;

public class LostTribeParkour extends JavaPlugin {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(new ParkourListener(this), this);

    }

    @Override
    public void onDisable() {
    }
}

