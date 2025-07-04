package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditLoadEvent;
import org.bukkit.entity.Player;

class SocialCreditLoadEventImpl implements SocialCreditLoadEvent {
    private final Player player;
    private final int score;

    public SocialCreditLoadEventImpl(Player player, int score) {
        this.player = player;
        this.score = score;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getScore() {
        return score;
    }
}
