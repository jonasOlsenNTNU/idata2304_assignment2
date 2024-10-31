package no.ntnu.idata2304.group2;

import java.util.Arrays;

/**
 * A SmartTv application.
 */
public class SmartTv {
  private TvLogic logic;
  private TvServer tvServer;

  /**
   * Default constructor for a SmartTv.
   * Has 10 channels.
   */
  public SmartTv() {
    this.logic = new TvLogic(10);
    this.tvServer = new TvServer(this.logic);
  }

  /**
   * Custom constructor for a SmartTV.
   * @param port TCP port to listen for incoming connections.
   */
  public SmartTv(int port) {
    if (port > 0) {
      this.logic = new TvLogic(10);
      this.tvServer = new TvServer(this.logic, port);
    }
  }

  /**
   * Creates and starts a SmartTv.
   * Default parameters: 1238
   *
   * @param args (port) - Set the TCP port to listen for incoming connections.
   */
  public static void main(String[] args) {

    if (args != null) {
      try {
        int port = Integer.parseInt(Arrays.stream(args).iterator().next());
        SmartTv tv = new SmartTv(port);
        tv.tvServer.startServer();
      } catch (Exception e) {
        System.out.println("Invalid arguments");
        throw new IllegalArgumentException("Invalid arguments");
      }
    } else {
      SmartTv tv = new SmartTv();
      tv.tvServer.startServer();
    }
  }
}
