package org.black_ixx.bossshop.misc;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.black_ixx.bossshop.managers.ClassManager;
import org.bukkit.OfflinePlayer;

import java.util.List;

public class NoEconomy implements Economy {

    @Override
    public EconomyResponse bankBalance(String arg0) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin found!");
        return null;
    }

    @Override
    public EconomyResponse bankDeposit(String arg0, double arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse bankHas(String arg0, double arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse bankWithdraw(String arg0, double arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse createBank(String arg0, String arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse createBank(String s, OfflinePlayer offlinePlayer) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public boolean createPlayerAccount(String arg0) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean createPlayerAccount(String arg0, String arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean createPlayerAccount(OfflinePlayer offlinePlayer, String s) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public String currencyNamePlural() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public String currencyNameSingular() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse deleteBank(String arg0) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, double arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, double v) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(String arg0, String arg1, double arg2) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse depositPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public String format(double arg0) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public int fractionalDigits() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return 0;
    }

    @Override
    public double getBalance(String arg0) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return 0;
    }

    @Override
    public double getBalance(String arg0, String arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return 0;
    }

    @Override
    public double getBalance(OfflinePlayer offlinePlayer, String s) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return 0;
    }

    @Override
    public List<String> getBanks() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public String getName() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public boolean has(String arg0, double arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, double v) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean has(String arg0, String arg1, double arg2) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean has(OfflinePlayer offlinePlayer, String s, double v) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean hasAccount(String arg0) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean hasAccount(String arg0, String arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean hasAccount(OfflinePlayer offlinePlayer, String s) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public boolean hasBankSupport() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public EconomyResponse isBankMember(String arg0, String arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse isBankMember(String s, OfflinePlayer offlinePlayer) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String arg0, String arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse isBankOwner(String s, OfflinePlayer offlinePlayer) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public boolean isEnabled() {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return false;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, double arg1) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, double v) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(String arg0, String arg1, double arg2) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

    @Override
    public EconomyResponse withdrawPlayer(OfflinePlayer offlinePlayer, String s, double v) {
        ClassManager.manager.getBugFinder().warn("No Economy Plugin was found... You need one if you want to work with Money! Get it there: http://plugins.bukkit.org/.");
        return null;
    }

}
