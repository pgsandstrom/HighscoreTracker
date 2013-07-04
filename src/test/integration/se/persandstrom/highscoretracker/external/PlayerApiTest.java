package se.persandstrom.highscoretracker.external;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.exception.NotFoundException;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.player.PlayerDb;
import se.persandstrom.highscoretracker.internal.player.PlayerDbMocker;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:integrationtest-context.xml"})
public class PlayerApiTest {

    @Autowired
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
    public void getNewestPlayers() {
        List<Player> latestUsers = playerApi.getNewestPlayers();
        assertNotNull(latestUsers);

        Player latestPlayer = null;
        for (Player player : latestUsers) {
            if (latestPlayer != null) {
                assertTrue(player.getCreated() < latestPlayer.getCreated());
            }
            latestPlayer = player;
        }
    }

    @Test(expected=NotFoundException.class)
    public void getPlayerById() throws NotFoundException {
        playerApi.getPlayer(-1);
    }

    @Test
    public void testNothing() {
        assertTrue(true);
    }
}
