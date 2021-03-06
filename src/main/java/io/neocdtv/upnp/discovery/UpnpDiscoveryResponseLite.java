package io.neocdtv.upnp.discovery;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UpnpDiscoveryResponseLite.
 *
 * @author xix
 * @since 21.12.17
 */
public class UpnpDiscoveryResponseLite extends Thread {

  private String uuid;
  private String baseUrl;

  public void setBaseUrl(String baseUrl) {
    this.baseUrl = baseUrl;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public static void main(String[] args) throws InterruptedException {
    startIt(UpnpHelper.buildUuid(),"localhost:1234");
  }

  public static void startIt(final String uuid, final String baseUrl) {
    final UpnpDiscoveryResponseLite upnpDiscoveryResponseLite = new UpnpDiscoveryResponseLite();
    upnpDiscoveryResponseLite.setUuid(uuid);
    upnpDiscoveryResponseLite.setBaseUrl(baseUrl);
    upnpDiscoveryResponseLite.start();
  }

  @Override
  public void run() {
    try {
      MulticastSocket socket = new MulticastSocket(SsdpConstants.MULTICAST_PORT);
      InetAddress group = InetAddress.getByName(SsdpConstants.MULTICAST_IP);

      socket.joinGroup(group);

      while (true) {
        byte[] requestBytes = new byte[NetworkConstants.BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(requestBytes, requestBytes.length);
        socket.receive(packet);

        final String receivedMessage = new String(requestBytes, 0, packet.getLength());
        TrafficLogger.logReceived(receivedMessage);

        if (receivedMessage.contains(GenaConstants.HTTP_METHOD_SEARCH) &&
            receivedMessage.contains(UpnpHelper.MEDIA_RENDERER)) {
          final String discoveryResponse =
              UpnpPayloadFactory.create(uuid).createLeanPlayerDiscoveryResponse(
                  LocationHelper.buildLocation(baseUrl),
                  LeanPlayerConstants.NAME,
                  LocationHelper.buildControlLocation(baseUrl),
                  LocationHelper.buildEventsLocation(baseUrl));

          send(packet.getAddress(), packet.getPort(), discoveryResponse);
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(UpnpDiscoveryResponseLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private static void send(InetAddress address, int port, String discoveryResponse) throws IOException {
    final DatagramSocket datagramSocket = new DatagramSocket();
    datagramSocket.setReuseAddress(true);

    byte[] discoveryResponseBytes = discoveryResponse.getBytes(StandardCharsets.UTF_8.displayName());
    DatagramPacket responseToDiscover = new DatagramPacket(discoveryResponseBytes, discoveryResponseBytes.length, address, port);
    datagramSocket.send(responseToDiscover);
    TrafficLogger.logSent(discoveryResponse);
  }
}