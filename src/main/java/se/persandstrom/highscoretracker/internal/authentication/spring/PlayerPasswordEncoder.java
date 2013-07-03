package se.persandstrom.highscoretracker.internal.authentication.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;

@Component
@Scope("application")
@SuppressWarnings("deprecation")
public class PlayerPasswordEncoder implements PasswordEncoder {

    //Ignore the deprecation. They want us to use PasswordEncoder, it assumes you store the salt in the same column
    // as the password or something.

    private static final Logger logger = LoggerFactory.getLogger(PlayerPasswordEncoder.class);

    @Autowired
    Authentication authentication;

    @Override
    public String encodePassword(String password, Object salt) {
        //this never seems to be called, except on startup...
        logger.info("encodePassword: " + password);
        return null;  //XXX NOT IMPLEMENTED
    }

    @Override
    public boolean isPasswordValid(String encryptedPassword, String rawPassword, Object salt) {
        String suppliedPasswordSalted = authentication.salt(rawPassword, (String) salt);
//        logger.info("isPasswordValid: " + suppliedPasswordSalted + " == " + encryptedPassword);
        return encryptedPassword.equals(suppliedPasswordSalted);
    }
}
