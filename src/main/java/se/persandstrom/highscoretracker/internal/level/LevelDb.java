package se.persandstrom.highscoretracker.internal.level;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.internal.DatabaseSingleton;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.simple.SimpleDb;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class LevelDb implements Serializable {

    @Inject
    private DatabaseSingleton database;

    private SimpleDb<Level> simpleDb;

    @PostConstruct
    private void postConstruct() {
        simpleDb = new SimpleDb(database, Player.class);
    }

    //TODO check out caches like this: http://stackoverflow
    // .com/questions/672754/why-eclipselink-query-cache-only-works-when-i-use-query-getsingleresult
    //especially important to do it when submitting score!

    public void create(Level level) {
        simpleDb.create(level);
    }

    public Level get(long gameId, String levelSlugName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select l from Level l, Game g WHERE g.id = :gameId AND g.id = l.gameId AND"
                    + " l.slugName = :levelSlugName");
            q.setParameter("gameId", gameId);
            q.setParameter("levelSlugName", levelSlugName);
            Level level = (Level) simpleDb.getSingleResult(q);
            return level;
        } finally {
            em.close();
        }
    }

    public Level get(String gameSlugName, String levelSlugName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select l from Level l, Game g WHERE g.slugName = :gameSlugName AND g.id = l.gameId AND"
                    + " l.slugName = :levelSlugName");

            q.setParameter("gameSlugName", gameSlugName);
            q.setParameter("levelSlugName", levelSlugName);
            Level level = (Level) simpleDb.getSingleResult(q);
            return level;
        } finally {
            em.close();
        }
    }

    public List<Level> getGameLevels(long gameId) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select l from Level l WHERE l.gameId = :gameId");
            q.setParameter("gameId", gameId);
            return (List<Level>) q.getResultList();
        } finally {
            em.close();
        }
    }
}
