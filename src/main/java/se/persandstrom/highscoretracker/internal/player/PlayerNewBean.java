package se.persandstrom.highscoretracker.internal.player;

import org.springframework.context.annotation.Scope;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@Scope("request")
public class PlayerNewBean implements Serializable {

    private String displayName;
    private String password;
    private String mail;
    private String description;

    public PlayerNewBean() {
    }

    public PlayerNewBean(String displayName, String password, String mail, String description) {
        this.displayName = displayName;
        this.password = password;
        this.mail = mail;
        this.description = description;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}