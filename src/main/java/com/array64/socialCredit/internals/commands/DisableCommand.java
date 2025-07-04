package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditConfig;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DisableCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkPermission(sender, "socialcreditsystem.fullaccess")) return true;

        SocialCreditConfig config = SocialCredit.getSystem().getConfig();
        if(config.isEnabled()) {
            config.disable();
            sender.sendMessage("The Social Credit System has been disabled.");
        } else
            sender.sendMessage("The Social Credit System already is disabled.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }

    @Override
    public boolean alwaysEnabled(String[] args) {
        return true;
    }
}
