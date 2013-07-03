package se.persandstrom.highscoretracker.internal.player;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.*;
import se.persandstrom.highscoretracker.internal.common.Constant;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.authentication.AuthData;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;
import se.persandstrom.highscoretracker.internal.authentication.SessionCookie;
import se.persandstrom.highscoretracker.internal.authentication.SessionData;
import se.persandstrom.highscoretracker.internal.simple.SimpleDb;

import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Component
@Scope("application")
public class PlayerApi implements Serializable {

    @Autowired
    private PlayerDb playerDb;

    @Autowired
    private Authentication authentication;

    public PlayerApi() {
    }

    public PlayerApi(PlayerDb playerDb, Authentication authentication) {
        this.playerDb = playerDb;
        this.authentication = authentication;
    }

    public List<Player> getNewestPlayers() {
        return playerDb.getNewest();
    }

    public boolean isNameFree(String displayName) {
        String slugName = Slugify.slugify(displayName);
        return playerDb.getBySlugName(slugName) == null;
    }

    public Player getPlayer(long id) throws NotFoundException {
        Player player = playerDb.getById(id);
        if (player == null) {
            throw new PlayerNotFoundException(id);
        }
        return player;
    }

    /**
     * Will return the player or throw exception
     *
     * @param slugName
     * @return
     * @throws NotFoundException
     */
    public Player getPlayer(String slugName) throws NotFoundException {
        if (slugName == null) {
            throw new PlayerNotFoundException(slugName);
        }
        Player player = playerDb.getBySlugName(slugName);
        if (player == null) {
            throw new PlayerNotFoundException(slugName);
        }
        return player;
    }

    public List<Player> getPlayerList(int offset, int count) {
        if (count > Constant.LIST_MAX_COUNT) {
            count = Constant.LIST_MAX_COUNT;
        }
        return playerDb.getList(offset, count);
    }

    public List<Player> getPlayerList(int pageNumber) {
        return playerDb.getList(pageNumber * Constant.LIST_STANDARD_COUNT, Constant.LIST_STANDARD_COUNT);
    }

    /**
     * Creates and logs in the user
     *
     * @param displayName
     * @param password
     * @param mail
     * @param description
     * @param createCookie
     * @return
     * @throws AlreadyExistException
     * @throws IllegalPasswordException
     * @throws ForbiddenNameException
     * @throws AuthenticationFailedException
     */
    public SessionData createPlayer(String displayName, String password, String mail, String description,
                                    boolean createCookie) throws AlreadyExistException, IllegalPasswordException,
            ForbiddenNameException {

        if (!isNameFree(displayName)) {
            throw new PlayerAlreadyExistException(displayName);
        }

        //TODO validate mail?
        if (mail != null && "".equals(mail.trim())) {
            mail = null;
        }

        Player player = new Player(displayName, mail, description);
        AuthData authData = authentication.createLoginData(password);
        PlayerFull playerFull = new PlayerFull(player, authData);
        playerDb.create(playerFull);

        if (createCookie) {
            try {
                return loginPlayer(displayName, authData.multihashedPassword, true);
            } catch (NotFoundException | AuthenticationFailedException e) {
                //this should be impossible
                e.printStackTrace();
                throw new IllegalStateException(e);
            }
        } else {
            return null;
        }
    }

    public void tryCreatePlayer(String displayName, String password, String mail,
                                String description) throws AlreadyExistException, IllegalPasswordException,
            ForbiddenNameException {

        if (!isNameFree(displayName)) {
            throw new PlayerAlreadyExistException(displayName);
        }

        //TODO validate mail?

        //this checks if the slugName is valid:
        new Player(displayName, mail, description);

        authentication.validatePassword(password);
    }

    /**
     * @param displayName
     * @param multihashedPassword
     * @param generateCookie      if we should generate a sessionCookie. If false, null will be returned.
     * @return a SessionData, but only if generateCookie was true
     * @throws se.persandstrom.highscoretracker.exception.NotFoundException
     *
     * @throws IllegalPasswordException
     * @throws AuthenticationFailedException
     */
    public SessionData loginPlayer(String displayName, String multihashedPassword,
                                   boolean generateCookie) throws NotFoundException, IllegalPasswordException,
            AuthenticationFailedException {
        System.out.println("loginPlayer: " + displayName + ", " + generateCookie);

        PlayerFull playerFull = playerDb.getFullByDisplayName(displayName);
        if (playerFull == null) {
            throw new PlayerNotFoundException(displayName);
        }

        boolean authenticated = authentication.checkPassword(multihashedPassword, playerFull.getAuthData());
        if (!authenticated) {
            throw new AuthenticationFailedException("Password was incorrect");
        }

        if (generateCookie) {
            return authentication.generateCookie(Player.getPlayer(playerFull));
        } else {
            return null;
        }
    }


    public void updatePlayer(String slugName, String mail, String description) throws NotFoundException {
        playerDb.update(slugName, mail, description);
    }

    // this method did not authenticate, so I felt it was dangerous to use...
//    public String getSlugNameFromCookie(Cookie[] cookies) {
//        for (Cookie cookie : cookies) {
//            if (SessionCookie.COOKIE_NAME.equals(cookie.getName())) {
//                return new SessionCookie(cookie.getValue()).playerName;
//            }
//        }
//        return null;
//    }

    public String isLoggedInGetSlugName(Cookie[] cookies) {
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (SessionCookie.COOKIE_NAME.equals(cookie.getName())) {
                SessionCookie sessionCookie = new SessionCookie(cookie.getValue());
                if (isLoggedIn(sessionCookie)) {
                    return sessionCookie.playerName;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    public boolean isLoggedIn(Cookie cookie) {
        if (cookie == null) {
            return false;
        }

        String value = cookie.getValue();
        return authentication.validateCookie(new SessionCookie(value));
    }

    public boolean isLoggedIn(SessionCookie sessionCookie) {
        if (sessionCookie == null) {
            return false;
        }

        //TODO do we have to check if the sessionCookie is timed out?

        return authentication.validateCookie(sessionCookie);
    }

    public PlayerFull getFullByDisplayName(String displayName) {
        return playerDb.getFullByDisplayName(displayName);
    }


}
