package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class ParameterMissingException extends AbstractException {

    public ParameterMissingException() {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.PARAMETER_MISSING, "Check your parameters");
    }
}
