package com.array64.socialCredit.internals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public final class CompositeCommand extends APICommand {
    private Map<String, APICommand> subcommands;
    private CompositeCommand() {}

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length < argPosition + 1) {
            sender.sendMessage(ChatColor.RED + "Please specify the subcommand you would like to use.");
            return true;
        }
        APICommand apiCommand = subcommands.get(args[argPosition]);
        if(apiCommand == null) {
            sender.sendMessage(ChatColor.RED + "Subcommand not found.");
            return true;
        }
        else
            return apiCommand.onCommand(sender, command, label, args);
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(args.length == argPosition + 1) return subcommands.keySet().stream().toList();
        if(args.length > argPosition + 1) {
            APICommand apiCommand = subcommands.get(args[argPosition]);
            if(apiCommand == null)
                return null;
            else
                return apiCommand.onTabComplete(sender, command, label, args);
        }
        return null;
    }
    public static class Builder {
        private final HashMap<String, APICommand> subcommands = new LinkedHashMap<>();
        public Builder addSubcommand(String name, APICommand subcommand) {
            if(subcommand != null) {
                this.subcommands.put(name, subcommand);
                subcommand.increaseArgPosition();
            }
            return this;
        }
        public CompositeCommand build() {
            CompositeCommand command = new CompositeCommand();
            command.subcommands = subcommands;
            return command;
        }
    }

    @Override
    protected void increaseArgPosition() {
        super.increaseArgPosition();
        subcommands.values().forEach(APICommand::increaseArgPosition);
    }

    @Override
    public boolean alwaysEnabled(String[] args) {
        if(args.length < argPosition + 1) {
            return true;
        }
        APICommand apiCommand = subcommands.get(args[argPosition]);
        if(apiCommand != null)
            return apiCommand.alwaysEnabled(args);

        return true;
    }
}
