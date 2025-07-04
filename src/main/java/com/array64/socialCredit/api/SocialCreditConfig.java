package com.array64.socialCredit.api;

import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;

/**
 * Parameters for the social credit system.
 * <p>An instance of this can be gotten using {@link SocialCreditSystem#getConfig()}.</p>
 * <p>All modifications of this instance will be saved and will apply even after the server restarts.</p>
 * <p><strong>Warning: All methods must be called on the main thread.</strong></p>
 */
public interface SocialCreditConfig {
    /**
     * Sets the maximum social credit score a player can have to <code>maxScore</code>.
     * You may need to call {@link SocialCreditSystem#refreshAll()} after to see changes.
     * <p>If the default score is greater than <code>maxScore</code>, it is set to <code>maxScore</code>.</p>
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @param maxScore The maximum score a player will have
     * @see #getMaxScore()
     * @throws ScoreOutOfBoundsException if <code>maxScore</code> is negative
     */
    void setMaxScore(int maxScore) throws ScoreOutOfBoundsException;

    /**
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @return The maximum social credit score a player can have
     * @see #setMaxScore(int)
     */
    int getMaxScore();

    /**
     * Sets the default score that players will get when they first join the server.
     * This doesn't affect players who have already joined the server at least once.
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @param defaultScore The default score that should be set
     * @see #getDefaultScore()
     * @throws ScoreOutOfBoundsException if <code>defaultScore</code> is negative or greater than {@link #getMaxScore()}
     */
    void setDefaultScore(int defaultScore) throws ScoreOutOfBoundsException;

    /**
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @return The default score that players will get when they first join the server
     * @see #setDefaultScore(int)
     */
    int getDefaultScore();

    /**
     * Enables the social credit system.
     * This reverts everything that {@link #disable()} does.
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @see #disable()
     * @see #isEnabled()
     */
    void enable();

    /**
     * Disables the social credit system.
     * <p>When disabled:</p>
     * <ul>
     *      <li>All listeners of the social credit system are disabled.</li>
     *      <li>Permissions are no longer actively changed based on a player's score.</li>
     *      <li>The social credit scores of players don't show up</li>
     *      <li>All built-in commands relating to the social credit system are disabled except for the command to enable it again.</li>
     *      <li>API calls can still be made and will work as normal.</li>
     * </ul>
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @see #enable()
     * @see #isEnabled()
     */
    void disable();

    /**
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     * @return <code>true</code> if the social credit system is enabled, otherwise <code>false</code>. By default, the social credit system is enabled.
     * @see #enable()
     * @see #disable()
     */
    boolean isEnabled();
}
