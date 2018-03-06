package io.neocdtv;

import java.util.Arrays;
import java.util.List;

/**
 * HeaderHelper.
 *
 * @author xix
 * @since 02.01.18
 */
public class HeaderHelper {

  public static String extractHeader(final String name, final String headers) {
    String value = null;
    List<String> headerLines = Arrays.asList(headers.split("\n"));
    for (final String headerLine: headerLines) {
      if (headerLine != null) {
        final String headerLineStart = name + ":"; // TODO: pretty unsecure, isn't it???
        if (headerLine.startsWith(headerLineStart)) {
          return headerLine.trim().replaceFirst(headerLineStart, "").trim();
        }
      }
    }
    return value;
  }

  // TODO: add unit test
  public static void main(String[] args) {
     final String message =   "HTTP/1.1 200\n" +
        "CACHE-CONTROL: no-cache\n" +
        "USN: uuid:3703f8df-39f1-4d87-909b-60a231b8ca6a::urn:schemas-upnp-org:device:LeanPlayer:1\n" +
        "LOCATION: http://localhost/desc.json\n" +
        "SERVER: LeanPlayer\n" +
        "EXT: \n" +
        "ST: urn:schemas-upnp-org:device:MediaRenderer:1\n" +
        "X-Control-Location: http://localhost/rs/control\n" +
        "X-Events-Location: ws://localhost/events";
    String server = HeaderHelper.extractHeader("X-Events-Location", message);
    System.out.println(server.trim());
  }
}
