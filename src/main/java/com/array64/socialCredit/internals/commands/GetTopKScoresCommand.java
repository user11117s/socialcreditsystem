package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditSystem;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GetTopKScoresCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 1)) return true;

        SocialCreditSystem system = SocialCredit.getSystem();

        Optional<Integer> optionalK = APICommandUtils.parseScore(sender, args[argPosition]);
        if(optionalK.isEmpty()) return true;
        int k = optionalK.get();
        if(k > 0) {
            system.getTopScoresAsync(k).thenAccept(
                scores -> {
                    for(Map.Entry<OfflinePlayer, Integer> entry : scores.entrySet()) {
                        sender.sendMessage(entry.getKey().getName() + " has score " + ChatColor.GOLD + entry.getValue());
                    }
                }
            );
        }
        else sender.sendMessage(ChatColor.RED + "The argument you provided was out of bounds. Please make sure it is positive.");

        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
