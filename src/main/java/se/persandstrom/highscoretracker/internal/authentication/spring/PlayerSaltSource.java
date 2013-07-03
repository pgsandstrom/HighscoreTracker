package se.persandstrom.highscoretracker.internal.authentication.spring;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.dao.SaltSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.internal.player.PlayerDb;
import se.persandstrom.highscoretracker.internal.player.PlayerFull;

@Component
@Scope("application")
public class PlayerSaltSource implements SaltSource {

    @Autowired
    PlayerDb playerDb;

    @Override
    public Object getSalt(UserDetails userDetails) {
        String username = userDetails.getUsername();
        PlayerFull player = playerDb.getFullByDisplayName(username);
        return player.getDynamicSalt();
    }
}
