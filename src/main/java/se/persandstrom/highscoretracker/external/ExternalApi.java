package se.persandstrom.highscoretracker.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import se.persandstrom.highscoretracker.exception.*;
import se.persandstrom.highscoretracker.internal.authentication.SessionData;
import se.persandstrom.highscoretracker.internal.common.Constant;
import se.persandstrom.highscoretracker.internal.common.PropertiesFactory;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.game.Game;
import se.persandstrom.highscoretracker.internal.game.GameApi;
import se.persandstrom.highscoretracker.internal.level.Level;
import se.persandstrom.highscoretracker.internal.level.LevelApi;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.score.Score;
import se.persandstrom.highscoretracker.internal.score.ScoreApi;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

@Controller
@Scope("application")
@RequestMapping(value = "/api")
public class ExternalApi {

    private static final Logger logger = LoggerFactory.getLogger(ExternalApi.class);

    @Autowired
    private PlayerApi playerApi;

    @Autowired
    private GameApi gameApi;

    @Autowired
    private LevelApi levelApi;

    @Autowired
    private ScoreApi scoreApi;


    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public String test(Locale locale, Model model) {
        logger.info("The client locale is {}.", locale);
        List<Player> latestPlayers = playerApi.getNewestPlayers();
        Player player = latestPlayers.get(0);
        String name = player.getDisplayName();
        model.addAttribute("serverTime", "" + name);
        return "home";
    }

    @RequestMapping(value = "/test2", method = RequestMethod.GET)
    @ResponseBody
    public String test2() throws IOException {
        Properties securityProperties = PropertiesFactory.getProperties("something");
        String staticSalt = securityProperties.getProperty("security.static_salt");
        logger.info("test2: "+staticSalt);
        return "hej";
    }


    @RequestMapping(value = "/slugify", method = RequestMethod.GET)
    @ResponseBody
    public String slugify(@RequestParam("name") String name) {
        name = Slugify.slugify(name);
        return name;
    }

    // ***
    // PLAYER STUFF
    // ***

    @RequestMapping(value = "player/isfree", method = RequestMethod.GET)
    @ResponseBody
    public boolean isPlayerNameFree(@RequestParam("name") String name) {
        return playerApi.isNameFree(name);
    }

    @RequestMapping(value = "player/list", method = RequestMethod.GET)
    @ResponseBody
    public List<Player> getPlayerList(@RequestParam("offset") Integer offset, @RequestParam(value = "count",
            required = false) Integer count) throws NotFoundException {

        if (count == null) {
            count = Constant.LIST_STANDARD_COUNT;
        }
        return playerApi.getPlayerList(offset, count);
    }

    @RequestMapping(value = "player/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Player getPlayer(@PathVariable("name") String name) throws NotFoundException {
        return playerApi.getPlayer(name);
    }

    @RequestMapping(value = "player/create", method = RequestMethod.POST)
    @ResponseBody
    public Player createPlayer(@RequestParam("name") String name, @RequestParam("password") String password,
                               @RequestParam(value = "mail", required = false) String mail,
                               @RequestParam(value = "description", required = false) String description,
                               HttpServletResponse response) throws Exception {
        SessionData sessionData = playerApi.createPlayer(name, password, mail, description, true);
        sessionData.sessionCookie.addToResponse(response);
        return sessionData.player;

    }

    @RequestMapping(value = "player/create/try", method = RequestMethod.POST)
    @ResponseBody
    public String tryCreatePlayer(@RequestParam("name") String name, @RequestParam("password") String password,
                                  @RequestParam(value = "mail", required = false) String mail,
                                  @RequestParam(value = "description", required = false) String description) throws
            AlreadyExistException, IllegalPasswordException, ForbiddenNameException {
        playerApi.tryCreatePlayer(name, password, mail, description);
        return "ok";
    }

    @RequestMapping(value = "player/login", method = RequestMethod.POST)
    @ResponseBody
    public Player loginPlayer(@RequestParam("name") String name, @RequestParam("password") String hashedPassword,
                              HttpServletResponse response) throws IllegalPasswordException, NotFoundException,
            AuthenticationFailedException {
        SessionData sessionData = playerApi.loginPlayer(name, hashedPassword, true);
        sessionData.sessionCookie.addToResponse(response);
        return sessionData.player;
    }

    @RequestMapping(value = "player/login/try", method = RequestMethod.POST)
    @ResponseBody
    public String tryLoginPlayer(@RequestParam("name") String name, @RequestParam("password") String hashedPassword)
            throws IllegalPasswordException, NotFoundException, AuthenticationFailedException {
        playerApi.loginPlayer(name, hashedPassword, false);
        return "ok";
    }

    @RequestMapping(value = "player", method = RequestMethod.PUT)
    @ResponseBody
    public String updatePlayer(@RequestParam(value = "mail", required = false) String mail,
                               @RequestParam(value = "description", required = false) String description,
                               HttpServletRequest request) throws NotFoundException, AuthenticationFailedException {
        String ownerSlugName = authenticate(request);
        playerApi.updatePlayer(ownerSlugName, mail, description);
        return "ok";
    }

    // ***
    // GAME STUFF
    // ***

    @RequestMapping(value = "game/{name}", method = RequestMethod.GET)
    @ResponseBody
    public Game getGame(@PathVariable("name") String name) throws NotFoundException {
        return gameApi.getGame(name);
    }

    @RequestMapping(value = "game/create", method = RequestMethod.POST)
    @ResponseBody
    public String createGame(@RequestParam("name") String name, @RequestParam(value = "description",
            required = false) String description, HttpServletRequest request) throws AlreadyExistException,
            IllegalPasswordException, ForbiddenNameException, AuthenticationFailedException, NotFoundException {

        String ownerSlugName = authenticate(request);
        gameApi.createGame(name, ownerSlugName, description);
        return "ok";
    }

