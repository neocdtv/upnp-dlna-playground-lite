package io.neocdtv;

import io.neocdtv.constants.NetworkConstants;
import io.neocdtv.constants.SsdpConstants;
import io.neocdtv.constants.UpnpHelper;
import io.neocdtv.constants.UpnpPayloadFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UpnpDiscoveryLite.
 *
 * @author xix
 * @since 21.12.17
 */
public class UpnpDiscoveryLite extends Thread {

  private EventsHandler eventsHandler;
  private String uuid;

  public static void main(String[] args) throws InterruptedException {
    startIt(new EventsHandlerDefault());
  }

  public static void startIt(final EventsHandler eventsHandler) {
    UpnpDiscoveryLite upnpDiscoveryLite = new UpnpDiscoveryLite();
    upnpDiscoveryLite.setEventsHandler(eventsHandler);
    upnpDiscoveryLite.setUuid(UpnpHelper.buildUuid());
    upnpDiscoveryLite.start();
  }

  public void setEventsHandler(EventsHandler eventsHandler) {
    this.eventsHandler = eventsHandler;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  @Override
  public void run() {

    try {
      InetAddress multicastAddress = Inet4Address.getByName(SsdpConstants.MULTICAST_IP);
      final DatagramSocket datagramSocket = new DatagramSocket();
      datagramSocket.setBroadcast(true);
      datagramSocket.setReuseAddress(true);

      UpnpPayloadFactory upnpPayloadFactory = UpnpPayloadFactory.
          create(uuid);
      send(multicastAddress, datagramSocket, upnpPayloadFactory.createMediaRendererDiscoveryRequest());

      while (true) {
        byte[] bytes = new byte[NetworkConstants.BUFFER_SIZE];
        DatagramPacket packet = new DatagramPacket(bytes, bytes.length);

        final String receivedMessage = receiveMessage(datagramSocket, bytes, packet);
        TrafficLogger.logReceived(receivedMessage);

        String deviceAddress = packet.getAddress().getHostAddress();
        final String location = extractLocation(receivedMessage);
        String deviceDescription = readDeviceDescription(location);
        eventsHandler.onDeviceDiscovery(deviceAddress, deviceDescription);
      }
    } catch (IOException ex) {
      Logger.getLogger(UpnpDiscoveryLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private String readDeviceDescription(final String location) {
    return "{}";
  }

  private String extractLocation(String receivedMessage) {
    return null;
  }

  private String receiveMessage(DatagramSocket msocket, byte[] inbuf, DatagramPacket packet) throws IOException {
    msocket.receive(packet);
    return new String(inbuf, 0, packet.getLength());
  }

  private static void send(final InetAddress broadcastAddress, final DatagramSocket datagramSocket, final String payload) throws IOException {
    byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8.displayName());
    DatagramPacket datagramPacket = new DatagramPacket(payloadBytes, payloadBytes.length, broadcastAddress, SsdpConstants.MULTICAST_PORT);
    datagramSocket.send(datagramPacket);
    TrafficLogger.logSent(payload);
  }
}
