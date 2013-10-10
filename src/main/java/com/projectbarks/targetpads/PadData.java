package com.projectbarks.targetpads;

import org.bukkit.Location;
import org.bukkit.util.Vector;

/**
 *
 * @author Brandon Barker
 */
public class PadData {

    private Location target;
    private Location current;

    public PadData(Location target, Location current) {
        this.target = target;
        this.current = current;
    }

    public Location getTarget() {
        return target;
    }

    public Location getCurrent() {
        return current;
    }

    public Vector getVector() {
        final double t = 10; //time
        double hzMult = Math.pow(1 / 0.91, t / 2);
        double yVeloc = (3.92 * (1 - Math.pow(0.98, 10))) + (target.getY() - current.getY()) / t;

        Vector vec = target.clone().subtract(current).toVector().multiply(hzMult / t);
        vec.setY(yVeloc);
        vec.multiply(1.01D);

        return vec;
    }

    public Vector getVectorToTarget(Location loc) {
        Vector c = loc.toVector();
        final long t = 20L; //time
        double hzMult = Math.pow(1 / 0.91, t / 2);
        double yVeloc = (3.92 * (1 - Math.pow(0.98, 10))) + (target.getY() - c.getY()) / t;

        Vector vec = target.clone().subtract(c).toVector().multiply(hzMult / t);
        vec.setY(yVeloc);
        vec.multiply(1.01D);

        return vec;
    }

    public double getDistance(Location loc) {
        Location s = loc.subtract(target);
        return Math.sqrt(Math.abs(Math.pow(s.getX(), 2))
                         + Math.abs(Math.pow(s.getY(), 2))
                         + Math.abs(Math.pow(s.getZ(), 2)));
    }

    public void setTarget(Location target) {
        this.target = target;
    }

    public void setCurrent(Location current) {
        this.current = current;
    }
}
