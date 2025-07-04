package com.array64.socialCredit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        SocialCreditSystemImpl system = SocialCredit.getInternalSystem();
        Player p = e.getPlayer();
        system.getScoreUpdater().updatePlayer(system, p, -1);

        e.setJoinMessage(ChatColor.YELLOW + p.getName() + " joined the game");
    }
}
