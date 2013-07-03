package se.persandstrom.highscoretracker.exception;

import java.util.HashMap;
import java.util.Map;

public class ErrorCodes {

    //TODO organize error codes in a more logical fashion

    public static final int PLAYER_NOT_FOUND = 1000;
    public static final int PLAYER_ALREADY_EXIST = 1001;
    public static final int GAME_NOT_FOUND = 1002;
    public static final int GAME_ALREADY_EXIST = 1003;
    public static final int LEVEL_NOT_FOUND = 1004;
    public static final int LEVEL_ALREADY_EXIST = 1005;
    public static final int SCORE_NOT_FOUND = 1005;
    public static final int API_METHOD_NOT_FOUND = 1006;

    public static final int FORBIDDEN_NAME = 2000;
    public static final int ILLEGAL_PASSWORD = 2001;
    public static final int PARAMETER_MISSING = 2002;
    public static final int PARAMETER_MALFORMED = 2003;
    public static final int AUTHENTICATION_FAILED = 2004;
    public static final int PERMISSION_DENIED = 2005;

    public static final Map<Integer, String> ERROR_NAME;

    static {
        ERROR_NAME = new HashMap<>();
        ERROR_NAME.put(PLAYER_NOT_FOUND, "Player not found");
        ERROR_NAME.put(PLAYER_ALREADY_EXIST, "Player already exist");
        ERROR_NAME.put(GAME_NOT_FOUND, "Game not found");
        ERROR_NAME.put(GAME_ALREADY_EXIST, "Game already exist");
        ERROR_NAME.put(LEVEL_NOT_FOUND, "Level not found");
        ERROR_NAME.put(LEVEL_ALREADY_EXIST, "Level already exist");
        ERROR_NAME.put(SCORE_NOT_FOUND, "Score not found");
        ERROR_NAME.put(API_METHOD_NOT_FOUND, "Api method not found");
        ERROR_NAME.put(FORBIDDEN_NAME, "Name was forbidden");
        ERROR_NAME.put(ILLEGAL_PASSWORD, "Illegal password");
        ERROR_NAME.put(PARAMETER_MISSING, "Parameter missing");
        ERROR_NAME.put(PARAMETER_MALFORMED, "Parameter malformed");
        ERROR_NAME.put(AUTHENTICATION_FAILED, "Authentication failed");
        ERROR_NAME.put(PERMISSION_DENIED, "Permission denied");
    }
}
