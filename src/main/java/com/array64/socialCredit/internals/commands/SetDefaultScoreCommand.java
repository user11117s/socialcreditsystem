package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditConfig;
import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SetDefaultScoreCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 1)) return true;
        if(!APICommandUtils.checkPermission(sender, "socialcreditsystem.fullaccess")) return true;

        SocialCreditConfig config = SocialCredit.getSystem().getConfig();
        try {
            Optional<Integer> optionalScore = APICommandUtils.parseScore(sender, args[argPosition]);
            if(optionalScore.isEmpty()) return true;
            int score = optionalScore.get();

            config.setDefaultScore(score);
            sender.sendMessage("The default Social Credit Score has been successfully set to: " + ChatColor.GOLD + score);
        }
        catch(ScoreOutOfBoundsException ignored) {
            sender.sendMessage(ChatColor.RED + "The score you provided was out of bounds. Please make sure it is non-negative and not greater than the maximum possible score.");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
