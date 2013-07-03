package se.persandstrom.highscoretracker.internal.common;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * User: pesandst
 * Date: 2013-02-27
 * Time: 16:49
 */
public class PropertiesFactory {

    //XXX there seem to be a propertiesfactory in spring, use it instead?

    public static Properties getProperties(String fileName) throws IOException {
        InputStream inputStream = PropertiesFactory.class.getClassLoader().getResourceAsStream(fileName);
        Properties properties = new Properties();
        properties.load(inputStream);
        return properties;
    }
}
