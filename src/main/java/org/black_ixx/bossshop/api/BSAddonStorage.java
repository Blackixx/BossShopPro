package org.black_ixx.bossshop.api;

import java.util.List;
import java.util.Set;


public interface BSAddonStorage {


    int TYPE_LOCAL_FILE = 0; //More types might be added in future

    /**
     * Set the path for the config
     * @param path path
     * @param value value
     */
    void set(String path, Object value);

    /**
     * Get a string from the config
     * @param path path
     * @param def default
     * @return string
     */
    String getString(String path, String def);

    /**
     * Get an int from the config
     * @param path path
     * @param def default
     * @return int
     */
    int getInt(String path, int def);

    /**
     * Get a double from the config
     * @param path path
     * @param def default
     * @return double
     */
    double getDouble(String path, double def);

    /**
     * Get a boolean from the config
     * @param path path
     * @param def default
     * @return boolean
     */
    boolean getBoolean(String path, boolean def);

    /**
     * Get a list of strings from the config
     * @param path path
     * @return list of strings
     */
    List<String> getStringList(String path);

    /**
     * Check if a config contains a path
     * @param key path
     * @return contains or not
     */
    boolean containsPath(String key);

    /**
     * List of keys from config
     * @param section path
     * @param deep subkeys or not
     * @return list of keys
     */
    Set<String> listKeys(String section, boolean deep);

    /**
     * Delete all parts from a section
     * @param section the section to delete
     */
    void deleteAll(String section);

    /**
     * Save the addon
     * @return saved or not
     */
    boolean save();

    /**
     * Save the addon async
     */
    void saveAsync();

    /**
     * Copies from file to new file
     * @param source the file to copy from
     * @return new addon storage file
     */
    boolean pasteContentFrom(BSAddonStorage source);


}
