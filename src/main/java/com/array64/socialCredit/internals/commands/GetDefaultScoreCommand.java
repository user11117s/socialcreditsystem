package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditConfig;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetDefaultScoreCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SocialCreditConfig config = SocialCredit.getSystem().getConfig();
        sender.sendMessage("The default Social Credit Score is " + ChatColor.GOLD + config.getDefaultScore());
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
