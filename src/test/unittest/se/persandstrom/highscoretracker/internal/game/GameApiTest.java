package se.persandstrom.highscoretracker.internal.game;


import org.junit.Before;
import org.junit.Test;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class GameApiTest {

    GameApi gameApi;

    @Before
    public void before() throws IOException, ForbiddenNameException {
//        gameApi = (GameApi) context.getBean("gameApi");
        GameDb gameDb = GameDbMocker.createInstance();
        gameApi = new GameApi(gameDb, new Authentication());
    }

    @Test
    public void getNewest() {
        List<Game> newestGames = gameApi.getNewest();
        assertNotNull(newestGames);
    }
}
