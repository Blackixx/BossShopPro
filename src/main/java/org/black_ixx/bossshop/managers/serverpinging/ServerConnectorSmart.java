package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.managers.ClassManager;

import java.util.List;

public class ServerConnectorSmart implements ServerConnector {


    private List<ServerConnector> modules;
    private ServerConnector current;
    private int current_id = -1;
    private boolean had_success;
    private int fails_amount;

    public ServerConnectorSmart(List<ServerConnector> modules) {
        this.modules = modules;

        had_success = ClassManager.manager.getStorageManager().getConfig().contains("serverpinging.connector");
        int connector_type = ClassManager.manager.getStorageManager().getConfig().getInt("serverpinging.connector");

        if (connector_type >= 0 && connector_type < modules.size()) {
            current_id = connector_type;
            current = modules.get(current_id);
        } else {
            next();
        }
    }


    public void next() {
        if (modules != null) {
            current_id++;
            if (modules.size() <= current_id) { //Reached end of modules
                current_id = 0;
            }
            current = modules.get(current_id);
        }
    }

    @Override
    public boolean update(ServerInfo info) {
        if (current != null) {

            if (info.isWaiting()) {
                return false;
            }

            info.setBeingPinged(true);
            if (current.update(info)) { //Success
                if (!had_success) { //If has not had success yet -> save connector type
                    ClassManager.manager.getStorageManager().getConfig().set("serverpinging.connector", current_id);
                    BossShop.log("Saving ServerPinging Connector '" + current.getClass() + "' to be instantly used next time.");
                    had_success = true;
                    fails_amount = 0;
                }
                info.setBeingPinged(false);
                return true;

            } else { //No success
                info.hadNoSuccess();
                fails_amount++;

                boolean next = false;
                if (had_success) { //If has had success already -> only try next connector when no success for ages
                    if (fails_amount > 50 & !ClassManager.manager.getSettings().getServerPingingFixConnector()) {
                        had_success = false;
                        next = true;
                    }
                } else { //If has had no success yet and still none -> try next connector fast
                    if (fails_amount > 4) {
                        next = true;
                    }
                }

                if (next) {
                    ClassManager.manager.getBugFinder().warn("[ServerPinging] Connector '" + current.getClass() + "' does not seem to fit.. Trying an other Connector type.");
                    fails_amount = 0;
                    next();
                }
            }
        }
        info.setBeingPinged(false);
        return false;
    }


}
