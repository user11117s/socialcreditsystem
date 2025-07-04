package com.array64.socialCredit.internals;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.node.Node;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class LuckPermsHandler implements PermissionsHandler {
    private final LuckPerms luckPerms;

    public LuckPermsHandler(JavaPlugin plugin) {
        LuckPerms luckPerms;
        try {
            luckPerms = LuckPermsProvider.get();
        } catch(Exception e) {
            plugin.getLogger().warning("Could not access LuckPerms due to " + e);
            luckPerms = null;
        }
        this.luckPerms = luckPerms;
    }

    @Override
    public void addPermission(Player p, String permission) {
        luckPerms.getUserManager().loadUser(p.getUniqueId()).thenAccept(
            user -> {
                Node node = Node.builder(permission).build();
                user.data().add(node);
                luckPerms.getUserManager().saveUser(user);
            }
        );
    }

    @Override
    public void removePermission(Player p, String permission) {
        luckPerms.getUserManager().loadUser(p.getUniqueId()).thenAccept(
            user -> {
                Node node = Node.builder(permission).build();
                user.data().remove(node);
                luckPerms.getUserManager().saveUser(user);
            }
        );
    }
}
