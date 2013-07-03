package se.persandstrom.highscoretracker.internal.common;

import se.persandstrom.highscoretracker.exception.ForbiddenNameException;

public class ValidateName {

    //remember, this is also reflected in the database...
    private static final int NAME_MAX_LENGTH = 45;

    //XXX move to some config file or something lol
    //TODO parse every word in documentation and put them here
    private static final String[] forbiddenNames = new String[]{"score", "search", "view", "create", "highscore",
            "login", "id", "page", "player", "level", "game", "validate", "slugify", "slug", "slugified", "list",
            "api", "doc", "docs", "documentation", "me", "this", "new", "name"};

    public static void game(String gameName) throws ForbiddenNameException {
        checkForbiddenNames(gameName);
        checkForbiddenCharacters(gameName);
        checkForbiddenGameAndPlayerNames(gameName);
    }

    public static void player(String playerName) throws ForbiddenNameException {
        checkForbiddenNames(playerName);
        checkForbiddenCharacters(playerName);
        checkForbiddenGameAndPlayerNames(playerName);
    }

    public static void level(String levelName) throws ForbiddenNameException {
        checkForbiddenNames(levelName);
        checkForbiddenCharacters(levelName);
    }

    private static void checkForbiddenGameAndPlayerNames(String name) throws ForbiddenNameException {
        if (isStringNumeric(name)) {
            throw new ForbiddenNameException(name, "name cannot be a number");
        }
    }

    private static void checkForbiddenNames(String name) throws ForbiddenNameException {

        if (name == null) {
            throw new ForbiddenNameException(name, "name not set");
        }
        if ("".equals(name.trim())) {
            throw new ForbiddenNameException(name, "name must contain characters");
        }

        if (name.trim().length() > NAME_MAX_LENGTH) {
            throw new ForbiddenNameException(name, "name max length is " + NAME_MAX_LENGTH);
        }

        String slugName = Slugify.slugify(name);
        if ("".equals(slugName)) {
            throw new ForbiddenNameException(name, "the slugified name was empty, please use NORMAL characters >:(");
        }

        if ("".equals(slugName.trim())) {
            throw new ForbiddenNameException(name, "slug name must contain characters (most likely the name only " +
                    "contained \"weird\" characters");
        }

        String nameLowerCase = name.toLowerCase();
        for (String forbiddenName : forbiddenNames) {
            if (nameLowerCase.equals(forbiddenName)) {
                throw new ForbiddenNameException(name, "This name is not permitted... BECAUSE I SAY SO! >:(");
            }
        }
    }

    private static void checkForbiddenCharacters(String name) throws ForbiddenNameException {
        //currently we allow EVERYTHING!!!! We just slugify the name later on for urls
//        String slugified = slugify(name);
//        try {
//            URLEncoder.encode(name, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            //should never happen...
//        }
    }


    public static boolean isStringNumeric(String string) {
        try {
            Integer.parseInt(string);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static boolean isStringLikeId(String string) {
        for (char c : string.toCharArray()) {
            if (!Character.isDigit(c) && c != '_') {
                return false;
            }
        }
        return true;
    }
}
