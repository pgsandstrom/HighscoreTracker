package se.persandstrom.highscoretracker.internal.common;

import java.text.Normalizer;

public class Slugify {

    public static String slugify(String string) {
        /*
        If this is changed after going live, all calls need to be studied very carefully,
        since we assume that this transformation holds true for all entries in the database
         */

//        System.out.println("before slugify: " + string);
        string = string.trim();
        string = Normalizer.normalize(string, Normalizer.Form.NFKD);
        string = string.toLowerCase();                  //toLowerCase ;)
        string = string.replaceAll("[\\t\\s-]", "_");   //white spaces, tabs and hyphen -> underscore
        string = string.replaceAll("[^a-z0-9_]", "");   //remove everything but standard characters
        string = string.replaceAll("[_]{2,}", "_");     //all duplicate underscores are removed
//        System.out.println("after slugify: " + string);
        return string;
    }
}
