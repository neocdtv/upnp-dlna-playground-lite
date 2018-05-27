package io.neocdtv.upnp.discovery;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * TrafficLogger.
 *
 * @author xix
 * @since 21.12.17
 */
public class TrafficLogger {
  private final static Logger LOGGER = Logger.getLogger(TrafficLogger.class.getName());

  public static void logSent(final String sent) {
    LOGGER.log(Level.INFO, "sending:\n" + sent);
  }

  public static void logReceived(final String received) {
    LOGGER.log(Level.INFO, "receiving\n" + received);
  }
}
