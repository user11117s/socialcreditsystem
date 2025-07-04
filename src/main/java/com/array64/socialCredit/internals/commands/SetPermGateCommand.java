package com.array64.socialCredit.internals.commands;

import com.array64.socialCredit.SocialCredit;
import com.array64.socialCredit.api.SocialCreditSystem;
import com.array64.socialCredit.api.exceptions.PermissionsNotAvailableException;
import com.array64.socialCredit.api.exceptions.ScoreOutOfBoundsException;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class SetPermGateCommand extends APICommand {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!APICommandUtils.checkArgsLength(sender, args, argPosition, 2)) return true;
        if(!APICommandUtils.checkPermission(sender, "socialcreditsystem.fullaccess")) return true;

        SocialCreditSystem system = SocialCredit.getSystem();

        Optional<Integer> optionalScore = APICommandUtils.parseScore(sender, args[argPosition + 1]);
        if(optionalScore.isEmpty()) return true;

        int score = optionalScore.get();
        try {
            String permission = args[argPosition];
            system.setPermissionGate(permission, score);
            sender.sendMessage(permission + " now requires a score of " + ChatColor.GOLD + score + ChatColor.RESET + " to obtain.");
        }
        catch(PermissionsNotAvailableException ignored) {
            sender.sendMessage(ChatColor.RED + "The Social Credit System couldn't find LuckPerms. Please make sure LuckPerms is installed on the server.");
        }
        catch(ScoreOutOfBoundsException ignored) {
            sender.sendMessage(ChatColor.RED + "The score you provided was out of bounds. Please make sure it is non-negative and not greater than the maximum possible score.");
        }
        return true;
    }

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        return List.of();
    }
}
