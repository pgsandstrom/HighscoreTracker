package se.persandstrom.highscoretracker.internal.player;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PlayerApiTest {

    PlayerApi playerApi;

    public PlayerApiTest() {
    }

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() throws IOException, ForbiddenNameException {
        PlayerDb playerDb = PlayerDbMocker.createInstance();
        playerApi = new PlayerApi(playerDb, new Authentication());
    }

    @Test
    public void testLatestUsers() {
        List<Player> latestUsers = playerApi.getNewestPlayers();
        assertNotNull(latestUsers);
    }

    @Test
    public void testNothing() {
        assertTrue(true);
    }
}
