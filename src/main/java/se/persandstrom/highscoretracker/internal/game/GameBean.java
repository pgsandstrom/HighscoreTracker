package se.persandstrom.highscoretracker.internal.game;

import org.springframework.context.annotation.Scope;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@Scope("request")
public class GameBean implements Serializable {

    private long id;
    private String displayName;
    private String slugName;
    private String description;
    private long ownerId;

    public GameBean() {
    }

    public GameBean(Game game) {
        this.id = game.getId();
        this.displayName = game.getDisplayName();
        this.slugName = game.getSlugName();
        this.description = game.getDescription();
        this.ownerId = game.getOwnerId();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getSlugName() {
        return slugName;
    }

    public void setSlugName(String slugName) {
        this.slugName = slugName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(long ownerId) {
        this.ownerId = ownerId;
    }
}
