package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class AddScoreCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 2)) return true;
        if(!APICommandUtils.checkPermission(sender, "socialcreditsystem.fullaccess")) return true;

        Player p = APICommandUtils.getOnlinePlayer(sender, args[argPosition]);
        if(p == null) return true;

        SocialCreditSystem system = SocialCredit.getSystem();

        Optional<Integer> optionalScore = APICommandUtils.parseScore(sender, args[argPosition + 1]);
        if(optionalScore.isEmpty()) return true;

        int scoreChange = system.changeScore(p, optionalScore.get());
        int score = system.getScore(p);
        notifyTarget(p, scoreChange, score);

        sender.sendMessage(p.getName() + "'s Social Credit Score has been changed: " + ChatColor.GOLD + (score - scoreChange) + ChatColor.RESET + " -> " + ChatColor.GOLD + score);
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return APICommandUtils.completeFor(args, argPosition, APICommandUtils.AutocompleteType.PLAYERS, APICommandUtils.AutocompleteType.NONE);
    }
    public static void changeScoreAndNotify(Player p, int scoreAdjust) {
        SocialCreditSystem system = SocialCredit.getSystem();
        int scoreChange = system.changeScore(p, scoreAdjust);
        int score = system.getScore(p);
        notifyTarget(p, scoreChange, score);
    }
    private static void notifyTarget(Player p, int scoreChange, int score) {
        if(scoreChange > 0)
            p.sendMessage(ChatColor.GREEN + "Your Social Credit Score has been increased to " + ChatColor.BOLD + score + ChatColor.GREEN + " (+" + scoreChange + ")!");

        if(scoreChange < 0)
            p.sendMessage(ChatColor.RED + "Your Social Credit Score has been reduced to " + ChatColor.BOLD + score + ChatColor.RED + " (-" + (-scoreChange) + ").");
    }
}
