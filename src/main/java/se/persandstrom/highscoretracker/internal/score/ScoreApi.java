package se.persandstrom.highscoretracker.internal.score;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.NotFoundException;
import se.persandstrom.highscoretracker.exception.ScoreNotFoundException;
import se.persandstrom.highscoretracker.internal.level.Level;
import se.persandstrom.highscoretracker.internal.level.LevelApi;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.player.PlayerApi;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class ScoreApi implements Serializable {

    @Inject
    PlayerApi playerApi;

    @Inject
    LevelApi levelApi;

    @Inject
    ScoreDb scoreDb;

    @Inject
    Authentication authentication;

    public ScoreApi() {
    }

    public Score getById(long id) throws NotFoundException {
        Score score = scoreDb.getById(id);
        if (score == null) {
            throw new ScoreNotFoundException(id);
        }
        return score;
    }

    public List<Score> getHighscore(String gameName, String levelName) {
        List<Score> scoreList = scoreDb.getHighscore(gameName, levelName);
        return scoreList;
    }

    /**
     * @param playerId
     * @param setFirstScoreForGame if we should set setFirstScoreForGame so jsf know when to print game information
     *                             above the score
     * @return
     */
    public List<Score> getAllPlayerHighscores(long playerId, boolean setFirstScoreForGame) {
        List<Score> scoreList = scoreDb.getAllPlayerHighscores(playerId);

        long previousGameId = -1;
        for (Score score : scoreList) {
            long gameId = score.getLevel().getGameId();
            if (setFirstScoreForGame) {
                score.setFirstScoreForGame(previousGameId != gameId);
            }
            previousGameId = gameId;
        }

        return scoreList;
    }

    public void createScore(String gameName, String levelName, String playerSlugName,
                            long score) throws NotFoundException {
        //TODO should score-creating require keys and stuff?

        Player player = playerApi.getPlayer(playerSlugName);

        Level level = levelApi.getLevel(gameName, levelName);

        Score scoreObject = new Score(level, player, score);
        scoreDb.create(scoreObject);
    }
}
