package dansplugins.playerlore.utils;

import dansplugins.playerlore.PlayerLore;

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
            System.out.println("[PlayerLore] " + message);
        }
    }

}
