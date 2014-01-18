package com.projectbarks.targetpads;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Utils {
    private Utils() {}
    public static final float gravConstant = 32;

    public static float getLaunchAngle(float deltaX, float deltaY, float initialVelocity, float gravConstant) {
        return (float) Math.atan(
                (Math.pow(initialVelocity, 2) + Math.sqrt(Math.pow(initialVelocity, 4) - gravConstant * (gravConstant
                        * Math.pow(deltaX, 2) + 2 * deltaY * Math.pow(initialVelocity, 2)))) / (gravConstant * initialVelocity)
        );
    }

    public static Vector getLaunchVector(Location start, Location end) {
        Vector baseVector = end.toVector().subtract(start.toVector());
        float launchAngle = getLaunchAngle(baseVector.getBlockX(), baseVector.getBlockZ(), 8, gravConstant);
        baseVector.normalize().multiply(Math.cos(launchAngle));
        baseVector.setZ(Math.sin(launchAngle));
        return baseVector;
    }
}
