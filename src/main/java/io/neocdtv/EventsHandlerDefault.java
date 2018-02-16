package io.neocdtv;

import java.util.logging.Logger;

/**
 * EventsHandlerDefault.
 *
 * @author xix
 * @since 21.12.17
 */
public class EventsHandlerDefault implements EventsHandler {

  private final static Logger LOGGER = Logger.getLogger(EventsHandlerDefault.class.getName());

  @Override
  public void onDeviceDiscovery(String deviceName, String controlLocation, String eventsLocation) {
    LOGGER.info(
        String.format("deviceName: %s, controlLocation: %s, eventsLocation: %s",
            deviceName,
            controlLocation,
            eventsLocation));
  }
}
