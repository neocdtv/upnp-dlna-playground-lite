package io.neocdtv.upnp.discovery;

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

  private DeviceDiscoveryEventsHandler deviceDiscoveryEventsHandler;

  public static void startIt(final DeviceDiscoveryEventsHandler deviceDiscoveryEventsHandler) {
    UpnpListenForNotifyLite upnpListenForNotifyLite = new UpnpListenForNotifyLite();
    upnpListenForNotifyLite.setDeviceDiscoveryEventsHandler(deviceDiscoveryEventsHandler);
    upnpListenForNotifyLite.start();
  }

  public void setDeviceDiscoveryEventsHandler(DeviceDiscoveryEventsHandler deviceDiscoveryEventsHandler) {
    this.deviceDiscoveryEventsHandler = deviceDiscoveryEventsHandler;
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

        if (receivedMessage.contains(GenaConstants.HTTP_METHOD_NOTIFY)) {
          deviceDiscoveryEventsHandler.onDeviceDiscovery(receivedMessage, packet.getAddress().getHostAddress());
        }
      }
    } catch (IOException ex) {
      Logger.getLogger(UpnpListenForNotifyLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }
}
