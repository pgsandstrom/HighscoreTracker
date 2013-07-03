package se.persandstrom.highscoretracker.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import se.persandstrom.highscoretracker.external.PlayerJsfApi;
import se.persandstrom.highscoretracker.internal.common.ApplicationContextProvider;
import se.persandstrom.highscoretracker.internal.common.FilterExclude;
import se.persandstrom.highscoretracker.internal.authentication.Authentication;
import se.persandstrom.highscoretracker.internal.authentication.SessionCookie;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//not used, so I commented this out
//@WebFilter({"/palunx"})
public class LoginFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

//    @Inject
    Authentication authentication;

    @Override
    public void init(FilterConfig config)  {
        // If you have any <init-param> in web.xml, then you could get them
        // here by config.getInitParameter("name") and assign it as field.
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException,
            ServletException {

        // I fail at injecting beans into this filter, so I do this hacky thing instead :(
        if (authentication == null) {
            logger.info("authentication init");
            authentication = ApplicationContextProvider.getApplicationContext().getBean("authentication", Authentication
                    .class);
        }

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
//        HttpSession session = request.getSession(false);

        String servletPath = request.getServletPath();
        logger.info("servletPath: " + servletPath);

        //is FilterExlude unnecessary?
        if (FilterExclude.isExcluded(servletPath)) {
            chain.doFilter(req, res);
            return;
        }

        boolean authenticated = false;
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
            if (SessionCookie.COOKIE_NAME.equals(cookie.getName())) {
                //TODOO what happens if I send an old cookie?
                String value = cookie.getValue();
                authenticated = authentication.validateCookie(new SessionCookie(value));
                break;
            }
        }

        logger.info("authenticated: " + authenticated);
        if (!authenticated) {
            //redirect to login
            response.sendRedirect("/login?nextUrl=" + servletPath);
        } else {
            chain.doFilter(req, res);
        }
    }

    @Override
    public void destroy() {
        // If you have assigned any expensive resources  as field of
        // this Filter class, then you could clean/close them here.
    }

}