package com.array64.socialCredit.internals.commands;

import org.bukkit.command.TabExecutor;

public abstract class APICommand implements TabExecutor {
    /**
     * The position of the argument of the command (including subcommands)
     */
    protected int argPosition = 0;
    protected void increaseArgPosition() {
        argPosition++;
    }
    public boolean alwaysEnabled(String[] args) {
        return false;
    }
}
