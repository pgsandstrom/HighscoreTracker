package se.persandstrom.highscoretracker.internal.simple;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.persandstrom.highscoretracker.internal.DatabaseSingleton;
import se.persandstrom.highscoretracker.internal.common.AbstractDb;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

public class SimpleDb<T> extends AbstractDb {

    private static final Logger logger = LoggerFactory.getLogger(SimpleDb.class);

    private DatabaseSingleton database;
    private Class<T> type;  //there is no other good way to get the generic class
    private String tableName;

    public SimpleDb(DatabaseSingleton database, Class<T> type) {
        this.database = database;
        this.type = type;
        this.tableName = type.getName();
    }

    public List<T> getNewest() {
        EntityManager em = database.getEntityManager();
        try {
            //easier to sort by id instead of created :)
            Query q = em.createQuery("select t from " + tableName + " t ORDER BY t.id DESC");
            q.setMaxResults(5);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public <Y> void create(Y object) {
        EntityManager em = database.getEntityManager();
        try {
            em.getTransaction().begin();
            em.persist(object);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    //TODO this could be done if the objects had an "update with"-method, but maybe that is stupid
//    public void update(String slugName, String mail, String description) throws NotFoundException {
//
//        Player player = getBySlugName(slugName);
//        if (player == null) {
//            throw new PlayerNotFoundException(slugName);
//        }
//
//        if (mail != null) {
//            player.setMail(mail);
//        }
//
//        if (description != null) {
//            player.setDescription(description);
//        }
//
//        EntityManager em = database.getEntityManager();
//        try {
//            em.getTransaction().begin();
//            em.merge(player);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }

    public T getBySlugName(String slugName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select t from " + tableName + " t WHERE t.slugName = :slugName");
            q.setParameter("slugName", slugName);
            return (T) getSingleResult(q);
        } finally {
            em.close();
        }
    }

    public T getByDisplayName(String displayName) {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select t from " + tableName + " t WHERE t.displayName = :displayName");
            q.setParameter("displayName", displayName);
            return (T) getSingleResult(q);
        } finally {
            em.close();
        }
    }

    public List<T> getList(int offset, int count) {
        //TODO try if 0,10 actually gets the first item in the list, or how it works
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select t from " + tableName + " t");
            q.setFirstResult(offset);
            q.setMaxResults(offset + count);
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public List<T> getList() {
        EntityManager em = database.getEntityManager();
        try {
            Query q = em.createQuery("select t from " + tableName + " t");   //TODO order?
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public T getById(long id) {
        EntityManager em = database.getEntityManager();
        try {
            return em.find(type, id);
        } finally {
            em.close();
        }
    }
}
