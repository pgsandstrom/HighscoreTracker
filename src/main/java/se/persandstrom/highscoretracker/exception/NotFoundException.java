package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public abstract class NotFoundException extends AbstractException {

    public NotFoundException(int errorCode, String message) {
        super(HttpStatus.NOT_FOUND, errorCode, message);
    }
}
