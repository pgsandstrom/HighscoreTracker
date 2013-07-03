package se.persandstrom.highscoretracker.internal.score;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheCoordinationType;
import org.eclipse.persistence.annotations.CacheType;
import se.persandstrom.highscoretracker.internal.common.DateNicifier;
import se.persandstrom.highscoretracker.internal.level.Level;
import se.persandstrom.highscoretracker.internal.player.Player;

import javax.persistence.*;
import java.io.Serializable;

@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "Score")
@Entity
@Cache(
        type = CacheType.SOFT, // Cache everything until the JVM decides memory is low.
        size = 32000,  // Use 32,000 as the initial cache size.
        expiry = 1000 * 60 * 60 * 24,  // 1 day
        coordinationType = CacheCoordinationType.INVALIDATE_CHANGED_OBJECTS  // if cache coordination is used,
        // only send invalidation messages.
)
public class Score implements Serializable {

    @Id
    private Long id;

    @Column(name = "level_id", insertable = false, updatable = false)
    private Long levelId;

    @ManyToOne(targetEntity = Level.class, fetch=FetchType.EAGER)
    @JoinColumn(name = "level_id", referencedColumnName = "id")
    @JsonIgnore
    private Level level;

    @Column(name = "player_id", insertable = false, updatable = false)
    private Long playerId;

    @ManyToOne(targetEntity = Player.class, fetch=FetchType.EAGER)
    @JoinColumn(name = "player_id", referencedColumnName = "id")
    @JsonIgnore
    private Player player;

    private Long score;

    //no need to set, is auto-populated by database
    //unix time is SECONDS, not milliseconds
    private long created;

    //this is my hacky solution to know when jsf should render the game title above the score
    @Transient
    @JsonIgnore
    private boolean firstScoreForGame;

    public Score() {
    }

    public Score(Level level, Player player, long score) {
        this.level = level;
        this.levelId = level.getId();
        this.player = player;
        this.playerId = player.getId();
        this.score = score;
    }

    public Long getId() {
        return id;
    }

    public Long getLevelId() {
        return levelId;
    }

    public Level getLevel() {
        return level;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public Player getPlayer() {
        return player;
    }

    public Long getScore() {
        return score;
    }

    public long getCreated() {
        return created;
    }

    public boolean isFirstScoreForGame() {
        return firstScoreForGame;
    }

    public void setFirstScoreForGame(boolean firstScoreForGame) {
        this.firstScoreForGame = firstScoreForGame;
    }

    @JsonIgnore
    public String getCreatedFormatted() {
        //TODO cache result? Find out exactly how long those objects live!
        return DateNicifier.nicify(created);
    }
}
