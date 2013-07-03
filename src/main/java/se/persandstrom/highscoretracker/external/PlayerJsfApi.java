package se.persandstrom.highscoretracker.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.AuthenticationFailedException;
import se.persandstrom.highscoretracker.exception.NotFoundException;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.authentication.SessionCookie;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import java.io.Serializable;
import java.util.Map;

/**
 * Holds the API that the jsf-files uses while rendering
 */
@Component
@Scope("application")
public class PlayerJsfApi implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PlayerJsfApi.class);

    @Autowired
    PlayerApi playerApi;

    public String getPlayerSlugName() throws AuthenticationFailedException {
        return Slugify.slugify(getPlayerDisplayName());
    }

    public String getPlayerDisplayName() throws AuthenticationFailedException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String playerName = null;
        if (auth != null) {
            playerName = auth.getName();
        }

        if (isValidUsername(playerName)) {
            return playerName;
        } else {
            throw new AuthenticationFailedException("no user logged in");
        }
    }

    public boolean isPlayerGameOwner(long gameOwnerId) throws AuthenticationFailedException, NotFoundException {
        if(!isPlayerLoggedIn()) {
            return false;
        }
        Player player = playerApi.getPlayer(getPlayerSlugName());
        return player.getId() == gameOwnerId;
    }

    public boolean isPlayerLoggedIn() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return false;
        }
        return isValidUsername(auth.getName());
    }

    private boolean isValidUsername(String playerName) {
        return playerName != null && !"anonymousUser".equals(playerName);
    }

    /**
     * @deprecated use spring security jsf tablib instead (for example, see session_control.xhtml)
     */
    @Deprecated
    public boolean isLoggedIn() {
        Cookie cookie = getAuthCookie();
        return playerApi.isLoggedIn(cookie);
    }

    private Cookie getAuthCookie() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        Map<String, Object> requestCookieMap = externalContext.getRequestCookieMap();
        return (Cookie) requestCookieMap.get(SessionCookie.COOKIE_NAME);
    }


}
