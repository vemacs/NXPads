package com.projectbarks.targetpads;

import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author Brandon Barker
 */
public class Commands implements CommandExecutor {

    private Map<String, PadData> unsavedPads;
    private Config config;

    public Commands(Config config) {
        this.unsavedPads = new HashMap<String, PadData>();
        this.config = config;
    }

    public boolean onCommand(CommandSender cs, Command cmd, String string, String[] args) {
        if (cmd.getName().equalsIgnoreCase("pads") && cs instanceof Player) {
            if (args.length >= 1) {
                String function = args[0].toLowerCase();
                Player p = (Player) cs;
                PadData data;
                if (this.unsavedPads.containsKey(p.getName())) {
                    data = this.unsavedPads.get(p.getName());
                } else {
                    data = new PadData(p.getLocation(), p.getLocation());
                }
                if (function.equals("pos1")) {
                    data.setCurrent(p.getLocation());
                    cs.sendMessage("Position one set");
                } else if (function.equals("pos2")) {
                    data.setTarget(p.getLocation());
                    cs.sendMessage("Position two set");
                } else if (function.equals("save")) {
                    config.getPads().add(data);
                    cs.sendMessage("Pad added");
                    unsavedPads.remove(cs.getName());
                    config.save();
                    return true;
                } else if (function.equals("del") || function.equals("delete")) {
                    if (args.length >= 2) {
                        try {
                            int id = Integer.parseInt(args[1]);
                            if (id >= 0 && id < config.getPads().size()) {
                                config.getPads().remove(id);
                                cs.sendMessage("Pad " + id + " was deleted");
                            } else {
                                cs.sendMessage("Id must be from 0 to " + (config.getPads().size() - 1));
                            }
                        } catch (NumberFormatException numberFormatException) {
                            cs.sendMessage("Id must be numeric");
                            return true;
                        }
                    } else {
                        cs.sendMessage("/pads del [id]");
                    }
                } else if (function.equals("list")) {
                    cs.sendMessage("List- [id]: [cords]");
                    int i = 0;
                    for (PadData d : config.getPads()) {
                        Location c = d.getCurrent();
                        cs.sendMessage("[" + i + "]:" + c.getBlockX() + ", " + c.getBlockY() + ", " + c.getBlockZ());
                        i++;
                    }
                } else {
                    cs.sendMessage("/pads [pos1|pos2|save|delete|list]");
                    return true;
                }
                this.unsavedPads.put(p.getName(), data);
            } else {
                cs.sendMessage("/pads [pos1|pos2|save|delete|list]");
            }
            return true;
        }
        return false;
    }
}
