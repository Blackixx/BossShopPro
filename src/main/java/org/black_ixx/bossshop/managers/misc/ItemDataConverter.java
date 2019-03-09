package org.black_ixx.bossshop.managers.misc;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import java.util.EnumSet;

@SuppressWarnings("deprecation")
public class ItemDataConverter {

    public static Material convertMaterial(Material m, byte data) {
        return Bukkit.getUnsafe().fromLegacy(new MaterialData(m, data));
    }

    public static Material convertMaterial(int ID, byte Data) {
        for (Material i : EnumSet.allOf(Material.class))
            if (i.getId() == ID) return Bukkit.getUnsafe().fromLegacy(new MaterialData(i, Data));
        return null;
    }

}
