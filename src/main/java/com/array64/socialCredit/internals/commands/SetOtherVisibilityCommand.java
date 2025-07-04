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
import java.util.Objects;

public class SetOtherVisibilityCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 1)) return true;
        if(!APICommandUtils.checkPermission(sender, "socialcreditsystem.fullaccess")) return true;

        Player p = APICommandUtils.getOnlinePlayer(sender, args[argPosition]);
        if(p == null) return true;

        SocialCreditSystem system = SocialCredit.getSystem();

        String visibilityString = args[argPosition + 1];

        if(Objects.equals(visibilityString, "public")) {
            system.setVisibility(p, SocialCreditSystem.Visibility.PUBLIC);
            sender.sendMessage(p.getName() + "'s Social Credit Score is now public.");
        }

        else if(Objects.equals(visibilityString, "private")) {
            system.setVisibility(p, SocialCreditSystem.Visibility.PRIVATE);
            sender.sendMessage(p.getName() + "'s Social Credit Score is now private.");
        } else
            sender.sendMessage(ChatColor.RED + "Please choose either public or private.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return APICommandUtils.completeFor(args, argPosition, APICommandUtils.AutocompleteType.PLAYERS, APICommandUtils.AutocompleteType.VISIBILITY);
    }
}
