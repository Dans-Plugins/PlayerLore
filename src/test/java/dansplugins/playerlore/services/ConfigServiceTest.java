package dansplugins.playerlore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dansplugins.playerlore.PlayerLore;

public class ConfigServiceTest {

    private ConfigService configService;
    private PlayerLore playerLore;
    private FileConfiguration config;
    private FileConfigurationOptions configOptions;
    private CommandSender sender;

    @BeforeEach
    public void setUp() {
        playerLore = mock(PlayerLore.class);
        config = mock(FileConfiguration.class);
        configOptions = mock(FileConfigurationOptions.class);
        sender = mock(CommandSender.class);

        when(playerLore.getConfig()).thenReturn(config);
        when(config.options()).thenReturn(configOptions);
        configService = new ConfigService(playerLore);
    }

    @Test
    public void setConfigOption_reportsOptionsThatArentPresent() {
        when(config.isSet("debugMode")).thenReturn(false);

        configService.setConfigOption("debugMode", "true", sender);

        verify(sender).sendMessage(ChatColor.RED + "That config option wasn't found.");
        verify(config, never()).set(anyString(), any());
        verify(playerLore, never()).saveConfig();
        assertFalse(configService.hasBeenAltered());
    }

    @Test
    public void setConfigOption_refusesToSetVersion() {
        when(config.isSet("version")).thenReturn(true);

        configService.setConfigOption("version", "9.9.9", sender);

        verify(sender).sendMessage(ChatColor.RED + "Cannot set version.");
        verify(config, never()).set(anyString(), any());
        verify(playerLore, never()).saveConfig();
        assertFalse(configService.hasBeenAltered());
    }

    @Test
    public void setConfigOption_storesDebugModeAsBoolean() {
        when(config.isSet("debugMode")).thenReturn(true);

        configService.setConfigOption("debugMode", "true", sender);

        verify(config).set("debugMode", true);
        verify(sender).sendMessage(ChatColor.GREEN + "Boolean set.");
        verify(playerLore).saveConfig();
        assertTrue(configService.hasBeenAltered());
    }

    @Test
    public void setConfigOption_storesAnyOtherOptionAsString() {
        // "A" and "C" were once parsed as an Integer and a Double by leftover template branches,
        // even though neither is a real config option. Every option other than version and
        // debugMode is now stored verbatim.
        when(config.isSet("A")).thenReturn(true);

        configService.setConfigOption("A", "5", sender);

        verify(config).set("A", "5");
        verify(sender).sendMessage(ChatColor.GREEN + "String set.");
        assertTrue(configService.hasBeenAltered());
    }

    @Test
    public void saveMissingConfigDefaultsIfNotPresent_seedsVersionAsADefaultWhenAbsent() {
        when(config.isString("version")).thenReturn(false);
        when(playerLore.getVersion()).thenReturn("v2.0.0");

        configService.saveMissingConfigDefaultsIfNotPresent();

        verify(config).addDefault("version", "v2.0.0");
        verify(config, never()).set(eq("version"), any());
    }

    @Test
    public void saveMissingConfigDefaultsIfNotPresent_overwritesAnExistingVersionWithThePluginVersion() {
        // An existing version is deliberately replaced rather than preserved: PlayerLore only
        // reaches this method on a version mismatch, and the stale value is what signalled it.
        when(config.isString("version")).thenReturn(true);
        when(playerLore.getVersion()).thenReturn("v2.0.0");

        configService.saveMissingConfigDefaultsIfNotPresent();

        verify(config).set("version", "v2.0.0");
        verify(config, never()).addDefault(eq("version"), any());
    }

    @Test
    public void saveMissingConfigDefaultsIfNotPresent_seedsDebugModeToFalseWhenAbsent() {
        when(config.isSet("debugMode")).thenReturn(false);

        configService.saveMissingConfigDefaultsIfNotPresent();

        verify(config).set("debugMode", false);
    }

