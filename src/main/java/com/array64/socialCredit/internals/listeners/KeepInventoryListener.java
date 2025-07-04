package com.array64.socialCredit.internals.listeners;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditChangeEvent;
import com.array64.socialCredit.api.SocialCreditListener;
import com.array64.socialCredit.api.SocialCreditLoadEvent;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

public class KeepInventoryListener implements Listener, SocialCreditListener {
    private static final double THRESHOLD_PROPORTION_OF_MAX_SCORE = 0.8;
    private final Map<OfflinePlayer, Boolean> shouldKeepInventory = new HashMap<>();
    private JavaPlugin plugin;
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        if(!shouldKeepInventory.containsKey(p)) {// This should never be the case
            plugin.getLogger().warning(p.getDisplayName() + "'s keepInventory status was not updated at the right time.");
            updateMap(p, SocialCredit.getSystem().getScore(p));
        }

        if(shouldKeepInventory.get(p)) {
            e.setKeepInventory(true);
            e.getDrops().clear();
        }
    }

    @Override
    public void onSocialCreditChange(SocialCreditChangeEvent e) {
        updateMap(e.getPlayer(), e.getNewScore());
    }

    @Override
    public void onSocialCreditLoad(SocialCreditLoadEvent e) {
        updateMap(e.getPlayer(), e.getScore());
    }

    private void updateMap(Player p, int score) {
        shouldKeepInventory.put(p, score >= THRESHOLD_PROPORTION_OF_MAX_SCORE * SocialCredit.getSystem().getConfig().getMaxScore());
    }
    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
