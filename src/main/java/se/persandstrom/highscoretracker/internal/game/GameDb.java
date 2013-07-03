package se.persandstrom.highscoretracker.internal.game;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.GameNotFoundException;
import se.persandstrom.highscoretracker.internal.DatabaseSingleton;
import se.persandstrom.highscoretracker.internal.common.AbstractDb;
import se.persandstrom.highscoretracker.internal.player.Player;
import se.persandstrom.highscoretracker.internal.simple.SimpleDb;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class GameDb extends AbstractDb implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(GameDb.class);

    @Autowired
    private DatabaseSingleton database;

    private SimpleDb<Game> simpleDb;

    @PostConstruct
    private void postConstruct() {
        simpleDb = new SimpleDb(database, Game.class);
    }

    public void create(Game game) {
        simpleDb.create(game);
    }

    public void update(String slugName, String description) throws GameNotFoundException {

        Game game = getBySlugName(slugName);
        if (game == null) {
            throw new GameNotFoundException(slugName);
        }

        if (description != null) {
            game.setDescription(description);
        }

        EntityManager em = database.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(game);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Game getById(long id) {
        return simpleDb.getById(id);
    }

    public Game getBySlugName(String slugName) {
        return simpleDb.getBySlugName(slugName);
    }

    public List<Game> getNewest() {
        return simpleDb.getNewest();
    }

    public List<Game> getList() {
        return simpleDb.getList();
    }

}
