package dansplugins.playerlore.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import dansplugins.playerlore.PlayerLore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

public class DefaultCommandTest {

    @Test
    public void execute_pointsPlayersToTheRegisteredHelpCommand() {
        PlayerLore playerLore = mock(PlayerLore.class);
        CommandSender commandSender = mock(CommandSender.class);
        DefaultCommand defaultCommand = new DefaultCommand(playerLore);

        defaultCommand.execute(commandSender);

        verify(commandSender).sendMessage(contains("/pl help"));
    }

    @Test
    public void execute_advertisesTheWikiUnderTheCurrentRepositoryOwner() {
        PlayerLore playerLore = mock(PlayerLore.class);
        CommandSender commandSender = mock(CommandSender.class);
        DefaultCommand defaultCommand = new DefaultCommand(playerLore);

        defaultCommand.execute(commandSender);

        verify(commandSender).sendMessage(contains("https://github.com/Dans-Plugins/PlayerLore/wiki"));
    }

    @Test
    public void execute_showsThePluginVersion() {
        PlayerLore playerLore = mock(PlayerLore.class);
        when(playerLore.getVersion()).thenReturn("v9.8.7");
        CommandSender commandSender = mock(CommandSender.class);
        DefaultCommand defaultCommand = new DefaultCommand(playerLore);

        boolean result = defaultCommand.execute(commandSender);

        assertTrue(result);
        verify(commandSender).sendMessage(ChatColor.AQUA + "PlayerLore v9.8.7");
    }

    @Test
    public void execute_withArgs_ignoresThemAndShowsTheSameOutput() {
        PlayerLore playerLore = mock(PlayerLore.class);
        when(playerLore.getVersion()).thenReturn("v9.8.7");
        CommandSender commandSender = mock(CommandSender.class);
        DefaultCommand defaultCommand = new DefaultCommand(playerLore);

        boolean result = defaultCommand.execute(commandSender, new String[]{"anything"});

        assertTrue(result);
        verify(commandSender).sendMessage(ChatColor.AQUA + "PlayerLore v9.8.7");
        verify(commandSender).sendMessage(contains("/pl help"));
    }
}
