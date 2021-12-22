package dansplugins.playerlore.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import preponderous.ponder.misc.AbstractCommand;

import java.util.ArrayList;
import java.util.Collections;

public class RemoveCommand extends AbstractCommand {
    private ArrayList<String> names = new ArrayList<>(Collections.singletonList("remove"));
    private ArrayList<String> permissions = new ArrayList<>(Collections.singletonList("pl.remove"));

    @Override
    public ArrayList<String> getNames() {
        return names;
    }

    @Override
    public ArrayList<String> getPermissions() {
        return permissions;
    }

    @Override
    public boolean execute(CommandSender commandSender) {
        commandSender.sendMessage(ChatColor.RED + "This command isn't implemented yet.");
        // TODO: implement
        return false;
    }

    @Override
    public boolean execute(CommandSender commandSender, String[] strings) {
        commandSender.sendMessage(ChatColor.RED + "This command isn't implemented yet.");
        // TODO: implement
        return false;
    }
}