package org.black_ixx.bossshop.pointsystem;

import java.util.LinkedHashMap;

public class BSPointsAPI {
    private static LinkedHashMap<String, BSPointsPlugin> interfaces = new LinkedHashMap<String, BSPointsPlugin>();

    public static void register(BSPointsPlugin points) {
        interfaces.put(points.getName(), points);
    }

    public static BSPointsPlugin get(String name) {
        if (name != null) {
            BSPointsPlugin p = interfaces.get(name);
            if (p != null) {
                return p;
            }
            for (BSPointsPlugin api : interfaces.values()) {
                if (api.getName().equalsIgnoreCase(name)) {
                    return api;
                }
            }
            for (BSPointsPlugin api : interfaces.values()) {
                for (String nickname : api.getNicknames()) {
                    if (nickname.equalsIgnoreCase(name)) {
                        return api;
                    }
                }
            }
        }
        return null;
    }

    public static BSPointsPlugin getFirstAvailable() {
        for (BSPointsPlugin api : interfaces.values()) {
            if (api.isAvailable()) {
                return api;
            }
        }
        return null;
    }
}
