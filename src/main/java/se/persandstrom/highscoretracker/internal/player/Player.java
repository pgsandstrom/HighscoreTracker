package se.persandstrom.highscoretracker.internal.player;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.context.annotation.Scope;
import se.persandstrom.highscoretracker.exception.ForbiddenNameException;
import se.persandstrom.highscoretracker.internal.common.DateNicifier;
import se.persandstrom.highscoretracker.internal.common.Slugify;
import se.persandstrom.highscoretracker.internal.common.ValidateName;

import javax.faces.bean.ManagedBean;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * The player database entry, excluding sensitive authdata
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "Player")
@Entity
@ManagedBean
@Scope("request")
public class Player implements Serializable {

    @Id
    private Long id;

    @Column(name = "slug_name")
    private String slugName;

    @Column(name = "display_name")
    private String displayName;

    private String mail;

    private String description;

    //no need to set, is auto-populated by database
    //unix time is SECONDS, not milliseconds
    private long created;

    public static Player getPlayer(PlayerFull playerFull) {
        Player player = new Player(playerFull.getDisplayName(), playerFull.getSlugName(), playerFull.getMail(),
                playerFull.getDescription());
        player.id = playerFull.getId();
        player.created = playerFull.getCreated();
        player.description = playerFull.getDescription();
        return player;
    }

    public Player() {
    }

    public Player(String displayName, String mail, String description) throws ForbiddenNameException {
        ValidateName.player(displayName);
        this.displayName = displayName;
        this.slugName = Slugify.slugify(displayName);
        this.mail = mail;
        this.description = description;
    }

    /**
     * Only to be used when creating a Player from another Player or a FullPlayer. No validation is made
     *
     * @param displayName
     * @param slugName
     * @param mail
     * @throws ForbiddenNameException
     */
    private Player(String displayName, String slugName, String mail, String description) {
        this.displayName = displayName;
        this.slugName = slugName;
        this.mail = mail;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getSlugName() {
        return slugName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        //TODO validate
        this.mail = mail;
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
        return DateNicifier.nicify(created);
    }

    public PlayerUpdateBean createUpdateBean() {
        return new PlayerUpdateBean(mail, description);
    }
}
