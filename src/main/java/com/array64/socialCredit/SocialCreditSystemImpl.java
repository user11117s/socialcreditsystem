package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditConfig;
import com.array64.socialCredit.api.SocialCreditListener;
import com.array64.socialCredit.api.SocialCreditSystem;
import com.array64.socialCredit.api.exceptions.PermissionsNotAvailableException;
import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import com.array64.socialCredit.internals.PermissionsHandler;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

class SocialCreditSystemImpl implements SocialCreditSystem {
    private final DatabaseService db;
    private final List<SocialCreditListener> listeners = new ArrayList<>();
    private final SocialCreditConfig config;
    private final JavaPlugin plugin;
    private final @Nullable PermissionsHandler luckPermsHandler; // This is annotated @Nullable because it can be null and not break our plugin.
    private final ScoreUpdater scoreUpdater;
    private final DisplayUpdater displayUpdater;
    private final PermissionsUpdater permsUpdater;
    private final Leaderboard leaderboard;

    SocialCreditSystemImpl(DatabaseService db, FileConfiguration fileConfig, JavaPlugin plugin) {
        PermissionsHandler luckPermsHandler;
        this.db = db;
        this.plugin = plugin;

        if(Bukkit.getPluginManager().isPluginEnabled("LuckPerms"))
            try {
                luckPermsHandler = (PermissionsHandler) Class
                    .forName("com.array64.socialCredit.internals.LuckPermsHandler")
                    .getConstructor(JavaPlugin.class)
                    .newInstance(plugin);
            } catch(Exception e) {
                plugin.getLogger().warning("Could not load permissions handler due to " + e);
                luckPermsHandler = null;
            }
        else {
            plugin.getLogger().warning("LuckPerms does not seem to be installed. Most permissions-related features will not be allowed.");
            luckPermsHandler = null;
        }

        this.luckPermsHandler = luckPermsHandler;
        this.displayUpdater = new DisplayUpdater(plugin);
        this.permsUpdater = new PermissionsUpdater(plugin, luckPermsHandler);
        this.scoreUpdater = new ScoreUpdater(plugin, db, listeners, displayUpdater, permsUpdater);
        this.config = new SocialCreditConfigImpl(fileConfig, plugin, displayUpdater);
        this.leaderboard = new Leaderboard(db, plugin);
    }

    static int limitScore(int score, boolean throwException) {
        int maxScore = SocialCredit.getSystem().getConfig().getMaxScore();
        if(score < 0) {
            if(throwException) throw new ScoreOutOfBoundsException("Score cannot be negative.");
            else score = 0;
        } else if(score > maxScore) {
            if(throwException) throw new ScoreOutOfBoundsException("Score cannot be greater than the max score of "
                + maxScore + ".");
            else score = maxScore;
        }
        return score;
    }

    @Override
    public int getScore(Player player) {
        try {
            return db.zscore("scs:scores", player.getUniqueId() + "").intValue();
        } catch(Exception ignored) {
            return getConfig().getDefaultScore();
        }
    }

    @Override
    public void setScore(Player player, int score) throws ScoreOutOfBoundsException {
        getScoreUpdater().updatePlayer(this, player, limitScore(score, true));
    }

    @Override
    public void refreshAll() {
        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> players.forEach(p -> {
            int score = limitScore(getScore(p), false);
            getScoreUpdater().updatePlayer(this, p, score);
        }));
    }

    @Override
    public int changeScore(Player player, int scoreAdjust) {
        int oldScore = getScore(player);
        int score = limitScore(oldScore + scoreAdjust, false);
        getScoreUpdater().updatePlayer(this, player, score);
        return getScoreUpdater().getMostRecentScore() - oldScore;
    }

    @Override
    public void setVisibility(Player player, Visibility visibility) {
        db.hset("scs:visibilities", player.getUniqueId() + "", Character.toString('A' + visibility.ordinal()));
        displayUpdater.updatePlayer(this, player, getScore(player));
    }

    @Override
    public Visibility getVisibility(Player player) {
        String redisReturn = db.hget("scs:visibilities", player.getUniqueId() + "");
        if(redisReturn == null)
            return Visibility.PUBLIC;
        else
            return Visibility.values()[
                db.hget("scs:visibilities", player.getUniqueId() + "").charAt(0) - 'A'
            ];
    }

    @Override
    public SocialCreditConfig getConfig() {
        return config;
    }

    @Override
    public void addListener(SocialCreditListener listener) {
        if(listener == null) throw new NullPointerException("addListener was called with a null listener.");
        listeners.add(listener);
    }

    @Override
    public void removeListener(SocialCreditListener listener) {
        if(listener == null) throw new NullPointerException("removeListener was called with a null listener.");
        listeners.remove(listener);
    }

    @Override
    public Map<OfflinePlayer, Integer> getTopScores(int numScores) {
        return leaderboard.getTopScores(numScores);
    }

    @Override
    public Map<OfflinePlayer, Integer> getBottomScores(int numScores) {
        return leaderboard.getBottomScores(numScores);
    }

    @Override
    public CompletableFuture<Map<OfflinePlayer, Integer>> getTopScoresAsync(int numScores) {
        return leaderboard.getTopScoresAsync(numScores);
    }

    @Override
    public CompletableFuture<Map<OfflinePlayer, Integer>> getBottomScoresAsync(int numScores) {
        return leaderboard.getBottomScoresAsync(numScores);
    }

    @Override
    public void setPermissionGate(String permission, int requiredScore) {
        if(!hasPermissionsSupport())
            throw new PermissionsNotAvailableException("LuckPerms cannot be detected by this plugin. Please make sure LuckPerms is installed on the server to use this method.");

        limitScore(requiredScore, true);
        db.hset("scs:permission_gates", permission, requiredScore + "");

        Bukkit.getScheduler().runTask(plugin, () -> {
            if(getConfig().isEnabled()) {
                Bukkit.getOnlinePlayers().forEach(
                    p -> permsUpdater.updatePermForPlayer(this, p, getScore(p), permission)
                );
            }
        }); // When a permission gate is set, we will update the permission gate for all online players immediately.
    }

    @Override
    public void removePermissionGate(String permission) {
        if(!hasPermissionsSupport())
            throw new PermissionsNotAvailableException("LuckPerms cannot be detected by this plugin. Please make sure LuckPerms is installed on the server to use this method.");
        db.hdel("scs:permission_gates", permission);
    }

    @Override
    public int getPermissionRequiredScore(String permission) {
        try {
            return Integer.parseInt(Objects.requireNonNull(db.hget("scs:permission_gates", permission)));
        } catch(Exception e) {
            return -1;
        }
    }
    @Override
    public Map<String, Integer> getAllPermissionGates() {
        return db.hgetAll("scs:permission_gates").entrySet().stream().collect(
            Collectors.toMap(Map.Entry::getKey, entry -> Integer.parseInt(entry.getValue()))
        );
    }

    @Override
    public CompletableFuture<Map<String, Integer>> getAllPermissionGatesAsync() {
        CompletableFuture<Map<String, Integer>> future = new CompletableFuture<>();
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Map<String, Integer> permissionGates = getAllPermissionGates();
                Bukkit.getScheduler().runTask(plugin, () -> future.complete(permissionGates));
            } catch(Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    @Override
    public boolean hasPermissionsSupport() {
        return luckPermsHandler != null;
    }

    void closeDB() {
        db.close();
    }

    ScoreUpdater getScoreUpdater() {
        return scoreUpdater;
    }
}
