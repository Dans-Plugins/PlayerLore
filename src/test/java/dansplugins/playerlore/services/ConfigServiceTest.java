package dansplugins.playerlore.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import dansplugins.playerlore.PlayerLore;

public class ConfigServiceTest {

    private ConfigService configService;
    private PlayerLore playerLore;
    private FileConfiguration config;
    private CommandSender sender;

    @BeforeEach
    public void setUp() {
        playerLore = mock(PlayerLore.class);
        config = mock(FileConfiguration.class);
        sender = mock(CommandSender.class);

        when(playerLore.getConfig()).thenReturn(config);
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
}
