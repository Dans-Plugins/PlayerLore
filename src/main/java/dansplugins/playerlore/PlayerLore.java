package dansplugins.playerlore;

import dansplugins.playerlore.commands.add.AddCommand;
import dansplugins.playerlore.commands.defaultcommand.DefaultCommand;
import dansplugins.playerlore.commands.edit.EditCommand;
import dansplugins.playerlore.commands.help.HelpCommand;
import dansplugins.playerlore.commands.remove.RemoveCommand;
import dansplugins.playerlore.bstats.Metrics;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Daniel McCoy Stephenson
 */
public final class PlayerLore extends PonderBukkitPlugin {
    private final String pluginVersion = "v" + getDescription().getVersion();

    private final CommandService commandService = new CommandService(getPonder());

    /**
     * This runs when the server starts.
     */
    @Override
    public void onEnable() {
        initializeCommandService();

        int pluginId = 17025;
        new Metrics(this, pluginId);
    }

    /**
     * This runs when the server stops.
     */
    @Override
    public void onDisable() {

    }

    /**
     * This method handles commands sent to the minecraft server and interprets them if the label matches one of the core commands.
     * @param sender The sender of the command.
     * @param cmd The command that was sent. This is unused.
     * @param label The core command that has been invoked.
     * @param args Arguments of the core command. Often sub-commands.
     * @return A boolean indicating whether the execution of the command was successful.
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            DefaultCommand defaultCommand = new DefaultCommand(this);
            return defaultCommand.execute(sender);
        }

        return commandService.interpretAndExecuteCommand(sender, label, args);
    }

    /**
     * This can be used to get the version of the plugin.
     * @return A string containing the version preceded by 'v'
     */
    public String getVersion() {
        return pluginVersion;
    }

    /**
     * Initializes Ponder's command service with the plugin's commands.
     */
    private void initializeCommandService() {
        ArrayList<AbstractPluginCommand> commands = new ArrayList<>(Arrays.asList(
                new HelpCommand(),
                new AddCommand(),
                new EditCommand(),
                new RemoveCommand()
        ));
        commandService.initialize(commands, "That command wasn't found.");
    }
}