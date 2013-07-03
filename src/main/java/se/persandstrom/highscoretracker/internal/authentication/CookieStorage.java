package se.persandstrom.highscoretracker.internal.authentication;

import java.util.HashMap;
import java.util.Map;

/**
 * User: pesandst
 * Date: 2013-02-27
 * Time: 12:02
 */
class CookieStorage {

    //TODO back with redis instead
    //TODO fix timeouts and stuff
    private final Map<String, String> map = new HashMap<String, String>(1024);

    void save(SessionCookie sessionCookie) {
        map.put(sessionCookie.authToken, sessionCookie.playerName);
    }

    boolean exists(SessionCookie sessionCookie) {
        String accountName = map.get(sessionCookie.authToken);
        return sessionCookie.playerName.equals(accountName);
    }

}
