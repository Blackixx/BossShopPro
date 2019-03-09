package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.core.BSBuy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


public class ServerPingingList {


    private Map<String, ServerInfo> to_add = new LinkedHashMap<>();
    private Map<String, ServerInfo> infos = new LinkedHashMap<>();
    private List<ServerInfo> to_ping = new Vector<ServerInfo>();


    public void update(ServerConnector current_connector, boolean complete) {
        synchronized (to_add) {
            for (String name : to_add.keySet()) {
                ServerInfo info = to_add.get(name);
                infos.put(name, info);
                to_ping.add(info);
            }
            to_add.clear();
        }

        if (complete) {
            synchronized (to_ping) {
                for (ServerInfo info : to_ping) {
                    info.update(current_connector);
                }
            }
        }
    }


    public void addServerInfo(String name, ServerInfo info) {
        to_add.put(name, info);
    }

    public ServerInfo getInfo(String name) {
        for (String s : infos.keySet()) {
            if (s.equalsIgnoreCase(name)) {
                return infos.get(s);
            }
        }
        return null;
    }

    public Map<String, ServerInfo> getInfos() {
        return infos;
    }

    public ServerInfo getFirstInfo(BSBuy buy) {
        for (ServerInfo info : infos.values()) {
            for (ConnectedBuyItem item : info.getConnectedBuyItems()) {
                if (item.getShopItem() == buy) {
                    return info;
                }
            }
        }
        return null;
    }


}
