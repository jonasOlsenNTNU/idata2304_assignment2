package no.ntnu;

import no.ntnu.idata2304.group2.SmartTv;
import no.ntnu.idata2304.group2.TvLogic;
import no.ntnu.idata2304.group2.TvServer;
import org.junit.Test;
import static org.junit.Assert.*;

public class SmartTvTest {

    @Test
    public void testTVTurnedOffByDefault() {
        TvLogic tvLogic = new TvLogic(10);
        TvServer tvServer = new TvServer(tvLogic);
        tvServer.startServer();
        assertEquals(false, tvServer.getLogic().isTvOn());
    }
}
