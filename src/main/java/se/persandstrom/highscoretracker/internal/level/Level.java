package se.persandstrom.highscoretracker.internal.level;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.internal.common.DateNicifier;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.common.ValidateName;
import se.persandstrom.highscoretracker.internal.game.Game;

import javax.persistence.*;
import java.io.Serializable;


@SuppressWarnings({"JpaDataSourceORMInspection", "UnusedDeclaration"})
@Table(name = "Level")
@Entity
@Cache(
        type = CacheType.SOFT, // Cache everything until the JVM decides memory is low.
        size = 1024,  // Use 1024 as the initial cache size.
        expiry = 1000 * 60 * 60 * 24,  // 1 day
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS  // if cache coordination is used,
        // only send invalidation messages.
)
public class Level implements Serializable {

    @Id
    private Long id;

    @Column(name = "game_id", insertable = false, updatable = false)
    private Long gameId;

    @ManyToOne(targetEntity = Game.class, fetch = FetchType.EAGER)
    @JoinColumn(name = "game_id", referencedColumnName = "id")
    @JsonIgnore
    private Game game;

    @Column(name = "slug_name")
    private String slugName;

    @Column(name = "display_name")
    private String displayName;

    //no need to set, is auto-populated by database
    //unix time is SECONDS, not milliseconds
    private long created;

    public Level() {
    }

    public Level(String displayName, Game game) throws ForbiddenNameException {
        ValidateName.level(displayName);
        this.displayName = displayName;
        this.slugName = Slugify.slugify(displayName);
        this.game = game;
        this.gameId = game.getId();
    }

    public Long getId() {
        return id;
    }

    public Long getGameId() {
        return gameId;
    }

    public Game getGame() {
        return game;
    }

    public String getSlugName() {
        return slugName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public long getCreated() {
        return created;
    }

    @JsonIgnore
    public String getCreatedFormatted() {
        //TODO cache result? Find out exactly how long those objects live!
        return DateNicifier.nicify(created);
    }
}
