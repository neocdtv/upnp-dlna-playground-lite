package io.neocdtv.constants;

/**
 * LocationHelper.
 *
 * @author xix
 * @since 02.01.18
 */
public class LocationHelper {
  public static String buildLocation(final String baseUrl) {
    return "http://" + baseUrl + "/desc.json";
  }

  // TODO: hardcoded api/control needs to be configured dynamically
  public static String buildControlLocation(final String baseUrl) {
    return "http://" + baseUrl + "/api/control";
  }

  public static String buildEventsLocation(final String baseUrl) {
    return "ws://" + baseUrl + "/events";
  }
}
