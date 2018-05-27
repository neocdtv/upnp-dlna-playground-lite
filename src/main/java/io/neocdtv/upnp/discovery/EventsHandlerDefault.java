package io.neocdtv.upnp.discovery;

import java.util.logging.Logger;

/**
 * EventsHandlerDefault.
 *
 * @author xix
 * @since 21.12.17
 */
public class EventsHandlerDefault implements DeviceDiscoveryEventsHandler {

  private final static Logger LOGGER = Logger.getLogger(EventsHandlerDefault.class.getName());

  @Override
  public void onDeviceDiscovery(String payload, final String deviceAddress) {
    LOGGER.info(
        String.format("payload: %s, deviceAddress: %s", payload, deviceAddress));
  }
}
