package se.persandstrom.highscoretracker.exception;


public class ApiMethodNotFoundException extends NotFoundException {

    public ApiMethodNotFoundException() {
        super(ErrorCodes.API_METHOD_NOT_FOUND, "Check your url!");
    }
}
