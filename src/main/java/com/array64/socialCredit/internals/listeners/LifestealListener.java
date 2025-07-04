package com.array64.socialCredit.internals.listeners;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditChangeEvent;
import com.array64.socialCredit.api.SocialCreditConfig;
import com.array64.socialCredit.api.SocialCreditListener;
import com.array64.socialCredit.api.SocialCreditLoadEvent;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

public class LifestealListener implements SocialCreditListener {
    private static final double HEALTH_VARIABILITY = 20.0;
    private static final long HEALTH_OFFSET = 11;
    @Override
    public void onSocialCreditChange(SocialCreditChangeEvent e) {
        changeHealth(e.getPlayer(), e.getNewScore());
    }

    @Override
    public void onSocialCreditLoad(SocialCreditLoadEvent e) {
        changeHealth(e.getPlayer(), e.getScore());
    }
    private void changeHealth(Player p, int score) {
        SocialCreditConfig config = SocialCredit.getSystem().getConfig();
        long newHealth = Math.round(HEALTH_VARIABILITY * score / config.getMaxScore()) - HEALTH_OFFSET;
        Objects.requireNonNull(p.getAttribute(Attribute.MAX_HEALTH)).setBaseValue(20 + newHealth);
    }
}
