package com.projectbarks.targetpads;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

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
        this.config.getPads().clear();
    }
    
    @EventHandler
    public void onMove(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (event.getAction() != Action.PHYSICAL) {
            return;
        }
        Material type = event.getClickedBlock().getType();
        if (!(type == Material.STONE_PLATE || type == Material.WOOD_PLATE
              || type == Material.IRON_PLATE || type == Material.GOLD_PLATE)) {
            return;
        }
        for (PadData data : this.config.getPads()) {
            Location pLoc = event.getClickedBlock().getLocation();
            int x = pLoc.getBlockX();
            int y = pLoc.getBlockY();
            int z = pLoc.getBlockZ();
            Location dLoc = data.getCurrent();
            if (pLoc.getWorld().getName().equalsIgnoreCase(dLoc.getWorld().getName())) {
                if (x == dLoc.getBlockX() && y == dLoc.getBlockY() && z == dLoc.getBlockZ()) {
                    player.setVelocity(data.getVectorToTarget(player.getLocation()));
                    player.playSound(player.getLocation(), Sound.BAT_TAKEOFF, 5.0F, 0.0F);
                    //player.playEffect(dLoc, Effect.SMOKE, 0F);
                    new ConstantTargeter(player, data).runTaskTimer(this, 0, 1L);
                    event.setCancelled(true);
                }
            }
        }
    }
    
    private class ConstantTargeter extends BukkitRunnable {
        
        private PadData data;
        private Player player;
        private int count;
        private int maxCount;
        
        public ConstantTargeter(Player player, PadData data) {
            this.player = player;
            this.data = data;
            count = 0;
            this.maxCount = (int) this.data.getDistance(player.getLocation());
        }
        
        public void run() {
            if (!player.isOnline()) {
                this.cancel();
                return;
            } else if (count >= maxCount) {
                player.teleport(data.getTarget());
                this.cancel();
                return;
            }
            Location pLoc = player.getLocation();
            int x = pLoc.getBlockX();
            int z = pLoc.getBlockZ();
            Location dLoc = data.getTarget();
            if (pLoc.getWorld().getName().equalsIgnoreCase(dLoc.getWorld().getName())) {
                if (x <= dLoc.getBlockX() + 3 && x >= dLoc.getBlockX() - 3 && z <= dLoc.getBlockZ() + 3 && z >= dLoc.getBlockZ() - 3) {
                    player.setVelocity(new Vector(0, 0, 0));
                    this.cancel();
                } else {
                    if (count < Math.round(maxCount / 2)) {
                        player.setVelocity(data.getVectorToTarget(player.getLocation()));
                    }
                }
            }
            count++;
        }
    }
}