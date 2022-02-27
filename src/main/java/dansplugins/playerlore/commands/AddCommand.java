package dansplugins.playerlore.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.misc.ArgumentParser;

/**
 * @author Daniel McCoy Stephenson
 */
public class AddCommand extends AbstractPluginCommand {

    public AddCommand() {
        super(new ArrayList<>(Arrays.asList("add")), new ArrayList<>(Arrays.asList("pl.add")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "Usage: /pl add \"line of lore\"");
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] args) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage("This command can only be used by a player.");
            return false;
        }
        Player player = (Player) commandSender;

        // get line of lore
        ArgumentParser argumentParser = new ArgumentParser();
        ArrayList<String> doubleQuoteArgs = argumentParser.getArgumentsInsideDoubleQuotes(args);
        if (doubleQuoteArgs.size() == 0) {
            player.sendMessage(ChatColor.RED + "Line of lore must be designated between double quotes.");
            return false;
        }
        String lineOfLore = doubleQuoteArgs.get(0);

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
        lore.add(ChatColor.WHITE + lineOfLore);
        itemMeta.setLore(lore);
        item.setItemMeta(itemMeta);

        player.sendMessage(ChatColor.GREEN + "Lore added.");
        return true;
    }
}