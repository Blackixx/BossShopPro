package org.black_ixx.bossshop.misc.userinput;

import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public abstract class BSUserInput {


    /**
     * Get user input from anvil
     * @param p the player to check
     * @param anvil_text the text from anvil
     * @param anvil_item the item from anvil
     * @param chat_text the chat text
     */
    public void getUserInput(Player p, String anvil_text, ItemStack anvil_item, String chat_text) { //Might not receive input for sure
        if (supportsAnvils()) {
            AnvilTools.openAnvilGui(anvil_text, anvil_item, new BSAnvilHolderUserInput(this), p); //Does not work atm
            return;
        }
        ClassManager.manager.getPlayerDataHandler().requestInput(p, new BSChatUserInput(p, this, ClassManager.manager.getSettings().getInputTimeout() * 1000));
        ClassManager.manager.getMessageHandler().sendMessageDirect(ClassManager.manager.getStringManager().transform(chat_text, p), p);
        p.closeInventory();
    }

    public abstract void receivedInput(Player p, String text);


    public boolean supportsAnvils() {
        return false; //Anvils are currently not working & when they are check for server version here
    }

}
