package com.array64.socialCredit.internals.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HelpCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        sender.sendMessage(
            ChatColor.translateAlternateColorCodes('&',
                """
                
                Commands:
                
                /scs score get <player> - Get the score of a player
                /scs score set <player> <score> - Sets the score of a player
                /scs score add <player> <adjust> - Adds to the score of a player
                /scs score get-top <num-players> - Gets the top players by their Social Credit Score
                /scs score get-bottom <num-players> - Gets the bottom players by their Social Credit Score
                
                /scs permgates set <permission> <score> - Creates a permission gate with a required score
                /scs permgates getall - Gets all permission gates and their required scores
                /scs permgates get <permission> - Gets the required score for this permission
                /scs permgates remove <permission> - Removes this permission gate
                
                /scs visibility set self [public|private] - Sets your Social Credit Score visibility to public or private.
                /scs visibility set other <player> [public|private] - Sets another player's Social Credit Score visibility.
                /scs visibility get <player> - Gets the visibility of a player's score
                
                /scs set maxscore <score> - Sets the maximum possible score
                /scs set defaultscore <score> - Sets the default Social Credit Score
                /scs get maxscore - Gets the maximum score
                /scs get defaultscore - Gets the default score
                
                /scs enable - Enables the Social Credit System
                /scs disable - Disables the Social Credit System
                /scs help - Sends this help message
                
                """
                    .trim()
                    .replace("\t", "")
                    .replace("/scs", "&7/scs")
                    .replace(" - ", "&r - ")
            )
        );
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
