package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class PermissionDeniedException extends AbstractException {

    public PermissionDeniedException(String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.PERMISSION_DENIED, message);
    }
}
