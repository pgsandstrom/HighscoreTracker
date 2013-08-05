package se.persandstrom.highscoretracker.internal.player;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import se.persandstrom.highscoretracker.exception.NotFoundException;
import se.persandstrom.highscoretracker.exception.PlayerNotFoundException;
import se.persandstrom.highscoretracker.internal.DatabaseSingleton;
import se.persandstrom.highscoretracker.internal.simple.SimpleDb;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Component
@Scope("application")
public class PlayerDb implements Serializable {

    private static final Logger logger = LoggerFactory.getLogger(PlayerDb.class);

    @Autowired
    private DatabaseSingleton database;

    private SimpleDb<Player> simpleDb;

    @PostConstruct
    private void postConstruct() {
        simpleDb = new SimpleDb(database, Player.class);
    }

    public void create(PlayerFull playerFull) {
        logger.info("Creating player: " + playerFull.getSlugName() + ", " + playerFull.getId());
        simpleDb.create(playerFull);
    }

    public void update(String slugName, String mail, String description) throws NotFoundException {

        Player player = getBySlugName(slugName);
        if (player == null) {
            throw new PlayerNotFoundException(slugName);
        }

        if (mail != null) {
            player.setMail(mail);
        }

        if (description != null) {
            player.setDescription(description);
        }

        EntityManager em = database.getEntityManager();
        try {
            em.getTransaction().begin();
            em.merge(player);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    public Player getById(long id) {
        return simpleDb.getById(id);
    }

    public Player getBySlugName(String slugName) {
        return simpleDb.getBySlugName(slugName);
    }

    public Player getByDisplayName(String displayName) {
        return simpleDb.getByDisplayName(displayName);
    }

    public List<Player> getList() {
        return simpleDb.getList();
    }

    public List<Player> getList(int offset, int count) {
        return simpleDb.getList(offset, count);
    }

    public List<Player> getNewest() {
        return simpleDb.getNewest();
    }


    public PlayerFull getFull(String slugName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select t from PlayerFull t WHERE t.slugName = :slugName");
            q.setParameter("slugName", slugName);
            return (PlayerFull) simpleDb.getSingleResult(q);
        } finally {
            em.close();
        }
    }

    public PlayerFull getFullByDisplayName(String displayName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select t from PlayerFull t WHERE t.displayName = :displayName");
            q.setParameter("displayName", displayName);
            return (PlayerFull) simpleDb.getSingleResult(q);
        } finally {
            em.close();
        }
    }

}
