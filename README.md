# PlayerLore
This open source plugin is intended to allow players to add lore to their items in Minecraft.

## Supported Minecraft Versions
This plugin is supported on the Minecraft versions listed in [`minecraft-versions.json`](minecraft-versions.json): currently **1.19.4**, **1.21.11** and **26.2** (Spigot and its forks). Every stable release is booted on a real server of each of these versions before it is published, and every build checks that the plugin only uses Bukkit API that exists on all of them. Other versions from 1.19.4 onwards are expected to work but are not tested. To support another version, add it to the file: both checks pick it up.

## Download
- [SpigotMC](https://www.spigotmc.org/resources/playerlore.98602/)
- [GitHub releases](https://github.com/Dans-Plugins/PlayerLore/releases)

## Works Well With
PlayerLore is part of the **medieval roleplay** set of Dan's Plugins. These are companion plugins that suit the same kind of server and run side by side; PlayerLore does not depend on or call into any of them.

- [Medieval Roleplay Engine](https://github.com/Dans-Plugins/Medieval-Roleplay-Engine) ([SpigotMC](https://www.spigotmc.org/resources/medieval-roleplay-engine.79993/), `/dpm get medievalroleplayengine`): character cards, local, global, whisper and yell chat, emotes, dice and messenger birds.
- [Medieval Factions](https://github.com/Dans-Plugins/Medieval-Factions) ([SpigotMC](https://www.spigotmc.org/resources/medieval-factions.79941/), `/dpm get medievalfactions`): nation-like factions with land claims, diplomacy and laws. Its add-ons are listed in its [Expansions](https://github.com/Dans-Plugins/Medieval-Factions#expansions) section.
- [Mailboxes](https://github.com/Dans-Plugins/Mailboxes) ([SpigotMC](https://www.spigotmc.org/resources/mailboxes.96611/), `/dpm get mailboxes`): persistent mail between players, with item attachments.
- [Medieval Economy](https://github.com/Dans-Plugins/Medieval-Economy) ([SpigotMC](https://www.spigotmc.org/resources/medieval-economy.81836/), `/dpm get medievaleconomy`): a coinpurse and a physical currency item.
- [Medieval Cookery](https://github.com/Dans-Plugins/Medieval-Cookery) (no SpigotMC page, no stable release yet): cooking recipes for custom foods, defined by the server owner.
- [Conquest Recipes](https://github.com/Dans-Plugins/Conquest-Recipes) ([SpigotMC](https://www.spigotmc.org/resources/conquest-recipes.83594/), `/dpm get conquestrecipes`): recipes for historical weapons, armour and shields named to match the Conquest resource pack.

Every plugin above is listed on [dansplugins.com](https://dansplugins.com). PlayerLore is listed at [dansplugins.com/resources/player-lore](https://dansplugins.com/resources/player-lore) and can be installed in game with [Dan's Plugin Manager](https://github.com/Dans-Plugins/Dans-Plugin-Manager): `/dpm get playerlore`.

## Usage reporting

PlayerLore reports its usage by default: when the plugin is enabled, and each time one of its commands is used, it sends its name, its version and the command's name to https://trace.danielstephenson.dev, so it is known which plugins are actually in use. Nothing about players, worlds or IP addresses is sent, and neither is anything typed after a command.

Each event also carries a random server ID (the `server-id` line in `plugins/trace/config.yml`) so
servers can be counted rather than events. It identifies no person, account or IP address; delete
the line to get a new one.

To turn it off:

- for this plugin only: set `usage-reporting.enabled: false` in `plugins/PlayerLore/config.yml`;
- for every plugin on the server that reports to trace: set `enabled: false` in `plugins/trace/config.yml` (created on the first start);
- for the whole server process: set the environment variable `TRACE_USAGE_REPORTING=off` or `DO_NOT_TRACK=1`.

Details: https://github.com/Stephenson-Software/trace#usage-reporting
