package com.projectbarks.targetpads;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class TargetPads extends JavaPlugin implements Listener {

    private Config config;
    private Commands commands;

    @Override
    public void onLoad() {
        this.config = new Config(this);
        this.commands = new Commands(config);
    }

    @Override
    public void onEnable() {
        this.config.load();
        this.getCommand("pads").setExecutor(commands);
        this.getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        this.config.save();
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        for (PadData data : this.config.getPads()) {
            Location pLoc = player.getLocation();
            int x = pLoc.getBlockX();
            int y = pLoc.getBlockY();
            int z = pLoc.getBlockZ();
            Location dLoc = data.getCurrent();
            if (pLoc.getWorld().getName().equalsIgnoreCase(dLoc.getWorld().getName())) {
                if (x == dLoc.getBlockX() && y == dLoc.getBlockY() && z == dLoc.getBlockZ()) {
                    player.setVelocity(data.gotoTarget(pLoc));
                    player.playSound(event.getPlayer().getLocation(), Sound.BAT_TAKEOFF, 5.0F, 0.0F);
                    player.playEffect(dLoc, Effect.SMOKE, 0F);
                } 
            }
        }
    }
}