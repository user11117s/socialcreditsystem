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

public class GetScoreCommand extends APICommand {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 1)) return true;

        Player p = APICommandUtils.getOnlinePlayer(sender, args[argPosition]);
        if(p == null) return true;
        SocialCreditSystem system = SocialCredit.getSystem();

        if(system.getVisibility(p) == SocialCreditSystem.Visibility.PRIVATE
            && !sender.hasPermission("socialcreditsystem.fullaccess")
            && !sender.equals(p))
            sender.sendMessage(ChatColor.RED + p.getName() + "'s Social Credit Score is private and cannot be accessed. You need the permission socialcreditsystem.fullaccess to see private scores.");
        else
            sender.sendMessage(p.getName() + "'s Social Credit Score is: " + ChatColor.GOLD + system.getScore(p));
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return APICommandUtils.completeFor(args, argPosition, APICommandUtils.AutocompleteType.PLAYERS);
    }
}
