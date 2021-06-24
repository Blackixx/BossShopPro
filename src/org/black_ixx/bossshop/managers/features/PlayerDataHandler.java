package org.black_ixx.bossshop.managers.features;

import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.misc.userinput.BSChatUserInput;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class PlayerDataHandler {

    private HashMap<Player, BSShop> last_shop = new HashMap<>();
    private HashMap<Player, String> input = new HashMap<>();
    private HashMap<Player, BSChatUserInput> input_waiting = new HashMap<>();

    public void openedShop(Player p, BSShop shop) {
        this.last_shop.put(p, shop);
    }

    /*
     * TODO:
     * - Add new input placeholder to the string manager
     * - Add an optional "input: <type>" property to shopitems which will cause input to be requested and the item being bought after the input is defined
     */

    public void requestInput(Player p, BSChatUserInput input) {
        this.input_waiting.put(p, input);
    }

    public void removeInputRequest(Player p) {
        this.input_waiting.remove(p);
    }

    public void enteredInput(Player p, String input) {
        this.input.put(p, input);
    }


    public void leftServer(Player p) {
        last_shop.remove(p);
        input.remove(p);
        input_waiting.remove(p);
    }


    public String getInput(Player p) {
        if (input.containsKey(p)) {
            return input.get(p);
        }
        return "Player did not have the option to enter input! Shop seems to not be set up correctly.";
    }

    public BSChatUserInput getInputRequest(Player p) {
        return input_waiting.get(p);
    }

    public BSShop getLastShop(Player p) {
        return last_shop.get(p);
    }

}
