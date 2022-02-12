package dansplugins.playerlore.commands;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import preponderous.ponder.minecraft.abs.AbstractPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Daniel McCoy Stephenson
 */
public class RemoveCommand extends AbstractPluginCommand {

    public RemoveCommand() {
        super(new ArrayList<>(Arrays.asList("remove")), new ArrayList<>(Arrays.asList("pl.remove")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "Usage: /pl remove (lineIndex)");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by a player.");
            return false;
        }
        Player player = (Player) commandSender;

        // get line to edit
        int lineIndex = Integer.parseInt(args[0]);

        // get item
        ItemStack item = player.getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) {
            player.sendMessage(ChatColor.RED + "You aren't holding anything.");
            return false;
        }

        // get item meta
        ItemMeta itemMeta = item.getItemMeta();
        if (itemMeta == null) {
            player.sendMessage(ChatColor.RED + "That item's meta information wasn't found.");
            return false;
        }

        // add lore
        List<String> lore = itemMeta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        if (lineIndex >= lore.size()) {
            player.sendMessage(ChatColor.RED + "There aren't that many lines of lore.");
            return false;
        }

        lore.remove(lineIndex);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        player.sendMessage(ChatColor.GREEN + "Line removed.");
        return true;
    }
}