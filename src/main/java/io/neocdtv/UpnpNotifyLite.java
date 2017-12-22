package io.neocdtv;

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
  private final String location;

  public UpnpNotifyLite(String uuid, String location) {
    this.uuid = uuid;
    this.location = location;
  }

  public static void main(String[] args) throws InterruptedException {
    startIt(UpnpHelper.buildUuid(), "http://dummy.com/spec.xml");
  }

  public static void startIt(final String uuid, final String location) {
    final UpnpNotifyLite upnpNotifyLite = new UpnpNotifyLite(uuid, location);
    upnpNotifyLite.sendNotification();
  }

  public void sendNotification() {

    try {
      InetAddress broadcastAddress = InetAddress.getByName(SsdpConstants.BROADCAST_IP);
      final DatagramSocket datagramSocket = new DatagramSocket();
      datagramSocket.setBroadcast(true);
      datagramSocket.setReuseAddress(true);

      UpnpPayloadFactory upnpPayloadFactory = UpnpPayloadFactory.
          create(uuid);

      sendNotification(broadcastAddress, datagramSocket, upnpPayloadFactory.createZenplayerNotifyRequest(location, "zenplayer"));

    } catch (IOException ex) {
      Logger.getLogger(UpnpNotifyLite.class.getName()).log(Level.SEVERE, null, ex);
    }
  }

  private void sendNotification(InetAddress multicastAddress, DatagramSocket datagramSocket, String payload) throws IOException {
    byte[] payloadBytes = payload.getBytes(StandardCharsets.UTF_8.name());
    DatagramPacket datagramPacket = new DatagramPacket(payloadBytes, payloadBytes.length, multicastAddress, SsdpConstants.BROADCAST_PORT);
    datagramSocket.send(datagramPacket);
    TrafficLogger.logSent(payload);
  }
}
