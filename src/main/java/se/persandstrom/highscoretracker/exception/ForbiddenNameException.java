package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class ForbiddenNameException extends AbstractException {

    public ForbiddenNameException(String name, String message) {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.FORBIDDEN_NAME, "forbidden name: \"" + name + "\". " + message);
    }
}
