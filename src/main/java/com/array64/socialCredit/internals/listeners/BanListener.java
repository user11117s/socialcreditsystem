package com.array64.socialCredit.internals.listeners;

import com.array64.socialCredit.api.SocialCreditChangeEvent;
import com.array64.socialCredit.api.SocialCreditListener;
import com.array64.socialCredit.api.SocialCreditLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;

public class BanListener implements SocialCreditListener {
    private JavaPlugin plugin;

    @Override
    public void onSocialCreditChange(SocialCreditChangeEvent e) {
        if(e.getOldScore() > 0 && e.getNewScore() == 0) {
            Bukkit.getScheduler().runTask(plugin, () -> e.getPlayer().ban(
                "Your Social Credit Score reached 0. Once unbanned, please improve your behavior.",
                Duration.ofSeconds(1000000), // 1 million seconds is over 11 days
                "The Social Credit System",
                true
            ));
        }
    }

    @Override
    public void onSocialCreditLoad(SocialCreditLoadEvent e) {

    }

    public void setPlugin(JavaPlugin plugin) {
        this.plugin = plugin;
    }
}
