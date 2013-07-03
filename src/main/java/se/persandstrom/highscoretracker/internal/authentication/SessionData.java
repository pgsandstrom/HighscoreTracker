package se.persandstrom.highscoretracker.internal.authentication;

import se.persandstrom.highscoretracker.internal.player.Player;

/**
 * User: pesandst
 * Date: 2013-02-27
 * Time: 16:33
 */
public class SessionData {

    public final Player player;
    public final SessionCookie sessionCookie;

    public SessionData(Player player, SessionCookie sessionCookie) {
        this.player = player;
        this.sessionCookie = sessionCookie;
    }
}
