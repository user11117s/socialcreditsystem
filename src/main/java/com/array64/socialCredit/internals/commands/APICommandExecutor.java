package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

// I forgot to disable these commands so I made this wrapper class xD
public class APICommandExecutor implements TabExecutor {
    private final APICommand apiCommand;

    public APICommandExecutor(APICommand apiCommand) {
        this.apiCommand = apiCommand;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(SocialCredit.getSystem().getConfig().isEnabled() || apiCommand.alwaysEnabled(args))
            apiCommand.onCommand(sender, command, label, args);
        else
            sender.sendMessage(ChatColor.RED + "The Social Credit System is disabled. Please use /scs enable to enable it.");
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> options = apiCommand.onTabComplete(sender, command, label, args);
        if(options == null)
            return null;
        else
            return options.stream().filter(
                option -> !option.equals("handleblockedchats") && option.startsWith(args[args.length - 1])
            ).toList();
        // Returns only the recommendations that complete your given argument
    }
}
