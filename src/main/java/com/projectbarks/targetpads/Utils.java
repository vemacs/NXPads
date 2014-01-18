package com.projectbarks.targetpads;

import org.bukkit.Location;
import org.bukkit.util.Vector;

public class Utils {
    private Utils() {}
    public static final float gravConstant = 32;
    public static final float initialVelocity = 5;

    public static float getLaunchAngle(float deltaX, float deltaY, float initialVelocity, float gravConstant, boolean plusMinus) {
        return (float) Math.atan(
                (Math.pow(initialVelocity, 2) + (plusMinus ? 1 : -1) * Math.sqrt(Math.pow(initialVelocity, 4) - gravConstant * (gravConstant
                        * Math.pow(deltaX, 2) + 2 * deltaY * Math.pow(initialVelocity, 2)))) / (gravConstant * initialVelocity)
        );
    }

    public static Vector getLaunchVector(Location start, Location end) {
        Vector baseVector = end.toVector().subtract(start.toVector());
        float length = (float) baseVector.clone().setY(0).length();
        float height = baseVector.getBlockY();
        float launchAngle = getLaunchAngle(length, height, initialVelocity, gravConstant, true);
        return baseVector.clone().setY(0).normalize().multiply(initialVelocity).setY(Math.sin(launchAngle));
    }
}
