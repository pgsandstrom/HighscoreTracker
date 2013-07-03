package se.persandstrom.highscoretracker.internal.authentication;

public class AuthData {

    public final String authToken;
    public final String dynamicSalt;

    public final String multihashedPassword;

    public AuthData(String authToken, String dynamicSalt) {
        this.authToken = authToken;
        this.dynamicSalt = dynamicSalt;
        this.multihashedPassword = null;
    }

    public AuthData(String authToken, String dynamicSalt, String multihashedPassword) {
        this.authToken = authToken;
        this.dynamicSalt = dynamicSalt;
        this.multihashedPassword = multihashedPassword;
    }
}
