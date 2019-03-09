package org.black_ixx.bossshop.managers.serverpinging;

import org.black_ixx.bossshop.BossShop;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ServerPingingRunnableHandler {

    private int id = -1;

    public void start(int speed, BossShop plugin, ServerPingingManager manager) {
        BukkitTask t = new ServerPingingRunnable(manager).runTaskTimerAsynchronously(plugin, speed, speed);
        id = t.getTaskId();
    }

    public void stop() {
        if (id == -1) {
            return;
        }
        Bukkit.getScheduler().cancelTask(id);
    }


    public class ServerPingingRunnable extends BukkitRunnable {

        private ServerPingingManager manager;

        public ServerPingingRunnable(ServerPingingManager manager) {
            this.manager = manager;
        }


        @Override
        public void run() {
            manager.update();
        }
    }

}