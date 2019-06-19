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

    /**
     * Load the settings
     * @param config the config to load from
     */
    public void load(ConfigurationSection config) {
        this.o = read(config);
    }

    /**
     * Read the config
     * @param config the config to read
     * @return config object
     */
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

    /**
     * Update a settings property
     * @param o
     */
    public void update(Object o) {
        //Can be overwritten
    }

    /**
     * Check if object contains value
     * @param input object to check
     * @param value value to check for
     * @return contains or not
     */
    public boolean containsValue(Object input, Object value) {
        if (input == null) {
            if (containsValueAny(value)) {
                return true;
            }
        }
        return isIdentical(o, value);
    }

    /**
     * Check if an object contains any value
     * @param value the value to check
     * @return contains or not
     */
    public boolean containsValueAny(Object value) {
        return isIdentical(o, value);
    }

    /**
     * Get the object
     * @param input the object to get
     * @return object
     */
    public Object getObject(Object input) {
        return o;
    }

    /**
     * Get a string from an object
     * @param input object
     * @return string
     */
    public String getString(Object input) {
        return (String) getObject(input);
    }

    /**
     * Get an int from an object
     * @param input object
     * @return int
     */
    public int getInt(Object input) {
        return (int) getObject(input);
    }

    /**
     * Get a double from an object
     * @param input object
     * @return double
     */
    public double getDouble(Object input) {
        return (double) getObject(input);
    }

    /**
     * Get a string list from an object
     * @param input object
     * @return string list
     */
    @SuppressWarnings("unchecked")
    public List<String> getStringList(Object input) {
        return (List<String>) getObject(input);
    }

    /**
     * Get a boolean from an object
     * @param input object
     * @return boolean
     */
    public boolean getBoolean(Object input) {
        return (boolean) getObject(input);
    }


    /**
     * Check if two objects are identical
     * @param a obj1
     * @param b obj2
     * @return identical or not
     */
    //Can be extended
    public boolean isIdentical(Object a, Object b) {
        if (a instanceof Boolean && b instanceof Boolean) {
            return ((Boolean) a) == ((Boolean) b);
        }
        return a == b;
    }

}
