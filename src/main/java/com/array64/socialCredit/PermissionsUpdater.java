package com.array64.socialCredit;

import com.array64.socialCredit.internals.PermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Map;

// The goal of this class is to handle updating permissions for us.
class PermissionsUpdater {
    private final JavaPlugin plugin;
    private final PermissionsHandler luckPermsHandler;

    public PermissionsUpdater(JavaPlugin plugin, PermissionsHandler luckPermsHandler) {
        this.plugin = plugin;
        this.luckPermsHandler = luckPermsHandler;
    }

    void updatePlayer(SocialCreditSystemImpl system, Player p, int score) {
        if(!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> this.updatePlayer(system, p, score));
            // This method must be run on the main thread.
            return;
        }
        if(!system.getConfig().isEnabled()) return;
        if(!system.hasPermissionsSupport()) return;

        system.getAllPermissionGatesAsync().thenAccept(
            perms -> {
                for(Map.Entry<String, Integer> perm : perms.entrySet()) {
                    if(score >= perm.getValue())
                        luckPermsHandler.addPermission(p, perm.getKey());
                    else
                        luckPermsHandler.removePermission(p, perm.getKey());
                }
            }
        );
    }
    void updatePermForPlayer(SocialCreditSystemImpl system, Player p, int score, String permission) {
        if(!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> this.updatePermForPlayer(system, p, score, permission));
            // This method must be run on the main thread.
            return;
        }
        if(!system.getConfig().isEnabled()) return;
        if(!system.hasPermissionsSupport()) return;

        system.getAllPermissionGatesAsync().thenAccept(
            perms -> {
                if(score >= perms.get(permission))
                    luckPermsHandler.addPermission(p, permission);
                else
                    luckPermsHandler.removePermission(p, permission);
            }
        );
    }
}
