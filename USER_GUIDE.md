# PlayerLore User Guide

## What is PlayerLore?

PlayerLore is a Spigot plugin that lets players add, edit, and remove custom lore lines on items they are holding. It is useful for roleplayers, server admins, and anyone who wants to add flavour text or descriptions to in-game items.

## Installation

1. Download the latest `PlayerLore-<version>.jar` from the [Releases](https://github.com/Dans-Plugins/PlayerLore/releases) page.
2. Place the JAR in your server's `plugins/` folder.
3. Restart the server.
4. The plugin generates `plugins/PlayerLore/config.yml` on first run.

## Getting Started

1. Hold an item in your main hand.
2. Add a lore line: `/pl add "This sword was forged in dragon fire."`
3. View the item's tooltip — the lore line appears below the item name.
4. Edit it: `/pl edit 1 "Forged in the fires of Mount Doom."`
5. Remove it: `/pl remove 1`

## Permissions

| Permission | Default | Description |
|------------|---------|-------------|
| `pl.help` | `true` | View the help menu. |
| `pl.add` | `true` | Add a lore line to a held item. |
| `pl.edit` | `true` | Edit a lore line on a held item. |
| `pl.remove` | `true` | Remove a lore line from a held item. |

## Support

Ask questions in the [Discord server](https://discord.gg/xXtuAQ2) or open a [GitHub issue](https://github.com/Dans-Plugins/PlayerLore/issues).
