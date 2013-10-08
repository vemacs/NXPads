package com.projectbarks.targetpads;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

/**
 *
 * @author Brandon Barker
 */
public class TimedLauncher extends BukkitRunnable {

    private static List<String> canMove = new ArrayList<String>();

    public static boolean canMove(OfflinePlayer player) {
        return !canMove.contains(player.getName());
    }
    private List<TimerDataBlock> players;

    public TimedLauncher() {
        this.players = new ArrayList<TimerDataBlock>();
    }

    public void run() {
        TimerDataBlock remove = null;
        for (TimerDataBlock data : players) {
            if (data.getTimeStamp() <= System.currentTimeMillis()) {
                canMove.remove(data.getPlayer().getName());
                if (data.getPlayer().isOnline()) {
                    Player player = (Player) data.getPlayer();

                }
                remove = data;
            }
        }
        if (remove != null) {
            players.remove(remove);
        }
    }

    public void addPlayer(Player player, PadData data) {
        players.add(new TimerDataBlock(System.currentTimeMillis() + 1000, player, data));
        canMove.add(player.getName());
    }
    
    public boolean containsPlayer(Player player) {
        for (TimerDataBlock data : players) {
            if (data.getPlayer().getName().equalsIgnoreCase(player.getName())) {
                return true;
            }
        }
        return false;
    }

    protected class TimerDataBlock {

        private long timeStamp;
        private OfflinePlayer player;
        private PadData data;

        protected TimerDataBlock(Long timeStamp, OfflinePlayer player, PadData data) {
            this.timeStamp = timeStamp;
            this.player = player;
            this.data = data;
        }

        public long getTimeStamp() {
            return timeStamp;
        }

        public OfflinePlayer getPlayer() {
            return player;
        }

        public PadData getData() {
            return data;
        }
    }
}
