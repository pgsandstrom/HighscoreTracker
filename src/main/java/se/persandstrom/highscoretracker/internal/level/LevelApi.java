package se.persandstrom.highscoretracker.internal.level;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.*;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.game.Game;
import se.persandstrom.highscoretracker.internal.game.GameApi;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class LevelApi implements Serializable {

    @Inject
    GameApi gameApi;

    @Inject
    LevelDb levelDb;

    @Inject
    Authentication authentication;

    public LevelApi() {
    }

    public Level getLevel(String gameName, String levelName) throws LevelNotFoundException {
        Level level = levelDb.get(gameName, levelName);
        if (level == null) {
            //XXX also check if the game exists?
            throw new LevelNotFoundException(gameName, levelName);
        }
        return level;
    }

    public List<Level> getGameLevels(long gameId) throws LevelNotFoundException {
        List<Level> levelList = levelDb.getGameLevels(gameId);
        return levelList;
    }

    public List<Level> getGameLevelsByName(String gameSlugName) throws NotFoundException {
        //EL (used by jsf) does not support overloading, thus the crappy method names
        /*
        TODO those things does two requests to the db, it could be done with one. Learn more about the cache and what
         is the most effective approach
         */
        Game game = gameApi.getGame(gameSlugName);
        List<Level> levelList = levelDb.getGameLevels(game.getId());
        return levelList;
    }

    public Level createLevel(Player player, Game game, String levelDisplayName) throws AlreadyExistException,
            ForbiddenNameException, GameNotFoundException, PermissionDeniedException {

        if (player.getId() != game.getOwnerId()) {
            throw new PermissionDeniedException("not owner of game");
        }

        String levelSlugName = Slugify.slugify(levelDisplayName);

        Level existingLevel = levelDb.get(game.getId(), levelSlugName);
        if (existingLevel != null) {
            throw new LevelAlreadyExistException(game.getDisplayName(), levelDisplayName);
        }

        Level level = new Level(levelDisplayName, game);
        levelDb.create(level);
        return level;
    }
}
