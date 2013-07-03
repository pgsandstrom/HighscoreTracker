package se.persandstrom.highscoretracker.internal.player;

import org.codehaus.jackson.annotate.JsonIgnore;
import se.persandstrom.highscoretracker.internal.authentication.AuthData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * Represents the full Player database entry
 */
@SuppressWarnings("JpaDataSourceORMInspection")
@Table(name = "Player")
@Entity
public class PlayerFull implements Serializable {

    /**
     * TODO this class is like an extended copy of Player. It bugs out if I extends it. Find a nicer solution...
     * One possible solution: Use only Player, have @JsonIgnore on authtoken and dynamicSalt
     */

    @Id
    private Long id;

    @Column(name = "slug_name")
    private String slugName;

    @Column(name = "display_name")
    private String displayName;

    private String mail;

    private String description;

    private long created;

    @JsonIgnore
    @Column(name = "auth_token")
    private String authToken;

    @JsonIgnore
    @Column(name = "dynamic_salt")
    private String dynamicSalt;

    public PlayerFull() {
    }

    public PlayerFull(Player player, AuthData authData) {
        this.id = player.getId();
        this.slugName = player.getSlugName();
        this.displayName = player.getDisplayName();
        this.mail = player.getMail();
        this.description = player.getDescription();
        this.created = player.getCreated();
        this.authToken = authData.authToken;
        this.dynamicSalt = authData.dynamicSalt;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getDynamicSalt() {
        return dynamicSalt;
    }

    public AuthData getAuthData() {
        return new AuthData(authToken, dynamicSalt);
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

    public String getDescription() {
        return description;
    }

    public long getCreated() {
        return created;
    }

}
