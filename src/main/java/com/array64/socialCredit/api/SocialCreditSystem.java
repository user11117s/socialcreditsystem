package com.array64.socialCredit.api;

import com.array64.socialCredit.api.exceptions.PermissionsNotAvailableException;
import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * An API for the social credit system.
 * <p>Methods ending in <code>Async</code> are asynchronous versions of their sync counterparts.
 * It's recommended to use them if the sync method documents a warning for being blocking.
 * These will return a {@link CompletableFuture} that will complete on the main thread with the sync method's return value.</p>
 */
public interface SocialCreditSystem {
    /**
     * An enum to represent visibility of each player's social credit.
     */
    enum Visibility {
        PUBLIC, PRIVATE
    }

    /**
     * @param player The player to get the social credit score of
     * @return The social credit score the player has
     * @see #setScore(Player, int)
     */
    int getScore(Player player);

    /**
     * Sets <code>player</code>'s social credit score to <code>score</code>.
     *
     * @param player The player to set the score of
     * @param score To score to be set
     * @see #getScore(Player)
     * @see #changeScore(Player, int)
     * @throws ScoreOutOfBoundsException if <code>score</code> is negative or greater than {@link SocialCreditConfig#getMaxScore() getConfig().getMaxScore()}
     */
    void setScore(Player player, int score) throws ScoreOutOfBoundsException;

    /**
     * Refreshes all scores in the following ways:
     * <ul>
     *      <li>Updates all visual indicators of social credit score from database.</li>
     *      <li>Bounds the social credit score from 0 to the maximum possible score.</li>
     *      <li>Updates permissions based on social credit score.</li>
     *      <li>Updates database with bounded scores if any.</li>
     * </ul>
     * <p><strong>This method's purpose is to update the scores of all players if scores or settings are out of sync.</strong></p>
     * <p><strong>Warning: This method must be called on the main thread.</strong></p>
     */
    void refreshAll();

    /**
     * Increases <code>player</code>'s social credit score by <code>scoreAdjust</code> if <code>scoreAdjust</code> is non-negative.
     * If <code>scoreAdjust</code> is negative, <code>player</code>'s social credit score is decreased by <code>-scoreAdjust</code>.
     * <p>This method also ensures that <code>player</code>'s social credit score will also be bounded between 0 and  {@link SocialCreditConfig#getMaxScore() getConfig().getMaxScore()} after changes.</p>
     *
     * @param player The player to increase the score of
     * @param scoreAdjust The adjustment of the score
     * @return The actual change in score, which can be different from <code>scoreAdjust</code> if the new social credit score was outside of the acceptable range and then bounded
     * @see #setScore(Player, int)
     */
    int changeScore(Player player, int scoreAdjust);

    /**
     * Sets the visibility of <code>player</code>'s social credit score.
     *
     * @param player The owner of the social credit score to set the visibility of
     * @param visibility The visibility to set the player's social credit score to
     * @see #getVisibility(Player)
     */
    void setVisibility(Player player, Visibility visibility);

    /**
     * @param player The owner of the social credit score to get the visibility of
     * @return The visibility the player's social credit score has
     * @see #setVisibility(Player, Visibility)
     */
    Visibility getVisibility(Player player);

    /**
     * @return The social credit system's config. This config can be modified and will be saved automatically.
     * However, some settings may need the use of {@link #refreshAll()} to be put into effect.
     */
    SocialCreditConfig getConfig();

    /**
     * Registers a {@link SocialCreditListener} to the social credit system.
     * Whenever certain events occur relating to the social credit system, they will be passed through each registered listener.
     * @param listener The listener to be added
     */
    void addListener(SocialCreditListener listener);

    /**
     * Removes a {@link SocialCreditListener} from the social credit system if it's found.
     * This means the listener will no longer receive events relating to the social credit system.
     * {@link java.util.Objects#equals(Object, Object)} is used to determine if two listeners match.
     * @param listener The listener to be removed
     */
    void removeListener(SocialCreditListener listener);

    /**
     * Returns the map with the highest <code>numScores</code> scores.
     * If there are less than <code>numScores</code> total scores registered, the map will return all registered scores.
     * <p>This map is iterated through in descending order of the players' scores.</p>
     * <p><strong>Warning: This method performs blocking operations. Use {@link #getTopScoresAsync(int)} for the asynchronous version of this method.</strong></p>
     * @param numScores The maximum number of scores to be retrieved
     * @return A map with each key being a player mapped to their score
     */
    Map<OfflinePlayer, Integer> getTopScores(int numScores);

