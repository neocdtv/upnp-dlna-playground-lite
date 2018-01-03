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

  public static String buildControlLocation(final String baseUrl) {
    return "http://" + baseUrl + "/rs/control";
  }

  public static String buildEventsLocation(final String baseUrl) {
    return "ws://" + baseUrl + "/events";
  }
}
