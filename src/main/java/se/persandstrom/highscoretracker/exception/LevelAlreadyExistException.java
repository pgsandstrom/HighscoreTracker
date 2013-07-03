package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class LevelAlreadyExistException extends AlreadyExistException {

    public LevelAlreadyExistException(String gameName, String levelName) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.LEVEL_ALREADY_EXIST, "Level name already exists: \"" + levelName +
                "\" in game " + gameName);
    }
}
