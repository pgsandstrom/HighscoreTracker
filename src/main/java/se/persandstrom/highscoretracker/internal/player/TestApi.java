package se.persandstrom.highscoretracker.internal.player;

import org.springframework.context.annotation.Scope;

import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.http.HttpServletRequest;

//so this syntax works, but whatever...
@Named
@Scope("request")
public class TestApi {

    @Inject
    PlayerApi playerApi;

    int i = 0;

    public String getTest() {
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
                .getRequest();
        i++;
        return "hej: " + i + ", " + (playerApi != null) + ", \"" + request.getContextPath()+"\"";
    }
}
