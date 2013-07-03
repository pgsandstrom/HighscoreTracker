package se.persandstrom.highscoretracker.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import se.persandstrom.highscoretracker.exception.*;
import se.persandstrom.highscoretracker.internal.authentication.spring.PlayerUserDetailsService;
import se.persandstrom.highscoretracker.internal.game.Game;
import se.persandstrom.highscoretracker.internal.game.GameApi;
import se.persandstrom.highscoretracker.internal.game.GameBean;
import se.persandstrom.highscoretracker.internal.level.Level;
import se.persandstrom.highscoretracker.internal.level.LevelApi;
import se.persandstrom.highscoretracker.internal.level.LevelNewBean;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.player.PlayerNewBean;
import se.persandstrom.highscoretracker.internal.player.PlayerUpdateBean;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
@Scope("application")
public class WebApi {

    private static final Logger logger = LoggerFactory.getLogger(WebApi.class);

    //TODO check this code out carefully!

    //TODO when we add "edit game", check out so it cant be haxxed without owning it

    @Autowired
    private PlayerUserDetailsService playerUserDetailsService;

    @Autowired
    private PlayerJsfApi playerJsfApi;

    @Autowired
    private PlayerApi playerApi;

    @Autowired
    private GameApi gameApi;

    @Autowired
    private LevelApi levelApi;

    public void updatePlayer(PlayerUpdateBean playerUpdateBean) throws IOException, NotFoundException,
            AuthenticationFailedException {
        String playerSlugName = playerJsfApi.getPlayerSlugName();
        playerApi.updatePlayer(playerSlugName, playerUpdateBean.getMail(), playerUpdateBean.getDescription());

        //I could use navigation-rules and that crap, but I hate how they write out the xhtml filename in the url
        sendTo("/");
//        return "success";
    }

    public void createNewUser(PlayerNewBean playerNewBean) throws IllegalPasswordException,
            AuthenticationFailedException, AlreadyExistException, NotFoundException, ForbiddenNameException,
            IOException {

        playerApi.createPlayer(playerNewBean.getDisplayName(), playerNewBean.getPassword(), playerNewBean.getMail(),
                playerNewBean.getDescription(), false);
        loginPlayer(playerNewBean.getDisplayName(), null);

        sendTo("/");
    }

    public void createGame(GameBean gameBean) throws AlreadyExistException, IllegalPasswordException,
            ForbiddenNameException, AuthenticationFailedException, NotFoundException, IOException {

        String playerSlugName = playerJsfApi.getPlayerSlugName();

        Game game = gameApi.createGame(gameBean.getDisplayName(), playerSlugName, gameBean.getDescription());

        sendTo("/game/" + game.getSlugName());
    }

    public void createLevel(long gameId, LevelNewBean levelNewBean) throws AlreadyExistException,
            IllegalPasswordException, ForbiddenNameException, AuthenticationFailedException, NotFoundException,
            IOException, PermissionDeniedException {

        String playerSlugName = playerJsfApi.getPlayerSlugName();
        Player player = playerApi.getPlayer(playerSlugName);
        Game game = gameApi.getGame(gameId);

        Level level = levelApi.createLevel(player, game, levelNewBean.getDisplayName());

        sendTo("/game/" + game.getSlugName() + "/level/" + level.getSlugName());
    }

    private void sendTo(String url) throws IOException {
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    }

    private void loginPlayer(String displayName, HttpServletRequest request) {

        if (request != null) {
            // generate session if one doesn't exist
            request.getSession();
        }

        // Authenticate the user
        UserDetails user = playerUserDetailsService.loadUserByUsername(displayName);
        Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
