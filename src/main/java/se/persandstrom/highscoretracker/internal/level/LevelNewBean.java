package se.persandstrom.highscoretracker.internal.level;

import org.springframework.context.annotation.Scope;

import javax.faces.bean.ManagedBean;
import java.io.Serializable;

@ManagedBean
@Scope("request")
public class LevelNewBean implements Serializable {

    private String displayName;

    public LevelNewBean() {
    }

    public LevelNewBean(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }
}