package se.persandstrom.highscoretracker.exception;


public class LevelNotFoundException extends NotFoundException {

    public LevelNotFoundException(String gameName, String levelName) {
        super(ErrorCodes.LEVEL_NOT_FOUND, "Level name not found: \"" + levelName + "\" in game " +
                gameName);
    }
}
