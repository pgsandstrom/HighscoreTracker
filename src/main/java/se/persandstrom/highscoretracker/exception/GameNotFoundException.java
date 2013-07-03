package se.persandstrom.highscoretracker.exception;


public class GameNotFoundException extends NotFoundException {

    public GameNotFoundException(String name) {
        super(ErrorCodes.GAME_NOT_FOUND, "Game name not found: \"" + name + "\"");
    }
}
