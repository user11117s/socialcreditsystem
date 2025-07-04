package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditChangeEvent;
import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import org.bukkit.entity.Player;

class SocialCreditChangeEventImpl implements SocialCreditChangeEvent {
    private final Player player;
    private final int oldScore;
    private int newScore;
    private boolean cancelled = false;

    public SocialCreditChangeEventImpl(Player player, int oldScore, int newScore) {
        this.player = player;
        this.oldScore = oldScore;
        this.newScore = newScore;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public int getNewScore() {
        return newScore;
    }

    @Override
    public void setNewScore(int newScore) throws ScoreOutOfBoundsException {
        this.newScore = SocialCreditSystemImpl.limitScore(newScore, true);
    }

    @Override
    public int getOldScore() {
        return oldScore;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }
}
