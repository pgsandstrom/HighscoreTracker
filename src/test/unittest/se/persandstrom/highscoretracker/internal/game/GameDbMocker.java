package se.persandstrom.highscoretracker.internal.game;

import se.persandstrom.highscoretracker.exception.ForbiddenNameException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * User: pesandst
 * Date: 2013-03-05
 * Time: 11:37
 */
public class GameDbMocker {

    public static GameDb createInstance() throws ForbiddenNameException {
        GameDb gameDb = mock(GameDb.class);

        List<Game> newestGames = new ArrayList<Game>();
        newestGames.add(new Game(0l, "Twisted metal 2", "cool gaem!"));
        when(gameDb.getNewest()).thenReturn(newestGames);

        return gameDb;
    }
}
