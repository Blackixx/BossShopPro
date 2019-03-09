package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.managers.ClassManager;

import java.net.InetSocketAddress;
import java.util.LinkedHashSet;
import java.util.Set;

public class ServerInfo {


    public final static int TYPE_NORMAL = 0;
    public final static int TYPE_BUNGEECORD = 1;


    private int type;

    private String host;
    private int port;
    private int timeout;
    private InetSocketAddress address;

    private int players;
    private int max_players;
    private boolean online;
    private String motd;

    private long wait;
    private boolean being_pinged;


    private Set<ConnectedBuyItem> buyitems = new LinkedHashSet<ConnectedBuyItem>();


    public ServerInfo(String host, int port, int timeout) {
        type = TYPE_NORMAL;
        this.host = host;
        this.port = port;
        this.timeout = timeout;
        this.address = new InetSocketAddress(host, port);
    }

    public ServerInfo(String bungeecord_servername, int timeout) {
        type = TYPE_BUNGEECORD;
        ClassManager.manager.getSettings().setBungeeCordServerEnabled(true);
        this.host = bungeecord_servername;
        this.port = -1;
        this.timeout = -1;
        this.address = null;
        this.motd = "unknown";
        this.timeout = timeout;
    }


    public void update(ServerConnector current_connector) {
        switch (type) {
            case TYPE_NORMAL:
                current_connector.update(this);
                break;
            case TYPE_BUNGEECORD:
                ClassManager.manager.getBungeeCordManager().sendPluginMessage(null, ClassManager.manager.getPlugin(), "PlayerCount", host);
                break;
        }
    }


    public InetSocketAddress getAddress() {
        return address;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public int getTimeout() {
        return timeout;
    }


    public int getPlayers() {
        return players;
    }

    public void setPlayers(int i) {
        this.players = i;
    }

    public int getMaxPlayers() {
        return max_players;
    }

    public void setMaxPlayers(int i) {
        this.max_players = i;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean b) {
        online = b;
    }

    public String getMotd() {
        return motd;
    }

    public void setMotd(String s) {
        motd = s;
    }

    public void setBeingPinged(boolean b) {
        being_pinged = b;
    }

    public void setNoConnection() {
        online = false;
        players = 0;
        max_players = 0;
        motd = "Offline";
    }

    public void hadNoSuccess() {
        int delay = ClassManager.manager.getSettings().getServerPingingWaittime();
        if (delay > 0) {
            wait = System.currentTimeMillis() + delay;
        }
    }

    public boolean isWaiting() {
        return wait > System.currentTimeMillis() || being_pinged;
    }


    public void addShopItem(ConnectedBuyItem shopitem) {
        buyitems.add(shopitem);
    }

    public Set<ConnectedBuyItem> getConnectedBuyItems() {
        return buyitems;
    }


}
