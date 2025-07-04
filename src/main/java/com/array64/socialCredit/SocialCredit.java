package com.array64.socialCredit;

import com.array64.socialCredit.api.SocialCreditListener;
import com.array64.socialCredit.api.SocialCreditSystem;
import com.array64.socialCredit.internals.commands.*;
import com.array64.socialCredit.internals.listeners.BanListener;
import com.array64.socialCredit.internals.listeners.KeepInventoryListener;
import com.array64.socialCredit.internals.listeners.LifestealListener;
import com.array64.socialCredit.internals.listeners.UHCListener;
import org.bukkit.Bukkit;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.java.JavaPlugin;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Objects;

public final class SocialCredit extends JavaPlugin {
    private static SocialCreditSystemImpl system;

    /**
     * This listener is added to the Social Credit System by default and bans players for 1,000,000 seconds when their Social Credit drops to 0.
     * Feel free to use {@link SocialCreditSystem#removeListener(SocialCreditListener) removeListener(SocialCredit.LISTENER_BAN)} if you don't want this behavior.
     */
    public static final SocialCreditListener LISTENER_BAN = new BanListener();
    /**
     * This listener can set your health from 5 hearts to 15 hearts depending on your Social Credit Score.
     * Feel free to use {@link SocialCreditSystem#removeListener(SocialCreditListener) removeListener(SocialCredit.LISTENER_BASE_HEALTH)} if you don't want this behavior.
     */
    public static final SocialCreditListener LISTENER_BASE_HEALTH = new LifestealListener();
    /**
     * This will disable regeneration for players whose Social Credit Score is less than 20% of the maximum possible score.
     * Feel free to use {@link SocialCreditSystem#removeListener(SocialCreditListener) removeListener(SocialCredit.LISTENER_REGEN)} if you don't want this behavior.
     */
    public static final SocialCreditListener LISTENER_REGEN = new UHCListener();
    /**
     * This will disable regeneration for players whose Social Credit Score is at least than 80% of the maximum possible score.
     * Feel free to use {@link SocialCreditSystem#removeListener(SocialCreditListener) removeListener(SocialCredit.LISTENER_KEEP_INVENTORY)} if you don't want this behavior.
     */
    public static final SocialCreditListener LISTENER_KEEP_INVENTORY = new KeepInventoryListener();
    private static final int JEDIS_TIMEOUT = 2000;

    @Override
    public void onEnable() {

        // Plugin startup logic

        // Config stuff
        saveDefaultConfig();
        FileConfiguration fileConfig = getConfig();

        // Registering events
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        getServer().getPluginManager().registerEvents((UHCListener) LISTENER_REGEN, this);
        getServer().getPluginManager().registerEvents((KeepInventoryListener) LISTENER_KEEP_INVENTORY, this);

        system = new SocialCreditSystemImpl(
            new DatabaseService(
                new JedisPool(
                    new JedisPoolConfig(),
                    fileConfig.getString("jedis_host"),
                    fileConfig.getInt("jedis_port"),
                    JEDIS_TIMEOUT
                ), this
            ),
            fileConfig,
            this
        );

        // We are registering some consequences of the Social Credit Score to show how it can be used to make the community suffer I mean help the community
        ((BanListener) LISTENER_BAN).setPlugin(this);
        ((UHCListener) LISTENER_REGEN).setPlugin(this);
        ((KeepInventoryListener) LISTENER_KEEP_INVENTORY).setPlugin(this);
        system.addListener(LISTENER_BAN);
        system.addListener(LISTENER_BASE_HEALTH);
        system.addListener(LISTENER_REGEN);
        system.addListener(LISTENER_KEEP_INVENTORY);
        system.refreshAll();

        // A tree of all the commands that players will have access to
        APICommand commandTree = new CompositeCommand.Builder()
            .addSubcommand("score", new CompositeCommand.Builder()
                .addSubcommand("get", new GetScoreCommand())
                .addSubcommand("set", new SetScoreCommand())
                .addSubcommand("add", new AddScoreCommand())
                .addSubcommand("get-top", new GetTopKScoresCommand())
                .addSubcommand("get-bottom", new GetBottomKScoresCommand())
                .build())
            .addSubcommand("permgates", new CompositeCommand.Builder()
                .addSubcommand("set", new SetPermGateCommand())
                .addSubcommand("getall", new GetAllPermGatesCommand())
                .addSubcommand("get", new GetPermGateCommand())
                .addSubcommand("remove", new RemovePermGateCommand())
                .build())
            .addSubcommand("visibility", new CompositeCommand.Builder()
                .addSubcommand("set", new CompositeCommand.Builder()
                    .addSubcommand("self", new SetOwnVisibilityCommand())
                    .addSubcommand("other", new SetOtherVisibilityCommand())
                    .build())
                .addSubcommand("get", new GetVisibilityCommand())
                .build())
            .addSubcommand("config", new CompositeCommand.Builder()
                .addSubcommand("set", new CompositeCommand.Builder()
                    .addSubcommand("maxscore", new SetMaxScoreCommand())
                    .addSubcommand("defaultscore", new SetDefaultScoreCommand())
                    .build())
                .addSubcommand("get", new CompositeCommand.Builder()
                    .addSubcommand("maxscore", new GetMaxScoreCommand())
                    .addSubcommand("defaultscore", new GetDefaultScoreCommand())
                    .build())
                .build())
            .addSubcommand("enable", new EnableCommand())
            .addSubcommand("disable", new DisableCommand())
            .addSubcommand("help", new HelpCommand())
            .addSubcommand("handleblockedchats", new HandleBlockedChatsCommand())
            .build();

        // Wrapping the command (which will give us autocomplete filtering and disabling commands), then registering it
        TabExecutor socialCreditSystemCLI = new APICommandExecutor(commandTree);
        Objects.requireNonNull(getCommand("scs")).setExecutor(socialCreditSystemCLI);
        Objects.requireNonNull(getCommand("scs")).setTabCompleter(socialCreditSystemCLI);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        system.closeDB();
        HandlerList.unregisterAll(this);
        saveConfig();
    }

    /**
     * @return The social credit system (API) used in the plugin if it has been successfully initialized, otherwise <code>null</code>
     */
    public static SocialCreditSystem getSystem() {
        return system;
    }
    static SocialCreditSystemImpl getInternalSystem() {
        return system;
    }
}
