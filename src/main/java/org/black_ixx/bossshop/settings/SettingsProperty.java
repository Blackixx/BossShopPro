package org.black_ixx.bossshop.settings;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;

public class SettingsProperty {

    protected String path;
    protected Object o;
    protected Class<?> type;

    public SettingsProperty(ConfigurationSection config, String path, Class<?> type) {
        this.path = path;
        this.type = type;
        load(config);
    }

    public void load(ConfigurationSection config) {
        this.o = read(config);
    }

    public Object read(ConfigurationSection config) {
        if (type == String.class) {
            return config.getString(path);
        }
        if (type == Integer.class) {
            return config.getInt(path);
        }
        if (type == Double.class) {
            return config.getDouble(path);
        }
        if (type == List.class) {
            return config.getStringList(path);
        }
        if (type == Boolean.class) {
            return config.getBoolean(path);
        }
        return config.get(path);
    }

    public void update(Object o) {
        //Can be overwritten
    }


    public boolean containsValue(Object input, Object value) {
        if (input == null) {
            if (containsValueAny(value)) {
                return true;
            }
        }
        return isIdentical(o, value);
    }

    public boolean containsValueAny(Object value) {
        return isIdentical(o, value);
    }

    public Object getObject(Object input) {
        return o;
    }

    public String getString(Object input) {
        return (String) getObject(input);
    }

    public int getInt(Object input) {
        return (int) getObject(input);
    }

    public double getDouble(Object input) {
        return (double) getObject(input);
    }

    @SuppressWarnings("unchecked")
    public List<String> getStringList(Object input) {
        return (List<String>) getObject(input);
    }

    public boolean getBoolean(Object input) {
        return (boolean) getObject(input);
    }


    //Can be extended
    public boolean isIdentical(Object a, Object b) {
        if (a instanceof Boolean && b instanceof Boolean) {
            return ((Boolean) a) == ((Boolean) b);
        }
        return a == b;
    }

}
