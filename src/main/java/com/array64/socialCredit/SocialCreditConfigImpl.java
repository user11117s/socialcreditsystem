package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditConfig;
import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collection;

class SocialCreditConfigImpl implements SocialCreditConfig {
    private final FileConfiguration fileConfig;
    private final JavaPlugin plugin;
    private final DisplayUpdater displayUpdater;

    public SocialCreditConfigImpl(FileConfiguration fileConfig, JavaPlugin plugin, DisplayUpdater display) {
        this.fileConfig = fileConfig;
        this.plugin = plugin;
        this.displayUpdater = display;
    }

    @Override
    public void setMaxScore(int maxScore) throws ScoreOutOfBoundsException {
        if(maxScore < 0)
            throw new ScoreOutOfBoundsException("Max score cannot be negative.");
        fileConfig.set("max_score", maxScore);
        if(getDefaultScore() > maxScore)
            setDefaultScore(maxScore);
        SocialCredit.getSystem().getAllPermissionGates().forEach(
            (permission, score) -> {
                if(score > maxScore)
                    SocialCredit.getSystem().setPermissionGate(permission, maxScore);
            }
        );
        SocialCredit.getSystem().refreshAll();
    }

    @Override
    public int getMaxScore() {
        return fileConfig.getInt("max_score");
    }

    @Override
    public void setDefaultScore(int defaultScore) throws ScoreOutOfBoundsException {
        SocialCreditSystemImpl.limitScore(defaultScore, true);
        fileConfig.set("default_score", defaultScore);
    }

    @Override
    public int getDefaultScore() {
        return fileConfig.getInt("default_score");
    }

    @Override
    public void enable() {
        fileConfig.set("enabled", true);
        SocialCredit.getSystem().refreshAll();
    }

    @Override
    public void disable() {
        fileConfig.set("enabled", false);
        SocialCreditSystemImpl system = SocialCredit.getInternalSystem();

        Collection<? extends Player> players = Bukkit.getOnlinePlayers();
        Bukkit.getScheduler().runTaskAsynchronously(plugin,
            () -> players.forEach(
                p -> displayUpdater.updatePlayer(system, p, -1)
            )
        );
    }

    @Override
    public boolean isEnabled() {
        return fileConfig.getBoolean("enabled");
    }
}
