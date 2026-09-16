package dansplugins.playerlore.services;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;

import dansplugins.playerlore.PlayerLore;

/**
 * @author Daniel McCoy Stephenson
 *     To add a new config option, the following methods must be altered:
 *     - saveMissingConfigDefaultsIfNotPresent
 *     - setConfigOption()
 *     - sendConfigList()
 */
public class ConfigService {
    private static final String USAGE_REPORTING_SECTION = "usage-reporting";
    private static final String USAGE_REPORTING_ENABLED_KEY = "usage-reporting.enabled";
    private static final String USAGE_REPORTING_ENDPOINT_KEY = "usage-reporting.endpoint";
    private static final String USAGE_REPORTING_KEY_KEY = "usage-reporting.key";
    private static final String DEFAULT_USAGE_REPORTING_ENDPOINT = "https://trace.danielstephenson.dev";

    private final PlayerLore playerLore;

    private boolean altered = false;

    public ConfigService(PlayerLore playerLore) {
        this.playerLore = playerLore;
    }

    public void saveMissingConfigDefaultsIfNotPresent() {
        // set version
        if (!getConfig().isString("version")) {
            getConfig().addDefault("version", playerLore.getVersion());
        } else {
            getConfig().set("version", playerLore.getVersion());
        }

        // save config options
        if (!isSet("debugMode")) { getConfig().set("debugMode", false); }

        getConfig().options().copyDefaults(true);
        playerLore.saveConfig();
    }

    public void setConfigOption(String option, String value, CommandSender sender) {
        if (getConfig().isSet(option)) {
            if (option.equalsIgnoreCase("version")) {
                sender.sendMessage(ChatColor.RED + "Cannot set version.");
                return;
            } else if (option.equalsIgnoreCase("debugMode")) {
                getConfig().set(option, Boolean.parseBoolean(value));
                sender.sendMessage(ChatColor.GREEN + "Boolean set.");
            } else {
                getConfig().set(option, value);
                sender.sendMessage(ChatColor.GREEN + "String set.");
            }

            // save
            playerLore.saveConfig();
            altered = true;
        } else {
            sender.sendMessage(ChatColor.RED + "That config option wasn't found.");
        }
    }

    public void sendConfigList(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "=== Config List ===");
        sender.sendMessage(ChatColor.AQUA + "version: " + getConfig().getString("version")
                + ", debugMode: " + getString("debugMode"));
    }

    public boolean hasBeenAltered() {
        return altered;
    }

    /**
     * Puts the usage-reporting block on disk if the file does not have one.
     * saveMissingConfigDefaultsIfNotPresent only rewrites config.yml on a first
     * run or a version mismatch, so a server upgraded in place from before usage
     * reporting kept reporting through the bundled defaults (see the getters
     * below) with no visible switch to turn it off. The values are copied from
     * the jar's config.yml, not written as new literals, so the key and endpoint
     * stay defined in one place.
     */
    public void saveUsageReportingDefaultsIfNotPresent() {
        FileConfiguration config = getConfig();
        Configuration defaults = config.getDefaults();
        if (defaults == null || config.isSet(USAGE_REPORTING_SECTION)) {
            return;
        }
        config.set(USAGE_REPORTING_ENABLED_KEY, defaults.get(USAGE_REPORTING_ENABLED_KEY));
        config.set(USAGE_REPORTING_ENDPOINT_KEY, defaults.get(USAGE_REPORTING_ENDPOINT_KEY));
        config.set(USAGE_REPORTING_KEY_KEY, defaults.get(USAGE_REPORTING_KEY_KEY));
        playerLore.saveConfig();
    }

    // The usage-reporting options are read through the one-argument getters,
    // deliberately. A server upgraded from a version before usage reporting
    // has no usage-reporting block in its config.yml: the file on disk is only
    // rewritten by saveMissingConfigDefaultsIfNotPresent, and only on a first
    // run or a version mismatch. Bukkit registers the jar's config.yml as the
    // defaults for that file, and the one-argument getters fall through to
    // them -- but the two-argument getters return their explicit fallback
    // instead, which for the key would be "" and would turn reporting off on
    // every existing installation. Verified against YamlConfiguration in
    // UsageReportingConfigTest, not assumed.

    public boolean isUsageReportingEnabled() {
        return getBoolean(USAGE_REPORTING_ENABLED_KEY);
    }

    public String getUsageReportingEndpoint() {
        return getStringOrDefault(USAGE_REPORTING_ENDPOINT_KEY, DEFAULT_USAGE_REPORTING_ENDPOINT);
    }

    /** Empty when no key is configured or bundled, which the client treats as "off". */
    public String getUsageReportingKey() {
        return getStringOrDefault(USAGE_REPORTING_KEY_KEY, "");
    }

    public FileConfiguration getConfig() {
        return playerLore.getConfig();
    }

    public boolean isSet(String option) {
        return getConfig().isSet(option);
    }

    public int getInt(String option) {
        return getConfig().getInt(option);
    }

    public int getIntOrDefault(String option, int defaultValue) {
        int toReturn = getInt(option);
        if (toReturn == 0) {
            return defaultValue;
        }
        return toReturn;
    }

    public boolean getBoolean(String option) {
        return getConfig().getBoolean(option);
    }

    public double getDouble(String option) {
        return getConfig().getDouble(option);
    }

    public double getDoubleOrDefault(String option, double defaultValue) {
        double toReturn = getDouble(option);
        if (toReturn == 0) {
            return defaultValue;
        }
        return toReturn;
    }

    public String getString(String option) {
        return getConfig().getString(option);
    }

    public String getStringOrDefault(String option, String defaultValue) {
        String toReturn = getString(option);
        if (toReturn == null) {
            return defaultValue;
        }
        return toReturn;
    }
}