    @RequestMapping(value = "game/create/try", method = RequestMethod.POST)
    @ResponseBody
    public void tryCreateGame(@RequestParam("name") String name, @RequestParam(value = "description",
            required = false) String description, @RequestParam(value = "skip_auth",
            required = false) boolean skipAuth, HttpServletRequest request) throws AlreadyExistException,
            IllegalPasswordException, ForbiddenNameException, AuthenticationFailedException, NotFoundException {
        String ownerSlugName = null;
        if (!skipAuth) {
            ownerSlugName = authenticate(request);
        }
        gameApi.tryCreateGame(name, ownerSlugName, description);
    }

    @RequestMapping(value = "game/{gameSlugName}", method = RequestMethod.PUT)
    @ResponseBody
    public String updateGame(@PathVariable("gameSlugName") String gameSlugName, @RequestParam(value = "description",
            required = false) String description, HttpServletRequest request) throws NotFoundException,
            AuthenticationFailedException {
        String playerSlugName = authenticate(request);
        Player owner = playerApi.getPlayer(playerSlugName);
        Game game = gameApi.getGame(gameSlugName);
        if (owner.getId() != game.getOwnerId()) {
            throw new AuthenticationFailedException("You do not own this game");
        }
        gameApi.updateGame(gameSlugName, description);
        return "ok";
    }

    // ***
    // LEVEL STUFF
    // ***

    @RequestMapping(value = "game/{gameName}/level/{levelName}", method = RequestMethod.GET)
    @ResponseBody
    public Level getLevel(@PathVariable("gameName") String gameName, @PathVariable("levelName") String levelName)
            throws NotFoundException {
        return levelApi.getLevel(gameName, levelName);
    }

    @RequestMapping(value = "game/{gameName}/level/create", method = RequestMethod.POST)
    @ResponseBody
    public String createLevel(@PathVariable("gameName") String gameSlugName,
                              @RequestParam("name") String levelDisplayName, HttpServletRequest request) throws
            AlreadyExistException, ForbiddenNameException, NotFoundException, AuthenticationFailedException,
            PermissionDeniedException {
        String playerSlugName = authenticate(request);
        Player player = playerApi.getPlayer(playerSlugName);
        Game game = gameApi.getGame(gameSlugName);
        levelApi.createLevel(player, game, levelDisplayName);
        return "ok";
    }

    @RequestMapping(value = "game/{gameName}/level/{levelName}/highscore", method = RequestMethod.GET)
    @ResponseBody
    public List<Score> getHighscore(@PathVariable("gameName") String gameName, @PathVariable("levelName") String
            levelName) throws NotFoundException {
        return scoreApi.getHighscore(gameName, levelName);
    }

    // ***
    // SCORE STUFF
    // ***

    @RequestMapping(value = "game/{gameName}/level/{levelName}/score", method = RequestMethod.POST)
    @ResponseBody
    public void createScore(@PathVariable("gameName") String gameName, @PathVariable("levelName") String levelName,
                            @RequestParam(value = "score") Long score, HttpServletRequest request) throws
            NotFoundException, AuthenticationFailedException {

        String playerSlugName = authenticate(request);
        scoreApi.createScore(gameName, levelName, playerSlugName, score);
    }

    @RequestMapping(value = "game/{gameName}/level/{levelName}/score/{scoreId}", method = RequestMethod.GET)
    @ResponseBody
    public Score getScore(@PathVariable("scoreId") Long scoreId) throws NotFoundException {
        return scoreApi.getById(scoreId);
    }

    //quick access thingy to score:
    @RequestMapping(value = "score/{scoreId}", method = RequestMethod.GET)
    @ResponseBody
    public Score getScoreShort(@PathVariable("scoreId") Long scoreId) throws NotFoundException {
        return scoreApi.getById(scoreId);
    }

    /**
     * When there is an exception thrown in our api, this is executed
     *
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    public String handleException(AbstractException ex, HttpServletResponse response) {
        response.setStatus(ex.getStatusCode());
        return ex.getJsonAsString();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseBody
    public String handleMissingParameterException(MissingServletRequestParameterException ex,
                                                  HttpServletResponse response) {
        response.setStatus(400);
        return new ParameterMissingException().getJsonAsString();
    }

    @ExceptionHandler(org.springframework.beans.TypeMismatchException.class)
    @ResponseBody
    public String handleMalformedParameterException(TypeMismatchException ex, HttpServletResponse response) {
        response.setStatus(400);
        return new ParameterFormatException().getJsonAsString();
    }

    /*
     *this overrides some servlets such as "game/{gameName}/level/{levelName}/score/{scoreId}". We catch it in
     * RedirectHtml instead
     * /
//    /**
//     * Here we catch all non-matched api-requests, to avoid that we send them a html-page when they want to parse json
//     *
//     * @return
//     */
//    @RequestMapping(value = "/**", method = RequestMethod.GET)
//    @ResponseBody
//    public String noMatch() throws ApiMethodNotFoundException {
//        throw new ApiMethodNotFoundException();
//    }

    /**
     * @param request
     * @return the slugName of the player
     * @throws AuthenticationFailedException
     */
    private String authenticate(HttpServletRequest request) throws AuthenticationFailedException {
        Cookie[] cookies = request.getCookies();
        String slugName = playerApi.isLoggedInGetSlugName(cookies);
        if (slugName == null) {
            logger.info("authenticate failed");
            throw new AuthenticationFailedException("authentication failed");
        }
        return slugName;
    }

    private void forward(HttpServletResponse response, String forwardUrl) {
        //If I want to get the domain the user is visiting: request.getServerName()
        try {
            response.sendRedirect(forwardUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}