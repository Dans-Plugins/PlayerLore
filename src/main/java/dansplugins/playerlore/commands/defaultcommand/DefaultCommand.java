package dansplugins.playerlore.commands.defaultcommand;

import dansplugins.playerlore.PlayerLore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public class DefaultCommand extends AbstractPluginCommand {
    private final PlayerLore playerLore;

    public DefaultCommand(PlayerLore playerLore) {
        super(new ArrayList<>(Arrays.asList("default")), new ArrayList<>(Arrays.asList("pl.default")));
        this.playerLore = playerLore;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.AQUA + "PlayerLore " + playerLore.getVersion());
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