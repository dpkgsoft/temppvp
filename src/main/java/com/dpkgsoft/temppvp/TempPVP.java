package com.dpkgsoft.temppvp;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author @mraliscoder
 */

public class TempPVP extends JavaPlugin implements Listener {
    /**
     * If pvp is now enable
     */
    boolean pvp = false;

    /**
     * Configuration
     */
    FileConfiguration config;

    /**
     * Things that happens on enable
     */
    @Override
    public void onEnable() {
        saveDefaultConfig();
        config = getConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        pvp = config.getBoolean("pvp", false);
    }

    /**
     * Things that happens on /pvp
     * @param sender Bukkit
     * @param command Bukkit
     * @param label Bukkit
     * @param arg Bukkit
     * @return boolean
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] arg) {
        if (label.equalsIgnoreCase("pvp")) {
            if (arg.length == 1) {
                if (sender.hasPermission("pvp.modify")) {
                    if (arg[0].equalsIgnoreCase("on")) {
                        pvp = true;
                        config.set("pvp", true);
                        saveConfig();
                        sender.sendMessage(color(config.getString("messages.enable")));
                        return true;
                    }
                    if (arg[0].equalsIgnoreCase("off")) {
                        pvp = false;
                        config.set("pvp", false);
                        saveConfig();
                        sender.sendMessage(color(config.getString("messages.disable")));
                        return true;
                    }
                } else {
                    sender.sendMessage(color(config.getString("messages.permissions")));
                    return true;
                }
                if (arg[0].equalsIgnoreCase("reload") && sender.hasPermission("pvp.reload")) {
                    reloadConfig();
                    config = getConfig();
                    sender.sendMessage(color(config.getString("messages.reload")));
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Things that happens on PVP
     * If player is damaging other player and pvp is off, event cancels
     *
     * @param e event
     */
    @EventHandler
    public void onPvp(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player && e.getDamager() instanceof Player) {
            if (!pvp) {
                e.setCancelled(true);
                e.getDamager().sendMessage(color(config.getString("messages.damage")));
            }
        }
    }


    /**
     * Colorize messages
     * @param msg original message with '&'
     * @return colored message
     */
    public static String color(String msg) {
        return ChatColor.translateAlternateColorCodes('&', msg);
    }
}
