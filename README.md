HighscoreTracker
========

A backend and web page for storing and displaying highscores to different games. The idea is to someday also support storing states for turn-based games, single- or multiplayer.

The backend has a REST api so external services can use it.

Current status
--------------

The basics work, but the web page is rather bad.

Why I did this
--------------

There was a number of techniques that I needed to learn:
* Spring (MVC/Security)
* JPA/Eclipselink
* JSF (Primefaces)

I would really prefer not to use Spring, but since it still is rather popular I might as well get to know it. I would really prefer to use JSR-330 annotation (@Named, @Inject) instead of the Spring stuff, but I used JSR-330 in PloxWormWeb, so I use the silly Spring syntax in this project.

Actually, I'm not completely sold on JSF either, but I can understand that it can be very nice to use in certain projects. If I would have done this project only to make the final product awesome, then I would have separated the frontend and backend, and retrieved data from the REST-api with ajax.