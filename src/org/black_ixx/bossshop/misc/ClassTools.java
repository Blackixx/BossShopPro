package org.black_ixx.bossshop.misc;

public class ClassTools {


    public static boolean classExists(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }


}
