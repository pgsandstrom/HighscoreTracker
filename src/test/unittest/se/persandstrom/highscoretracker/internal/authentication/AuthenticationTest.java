package se.persandstrom.highscoretracker.internal.authentication;

import org.junit.Before;
import org.junit.Test;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.exception.IllegalPasswordException;
import se.persandstrom.highscoretracker.internal.player.Player;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AuthenticationTest {

    Authentication authentication;

    @Before
    public void before() throws IOException {
        authentication = new Authentication(CookieStorageMocker.createInstance());
    }

    @Test
    public void cookieConflicts() throws ForbiddenNameException {
        Player player = new Player("some name", null, null);
        Set<String> cookieAuthTokenSet = new HashSet<>();
        for (int i = 0; i < 100; i++) {
            SessionData sessionData = authentication.generateCookie(player);
            assertFalse(cookieAuthTokenSet.contains(sessionData.sessionCookie.authToken));
            cookieAuthTokenSet.add(sessionData.sessionCookie.authToken);
        }
    }

    @Test(expected = IllegalPasswordException.class)
    public void passwordRequirement1() throws IllegalPasswordException {
        String password = "pass";
        authentication.createLoginData(password);
    }

    @Test
    public void passwordRequirement2() throws IllegalPasswordException {
        String password = "password";
        authentication.createLoginData(password);
    }

    @Test
    public void passwordMatch() throws IllegalPasswordException {
        String password = "password";
        AuthData authData = authentication.createLoginData(password);
        String multiHashedPassword = authentication.multiHash(password);

        boolean authenticated = authentication.checkPassword(multiHashedPassword, authData);
        assertTrue(authenticated);
    }

    /**
     * I know, this test is a bit silly. But I think it's cute :(
     *
     * @throws se.persandstrom.highscoretracker.exception.IllegalPasswordException
     *
     */
    @Test(timeout = 600)
    public void hashTime() throws IllegalPasswordException {
        long startTime = System.currentTimeMillis();
        authentication.createLoginData("my magical little password!");
        long runTime = System.currentTimeMillis() - startTime;
        System.out.println("runTime: " + runTime);
        assertTrue(10 < runTime);
        assertTrue(runTime < 200);
    }
}
