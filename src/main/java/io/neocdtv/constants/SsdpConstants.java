package io.neocdtv.constants;

/**
 * SsdpConstants.
 *
 * @author xix
 * @since 21.12.17
 */
public class SsdpConstants {
  public final static String BROADCAST_IP = "239.255.255.250";
  public final static int BROADCAST_PORT = 1900;
  public final static String BROADCAST_ADDRESS = BROADCAST_IP + ":" + BROADCAST_PORT;
  public final static String HTTP_HEADER_NAME_UNIQUE_SERVICE_NAME = "USN";
  public final static String SSDP_DISCOVER = "ssdp:discover";
  public final static String SSDP_ALIVE = "ssdp:alive";
  public final static String SSDP_BYEBYE = "ssdp:byebye";

}
