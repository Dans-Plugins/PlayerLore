package dansplugins.playerlore;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import preponderous.ponder.minecraft.bukkit.abs.AbstractPluginCommand;
import preponderous.ponder.minecraft.bukkit.services.CommandService;

// initializeCommandService decides which /pl subcommands exist and which permission node each
// one checks. Nothing else ties that list to plugin.yml, COMMANDS.md or the USER_GUIDE.md
// permissions table, so a subcommand added, renamed or re-permissioned in code alone would
// compile and pass every other test while the manifest and the docs went stale.
public class PlayerLoreCommandRegistrationTest {

    private List<AbstractPluginCommand> registered;
    private String notFoundMessage;

    @BeforeEach
    @SuppressWarnings("unchecked")
    public void setUp() throws Exception {
        PlayerLore playerLore = mock(PlayerLore.class);
        CommandService commandService = mock(CommandService.class);

        // A mock runs no field initializers, so the command service is put in by hand.
        Field field = PlayerLore.class.getDeclaredField("commandService");
        field.setAccessible(true);
        field.set(playerLore, commandService);

        Method initialize = PlayerLore.class.getDeclaredMethod("initializeCommandService");
        initialize.setAccessible(true);
        initialize.invoke(playerLore);

        ArgumentCaptor<ArrayList<AbstractPluginCommand>> commands = ArgumentCaptor.forClass(ArrayList.class);
        ArgumentCaptor<String> message = ArgumentCaptor.forClass(String.class);
        verify(commandService).initialize(commands.capture(), message.capture());
        registered = commands.getValue();
        notFoundMessage = message.getValue();
    }

    private List<String> registeredNames() {
        List<String> names = new ArrayList<>();
        for (AbstractPluginCommand command : registered) {
            names.addAll(command.getNames());
        }
        return names;
    }

    private Set<String> registeredPermissions() {
        Set<String> permissions = new HashSet<>();
        for (AbstractPluginCommand command : registered) {
            permissions.addAll(command.getPermissions());
        }
        return permissions;
    }

    private static String readProjectFile(String name) throws IOException {
        // Maven runs the tests from the project root, where the docs live.
        return new String(Files.readAllBytes(Paths.get(name)), StandardCharsets.UTF_8);
    }

    @Test
    public void registersExactlyTheDocumentedSubcommands() {
        assertEquals(Arrays.asList("help", "add", "edit", "remove"), registeredNames());
    }

    @Test
    public void eachSubcommandChecksOnlyItsOwnPermissionNode() {
        for (AbstractPluginCommand command : registered) {
            String name = command.getNames().get(0);
            assertEquals(Arrays.asList("pl." + name), command.getPermissions(), "/pl " + name);
        }
    }

    @Test
    public void registeredPermissions_areExactlyThoseDeclaredInPluginYml() throws Exception {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("plugin.yml");
        assertNotNull(stream, "src/main/resources/plugin.yml must be on the classpath");
        // Permission nodes contain dots, so the default '.' path separator would read pl.help as pl -> help.
        YamlConfiguration pluginYml = new YamlConfiguration();
        pluginYml.options().pathSeparator('/');
        pluginYml.load(new InputStreamReader(stream, StandardCharsets.UTF_8));

        assertEquals(pluginYml.getConfigurationSection("permissions").getKeys(false), registeredPermissions());
    }

    @Test
    public void everySubcommand_hasARowInCommandsMdWithItsPermission() throws IOException {
        String commandsMd = readProjectFile("COMMANDS.md");
        for (AbstractPluginCommand command : registered) {
            String name = command.getNames().get(0);
            String permission = command.getPermissions().get(0);
            boolean found = false;
            for (String line : commandsMd.split("\n")) {
                if (line.startsWith("| `/pl " + name) && line.contains("| `" + permission + "` |")) {
                    found = true;
                }
            }
            assertTrue(found, "COMMANDS.md has no row for /pl " + name + " with " + permission);
        }
    }

    @Test
    public void everyPermission_hasARowInTheUserGuidePermissionsTable() throws IOException {
        String userGuide = readProjectFile("USER_GUIDE.md");
        for (String permission : registeredPermissions()) {
            assertTrue(userGuide.contains("| `" + permission + "` |"), "USER_GUIDE.md has no row for " + permission);
        }
    }

    @Test
    public void reportsAnUnknownSubcommandAsNotFound() {
        assertEquals("That command wasn't found.", notFoundMessage);
    }
}
