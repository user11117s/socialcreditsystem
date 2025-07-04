package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditChangeEvent;
import com.array64.socialCredit.api.SocialCreditListener;
import com.array64.socialCredit.api.SocialCreditLoadEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

class ScoreUpdater {
    private final JavaPlugin plugin;
    private final DatabaseService db;
    private final List<SocialCreditListener> listeners;
    private final DisplayUpdater displayUpdater;
    private final PermissionsUpdater permsUpdater;
    private int mostRecentScore = -1;

    public ScoreUpdater(JavaPlugin plugin, DatabaseService db, List<SocialCreditListener> listeners, DisplayUpdater display, PermissionsUpdater perms) {
        this.plugin = plugin;
        this.db = db;
        this.listeners = listeners;
        this.displayUpdater = display;
        this.permsUpdater = perms;
    }

    /**
     * @param score Can be -1 if you don't want the score to change at all.
     */
    void updatePlayer(SocialCreditSystemImpl system, Player p, int score) {
        if(!Bukkit.isPrimaryThread()) {
            int lambdaScore = score;
            Bukkit.getScheduler().runTask(plugin, () -> this.updatePlayer(system, p, lambdaScore));
            // This method must be run on the main thread.
            return;
        }

        int oldScore = system.getScore(p);
        if(score != oldScore && score != -1) { // Will not trigger if score doesn't change.
            SocialCreditChangeEvent e = new SocialCreditChangeEventImpl(p, oldScore, score);
            if(system.getConfig().isEnabled()) {
                for(SocialCreditListener listener : listeners) {
                    listener.onSocialCreditChange(e);
                }
            }
            if(e.isCancelled() && oldScore >= 0 && oldScore < system.getConfig().getMaxScore())
                score = oldScore;
            else {
                score = e.getNewScore();
                db.zadd("scs:scores", p.getUniqueId() + "", score);
            }
        }
        else { // This means that the score is not set and just "loaded", therefore we notify the load event listeners.
            SocialCreditLoadEvent e = new SocialCreditLoadEventImpl(p, oldScore);
            if(system.getConfig().isEnabled()) {
                for(SocialCreditListener listener : listeners) {
                    listener.onSocialCreditLoad(e);
                }
            }
        }
        if(score == -1)
            score = oldScore;

        displayUpdater.updatePlayer(system, p, score);
        permsUpdater.updatePlayer(system, p, score);
        mostRecentScore = score;
    }

    int getMostRecentScore() {
        return mostRecentScore;
    }
}
