package org.black_ixx.bossshop.misc.userinput;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;

import java.util.UUID;

public class BSChatUserInput {

    private UUID uuid;
    private BSUserInput input;
    private long time;


    public BSChatUserInput(Player p, BSUserInput input, long allowedDelay) {
        this.uuid = p.getUniqueId();
        this.input = input;
        time = System.currentTimeMillis() + allowedDelay;

        if (ClassManager.manager.getSettings().getBungeeCordServerEnabled()) {
            ClassManager.manager.getBungeeCordManager().playerInputNotification(p, "start", String.valueOf(time));
        }
    }


    /**
     * Check if time is up to date
     * @return up to date
     */
    public boolean isUpToDate() {
        return time > System.currentTimeMillis();
    }

    /**
     * Check if it's the correct player being checked
     * @param p player to check
     * @return correct or not
     */
    public boolean isCorrectPlayer(Player p) {
        return p.getUniqueId().equals(uuid);
    }

    /**
     * Input received
     * @param p the player to check
     * @param text string
     */
    public void input(Player p, String text) {
        input.receivedInput(p, text);
        if (ClassManager.manager.getSettings().getBungeeCordServerEnabled()) {
            ClassManager.manager.getBungeeCordManager().playerInputNotification(p, "end", null);
        }
    }

}
