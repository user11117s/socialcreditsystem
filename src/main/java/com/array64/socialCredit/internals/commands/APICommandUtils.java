package com.array64.socialCredit.internals.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class APICommandUtils {
    public static boolean checkArgsLength(CommandSender sender, String[] args, int argPosition, int required) {
        if(args.length < argPosition + required) {
            sender.sendMessage(ChatColor.RED + "Not enough arguments. Please check /scs help for usage.");
            return false;
        }
        return true;
    }

    public static Optional<Integer> parseScore(CommandSender sender, String scoreText) {
        try {
            return Optional.of(Integer.parseInt(scoreText));
        } catch(NumberFormatException e) {
            sender.sendMessage(ChatColor.RED + "This argument must be an integer.");
            return Optional.empty();
        }
    }

    public static Player getOnlinePlayer(CommandSender sender, String name) {
        Player player = Bukkit.getPlayer(name);
        if(player == null) {
            sender.sendMessage(ChatColor.RED + "No player named " + name + " is currently online.");
        }
        return player;
    }

    public static boolean checkPermission(CommandSender sender, String permission) {
        if(!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to do this.");
            return false;
        }
        return true;
    }
    public static List<String> completeFor(String[] args, int argPosition, AutocompleteType... types) {
        int localArgPosition = args.length - (argPosition + 1);
        if(types.length <= localArgPosition) return List.of();

        return switch(types[localArgPosition]) {
            case NONE -> List.of();
            case PLAYERS -> null;
            case VISIBILITY -> List.of("public", "private");
        };
    }
    public enum AutocompleteType {
        NONE,
        VISIBILITY,
        PLAYERS
    }
}