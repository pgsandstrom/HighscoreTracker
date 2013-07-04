package se.persandstrom.highscoretracker.external;


import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import se.persandstrom.highscoretracker.exception.AlreadyExistException;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.exception.IllegalPasswordException;
import se.persandstrom.highscoretracker.exception.NotFoundException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:integrationtest-context.xml"})
public class ExternalApiTest {

    //XXX just use the normal context, if you figure out how to...

    @Autowired
    ExternalApi externalApi;

    public ExternalApiTest() {
    }

    @BeforeClass
    public static void beforeClass() {
    }

    @Before
    public void before() {
    }

    @Test
    public void isPlayerNameFree() throws NotFoundException {
        //"game" is a reserved name and should always be free
        boolean gameFree = externalApi.isPlayerNameFree("game");
        assertTrue(gameFree);

        boolean adminFree = externalApi.isPlayerNameFree("admin");
        assertFalse(adminFree);
    }

    @Test(expected = AlreadyExistException.class)
    public void createPlayer1() throws Exception {
        externalApi.createPlayer("admin", "password", "mail", "description", null);
    }

    @Test(expected = IllegalPasswordException.class)
    public void createPlayer2() throws Exception {
        externalApi.createPlayer("afefekjfkejfdddd", "pass", "mail", "description", null);
    }

    @Test(expected = ForbiddenNameException.class)
    public void createPlayer3() throws Exception {
        externalApi.createPlayer("gamE", "password", "mail", "description", null);
    }

}
