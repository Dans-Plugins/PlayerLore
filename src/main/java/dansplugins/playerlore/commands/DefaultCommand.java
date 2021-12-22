package dansplugins.playerlore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import dansplugins.playerlore.PlayerLore;
import preponderous.ponder.misc.AbstractCommand;

/**
 * @author Daniel Stephenson
 */
public class DefaultCommand extends AbstractCommand {

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "PlayerLore " + PlayerLore.getInstance().getVersion());
        commandSender.sendMessage(ChatColor.AQUA + "Developed by: Daniel Stephenson");
        commandSender.sendMessage(ChatColor.AQUA + "Requested by: Rochelle");
        commandSender.sendMessage(ChatColor.AQUA + "Wiki: https://github.com/dmccoystephenson/PlayerLore/wiki");
        commandSender.sendMessage("");
        commandSender.sendMessage(ChatColor.AQUA + "For a list of commands, type /lp help");
        return true;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        return execute(commandSender);
    }
}