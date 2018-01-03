package io.neocdtv.constants;

/**
 * UpnpPayloadFactory.
 *
 * @author xix
 * @since 21.12.17
 */
public class UpnpPayloadFactory {
  private final String uuid;

  public UpnpPayloadFactory(final String uuid) {
    this.uuid = uuid;
  }

  public static UpnpPayloadFactory create(final String uuid) {
    return new UpnpPayloadFactory(uuid);
  }


  public String createLeanPlayerNotifyRequest(final String location, final String name, final String controlLocation, final String eventLocation) {
    return createNotifyRequest(location, name, UpnpHelper.MEDIA_RENDERER_LEANPLAYER).
        addHeader(LeanPlayerConstants.HTTP_HEADER_NAME_CONTROL_LOCATION, controlLocation).
        addHeader(LeanPlayerConstants.HTTP_HEADER_NAME_EVENTS_LOCATION, eventLocation).
        build();
  }

  private UpnpPayloadBuilder createNotifyRequest(final String location, final String name, final String deviceType) {
    return UpnpPayloadBuilder.
        create().
        method(GenaConstants.HTTP_METHOD_NOTIFY).
        cacheControl(HttpConstants.HTTP_HEADER_VALUE_NO_CACHE).
        host(SsdpConstants.MULTICAST_ADDRESS).
        usn(UpnpHelper.buildUsn(uuid, deviceType)).
        location(location).
        server(name).
        nt(UpnpHelper.buildNt(uuid, deviceType)).
        nts(SsdpConstants.SSDP_ALIVE);
  }

  public String createMediaRendererDiscoveryRequest() {
    return createDiscoveryRequest(UpnpHelper.MEDIA_RENDERER);
  }

  public String createLeanPlayerDiscoveryRequest() {
    return createDiscoveryRequest(UpnpHelper.MEDIA_RENDERER_LEANPLAYER);
  }

  private String createDiscoveryRequest(final String deviceType) {
    return UpnpPayloadBuilder.
        create().
        method(GenaConstants.HTTP_METHOD_SEARCH).
        mx("3"). // TODO: what does it stand for???
        st(deviceType).
        host(SsdpConstants.MULTICAST_ADDRESS).
        man("\"" + SsdpConstants.SSDP_DISCOVER + "\"").
        build();
  }

  public String createMediaRendererDiscoveryResponse(final String location, final String serverName) {
    return createDiscoveryResponse(location, serverName, UpnpHelper.MEDIA_RENDERER).build();
  }

  public String createLeanPlayerDiscoveryResponse(final String location, final String serverName, final String controlLocation, final String eventLocation) {
    return createDiscoveryResponse(location, serverName, UpnpHelper.MEDIA_RENDERER_LEANPLAYER).
        addHeader(LeanPlayerConstants.HTTP_HEADER_NAME_CONTROL_LOCATION, controlLocation).
        addHeader(LeanPlayerConstants.HTTP_HEADER_NAME_EVENTS_LOCATION, eventLocation).
        build();
  }

  private UpnpPayloadBuilder createDiscoveryResponse(String location, String server, String deviceType) {
    return UpnpPayloadBuilder.
        create().
        status("200"). // TODO check if this is correct
        cacheControl(HttpConstants.HTTP_HEADER_VALUE_NO_CACHE).
        usn(UpnpHelper.buildUsn(uuid, deviceType)).
        location(location).
        server(server).
        ext("").
        st(UpnpHelper.MEDIA_RENDERER);
  }

  public String createMediaRendererShutdownRequest() {
    // SsdpConstants.SSDP_BYEBYE;
    return null;
  }
}