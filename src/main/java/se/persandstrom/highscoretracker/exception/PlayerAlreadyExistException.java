package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class PlayerAlreadyExistException extends AlreadyExistException {

    public PlayerAlreadyExistException(String name) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.PLAYER_ALREADY_EXIST, "player name already exists: \"" + name + "\"");
    }
}
