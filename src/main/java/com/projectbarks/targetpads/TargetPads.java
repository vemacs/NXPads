package com.projectbarks.targetpads;

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
    private TimedLauncher timedL;

    @Override
    public void onLoad() {
        this.config = new Config(this);
        this.commands = new Commands(config);
        this.timedL = new TimedLauncher();
    }

    @Override
    public void onEnable() {
        timedL.runTaskTimer(this, 0L, 1L);
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
        if (!TimedLauncher.canMove(player)) {
            event.setCancelled(true);
            return;
        }

        for (PadData data : this.config.getPads()) {
            Location pLoc = event.getTo();
            int x = (int) Math.round(pLoc.getX());
            int y = (int) Math.round(pLoc.getY());
            int z = (int) Math.round(pLoc.getZ());
            Location dLoc = data.getCurrent();
            if (pLoc.getWorld().getName().equalsIgnoreCase(dLoc.getWorld().getName())) {
                if (x == dLoc.getBlockX() && y == dLoc.getBlockY() && z == dLoc.getBlockZ()) {
                    player.setVelocity(data.getVectorToTarget(player.getLocation()));
                    player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 5.0F, 0.0F);
                    //player.playEffect(dLoc, Effect.SMOKE, 0F);
                }
            }
        }
    }
}