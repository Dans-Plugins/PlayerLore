package dansplugins.playerlore.utils;

import dansplugins.playerlore.PlayerLore;

import java.util.logging.Level;

/**
 * @author Daniel McCoy Stephenson
 */
public class Logger {

    private static Logger instance;

    private Logger() {

    }

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String message) {
        if (PlayerLore.getInstance().isDebugEnabled()) {
            PlayerLore.getInstance().getLogger().log(Level.INFO, "[PlayerLore] " + message);
        }
    }
}