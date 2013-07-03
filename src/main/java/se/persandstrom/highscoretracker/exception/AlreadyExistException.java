package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public abstract class AlreadyExistException extends AbstractException {

    AlreadyExistException(HttpStatus statusCode, int errorCode, String message) {
        super(statusCode, errorCode, message);
    }
}
