package org.black_ixx.bossshop.misc;

import org.black_ixx.bossshop.managers.misc.InputReader;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Misc {

    /**
     * Fix the lore from a stringlist
     * @param itemData the sting list
     * @return fixed lore list
     */
    public static List<String> fixLore(List<String> itemData) {
        Map<Integer, String> lore = null;
        List<String> new_list = null;
        int highest = -1;

        for (String line : itemData) {
            if (line.toLowerCase().startsWith("lore")) {
                String[] parts = line.split(":", 2);
                String start = parts[0];
                if (start.length() > "lore".length()) {

                    try {
                        int i = Integer.parseInt(start.replace("lore", "")) - 1;

                        if (lore == null) {
                            lore = new HashMap<Integer, String>();
                            new_list = new ArrayList<>();
                        }

                        lore.put(i, parts[1]);
                        highest = Math.max(highest, i);

                    } catch (NumberFormatException e) {
                        //Fail
                    }

                }
            }
        }

        if (new_list != null) {
            for (String line : itemData) {
                if (!line.toLowerCase().startsWith("lore")) {
                    new_list.add(line);
                }
            }
            for (int i = 0; i <= highest; i++) {
                String s = "lore:";
                if (lore.containsKey(i)) {
                    s += lore.get(i);
                }
                new_list.add(s);
            }
        }


        if (new_list != null) {
            return new_list;
        }
        return itemData;
    }


    /**
     * Play a sound for a player
     * @param p the player to play the sound for
     * @param sound the sound to play
     */
    public static void playSound(Player p, String sound) {
        if (sound != null) {
            if (!sound.isEmpty()) {
                String[] parts = sound.split(":");
                Sound s = null;
                for (Sound e : Sound.values()) {
                    if (e.name().equalsIgnoreCase(parts[0])) {
                        s = e;
                        break;
                    }
                }
                if (s != null) {
                    float volume = 1;
                    float pitch = 1;
                    if (parts.length >= 2) {
                        volume = (float) InputReader.getDouble(parts[1], 1);
                    }
                    if (parts.length >= 3) {
                        pitch = (float) InputReader.getDouble(parts[2], 1);
                    }
                    p.playSound(p.getLocation(), s, volume, pitch);
                }
            }
        }
    }


    /**
     * Get the item in the player's main hand
     * @param p player to get item from
     * @return item
     */
    @SuppressWarnings("deprecation")
    public static ItemStack getItemInMainHand(Player p) {
        ItemStack item = null;
        try {
            item = p.getInventory().getItemInMainHand();
        } catch (NoSuchMethodError e) {
            item = p.getItemInHand();
        }
        return item;
    }

}
