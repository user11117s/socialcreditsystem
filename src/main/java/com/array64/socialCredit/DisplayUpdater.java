package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditSystem;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.Objects;

// The goal of this class is to handle all the scoreboards for us.
class DisplayUpdater {
    private final JavaPlugin plugin;

    public DisplayUpdater(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    void updatePlayer(SocialCreditSystemImpl system, Player p, int score) {
        if(!Bukkit.isPrimaryThread()) {
            Bukkit.getScheduler().runTask(plugin, () -> this.updatePlayer(system, p, score));
            // This method must be run on the main thread.
            return;
        }

        SocialCreditSystem.Visibility visibility = system.getVisibility(p);

        Scoreboard mainSB = Objects.requireNonNull(Bukkit.getScoreboardManager()).getMainScoreboard();

        String teamName = "social_credit_" + p.getUniqueId();
        Team team = mainSB.getTeam(teamName);

        if(team == null)
            team = mainSB.registerNewTeam(teamName);

        if(!team.hasEntry(p.getName()))
            team.addEntry(p.getName());

        team.setSuffix(
            (visibility == SocialCreditSystem.Visibility.PRIVATE
                || !system.getConfig().isEnabled())
                ? "" : (ChatColor.WHITE + " (" + ChatColor.GOLD + score + ChatColor.WHITE + ")"));
        p.setScoreboard(mainSB);

        // I was going to use blankSB but not anymore. I thought it changed that player's scoreboard entry for everyone.
    }
}
