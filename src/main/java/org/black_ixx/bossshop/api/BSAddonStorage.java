package org.black_ixx.bossshop.api;

import java.util.List;
import java.util.Set;


public interface BSAddonStorage {


    int TYPE_LOCAL_FILE = 0; //More types might be added in future

    void set(String path, Object value);

    String getString(String path, String def);

    int getInt(String path, int def);

    double getDouble(String path, double def);

    boolean getBoolean(String path, boolean def);

    List<String> getStringList(String path);

    boolean containsPath(String key);

    Set<String> listKeys(String section, boolean deep);

    void deleteAll(String section);

    boolean save();

    void saveAsync();


    boolean pasteContentFrom(BSAddonStorage source);


}
