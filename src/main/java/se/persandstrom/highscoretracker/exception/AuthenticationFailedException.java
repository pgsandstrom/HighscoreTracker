package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class AuthenticationFailedException extends AbstractException {

    public AuthenticationFailedException(String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.AUTHENTICATION_FAILED, message);
    }
}
