package se.persandstrom.highscoretracker.internal.game;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.internal.common.DateNicifier;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.common.ValidateName;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "Game")
@Entity
@Cache(
        type = CacheType.SOFT, // Cache everything until the JVM decides memory is low.
        size = 128,  // Use 128 as the initial cache size.
        expiry = 1000 * 60 * 60 * 24,  // 1 day
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS  // if cache coordination is used,
        // only send invalidation messages.
)
public class Game implements Serializable {

    @Id
    private Long id;

    @Column(name = "owner_id")
    private Long ownerId;

    @Column(name = "slug_name")
    private String slugName;

    @Column(name = "display_name")
    private String displayName;

    private String description;

    //no need to set, is auto-populated by database
    //unix time is SECONDS, not milliseconds
    private long created;

    public Game() {
    }

    public Game(Long ownerId, String displayName, String description) throws ForbiddenNameException {
        ValidateName.game(displayName);
        this.ownerId = ownerId;
        this.displayName = displayName;
        this.slugName = Slugify.slugify(displayName);
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public String getSlugName() {
        return slugName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getCreated() {
        return created;
    }

    @JsonIgnore
    public String getCreatedFormatted() {
        //TODO cache result? Find out exactly how long those objects live!
        //TODO maybe this method should be, eh, inherited or something, if it gets more complex. It is repeadet in
        // many objects...
        return DateNicifier.nicify(created);
    }
}
