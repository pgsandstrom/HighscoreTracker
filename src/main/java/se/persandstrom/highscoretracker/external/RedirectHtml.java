package se.persandstrom.highscoretracker.external;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import se.persandstrom.highscoretracker.exception.AbstractException;
import se.persandstrom.highscoretracker.exception.ApiMethodNotFoundException;
import se.persandstrom.highscoretracker.exception.AuthenticationFailedException;
import se.persandstrom.highscoretracker.exception.NotFoundException;
import se.persandstrom.highscoretracker.internal.common.ValidateName;
import se.persandstrom.highscoretracker.internal.game.Game;
import se.persandstrom.highscoretracker.internal.game.GameApi;
import se.persandstrom.highscoretracker.internal.game.GameBean;
import se.persandstrom.highscoretracker.internal.level.LevelApi;
import se.persandstrom.highscoretracker.internal.level.LevelNewBean;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.player.PlayerNewBean;
import se.persandstrom.highscoretracker.internal.score.Score;
import se.persandstrom.highscoretracker.internal.score.ScoreApi;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Redirects urls to our jsf-pages, while loading the beans the pages uses.
 */
@Controller
@Scope("application")
@RequestMapping(value = "")
public class RedirectHtml {

    private static final Logger logger = LoggerFactory.getLogger(RedirectHtml.class);

    @Autowired
    PlayerJsfApi playerJsfApi;

    @Autowired
    PlayerApi playerApi;

    @Autowired
    GameApi gameApi;

    @Autowired
    LevelApi levelApi;

    @Autowired
    ScoreApi scoreApi;

    //TODO urls dont work if the end with /

    //TODO block direct access to the xhtml files

    //TODO this whole class is stupid, can we somehow make it smart?

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String home() {
        logger.info("redirecting to index");
        return "index";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register(Model model) {
        model.addAttribute("playerNewBean", new PlayerNewBean());
        return "register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String edit(Model model) throws NotFoundException, AuthenticationFailedException {
        String playerSlugName = playerJsfApi.getPlayerSlugName();
        Player player = playerApi.getPlayer(playerSlugName);
        model.addAttribute("playerUpdateBean", player.createUpdateBean());
        return "edit";
    }

    @RequestMapping(value = "/player", method = RequestMethod.GET)
    public String viewPlayersAll(Model model) {
        return "view-players-all";
    }

    @RequestMapping(value = "/player/{name}", method = RequestMethod.GET)
    public String viewPlayer(HttpServletResponse response, Model model, @PathVariable("name") String slugName) throws
            NotFoundException {

        // This is so we can use links like "player/15", which allows us to link to players without getting their name
        // XXX but isnt it kind of stupid though? I believe it is...
        if (ValidateName.isStringLikeId(slugName)) {
            long id = Long.valueOf(slugName);
            Player player = playerApi.getPlayer(id);
            forward(response, "/player/" + player.getSlugName());
        }

        Player player = playerApi.getPlayer(slugName);
        model.addAttribute("player", player);
        return "view-player";
    }

    @RequestMapping(value = "/game", method = RequestMethod.GET)
    public String viewGamesAll(Model model) {
        return "view-games-all";
    }

    @RequestMapping(value = "/game/create", method = RequestMethod.GET)
    public String gameCreate(Model model) {
        model.addAttribute("gameBean", new GameBean());
        return "create-game";
    }

    //TODO currently /game/lol works, but not /game/lol/
    @RequestMapping(value = "/game/{name}", method = RequestMethod.GET)
    public String viewGame(Model model, @PathVariable("name") String slugName) throws NotFoundException {
        Game game = gameApi.getGame(slugName);
        Player owner = playerApi.getPlayer(game.getOwnerId());
        model.addAttribute("gameBean", new GameBean(game));
        model.addAttribute("owner", owner);
        return "view-game";
    }

    @RequestMapping(value = "/game/{gameName}/level/{levelName}", method = RequestMethod.GET)
    public String viewLevel(Model model, @PathVariable("gameName") String gameName,
                            @PathVariable("levelName") String levelName) throws NotFoundException {
        //just check so the level exists:
        levelApi.getLevel(gameName, levelName);

        model.addAttribute("gameName", gameName);
        model.addAttribute("levelName", levelName);
        return "view-level";
    }

    @RequestMapping(value = "/game/{gameName}/level/create", method = RequestMethod.GET)
    public String createLevel(Model model, @PathVariable("gameName") String gameName) throws NotFoundException {
        //TODO check so we own game

        Game game = gameApi.getGame(gameName);
        model.addAttribute("gameBean", new GameBean(game));
        model.addAttribute("levelNewBean", new LevelNewBean());
        return "create-level";
    }

    @RequestMapping(value = "/score/{id}", method = RequestMethod.GET)
    public String viewScore(Model model, @PathVariable("id") String idString) throws NotFoundException {
        long id = Long.valueOf(idString);
        Score score = scoreApi.getById(id);
        model.addAttribute("score", score);
        return "view-score";
    }

    /**
     * Handle all exceptions that are not more specifically handled below
     *
     * @param ex
     * @param response
     * @return
     */
    @ExceptionHandler(AbstractException.class)
    @ResponseBody
    public String handleException(AbstractException ex, HttpServletResponse response) {
        //TODO make it nicer
        int statusCode = ex.getStatusCode();
        response.setStatus(statusCode);
        return statusCode + "SOMEDAY THIS WILL BE A BEAUTIFUL ERROR PAGE! In the meantime, " +
                "here is some json: <br/>" + ex.getJsonAsString();
    }


    @ExceptionHandler(NotFoundException.class)
    public String showNotFoundException(NotFoundException e) {
        return e.getJsonAsString();
    }

    /**
     * When no matching servlets is found, we come here. We need to give json back if the url was directed to the api
     *
     * @return
     */
    @RequestMapping(value = "/404")
    @ResponseBody
    public String showNotFoundError(HttpServletRequest request) {
        String originalUri = (String) request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI);
        if (originalUri.startsWith("/api/")) {
            return new ApiMethodNotFoundException().getJsonAsString();
        } else {
            return "404: there is nothing here, stop looking (;_;)"; //TODO fix awesome error peage
        }
    }

    /**
     * All errors that would result in a 500 error goes here
     *
     * @return
     */
    @RequestMapping(value = "/500")
    @ResponseBody
    public String showServerError() {
        return "500: Application made an oopsie"; //TODO fix awesome error peage
    }

    /**
     * this is for those really weird errors that slips by all other filters. If parameters are malformed and stuff.
     *
     * @return
     */
    @RequestMapping(value = "/error")
    @ResponseBody
    public String showUnknownError() {
        return "Oh yeah, something went reaaaally wrong";
        //TODO fix awesome error page... or maybe some json is better :/
        //TODO either serve json, or fix so /api get other errors for malformed parameters and stuff
    }

    private void forward(HttpServletResponse response, String forwardUrl) {
        try {
            response.sendRedirect(forwardUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
