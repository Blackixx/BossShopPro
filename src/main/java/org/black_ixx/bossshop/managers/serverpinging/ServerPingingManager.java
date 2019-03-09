package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.BossShop;
import org.black_ixx.bossshop.core.BSBuy;
import org.black_ixx.bossshop.core.BSShop;
import org.black_ixx.bossshop.managers.ClassManager;
import org.black_ixx.bossshop.managers.misc.StringManipulationLib;

import java.util.ArrayList;
import java.util.List;

public class ServerPingingManager {

    private final String[] placeholder_names = new String[]{"players", "motd"};


    private ServerPingingList list = new ServerPingingList();
    private ServerConnectorSmart connector;
    private boolean ready_to_transform;

    private ServerPingingRunnableHandler runnablehandler = new ServerPingingRunnableHandler();


    public ServerPingingManager(BossShop plugin) {
        BossShop.log("[ServerPinging] Loading ServerPinging Package!");

        int connector_type = plugin.getClassManager().getStorageManager().getConfig().getInt("serverpinging.connector");
        setup(plugin.getConfig().getStringList("ServerPinging.List"), connector_type);
    }

    public ServerPingingRunnableHandler getServerPingingRunnableHandler() {
        return runnablehandler;
    }

    public ServerPingingList getList() {
        return list;
    }


    public void setReadyToTransform(boolean b) {
        ready_to_transform = b;
    }


    public void setup(List<String> pinging_list, int connector_type) {
        List<ServerConnector> connectors = new ArrayList<>();
        try {
            connectors.add(new ServerConnector1());
        } catch (NoClassDefFoundError e) {
        }
        try {
            connectors.add(new ServerConnector2());
        } catch (NoClassDefFoundError e) {
        }
        try {
            connectors.add(new ServerConnector3());
        } catch (NoClassDefFoundError e) {
        }
        try {
            connectors.add(new ServerConnector4());
        } catch (NoClassDefFoundError e) {
        }
        connector = new ServerConnectorSmart(connectors);

        if (pinging_list != null) {
            for (String connection : pinging_list) {
                String[] parts = connection.split(":", 3);

                if (parts.length != 3 && parts.length != 2 && parts.length != 1) {
                    ClassManager.manager.getBugFinder().warn("Unable to read ServerPinging list entry '" + connection + "'. It should look like following: '<name>:<server ip>:<server port>' or if you are using BungeeCord then simply enter the name of the connected BungeeCord server: 'server name'.");
                    continue;
                }

                String name = parts[0].trim();

                if (parts.length == 1) {
                    ServerInfo info = new ServerInfo(name, ClassManager.manager.getSettings().getServerPingingTimeout());
                    list.addServerInfo(name, info);
                    continue;
                }


                String host = parts[1].trim();
                int port = 25565;

                if (parts.length == 3) {
                    try {
                        port = Integer.parseInt(parts[2].trim());
                    } catch (NumberFormatException e) {
                        //keep default port
                    }
                }

                ServerInfo info = new ServerInfo(host, port, ClassManager.manager.getSettings().getServerPingingTimeout());
                list.addServerInfo(name, info);

            }
        }
        list.update(connector, false);
    }

    public void update() {
        list.update(connector, true);
        if (ClassManager.manager.getShops() != null) {
            ClassManager.manager.getShops().refreshShops(true);
        }
    }


    public String transform(String s) {
        if (!ready_to_transform) {
            return s;
        }

        if (StringManipulationLib.figureOutVariable(s, 0, placeholder_names) != null) {

            for (String placeholder_name : placeholder_names) {
                int fromIndex = 0;

                String variable = StringManipulationLib.figureOutVariable(s, placeholder_name, fromIndex);
                while (variable != null) {
                    s = transform(variable.split(":"), s, fromIndex);
                    fromIndex = StringManipulationLib.getIndexOfVariableEnd(s, placeholder_name, fromIndex);
                    variable = StringManipulationLib.figureOutVariable(s, placeholder_name, fromIndex);
                }

            }
        }

        return s;
    }

    public String transform(String[] servers, String current, int fromIndex) {
        if (!ready_to_transform) {
            return current;
        }

        String motd = null;
        int players = 0;

        for (String server_name : servers) {
            ServerInfo c = getInfo(server_name);
            if (c != null) {
                if (c.isOnline()) {
                    if (motd == null) {
                        motd = c.getMotd();
                    }
                    players += c.getPlayers();
                }
            }
        }
        return transform(current, current, motd == null ? "unknown" : motd, String.valueOf(players), fromIndex);
    }

    public String transform(String server_name, String current, int fromIndex) {
        if (!ready_to_transform) {
            return current;
        }

        ServerInfo c = getInfo(server_name);
        if (c != null) {
            if (c.isOnline()) {
                String motd = c.getMotd();
                String players = String.valueOf(c.getPlayers());
                return transform(current, current, motd, players, fromIndex);
            }
        }
        return current;
    }

    private String transform(String original, String current, String motd, String players, int fromIndex) {
        if (!ready_to_transform) {
            return current;
        }

        boolean b = false;
        if (original.contains("%motd_") && motd != null) {
            original = StringManipulationLib.replacePlaceholder(original, "motd", motd, fromIndex);
            b = true;
        }
        if (original.contains("%players_") && players != null) {
            original = StringManipulationLib.replacePlaceholder(original, "players", players, fromIndex);
            b = true;
        }
        if (!b) {
            return current;
        }
        return original;
    }


    public void registerShopItem(String server_name, ConnectedBuyItem connection) {
        ServerInfo info = getInfo(server_name);
        if (info != null) {
            info.addShopItem(connection);
        }
    }

    public void clear() {
        //not doing that yet
    }


    public ServerInfo getInfo(String server_name) {
        return list.getInfo(server_name);
    }


    public boolean containsServerpinging(BSShop shop) {
        for (BSBuy b : shop.getItems()) {
            if (b != null) {
                if (isConnected(b)) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isConnected(BSBuy buy) {
        return getFirstInfo(buy) != null;
    }

    public ServerInfo getFirstInfo(BSBuy buy) {
        return list.getFirstInfo(buy);
    }


}
