package dansplugins.playerlore.commands;

import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import dansplugins.playerlore.PlayerLore;
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
}
