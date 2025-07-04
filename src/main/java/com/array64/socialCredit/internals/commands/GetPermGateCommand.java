package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GetPermGateCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 1)) return true;

        SocialCreditSystem system = SocialCredit.getSystem();

        String permission = args[argPosition];
        int requiredScore = system.getPermissionRequiredScore(permission);
        if(requiredScore == -1)
            sender.sendMessage("No permission gate has been set for " + permission + ".");
        else
            sender.sendMessage(permission + " requires a score of " + ChatColor.GOLD + requiredScore + ChatColor.RESET + " to obtain.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
