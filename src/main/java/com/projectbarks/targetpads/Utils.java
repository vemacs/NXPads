package com.projectbarks.targetpads;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Utils {
    private Utils() {}
    public static final float gravConstant = 32;
    public static final float initialVelocity = 3;

    public static float getLaunchAngle(float deltaX, float deltaY, float initialVelocity, float gravConstant, boolean plusMinus) {
        return (float) Math.atan(
                (Math.pow(initialVelocity, 2) + (plusMinus ? 1 : -1) * Math.sqrt(Math.pow(initialVelocity, 4) - gravConstant * (gravConstant
                        * Math.pow(deltaX, 2) + 2 * deltaY * Math.pow(initialVelocity, 2)))) / (gravConstant * initialVelocity)
        );
    }

    public static Vector getLaunchVector(Location start, Location end) {
        Vector baseVector = end.toVector().subtract(start.toVector());
        Bukkit.getLogger().info("Start vector: " + baseVector.toString());
        float launchAngle = getLaunchAngle(baseVector.getBlockX(), baseVector.getBlockY(), initialVelocity, gravConstant, true);
        Bukkit.getLogger().info("Angle: " + launchAngle + "");
        baseVector.setY(0);
        baseVector.normalize().multiply(Math.cos(launchAngle));
        baseVector.setY(Math.sin(launchAngle));
        baseVector.normalize().multiply(initialVelocity);
        Bukkit.getLogger().info("Velocity: " + baseVector.toString());
        return baseVector;
    }
}