    /**
     * Returns the map with the lowest <code>numScores</code> scores.
     * If there are less than <code>numScores</code> total scores registered, the map will return all registered scores.
     * <p>This map is sorted in ascending order of the players' scores.</p>
     * <p><strong>Warning: This method performs blocking operations. Use {@link #getBottomScoresAsync(int)} for the asynchronous version of this method.</strong></p>
     * @param numScores The maximum number of scores to be retrieved
     * @return A map with each key being a player mapped to their score
     */
    Map<OfflinePlayer, Integer> getBottomScores(int numScores);

    /**
     * Async version of {@link #getTopScores(int)}.
     * See {@link SocialCreditSystem} for more details about async methods.
     */
    CompletableFuture<Map<OfflinePlayer, Integer>> getTopScoresAsync(int numScores);
    /**
     * Async version of {@link #getBottomScores(int)}.
     * See {@link SocialCreditSystem} for more details about async methods.
     */
    CompletableFuture<Map<OfflinePlayer, Integer>> getBottomScoresAsync(int numScores);

    /**
     * Creates a permission gate. This gate gives players with scores of at least <code>requiredScore</code> a permission using <code>LuckPerms</code>.
     * It also takes away permissions from players with scores less than <code>requiredScore</code>.
     * <p>Whenever a player's score is set, a permission gate manages the player's access to its permission.</p>
     * <p>If a gate already exists for <code>permission</code>, it is updated with the new <code>requiredScore</code>.</p>
     * <p><strong>Warning: This method requires the LuckPerms plugin to be installed on the server.</strong></p>
     * <p><strong>Warning: Do not add or remove a permission for a player directly using LuckPerms if a permission gate is assigned to that player.</strong></p>
     * @param permission The permission to be monitored
     * @param requiredScore The score required to get that permission
     * @throws PermissionsNotAvailableException if LuckPerms is not installed on the server
     * @throws ScoreOutOfBoundsException if <code>requiredScore</code> is negative or greater than {@link SocialCreditConfig#getMaxScore() getConfig().getMaxScore()}
     */
    void setPermissionGate(String permission, int requiredScore) throws PermissionsNotAvailableException, ScoreOutOfBoundsException;

    /**
     * Removes the permission gate which monitors <code>permission</code> if there is one.
     * <p><strong>Warning: This method requires the LuckPerms plugin to be installed on the server.</strong></p>
     * @param permission The permission to no longer be monitored
     * @see #setPermissionGate(String, int)
     * @throws PermissionsNotAvailableException if LuckPerms is not installed on the server
     */
    void removePermissionGate(String permission) throws PermissionsNotAvailableException;

    /**
     * Gets the social credit score required to have this LuckPerms permission. This is determined by {@linkplain #setPermissionGate(String, int) permission gates}.
     * @param permission The permission being queried
     * @return The minimum social credit score required to have this permission. If there isn't one, this method returns -1.
     * @see #setPermissionGate(String, int)
     */
    int getPermissionRequiredScore(String permission);

    /**
     * Returns all permission gates in the form of a {@link Map}.
     * <p><strong>Note: The map returned is a representation of the internal data. Modifying the map returned will not change the permission gates.</strong></p>
     * <p><strong>Warning: This method performs blocking operations. Use {@link} for the asynchronous version of this method.</strong></p>
     * @return A map with each key being a permission mapped to the minimum social credit score required to have it
     * @see #setPermissionGate(String, int)
     */
    Map<String, Integer> getAllPermissionGates();
    /**
     * Async version of {@link #getAllPermissionGates()}.
     * See {@link SocialCreditSystem} for more details about async methods.
     */
    CompletableFuture<Map<String, Integer>> getAllPermissionGatesAsync();

    /**
     * Returns whether the plugin has access to LuckPerms.
     * This is meant as an alternative to a try-catch block for permissions-related operations.
     * @return <code>true</code> if the social credit system has access to LuckPerms, otherwise <code>false</code>
     */
    boolean hasPermissionsSupport();
}
