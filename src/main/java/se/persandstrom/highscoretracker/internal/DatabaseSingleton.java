package se.persandstrom.highscoretracker.internal;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Component
@Scope("application")
public class DatabaseSingleton {


    //TODO for example playerDb.update() creates several EntityManager. It would be awesome to use @PersistenceContext
    // this might be helpful:
    // http://stackoverflow.com/questions/12252088/unable-to-import-persistence-xml-within-applicationcontext-xml-file
    private EntityManagerFactory factory;

    @PostConstruct
    public void constructor() {
        factory = Persistence.createEntityManagerFactory("default");
    }

    public EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
}
