package com.array64.socialCredit;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.resps.Tuple;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

class Leaderboard {
    private final DatabaseService db;
    private final JavaPlugin plugin;

    public Leaderboard(DatabaseService db, JavaPlugin plugin) {
        this.db = db;
        this.plugin = plugin;
    }

    public Map<OfflinePlayer, Integer> getTopScores(int numScores) {
        if(numScores <= 0)
            throw new IllegalArgumentException(numScores + " must be a positive integer.");

        List<Tuple> redisReturn = db.zrevrangeWithScores("scs:scores", 0, numScores - 1);

        Map<OfflinePlayer, Integer> result = new LinkedHashMap<>();
        for(Tuple tuple : redisReturn) {
            result.put(Bukkit.getOfflinePlayer(UUID.fromString(tuple.getElement())), (int) tuple.getScore());
        }
        return result;
    }

    public Map<OfflinePlayer, Integer> getBottomScores(int numScores) {
        if(numScores <= 0)
            throw new IllegalArgumentException(numScores + " must be a positive integer.");

        List<Tuple> redisReturn = db.zrangeWithScores("scs:scores", 0, numScores - 1);

        Map<OfflinePlayer, Integer> result = new LinkedHashMap<>();
        for(Tuple tuple : redisReturn) {
            result.put(Bukkit.getOfflinePlayer(UUID.fromString(tuple.getElement())), (int) tuple.getScore());
        }
        return result;
    }

    public CompletableFuture<Map<OfflinePlayer, Integer>> getTopScoresAsync(int numScores) {

        CompletableFuture<Map<OfflinePlayer, Integer>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Map<OfflinePlayer, Integer> topScores = getTopScores(numScores);
                Bukkit.getScheduler().runTask(plugin, () -> future.complete(topScores));
            } catch(Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }

    public CompletableFuture<Map<OfflinePlayer, Integer>> getBottomScoresAsync(int numScores) {

        CompletableFuture<Map<OfflinePlayer, Integer>> future = new CompletableFuture<>();

        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try {
                Map<OfflinePlayer, Integer> bottomScores = getBottomScores(numScores);
                Bukkit.getScheduler().runTask(plugin, () -> future.complete(bottomScores));
            } catch(Exception e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
