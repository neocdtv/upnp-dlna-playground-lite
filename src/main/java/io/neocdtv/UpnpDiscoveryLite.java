package io.neocdtv;

import io.neocdtv.constants.GenaConstants;
import io.neocdtv.constants.HeaderHelper;
import io.neocdtv.constants.HttpConstants;
import io.neocdtv.constants.LeanPlayerConstants;
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
    startIt(new EventsHandlerDefault(), UpnpHelper.buildUuid());
  }

  public static void startIt(final EventsHandler eventsHandler, final String uuid) {
    UpnpDiscoveryLite upnpDiscoveryLite = new UpnpDiscoveryLite();
    upnpDiscoveryLite.setEventsHandler(eventsHandler);
    upnpDiscoveryLite.setUuid(uuid);
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

        // TODO: is device address getting from packet.getAddress().getHostAddress() and adding to location, controlLocation, eventsLocation
        // or get just the absolute addresses in location, controlLocation, eventsLocation
        String deviceAddress = packet.getAddress().getHostAddress();
        final String deviceName = extractDeviceName(receivedMessage);
        final String controlLocation = extractControlLocation(receivedMessage);
        final String eventsLocation = extractEventsLocation(receivedMessage);

          if (receivedMessage.contains(UpnpHelper.MEDIA_RENDERER) &&
              receivedMessage.contains(LeanPlayerConstants.HTTP_HEADER_NAME_CONTROL_LOCATION) &&
              receivedMessage.contains(LeanPlayerConstants.HTTP_HEADER_NAME_EVENTS_LOCATION)) {
            eventsHandler.onDeviceDiscovery(deviceName, controlLocation, eventsLocation);
          }
      }
    } catch (IOException ex) {
      Logger.getLogger(UpnpDiscoveryLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private String extractDeviceName(final String receivedMessage) {
    return HeaderHelper.extractHeader(HttpConstants.HTTP_HEADER_NAME_SERVER, receivedMessage);
  }

  private String extractControlLocation(final String receivedMessage) {
    return HeaderHelper.extractHeader(LeanPlayerConstants.HTTP_HEADER_NAME_CONTROL_LOCATION, receivedMessage);
  }

  private String extractEventsLocation(final String receivedMessage) {
    return HeaderHelper.extractHeader(LeanPlayerConstants.HTTP_HEADER_NAME_EVENTS_LOCATION, receivedMessage);
  }

  private String receiveMessage(DatagramSocket msocket, byte[] bytes, DatagramPacket packet) throws IOException {
    msocket.receive(packet);
    return new String(bytes, 0, packet.getLength());
  }

  private static void send(final InetAddress broadcastAddress, final DatagramSocket datagramSocket, final String payload) throws IOException {
    byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8.displayName());
    DatagramPacket datagramPacket = new DatagramPacket(payloadBytes, payloadBytes.length, broadcastAddress, SsdpConstants.MULTICAST_PORT);
    datagramSocket.send(datagramPacket);
    TrafficLogger.logSent(payload);
  }
}