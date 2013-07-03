package se.persandstrom.highscoretracker.internal.score;


import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.internal.DatabaseSingleton;
import se.persandstrom.highscoretracker.internal.common.AbstractDb;
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
public class ScoreDb extends AbstractDb implements Serializable {

    @Inject
    DatabaseSingleton database;

    private SimpleDb<Score> simpleDb;

    @PostConstruct
    private void postConstruct() {
        simpleDb = new SimpleDb(database, Score.class);
    }

    public void create(Score game) {
        simpleDb.create(game);
    }

    public Score getById(long id) {
        return simpleDb.getById(id);
    }

    public List<Score> getHighscore(String gameSlugName, String levelSlugName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select s from Score s, Level l,  " +
                    "Game g WHERE g.slugName = :gameSlugName AND g.id = l.gameId AND l.slugName = :levelSlugName" +
                    " AND l.id = s.levelId  ORDER BY s.score DESC");
            q.setParameter("gameSlugName", gameSlugName);
            q.setParameter("levelSlugName", levelSlugName);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    @SuppressWarnings("JpaQueryApiInspection")
    public List<Score> getAllPlayerHighscores(long playerId) {
        EntityManager em = database.getEntityManager();
        try {
            //This is a rather complicated query.
            //Inspired by http://dev.mysql.com/doc/refman/5.6/en/example-maximum-column-group-row.html

            //XXX perhaps check out if it can be optimized by re-arranging the where-clause,
            // since I guess s2 has no indexes or anything
            Query q = em.createQuery("select s1 from Score s1, (select temp.levelId, " +
                    "max(temp.score) from Score temp where temp.playerId = :playerId group by temp.levelId) s2 where " +
                    "s1.score = s2.score and s1.levelId = s2.levelId");
            q.setParameter("playerId", playerId);

            return q.getResultList();
        } finally {
            em.close();
        }
    }

}
