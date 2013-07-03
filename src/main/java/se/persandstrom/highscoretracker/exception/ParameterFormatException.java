package se.persandstrom.highscoretracker.exception;


import org.springframework.http.HttpStatus;

public class ParameterFormatException extends AbstractException {

    public ParameterFormatException() {
        super(HttpStatus.BAD_REQUEST, ErrorCodes.PARAMETER_MALFORMED, "Parameter malformed!");
    }
}
