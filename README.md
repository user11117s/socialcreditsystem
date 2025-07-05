# Social Credit System

A Spigot plugin for 1.21 servers that adds a Social Credit System.

## Features
- Use `/scs` to access different parts of the social credit system. If you have trouble, use `/scs help`.
- You can configure the maximum possible social credit score and default social credit score, which default to 1000 and 550 respectively.
- Using the API, you can add listeners to the social credit system to track changes in players' scores and give them consequences.
- Players banned upon reaching a score of 0
- You can make your score private if you don't want others to see it (doesn't work for server operators)
- Permission gates: If you have LuckPerms installed, you can use `/scs permgates set some.random.permission <score>` to let the system give out permissions if players reach a certain score, or take away permissions if they drop below that score. You can remove these at any time.
- Disable and re-enable the social credit system at any time

## Dependencies
- A Redis database running for storage
- LuckPerms installed if you want to use permissions-related features

## API

### Gradle
Add this to your `repositories` block in your build.gradle file:
```gradle
maven {
  url 'https://jitpack.io'
}
```
Add this to your `dependencies` block in your build.gradle file:
```gradle
implementation 'com.github.user11117s:socialcreditsystem:1.0.2'
```

You can access the API using
```java
SocialCreditSystem system = SocialCredit.getSystem();

// Example usages
system.setScore(Bukkit.getPlayer("Notch"), 500);
system.getConfig().setMaxScore(5000);
system.addListener(new KickForLowScoreListener());
system.removeListener(SocialCredit.LISTENER_BAN);

```
