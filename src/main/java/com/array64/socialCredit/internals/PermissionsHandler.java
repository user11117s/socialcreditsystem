package com.array64.socialCredit.internals;

import org.bukkit.entity.Player;

public interface PermissionsHandler {
    void addPermission(Player p, String permission);
    void removePermission(Player p, String permission);
}
