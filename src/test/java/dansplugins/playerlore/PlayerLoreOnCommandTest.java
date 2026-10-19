package dansplugins.playerlore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.util.Collections;

import dansplugins.playerlore.trace.TraceClient;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

// onCommand is the single entry point for /pl and /playerlore. It sends a bare
// /pl straight to DefaultCommand, bypassing the command service, and reports
// every command to trace -- with the command's name only, which is what the
// README's "neither is anything typed after a command" promise rests on.
public class PlayerLoreOnCommandTest {

    private PlayerLore playerLore;
    private CommandService commandService;
    private TraceClient trace;
    private CommandSender sender;
    private Command command;

    @BeforeEach
    public void setUp() throws Exception {
        playerLore = mock(PlayerLore.class);
        commandService = mock(CommandService.class);
        trace = mock(TraceClient.class);
        sender = mock(CommandSender.class);
        command = mock(Command.class);

        // A mock runs no field initializers, so the collaborators onCommand uses are put in by hand.
        setField("commandService", commandService);
        setField("trace", trace);

        when(playerLore.onCommand(any(), any(), anyString(), any())).thenCallRealMethod();
        when(playerLore.getVersion()).thenReturn("v2.1.3");
        when(command.getName()).thenReturn("pl");
    }

    private void setField(String name, Object value) throws Exception {
        Field field = PlayerLore.class.getDeclaredField(name);
        field.setAccessible(true);
        field.set(playerLore, value);
    }

    @Test
    public void onCommand_withNoArgs_showsTheDefaultCommandOutput() {
        boolean result = playerLore.onCommand(sender, command, "pl", new String[]{});

        assertTrue(result);
        verify(sender).sendMessage(ChatColor.AQUA + "PlayerLore v2.1.3");
        verify(sender).sendMessage(ChatColor.AQUA + "For a list of commands, type /pl help");
    }

    @Test
    public void onCommand_withNoArgs_doesNotGoThroughTheCommandService() {
        playerLore.onCommand(sender, command, "pl", new String[]{});

        verify(commandService, never()).interpretAndExecuteCommand(any(), anyString(), any());
    }

    @Test
    public void onCommand_withArgs_handsThemToTheCommandService() {
        String[] args = {"add", "\"forged", "in", "fire\""};
        when(commandService.interpretAndExecuteCommand(sender, "pl", args)).thenReturn(true);

        boolean result = playerLore.onCommand(sender, command, "pl", args);

        assertTrue(result);
        verify(commandService).interpretAndExecuteCommand(sender, "pl", args);
        verify(sender, never()).sendMessage(ChatColor.AQUA + "PlayerLore v2.1.3");
    }

    @Test
    public void onCommand_withArgs_returnsTheCommandServiceResult() {
        String[] args = {"remove", "9"};
        when(commandService.interpretAndExecuteCommand(sender, "pl", args)).thenReturn(false);

        assertFalse(playerLore.onCommand(sender, command, "pl", args));
    }

    @Test
    public void onCommand_withNoArgs_reportsTheCommandName() {
        playerLore.onCommand(sender, command, "pl", new String[]{});

        verify(trace).report("command", null, Collections.singletonMap("name", "pl"));
    }

    @Test
    public void onCommand_withArgs_reportsTheCommandNameAndNothingTyped() {
        // The exact-map match fails if any argument, or the subcommand, were added as a tag.
        playerLore.onCommand(sender, command, "pl", new String[]{"add", "\"secret", "lore\""});

        verify(trace).report("command", null, Collections.singletonMap("name", "pl"));
    }

    @Test
    public void onCommand_reportsTheRegisteredNameRatherThanTheAliasTyped() {
        when(command.getName()).thenReturn("playerlore");

        playerLore.onCommand(sender, command, "PL", new String[]{});

        verify(trace).report("command", null, Collections.singletonMap("name", "playerlore"));
    }
}
