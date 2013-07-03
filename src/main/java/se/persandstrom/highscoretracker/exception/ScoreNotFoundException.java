package se.persandstrom.highscoretracker.exception;


public class ScoreNotFoundException extends NotFoundException {

    public ScoreNotFoundException(long scoreId) {
        super(ErrorCodes.GAME_NOT_FOUND, "Score id not found: \"" + scoreId + "\"");
    }
}
