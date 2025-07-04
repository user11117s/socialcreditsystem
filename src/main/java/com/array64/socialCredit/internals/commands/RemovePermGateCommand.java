package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditSystem;
import com.array64.socialCredit.api.exceptions.PermissionsNotAvailableException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RemovePermGateCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 1)) return true;
        if(!APICommandUtils.checkPermission(sender, "socialcreditsystem.fullaccess")) return true;

        SocialCreditSystem system = SocialCredit.getSystem();

        try {
            String permission = args[argPosition];
            system.removePermissionGate(permission);
            sender.sendMessage(permission + "'s permission gate has been removed.");
        }
        catch(PermissionsNotAvailableException ignored) {
            sender.sendMessage(ChatColor.RED + "The Social Credit System couldn't find LuckPerms. Please make sure LuckPerms is installed on the server.");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
