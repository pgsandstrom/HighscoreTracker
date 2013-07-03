package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class IllegalPasswordException extends AbstractException {

    public IllegalPasswordException(String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.ILLEGAL_PASSWORD, message);
    }
}
