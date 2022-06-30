package dansplugins.playerlore.utils;

import dansplugins.playerlore.PlayerLore;

import java.util.logging.Level;

/**
 * @author Daniel McCoy Stephenson
 */
public class Logger {
    private final PlayerLore playerLore;

    public Logger(PlayerLore playerLore) {
        this.playerLore = playerLore;
    }

    public void log(String message) {
        if (playerLore.isDebugEnabled()) {
            playerLore.getLogger().log(Level.INFO, "[PlayerLore] " + message);
        }
    }
}