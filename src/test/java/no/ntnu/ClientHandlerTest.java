package no.ntnu;

import no.ntnu.idata2304.group2.ClientHandler;
import no.ntnu.idata2304.group2.TvLogic;
import no.ntnu.idata2304.group2.TvServer;
import org.junit.Test;

import java.net.Socket;

import static org.junit.Assert.assertEquals;

public class ClientHandlerTest {

    @Test
    public void onCorrectInterpretation() {
        TvLogic logic = new TvLogic(10);
        TvServer testServer = new TvServer(logic);
        Socket testSocket = new Socket();
        ClientHandler clientHandler = new ClientHandler(testSocket, testServer);
        assertEquals("Tv is turned on", clientHandler.handleClientRequest("on"));
    }

    @Test
    public void cantGetChannelListWhenTVIsOff() {
        TvLogic logic = new TvLogic(10);
        TvServer testServer = new TvServer(logic);
        Socket testSocket = new Socket();
        ClientHandler clientHandler = new ClientHandler(testSocket, testServer);
        assertEquals("The TV is off", clientHandler.handleClientRequest("count"));
    }

    @Test
    public void canGetChannelListWhenTVIsOn() {
        TvLogic logic = new TvLogic(10);
        TvServer testServer = new TvServer(logic);
        Socket testSocket = new Socket();
        ClientHandler clientHandler = new ClientHandler(testSocket, testServer);
        clientHandler.handleClientRequest("on");
        assertEquals("Number of channels: 10", clientHandler.handleClientRequest("count"));
    }

    @Test
    public void cantChangeToNegativeChannel() {
        TvLogic logic = new TvLogic(10);
        TvServer testServer = new TvServer(logic);
        Socket testSocket = new Socket();
        ClientHandler clientHandler = new ClientHandler(testSocket, testServer);
        clientHandler.handleClientRequest("on");
        assertEquals("Not an available channel.", clientHandler.handleClientRequest("down"));
    }

    @Test
    public void correctResponseToInvalidCommand() {
        TvLogic logic = new TvLogic(10);
        TvServer testServer = new TvServer(logic);
        Socket testSocket = new Socket();
        ClientHandler clientHandler = new ClientHandler(testSocket, testServer);
        clientHandler.handleClientRequest("on");
        assertEquals("Not a valid command. See protocol.md.", clientHandler.handleClientRequest("abc"));
    }

    @Test
    public void canChangeChannel() {
        TvLogic logic = new TvLogic(10);
        TvServer testServer = new TvServer(logic);
        Socket testSocket = new Socket();
        ClientHandler clientHandler = new ClientHandler(testSocket, testServer);
        clientHandler.handleClientRequest("on");
        assertEquals("Channel changed.", clientHandler.handleClientRequest("up"));
    }
}
