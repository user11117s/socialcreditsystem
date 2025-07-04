package com.array64.socialCredit.api;

import org.bukkit.entity.Player;

/**
 * Represents the event of the social credit score of a player being loaded and not changed.
 * This can be passed to {@link SocialCreditListener}s to listen to the event.
 * <p>This event can be called:</p>
 * <ul>
 *     <li>When {@link SocialCreditSystem#changeScore(Player, int)} is called but doesn't change the score (either because it is already at the highest or lowest, or because the score adjustment passed to it was 0).</li>
 *     <li>When the Social Credit plugin is enabled.</li>
 *     <li>When a player joins the server.</li>
 *     <li>When {@link SocialCreditSystem#refreshAll()} is called (and does <strong>not</strong> change the score because of bounding).</li>
 *     <li>When {@link SocialCreditSystem#setScore(Player, int)} is called with the same score.</li>
 * </ul>
 */
public interface SocialCreditLoadEvent {
    /**
     * @return The player involved in this event
     */
    Player getPlayer();

    /**
     * @return The loaded score of the player
     */
    int getScore();
}
