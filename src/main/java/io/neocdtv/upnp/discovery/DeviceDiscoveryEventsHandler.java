package io.neocdtv.upnp.discovery;

/**
 * EventsHandler.
 *
 * @author xix
 * @since 21.12.17
 */
public interface DeviceDiscoveryEventsHandler {
  void onDeviceDiscovery(
      final String payload,
      final String deviceAddress);
}
