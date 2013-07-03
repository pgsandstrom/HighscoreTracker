package se.persandstrom.highscoretracker.internal.common;

/**
 * User: pesandst
 * Date: 2013-03-07
 * Time: 13:44
 */
public class FilterExclude {

    public static boolean isExcluded(String servletPath) {
        if(servletPath.startsWith("/javax.faces.resource/") ||servletPath.startsWith("/resource/")) {
            return true;
        }

        return false;
    }
}
