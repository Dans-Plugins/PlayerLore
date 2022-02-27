package dansplugins.playerlore.commands;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

/**
 * @author Daniel McCoy Stephenson
 */
public class HelpCommand extends AbstractPluginCommand {

    public HelpCommand() {
        super(new ArrayList<>(Arrays.asList("help")), new ArrayList<>(Arrays.asList("pl.help")));
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "/pl help - View a list of commands.");
        commandSender.sendMessage(ChatColor.AQUA + "/pl add \"line of lore\" - Add a line of lore to an item.");
        commandSender.sendMessage(ChatColor.AQUA + "/pl edit (lineIndex) \"new line of lore\" - Edit a line of lore of an item.");
        commandSender.sendMessage(ChatColor.AQUA + "/pl remove (lineIndex) - Remove a line of lore from an item.");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}