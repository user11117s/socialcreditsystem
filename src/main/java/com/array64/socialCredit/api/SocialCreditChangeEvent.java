package com.array64.socialCredit.api;

import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import org.bukkit.entity.Player;

/**
 * Represents the event of the social credit score of a player changing.
 * This can be passed to {@link SocialCreditListener}s to change the outcome of the event or to listen to the event.
 */
public interface SocialCreditChangeEvent {
    /**
     * @return The player involved in this event
     */
    Player getPlayer();

    /**
     * @return The new score of the player
     * @see #setNewScore(int)
     */
    int getNewScore();

    /**
     * Sets the new score of the player.
     * @param newScore The new score of the player
     * @see #getNewScore()
     * @throws ScoreOutOfBoundsException if <code>newScore</code> is negative or greater than the maximum social credit score.
     */
    void setNewScore(int newScore) throws ScoreOutOfBoundsException;

    /**
     * @return The old score of the player
     */
    int getOldScore();

    /**
     * Sets whether the event is cancelled or not.
     * This does not stop the event from going to other {@link SocialCreditListener}s where they can be uncancelled.
     * @param cancelled Whether the event should be cancelled or not
     * @see #isCancelled()
     */
    void setCancelled(boolean cancelled);

    /**
     * @return Whether this event is cancelled or not
     * @see #setCancelled(boolean)
     */
    boolean isCancelled();
}
