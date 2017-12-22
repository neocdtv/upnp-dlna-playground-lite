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
        // TODO: is OK at the end of the header required???
        append(NEW_LINE);
    return this;
  }

  public UpnpPayloadBuilder usn(final String usn) {
    addHeader(SsdpConstants.HTTP_HEADER_NAME_UNIQUE_SERVICE_NAME, usn);
    return this;
  }

  public UpnpPayloadBuilder host(final String host) {
    addHeader(HttpConstants.HTTP_HEADER_NAME_HOST, host);
    return this;
  }

  public UpnpPayloadBuilder cacheControl(final String cacheControl) {
    addHeader(HttpConstants.HTTP_HEADER_NAME_CACHE_CONTROL, cacheControl);
    return this;
  }

  public UpnpPayloadBuilder location(final String location) {
    addHeader(HttpConstants.HTTP_HEADER_NAME_LOCATION, location);
    return this;
  }

  public UpnpPayloadBuilder server(final String server) {
    addHeader(HttpConstants.HTTP_HEADER_NAME_SERVER, server);
    return this;
  }

  public UpnpPayloadBuilder nt(final String nt) {
    addHeader(GenaConstants.HTTP_HEADER_NAME_NOTIFICATION_TYPE, nt);
    return this;
  }

  public UpnpPayloadBuilder st(final String st) {
    addHeader(UpnpHelper.ST, st);
    return this;
  }

  public UpnpPayloadBuilder ext(final String ext) {
    addHeader(UpnpHelper.EXT, ext);
    return this;
  }

  public UpnpPayloadBuilder mx(final String mx) {
    addHeader(UpnpHelper.MX, mx);
    return this;
  }

  public UpnpPayloadBuilder man(final String man) {
    addHeader(UpnpHelper.MAN, man);
    return this;
  }

  public UpnpPayloadBuilder nts(final String st) {
    addHeader(GenaConstants.HTTP_HEADER_NAME_NOTIFICATION_SUB_TYPE, st);
    return this;
  }


  public String build() {
    return response.
        append(NEW_LINE).
        toString();
  }

  public void addHeader(final String name, final String value) {
    response.
        append(name).
        append(": ").
        append(value).
        append(NEW_LINE);
  }
}
