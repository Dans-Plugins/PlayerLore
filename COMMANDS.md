# PlayerLore Commands

All commands use `/pl` or `/playerlore` as the base. The lore commands (`add`, `edit`, `remove`) require the player to be holding the item they wish to modify.

| Command | Description | Permission |
|---------|-------------|------------|
| `/pl` | View the plugin version, credits and wiki link. | *(none)* |
| `/pl help` | View a list of commands. | `pl.help` |
| `/pl add "<line of lore>"` | Add a line of lore to the held item. | `pl.add` |
| `/pl edit <lineIndex> "<new lore>"` | Edit a line of lore on the held item (1-based index). | `pl.edit` |
| `/pl remove <lineIndex>` | Remove a line of lore from the held item (1-based index). | `pl.remove` |
