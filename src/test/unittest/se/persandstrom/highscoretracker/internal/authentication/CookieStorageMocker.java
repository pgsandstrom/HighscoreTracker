package se.persandstrom.highscoretracker.internal.authentication;

import static org.mockito.Mockito.mock;

/**
 * User: pesandst
 * Date: 2013-03-05
 * Time: 13:41
 */
public class CookieStorageMocker {

    public static CookieStorage createInstance() {
        CookieStorage cookieStorage = mock(CookieStorage.class);
        return cookieStorage;
    }
}
