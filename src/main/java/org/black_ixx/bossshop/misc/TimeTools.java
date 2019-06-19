package org.black_ixx.bossshop.misc;

import org.black_ixx.bossshop.managers.ClassManager;

public class TimeTools {


    public final static int SECOND = 1;
    public final static int MINUTE = SECOND * 60;
    public final static int HOUR = MINUTE * 60;
    public final static int DAY = HOUR * 24;
    public final static int WEEK = DAY * 7;

    /**
     * Transform a long into time
     * @param time_in_seconds the time to transform
     * @return transformed string
     */
    public static String transform(long time_in_seconds) {
        if (time_in_seconds > WEEK) {
            return ClassManager.manager.getMessageHandler().get("Time.Weeks").replace("%time%", String.valueOf(time_in_seconds / WEEK));
        }
        if (time_in_seconds > DAY) {
            return ClassManager.manager.getMessageHandler().get("Time.Days").replace("%time%", String.valueOf(time_in_seconds / DAY));
        }
        if (time_in_seconds > HOUR) {
            return ClassManager.manager.getMessageHandler().get("Time.Hours").replace("%time%", String.valueOf(time_in_seconds / HOUR));
        }
        if (time_in_seconds > MINUTE) {
            return ClassManager.manager.getMessageHandler().get("Time.Minutes").replace("%time%", String.valueOf(time_in_seconds / MINUTE));
        }
        return ClassManager.manager.getMessageHandler().get("Time.Seconds").replace("%time%", String.valueOf(time_in_seconds));
    }

}
