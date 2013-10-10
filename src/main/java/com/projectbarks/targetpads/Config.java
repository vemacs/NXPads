package com.projectbarks.targetpads;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;

/**
 *
 * @author Brandon Barker
 */
public class Config extends AbstractConfig {

    private List<PadData> pads;

    public Config(Plugin plugin) {
        super(plugin, "pads");
        this.pads = new ArrayList<PadData>();
    }

    @Override
    protected void loadConfig() {
        for (String key : this.getConfig().getKeys(false)) {
            Location to = this.stringToLoc(this.getConfig().getString(key + ".target", "0|0|0|world"));
            Location current = this.stringToLoc(this.getConfig().getString(key + ".pad", "0|0|0|world"));
            this.getPads().add(new PadData(to, current));
        }
    }

    @Override
    protected void saveConfig() {
        int i = 0;
        for (PadData data : this.getPads()) {
            this.getConfig().set(i + ".target", locToString(data.getTarget()));
            this.getConfig().set(i + ".pad", locToString(data.getCurrent()));
            i++;
        }
    }

    private String locToString(Location loc) {
        return loc.getX() + ":" + loc.getY() + ":" + loc.getZ() + ":" + loc.getWorld().getName();
    }

    private Location stringToLoc(String string) {
        String[] split = string.split(":");
        Location loc = new Location(Bukkit.getWorld(split[3]), Double.parseDouble(split[0]),
                                    Double.parseDouble(split[1]), Double.parseDouble(split[2]));
        return loc;
    }

    public List<PadData> getPads() {
        return pads;
    }
}
