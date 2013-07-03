package se.persandstrom.highscoretracker.internal.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.*;
import se.persandstrom.highscoretracker.external.RedirectHtml;
import se.persandstrom.highscoretracker.internal.common.Constant;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.common.ValidateName;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;

import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class GameApi implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(GameApi.class);

    @Autowired
    GameDb gameDb;

    @Autowired
    Authentication authentication;

    @Autowired
    PlayerApi playerApi;

    public GameApi() {
    }

    public GameApi(GameDb gameDb, Authentication authentication) {
        this.gameDb = gameDb;
        this.authentication = authentication;
    }

    public boolean isNameFree(String displayName) {
        String slugName = Slugify.slugify(displayName);
        return gameDb.getBySlugName(slugName) == null;
    }

    public Game getGame(long gameId) throws GameNotFoundException {
        Game game = gameDb.getById(gameId);
        if (game == null) {
            throw new GameNotFoundException(game.getSlugName());
        }
        return game;
    }

    public Game getGame(String slugName) throws GameNotFoundException {
        Game game = gameDb.getBySlugName(slugName);
        if (game == null) {
            throw new GameNotFoundException(slugName);
        }
        return game;
    }

    public List<Game> getGameList() {
        return gameDb.getList();
    }

    public List<Game> getNewest() {
        return gameDb.getNewest();
    }

    public Game createGame(String displayName, String ownerSlugName,
                           String description) throws AlreadyExistException, ForbiddenNameException, NotFoundException {
        Player player = playerApi.getPlayer(ownerSlugName);
        return createGame(displayName, player.getId(), description);
    }

    public Game createGame(String displayName, long ownerId, String description) throws AlreadyExistException,
            ForbiddenNameException, NotFoundException {
        if (!isNameFree(displayName)) {
            throw new GameAlreadyExistException(displayName);
        }

        Game game = new Game(ownerId, displayName, description);
        gameDb.create(game);
        return game;
    }

    /**
     *
     * @param displayName
     * @param ownerSlugName if not null, checks so the user exists
     * @param description
     * @throws AlreadyExistException
     * @throws ForbiddenNameException
     * @throws NotFoundException
     */
    public void tryCreateGame(String displayName, String ownerSlugName,
                              String description) throws AlreadyExistException, ForbiddenNameException,
            NotFoundException {
        if (!isNameFree(displayName)) {
            throw new GameAlreadyExistException(displayName);
        }

        ValidateName.game(displayName);

        if (ownerSlugName != null) {
            //check so the player exists:
            playerApi.getPlayer(ownerSlugName);
        }
    }

    public void updateGame(String name, String mail) throws GameNotFoundException {
        gameDb.update(name, mail);
    }
}
