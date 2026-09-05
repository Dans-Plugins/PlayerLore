package dansplugins.playerlore.commands;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.Test;

public class HelpCommandTest {

    @Test
    public void execute_noArgs_listsAllCommands() {
        HelpCommand helpCommand = new HelpCommand();
        CommandSender commandSender = mock(CommandSender.class);

        boolean result = helpCommand.execute(commandSender);

        assertTrue(result);
        verify(commandSender).sendMessage(contains("/pl - View the plugin version, credits and wiki link."));
        verify(commandSender).sendMessage(contains("/pl help"));
        verify(commandSender).sendMessage(contains("/pl add"));
        verify(commandSender).sendMessage(contains("/pl edit"));
        verify(commandSender).sendMessage(contains("/pl remove"));
    }

    @Test
    public void execute_withArgs_delegatesToNoArgVersion() {
        HelpCommand helpCommand = new HelpCommand();
        CommandSender commandSender = mock(CommandSender.class);

        boolean result = helpCommand.execute(commandSender, new String[]{"ignored"});

        assertTrue(result);
        verify(commandSender).sendMessage(contains("/pl help"));
    }
}
