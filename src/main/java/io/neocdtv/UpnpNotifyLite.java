package io.neocdtv;

import io.neocdtv.constants.LeanPlayerConstants;
import io.neocdtv.constants.LocationHelper;
import io.neocdtv.constants.SsdpConstants;
import io.neocdtv.constants.UpnpHelper;
import io.neocdtv.constants.UpnpPayloadFactory;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * UpnpNotifyLite.
 *
 * @author xix
 * @since 21.12.17
 */
public class UpnpNotifyLite {

  private final String uuid;
  private final String baseUrl;

  public UpnpNotifyLite(String uuid, String baseUrl) {
    this.uuid = uuid;
    this.baseUrl = baseUrl;
  }

  public static void main(String[] args) throws InterruptedException {
    startIt(UpnpHelper.buildUuid(), "dummy.com/appUrl");
  }

  public static void startIt(final String uuid, final String baseUrl) {
    final UpnpNotifyLite upnpNotifyLite = new UpnpNotifyLite(uuid, baseUrl);
    upnpNotifyLite.sendNotification();
  }

  public void sendNotification() {

    try {
      InetAddress broadcastAddress = InetAddress.getByName(SsdpConstants.MULTICAST_IP);
      final DatagramSocket datagramSocket = new DatagramSocket();
      datagramSocket.setBroadcast(true);
      datagramSocket.setReuseAddress(true);

      UpnpPayloadFactory upnpPayloadFactory = UpnpPayloadFactory.
          create(uuid);

      sendNotification(
          broadcastAddress,
          datagramSocket,
          upnpPayloadFactory.createLeanPlayerNotifyRequest(
              LocationHelper.buildLocation(baseUrl), // TODO: currently not used at all
              LeanPlayerConstants.NAME,
              LocationHelper.buildControlLocation(baseUrl),
              LocationHelper.buildEventsLocation(baseUrl)));

    } catch (IOException ex) {
      Logger.getLogger(UpnpNotifyLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void sendNotification(InetAddress multicastAddress, DatagramSocket datagramSocket, String payload) throws IOException {
    byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8.name());
    DatagramPacket datagramPacket = new DatagramPacket(payloadBytes, payloadBytes.length, multicastAddress, SsdpConstants.MULTICAST_PORT);
    datagramSocket.send(datagramPacket);
    TrafficLogger.logSent(payload);
  }
}
