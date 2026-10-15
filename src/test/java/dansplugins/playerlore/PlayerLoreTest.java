package dansplugins.playerlore;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.bukkit.configuration.file.FileConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// isVersionMismatched decides whether onEnable rewrites config.yml with the
// missing defaults, so it is what makes an upgraded server pick up new options.
public class PlayerLoreTest {

    private PlayerLore playerLore;
    private FileConfiguration config;

    @BeforeEach
    public void setUp() {
        playerLore = mock(PlayerLore.class);
        config = mock(FileConfiguration.class);

        when(playerLore.getConfig()).thenReturn(config);
        when(playerLore.isVersionMismatched()).thenCallRealMethod();
    }

    @Test
    public void isVersionMismatched_isFalseWhenTheConfigVersionMatches() {
        when(config.getString("version")).thenReturn("v2.1.1");
        when(playerLore.getVersion()).thenReturn("v2.1.1");

        assertFalse(playerLore.isVersionMismatched());
    }

    @Test
    public void isVersionMismatched_isTrueWhenTheConfigVersionDiffers() {
        when(config.getString("version")).thenReturn("v2.1.0");
        when(playerLore.getVersion()).thenReturn("v2.1.1");

        assertTrue(playerLore.isVersionMismatched());
    }

    @Test
    public void isVersionMismatched_ignoresCase() {
        when(config.getString("version")).thenReturn("V2.1.1-SNAPSHOT");
        when(playerLore.getVersion()).thenReturn("v2.1.1-snapshot");

        assertFalse(playerLore.isVersionMismatched());
    }

    @Test
    public void isVersionMismatched_isFalseWhenTheConfigHasNoVersion() {
        when(config.getString("version")).thenReturn(null);
        when(playerLore.getVersion()).thenReturn("v2.1.1");

        assertFalse(playerLore.isVersionMismatched());
    }

    @Test
    public void isVersionMismatched_isFalseWhenThePluginVersionIsUnknown() {
        when(config.getString("version")).thenReturn("v2.1.0");
        when(playerLore.getVersion()).thenReturn(null);

        assertFalse(playerLore.isVersionMismatched());
    }
}
