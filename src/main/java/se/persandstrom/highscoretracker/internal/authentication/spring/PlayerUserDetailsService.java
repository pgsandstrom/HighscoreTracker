package se.persandstrom.highscoretracker.internal.authentication.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.player.PlayerFull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Used to handle login
 */
@Component
@Scope("application")
@Transactional(readOnly = true)
public class PlayerUserDetailsService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(PlayerUserDetailsService.class);

    @Autowired
    PlayerApi playerApi;

    //TODO review this whole class carefully
    //TODO vad fan betyder transactional?

    /**
     * Retrieves a user record containing the user's credentials and access.
     */
    @Override
    public UserDetails loadUserByUsername(String displayName) throws UsernameNotFoundException, DataAccessException {

        PlayerFull player = playerApi.getFullByDisplayName(displayName);

        if (player == null) {
            throw new UsernameNotFoundException("Error in retrieving user: \"" + displayName + "\"");
        }

        return new User(player.getDisplayName(), player.getAuthToken(), true, true, true, true,
                getAuthorities(0));  //access is always 0 lol
    }

    /**
     * @param access An integer value representing the access of the user
     * @return Collection of granted authorities
     */
    public Collection<GrantedAuthority> getAuthorities(int access) {
        List<GrantedAuthority> authList = new ArrayList<>();

        authList.add(new SimpleGrantedAuthority("ROLE_USER"));

        if (access >= 1) {
            //TODO fix more authorities and stuff
        }

        return authList;
    }
}
