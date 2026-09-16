# PlayerLore
This open source plugin is intended to allow players to add lore to their items in Minecraft.

## Usage reporting

PlayerLore reports its usage by default: when the plugin is enabled, and each time one of its commands is used, it sends its name, its version and the command's name to https://trace.danielstephenson.dev, so it is known which plugins are actually in use. Nothing about players, worlds, IP addresses or the server is sent, and neither is anything typed after a command.

To turn it off:

- for this plugin only: set `usage-reporting.enabled: false` in `plugins/PlayerLore/config.yml`;
- for every plugin on the server that reports to trace: set `enabled: false` in `plugins/trace/config.yml` (created on the first start);
- for the whole server process: set the environment variable `TRACE_USAGE_REPORTING=off` or `DO_NOT_TRACK=1`.

Details: https://github.com/Stephenson-Software/trace#usage-reporting