    @Test
    public void saveMissingConfigDefaultsIfNotPresent_leavesAnExistingDebugModeUntouched() {
        when(config.isSet("debugMode")).thenReturn(true);

        configService.saveMissingConfigDefaultsIfNotPresent();

        verify(config, never()).set(eq("debugMode"), any());
    }

    @Test
    public void saveMissingConfigDefaultsIfNotPresent_copiesDefaultsAndWritesTheFile() {
        configService.saveMissingConfigDefaultsIfNotPresent();

        verify(configOptions).copyDefaults(true);
        verify(playerLore).saveConfig();
    }

    @Test
    public void saveMissingConfigDefaultsIfNotPresent_doesNotCountAsAnAlteration() {
        // hasBeenAltered tracks operator edits made through setConfigOption, not the defaults
        // written on startup, so seeding the file leaves it false.
        configService.saveMissingConfigDefaultsIfNotPresent();

        assertFalse(configService.hasBeenAltered());
    }

    @Test
    public void sendConfigList_printsTheVersionAndDebugModeValues() {
        // debugMode is stubbed to "true" rather than "false" on purpose: an unstubbed boolean
        // read would also render as "false", which would leave this assertion unable to tell
        // the two apart.
        when(config.getString("version")).thenReturn("v2.0.0");
        when(config.getString("debugMode")).thenReturn("true");

        configService.sendConfigList(sender);

        verify(sender).sendMessage(ChatColor.AQUA + "=== Config List ===");
        verify(sender).sendMessage(ChatColor.AQUA + "version: v2.0.0, debugMode: true");
    }

    @Test
    public void sendConfigList_readsDebugModeAsAStringRatherThanABoolean() {
        configService.sendConfigList(sender);

        verify(config).getString("debugMode");
        verify(config, never()).getBoolean("debugMode");
    }

    @Test
    public void getIntOrDefault_returnsTheConfiguredValueWhenItIsNonZero() {
        when(config.getInt("someOption")).thenReturn(5);

        assertEquals(5, configService.getIntOrDefault("someOption", 9));
    }

    @Test
    public void getIntOrDefault_returnsTheDefaultWhenTheConfiguredValueIsZero() {
        // A deliberately configured 0 is indistinguishable from an absent option here, so it
        // yields the default rather than 0.
        when(config.getInt("someOption")).thenReturn(0);

        assertEquals(9, configService.getIntOrDefault("someOption", 9));
    }

    @Test
    public void getDoubleOrDefault_returnsTheConfiguredValueWhenItIsNonZero() {
        when(config.getDouble("someOption")).thenReturn(1.5);

        assertEquals(1.5, configService.getDoubleOrDefault("someOption", 9.5));
    }

    @Test
    public void getDoubleOrDefault_returnsTheDefaultWhenTheConfiguredValueIsZero() {
        // Same sentinel behaviour as getIntOrDefault: a configured 0.0 yields the default.
        when(config.getDouble("someOption")).thenReturn(0.0);

        assertEquals(9.5, configService.getDoubleOrDefault("someOption", 9.5));
    }

    @Test
    public void getStringOrDefault_returnsTheConfiguredValueWhenPresent() {
        when(config.getString("someOption")).thenReturn("configured");

        assertEquals("configured", configService.getStringOrDefault("someOption", "fallback"));
    }

    @Test
    public void getStringOrDefault_returnsTheDefaultWhenTheOptionIsAbsent() {
        when(config.getString("someOption")).thenReturn(null);

        assertEquals("fallback", configService.getStringOrDefault("someOption", "fallback"));
    }

    @Test
    public void getStringOrDefault_returnsAnEmptyStringInsteadOfTheDefault() {
        // Unlike the numeric variants, only null falls back — an empty string is returned as-is.
        when(config.getString("someOption")).thenReturn("");

        assertEquals("", configService.getStringOrDefault("someOption", "fallback"));
    }
}
