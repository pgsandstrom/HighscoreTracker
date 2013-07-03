package se.persandstrom.highscoretracker.exception;


public class PlayerNotFoundException extends NotFoundException {

    public PlayerNotFoundException(String name) {
        super(ErrorCodes.PLAYER_NOT_FOUND, "Player name not found: \"" + name + "\"");
    }

    public PlayerNotFoundException(long id) {
        super(ErrorCodes.PLAYER_NOT_FOUND, "Player id not found: " + id);
    }
}
