package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditSystem;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class GetAllPermGatesCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        SocialCreditSystem system = SocialCredit.getSystem();
        system.getAllPermissionGatesAsync().thenAccept(
            map -> {
                for(Map.Entry<String, Integer> entry : map.entrySet()) {
                    sender.sendMessage(entry.getKey() + " requires score " + ChatColor.GOLD + entry.getValue());
                }
            }
        );
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
