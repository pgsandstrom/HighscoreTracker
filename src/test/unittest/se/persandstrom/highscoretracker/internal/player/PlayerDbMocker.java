package se.persandstrom.highscoretracker.internal.player;

import se.persandstrom.highscoretracker.exception.ForbiddenNameException;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * User: pesandst
 * Date: 2013-03-05
 * Time: 11:37
 */
public class PlayerDbMocker {

    public static PlayerDb createInstance() throws ForbiddenNameException {
        PlayerDb playerDb = mock(PlayerDb.class);

        List<Player> newestPlayers = new ArrayList<Player>();
        newestPlayers.add(new Player("Per Sandström", "pg.sandstrom@gmail.com", "I am Per"));
        when(playerDb.getNewest()).thenReturn(newestPlayers);

        return playerDb;
    }
}
