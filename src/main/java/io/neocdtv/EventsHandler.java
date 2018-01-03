package io.neocdtv;

/**
 * EventsHandler.
 *
 * @author xix
 * @since 21.12.17
 */
public interface EventsHandler {
  void onDeviceDiscovery(
      final String deviceName,
      final String location,
      final String controlLocation,
      final String eventsLocation);
}
