package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class GameAlreadyExistException extends AlreadyExistException {

    public GameAlreadyExistException(String name) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.GAME_ALREADY_EXIST, "game name already exists: \"" + name + "\"");
    }
}
