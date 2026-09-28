# PlayerLore
This open source plugin is intended to allow players to add lore to their items in Minecraft.

## Supported Minecraft Versions
This plugin is supported on the Minecraft versions listed in [`minecraft-versions.json`](minecraft-versions.json): currently **1.19.4**, **1.21.11** and **26.2** (Spigot and its forks). Every stable release is booted on a real server of each of these versions before it is published, and every build checks that the plugin only uses Bukkit API that exists on all of them. Other versions from 1.19.4 onwards are expected to work but are not tested. To support another version, add it to the file: both checks pick it up.

## Download
- [SpigotMC](https://www.spigotmc.org/resources/playerlore.98602/)
- [GitHub releases](https://github.com/Dans-Plugins/PlayerLore/releases)

## Usage reporting

PlayerLore reports its usage by default: when the plugin is enabled, and each time one of its commands is used, it sends its name, its version and the command's name to https://trace.danielstephenson.dev, so it is known which plugins are actually in use. Nothing about players, worlds, IP addresses or the server is sent, and neither is anything typed after a command.

To turn it off:

- for this plugin only: set `usage-reporting.enabled: false` in `plugins/PlayerLore/config.yml`;
- for every plugin on the server that reports to trace: set `enabled: false` in `plugins/trace/config.yml` (created on the first start);
- for the whole server process: set the environment variable `TRACE_USAGE_REPORTING=off` or `DO_NOT_TRACK=1`.

Details: https://github.com/Stephenson-Software/trace#usage-reporting
