package se.persandstrom.highscoretracker.internal.common;

import javax.persistence.NoResultException;
import javax.persistence.Query;

public abstract class AbstractDb {

    //XXX: Encapsulation over inheritence?

    protected Object getSingleResult(Query q) {
        try {
            return q.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
