package io.neocdtv.constants;

/**
 * UpnpPayloadBuilder.
 *
 * @author xix
 * @since 21.12.17
 */
public class UpnpPayloadBuilder {

  private final static String NEW_LINE = "\r\n";
  private final StringBuffer response;

  private UpnpPayloadBuilder() {
    response = new StringBuffer();
  }

  public static UpnpPayloadBuilder create() {
    return new UpnpPayloadBuilder();
  }

  public UpnpPayloadBuilder method(final String method) {
    response.
        append(method).
        append(" ").
        append("* HTTP/1.1").
        append(NEW_LINE);
    return this;
  }

  public UpnpPayloadBuilder status(final String status) {
    response.
        append("HTTP/1.1").
        append(" ").
        append(status).
        append(NEW_LINE);
    return this;
  }

  public UpnpPayloadBuilder usn(final String usn) {
    return addHeader(SsdpConstants.HTTP_HEADER_NAME_UNIQUE_SERVICE_NAME, usn);
  }

  public UpnpPayloadBuilder host(final String host) {
    return addHeader(HttpConstants.HTTP_HEADER_NAME_HOST, host);
  }

  public UpnpPayloadBuilder cacheControl(final String cacheControl) {
    return addHeader(HttpConstants.HTTP_HEADER_NAME_CACHE_CONTROL, cacheControl);
  }

  public UpnpPayloadBuilder location(final String location) {
    return addHeader(HttpConstants.HTTP_HEADER_NAME_LOCATION, location);
  }

  public UpnpPayloadBuilder server(final String server) {
    return addHeader(HttpConstants.HTTP_HEADER_NAME_SERVER, server);
  }

  public UpnpPayloadBuilder nt(final String nt) {
    return addHeader(GenaConstants.HTTP_HEADER_NAME_NOTIFICATION_TYPE, nt);
  }

  public UpnpPayloadBuilder st(final String st) {
    return addHeader(UpnpHelper.ST, st);
  }

  public UpnpPayloadBuilder ext(final String ext) {
    return addHeader(UpnpHelper.EXT, ext);
  }

  public UpnpPayloadBuilder mx(final String mx) {
    return addHeader(UpnpHelper.MX, mx);
  }

  public UpnpPayloadBuilder man(final String man) {
    return addHeader(UpnpHelper.MAN, man);
  }

  public UpnpPayloadBuilder nts(final String st) {
    return addHeader(GenaConstants.HTTP_HEADER_NAME_NOTIFICATION_SUB_TYPE, st);
  }

  public String build() {
    return response.
        append(NEW_LINE).
        toString();
  }

  public UpnpPayloadBuilder addHeader(final String name, final String value) {
    response.
        append(name).
        append(": ").
        append(value).
        append(NEW_LINE);
    return this;
  }
}
