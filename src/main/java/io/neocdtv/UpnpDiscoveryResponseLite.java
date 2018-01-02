package io.neocdtv;

import io.neocdtv.constants.GenaConstants;
import io.neocdtv.constants.NetworkConstants;
import io.neocdtv.constants.SsdpConstants;
import io.neocdtv.constants.UpnpHelper;
import io.neocdtv.constants.UpnpPayloadFactory;

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
  private String location;
  private String serverName;

  public void setLocation(String location) {
    this.location = location;
  }

  public void setServerName(String serverName) {
    this.serverName = serverName;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public static void main(String[] args) throws InterruptedException {
    startIt(UpnpHelper.buildUuid(),"http://localhost/device-description", "Zenplayer Version 0.1");
  }

  public static void startIt(final String uuid, final String location, final String serverName) {
    final UpnpDiscoveryResponseLite UpnpDiscoveryResponseLite = new UpnpDiscoveryResponseLite();
    UpnpDiscoveryResponseLite.setUuid(uuid);
    UpnpDiscoveryResponseLite.setLocation(location);
    UpnpDiscoveryResponseLite.setServerName(serverName);
    UpnpDiscoveryResponseLite.start();
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
          UpnpPayloadFactory.create(uuid).createMediaRendererDiscoveryResponse(location, serverName);

          // get ip address of recipient
          String responseRecipientAddress = packet.getAddress().getHostAddress();
          // get port of recipient

          // send response

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