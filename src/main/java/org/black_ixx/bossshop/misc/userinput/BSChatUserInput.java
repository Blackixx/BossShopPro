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


    public boolean isUpToDate() {
        return time > System.currentTimeMillis();
    }

    public boolean isCorrectPlayer(Player p) {
        return p.getUniqueId().equals(uuid);
    }

    public void input(Player p, String text) {
        input.receivedInput(p, text);
        if (ClassManager.manager.getSettings().getBungeeCordServerEnabled()) {
            ClassManager.manager.getBungeeCordManager().playerInputNotification(p, "end", null);
        }
    }

}
