package io.neocdtv;

import io.neocdtv.constants.GenaConstants;
import io.neocdtv.constants.HeaderHelper;
import io.neocdtv.constants.HttpConstants;
import io.neocdtv.constants.LeanPlayerConstants;
import io.neocdtv.constants.NetworkConstants;
import io.neocdtv.constants.SsdpConstants;
import io.neocdtv.constants.UpnpHelper;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * UpnpNotifyLite.
 *
 * @author xix
 * @since 21.12.17
 */
public class UpnpListenForNotifyLite extends Thread {

  private EventsHandler eventsHandler;

  public static void startIt(final EventsHandler eventsHandler) {
    UpnpListenForNotifyLite upnpListenForNotifyLite = new UpnpListenForNotifyLite();
    upnpListenForNotifyLite.setEventsHandler(eventsHandler);
    upnpListenForNotifyLite.start();
  }

  public void setEventsHandler(EventsHandler eventsHandler) {
    this.eventsHandler = eventsHandler;
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

        if (receivedMessage.contains(GenaConstants.HTTP_METHOD_NOTIFY) &&
            receivedMessage.contains(UpnpHelper.MEDIA_RENDERER) &&
            receivedMessage.contains(LeanPlayerConstants.HTTP_HEADER_NAME_CONTROL_LOCATION) &&
            receivedMessage.contains(LeanPlayerConstants.HTTP_HEADER_NAME_EVENTS_LOCATION)) {
          final String deviceName = HeaderHelper.extractHeader(HttpConstants.HTTP_HEADER_NAME_SERVER, receivedMessage);
          final String controlLocation = HeaderHelper.extractHeader(LeanPlayerConstants.HTTP_HEADER_NAME_CONTROL_LOCATION, receivedMessage);
          final String location = HeaderHelper.extractHeader(HttpConstants.HTTP_HEADER_NAME_LOCATION, receivedMessage);
          final String eventsLocation = HeaderHelper.extractHeader(LeanPlayerConstants.HTTP_HEADER_NAME_EVENTS_LOCATION, receivedMessage);
          eventsHandler.onDeviceDiscovery(
              deviceName,
              location,
              controlLocation,
              eventsLocation
          );
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(UpnpListenForNotifyLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

}
