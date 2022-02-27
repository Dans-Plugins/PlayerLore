package dansplugins.playerlore;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import dansplugins.playerlore.commands.AddCommand;
import dansplugins.playerlore.commands.DefaultCommand;
import dansplugins.playerlore.commands.EditCommand;
import dansplugins.playerlore.commands.HelpCommand;
import dansplugins.playerlore.commands.RemoveCommand;
import dansplugins.playerlore.services.LocalConfigService;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.abs.PonderBukkitPlugin;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

/**
 * @author Daniel McCoy Stephenson
 */
public final class PlayerLore extends PonderBukkitPlugin {
    private static PlayerLore instance;
    private final String pluginVersion = "v" + getDescription().getVersion();
    private final CommandService commandService = new CommandService(getPonder());

    /**
     * This can be used to get the instance of the main class that is managed by itself.
     * @return The managed instance of the main class.
     */
    public static PlayerLore getInstance() {
        return instance;
    }

    /**
     * This runs when the server starts.
     */
    @Override
    public void onEnable() {
        instance = this;
        initializeConfig();
        registerEventHandlers();
        initializeCommandService();
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
            DefaultCommand defaultCommand = new DefaultCommand();
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
     * Checks if the version is mismatched.
     * @return A boolean indicating if the version is mismatched.
     */
    public boolean isVersionMismatched() {
        String configVersion = this.getConfig().getString("version");
        if (configVersion == null || this.getVersion() == null) {
            return false;
        } else {
            return !configVersion.equalsIgnoreCase(this.getVersion());
        }
    }

    /**
     * Checks if debug is enabled.
     * @return Whether debug is enabled.
     */
    public boolean isDebugEnabled() {
        return LocalConfigService.getInstance().getBoolean("debugMode");
    }

    private void initializeConfig() {
        if (configFileExists()) {
            performCompatibilityChecks();
        }
        else {
            LocalConfigService.getInstance().saveMissingConfigDefaultsIfNotPresent();
        }
    }

    private boolean configFileExists() {
        return new File("./plugins/" + getName() + "/config.yml").exists();
    }

    private void performCompatibilityChecks() {
        if (isVersionMismatched()) {
            LocalConfigService.getInstance().saveMissingConfigDefaultsIfNotPresent();
        }
        reloadConfig();
    }

    /**
     * Registers the event handlers of the plugin using Ponder.
     */
    private void registerEventHandlers() {
        /*
        ArrayList<Listener> listeners = new ArrayList<>();
        getToolbox().getEventHandlerRegistry().registerEventHandlers(listeners, this);
        */
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