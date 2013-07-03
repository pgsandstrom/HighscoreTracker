package se.persandstrom.highscoretracker.internal.authentication;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.IllegalPasswordException;
import se.persandstrom.highscoretracker.internal.common.PropertiesFactory;
import se.persandstrom.highscoretracker.internal.player.Player;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Properties;

@Component
@Scope("application")
public class Authentication {

    private static final int NUMBER_OF_HASHES = 1000;

    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_HASH_EXPECTED_LENGTH = 128;
    private static final int SALT_LENGTH = 8;
    private static final int COOKIE_LENGTH = 16;

    private final SecretKeySpec secretKey;
    private final SecureRandom random = new SecureRandom();
    private final CookieStorage cookieStorage;

    public Authentication() throws IOException {
        this(new CookieStorage());
    }

    public Authentication(CookieStorage cookieStorage) throws IOException {
        Properties securityProperties = PropertiesFactory.getProperties("security.properties");
        String staticSalt = securityProperties.getProperty("security.static_salt");
        secretKey = new SecretKeySpec(staticSalt.getBytes("utf-8"), "HmacSHA512");
        this.cookieStorage = cookieStorage;
    }

    public boolean validateCookie(SessionCookie sessionCookie) {
        return cookieStorage.exists(sessionCookie);
    }

    /**
     * This also saves the cookie to memory
     *
     * @return
     */
    public SessionData generateCookie(Player player) {
        String authToken = randomString().substring(0, COOKIE_LENGTH);
        SessionCookie sessionCookie = new SessionCookie(player.getSlugName(), authToken);
        SessionData sessionData = new SessionData(player, sessionCookie);
        cookieStorage.save(sessionCookie);
        return sessionData;
    }

    /**
     * Generates the final auth-data to store and the dynamic salt
     *
     * @param password the un-hashed password
     * @return
     */
    public AuthData createLoginData(String password) throws IllegalPasswordException {

        validatePassword(password);

        String dynamicSalt = generateDynamicSalt();

        String hashedPassword = multiHash(password);
        String saltedPassword = salt(hashedPassword, dynamicSalt);

        return new AuthData(saltedPassword, dynamicSalt, hashedPassword);
    }

    public void validatePassword(String password) throws IllegalPasswordException {
        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new IllegalPasswordException("password must be " + PASSWORD_MIN_LENGTH + " characters long");
        }
    }

    /**
     * Checks if the login data was correct
     *
     * @param suppliedHashedPassword the supplied and multi-hashed password
     * @param authData
     * @return
     */
    public boolean checkPassword(String suppliedHashedPassword, AuthData authData) throws IllegalPasswordException {

        if (suppliedHashedPassword.length() != PASSWORD_HASH_EXPECTED_LENGTH) {
            throw new IllegalPasswordException("something is wrong with your password hash. It should be a " +
                                                       "sha512-hash, 128 characters long");
        }

        String finishedSuppliedPassword = salt(suppliedHashedPassword, authData.dynamicSalt);
        return finishedSuppliedPassword.equals(authData.authToken);
    }

    public String multiHash(String password) {
        //5000 iterations takes at most 70ms
        for (int i = 0; i < NUMBER_OF_HASHES; i++) {
            password = DigestUtils.sha512Hex(password);
        }
        return password;
    }

    public String salt(String password, String dynamicSalt) {

        Mac mac;
        String result = null;
        try {
            mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKey);
            String data = password + dynamicSalt;
            final byte[] macData = mac.doFinal(data.getBytes());
            byte[] hex = new Hex().encode(macData);
            result = new String(hex, "UTF-8");
        } catch (final NoSuchAlgorithmException e) {
            e.printStackTrace();
            //TODO what to do about these?
//            AppLogService.error( e );
        } catch (final InvalidKeyException e) {
            e.printStackTrace();
//            AppLogService.error( e );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
//            AppLogService.error( e );
        }
        return result;
    }

    private String generateDynamicSalt() {
        return randomString().substring(0, SALT_LENGTH);
    }

    private String randomString() {
        //26 characters long
        return new BigInteger(130, random).toString(32);
    }

}
