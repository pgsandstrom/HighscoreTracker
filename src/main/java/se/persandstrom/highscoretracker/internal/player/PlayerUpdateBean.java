package se.persandstrom.highscoretracker.internal.player;

import org.springframework.context.annotation.Scope;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@Scope("request")
public class PlayerUpdateBean implements Serializable {

    private String mail;

    private String description;

    public PlayerUpdateBean() {
    }

    public PlayerUpdateBean(String mail, String description) {
        this.mail = mail;
        this.description = description;
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