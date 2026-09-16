package dansplugins.playerlore.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import dansplugins.playerlore.PlayerLore;
import org.bukkit.configuration.file.YamlConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pins the on-disk behaviour that ConfigService's usage-reporting getters rely on, against the
 * real YamlConfiguration and the real bundled config.yml rather than a mock.
 *
 * PlayerLore never calls saveDefaultConfig(); config.yml is written by
 * ConfigService.saveMissingConfigDefaultsIfNotPresent, and only on a first run or a version
 * mismatch. So a server upgraded in place from before usage reporting keeps a config.yml with no
 * usage-reporting block. The plugin still has to read the bundled key on such a server, and the
 * plugin's own version/debugMode seeding must keep working now that a config.yml ships in the jar.
 */
public class UsageReportingConfigTest {

    private static final String BUNDLED_KEY = "iiS1wo6BOAd7vQ1NN7iEoIVo_i6bkB6GI4Oeuqm1JX4";

    /** What an installation from before usage reporting has on disk. */
    private static final String PRE_EXISTING_FILE = "version: v1.1\ndebugMode: false\n";

    private YamlConfiguration bundled;

    @BeforeEach
    public void setUp() {
        InputStream stream = getClass().getClassLoader().getResourceAsStream("config.yml");
        assertNotNull(stream, "src/main/resources/config.yml must be on the classpath");
        bundled = YamlConfiguration.loadConfiguration(new InputStreamReader(stream, StandardCharsets.UTF_8));
    }

    private YamlConfiguration loadWithBundledDefaults(String onDisk) {
        // Mirrors JavaPlugin.reloadConfig(): the file's contents, with the jar's config.yml as defaults.
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(onDisk);
        } catch (org.bukkit.configuration.InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        config.setDefaults(bundled);
        return config;
    }

    @Test
    public void bundledConfig_carriesTheUsageReportingBlock() {
        assertTrue(bundled.getBoolean("usage-reporting.enabled"));
        assertEquals("https://trace.danielstephenson.dev", bundled.getString("usage-reporting.endpoint"));
        assertEquals(BUNDLED_KEY, bundled.getString("usage-reporting.key"));
    }

    @Test
    public void bundledConfig_doesNotCarryTheSeededOptions() {
        // version and debugMode are seeded by saveMissingConfigDefaultsIfNotPresent, which
        // decides what to write with isString/isSet. Once copyDefaults(true) is on, isSet also
        // sees defaults -- so bundling either option would silently stop it being seeded.
        assertFalse(bundled.contains("version"));
        assertFalse(bundled.contains("debugMode"));
    }

    @Test
    public void oneArgumentGetters_fallThroughToTheBundledKeyWhenTheFileHasNoBlock() {
        YamlConfiguration config = loadWithBundledDefaults(PRE_EXISTING_FILE);

        assertTrue(config.getBoolean("usage-reporting.enabled"));
        assertEquals("https://trace.danielstephenson.dev", config.getString("usage-reporting.endpoint"));
        assertEquals(BUNDLED_KEY, config.getString("usage-reporting.key"));
    }

    @Test
    public void twoArgumentGetters_returnTheirFallbackInsteadOfTheBundledKey() {
        // This is the trap: getString(path, def) would hand back "" and turn reporting off on
        // every server whose config.yml predates the block.
        YamlConfiguration config = loadWithBundledDefaults(PRE_EXISTING_FILE);

        assertEquals("", config.getString("usage-reporting.key", ""));
        assertFalse(config.getBoolean("usage-reporting.enabled", false));
    }

    @Test
    public void configuredValues_winOverTheBundledDefaults() {
        YamlConfiguration config = loadWithBundledDefaults(PRE_EXISTING_FILE
                + "usage-reporting:\n  enabled: false\n  endpoint: http://localhost:8080\n  key: abc\n");

        assertFalse(config.getBoolean("usage-reporting.enabled"));
        assertEquals("http://localhost:8080", config.getString("usage-reporting.endpoint"));
        assertEquals("abc", config.getString("usage-reporting.key"));
    }

    private YamlConfiguration reload(String written) {
        YamlConfiguration reloaded = new YamlConfiguration();
        try {
            reloaded.loadFromString(written);
        } catch (org.bukkit.configuration.InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        return reloaded;
    }

    @Test
    public void missingBlock_isWrittenToDiskFromTheBundledDefaults() {
        // The in-place upgrade case: the getters fell through to the bundled key, but the file
        // had no usage-reporting block, so the switch the console line points at did not exist.
        YamlConfiguration config = loadWithBundledDefaults(PRE_EXISTING_FILE);
        PlayerLore playerLore = mock(PlayerLore.class);
        when(playerLore.getConfig()).thenReturn(config);

        new ConfigService(playerLore).saveUsageReportingDefaultsIfNotPresent();

        // copyDefaults is off here, so what saveToString() emits is exactly what is on disk.
        YamlConfiguration reloaded = reload(config.saveToString());
        assertTrue(reloaded.isSet("usage-reporting"));
        assertTrue(reloaded.getBoolean("usage-reporting.enabled"));
        assertEquals("https://trace.danielstephenson.dev", reloaded.getString("usage-reporting.endpoint"));
        assertEquals(BUNDLED_KEY, reloaded.getString("usage-reporting.key"));
        assertEquals("v1.1", reloaded.getString("version"));
        verify(playerLore).saveConfig();
    }

    @Test
    public void existingBlock_isLeftAloneAndNotResaved() {
        YamlConfiguration config = loadWithBundledDefaults(PRE_EXISTING_FILE
                + "usage-reporting:\n  enabled: false\n  endpoint: http://localhost:8080\n  key: abc\n");
        PlayerLore playerLore = mock(PlayerLore.class);
        when(playerLore.getConfig()).thenReturn(config);

        new ConfigService(playerLore).saveUsageReportingDefaultsIfNotPresent();

        assertFalse(config.getBoolean("usage-reporting.enabled"));
        assertEquals("abc", config.getString("usage-reporting.key"));
        verify(playerLore, never()).saveConfig();
    }

    @Test
    public void copyDefaults_writesTheBlockToDiskAndLeavesSeedingAlone() {
        // saveMissingConfigDefaultsIfNotPresent turns copyDefaults on before saving, so a first
        // run or a version mismatch lands the block in the file -- while the isSet check that
        // guards debugMode seeding still reports it missing.
        YamlConfiguration config = loadWithBundledDefaults("");
        config.options().copyDefaults(true);

        assertFalse(config.isSet("debugMode"));
        assertFalse(config.isString("version"));

        String written = config.saveToString();
        YamlConfiguration reloaded = new YamlConfiguration();
        try {
            reloaded.loadFromString(written);
        } catch (org.bukkit.configuration.InvalidConfigurationException e) {
            throw new IllegalStateException(e);
        }
        assertEquals(BUNDLED_KEY, reloaded.getString("usage-reporting.key"));
        assertTrue(reloaded.getBoolean("usage-reporting.enabled"));
    }
}
