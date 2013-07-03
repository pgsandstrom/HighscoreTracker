package se.persandstrom.highscoretracker.internal.authentication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

/**
 * User: pesandst
 * Date: 2013-03-06
 * Time: 16:30
 */
public class SessionCookie {

    private static final int EXPIRY_TIME = 60 * 60 * 24 * 180; //180 days

    public static final String COOKIE_NAME = "auth_data";

    private static final String COOKIE_JSON_AUTH_TOKEN = "auth_token";
    private static final String COOKIE_JSON_ACCOUNT = "account";

    public final String playerName;
    public final String authToken;

    public SessionCookie(String cookieValue) {
        JsonObject jsonObject = new JsonParser().parse(cookieValue).getAsJsonObject();
        playerName = jsonObject.getAsJsonPrimitive(COOKIE_JSON_ACCOUNT).getAsString();
        authToken = jsonObject.getAsJsonPrimitive(COOKIE_JSON_AUTH_TOKEN).getAsString();
    }

    public SessionCookie(String playerName, String authToken) {
        this.playerName = playerName;
        this.authToken = authToken;
    }

    public void addToResponse(HttpServletResponse response) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(COOKIE_JSON_ACCOUNT, playerName);
        jsonObject.addProperty(COOKIE_JSON_AUTH_TOKEN, authToken);
        Cookie cookie = new Cookie(COOKIE_NAME, jsonObject.toString());
        cookie.setMaxAge(EXPIRY_TIME);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        //TODO force https for everything always
        //TODO set so cookies are secure (only https) when https is forced
//        cookie.setSecure(true);
        response.addCookie(cookie);
    }
}
