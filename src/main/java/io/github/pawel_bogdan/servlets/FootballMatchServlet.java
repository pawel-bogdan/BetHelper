package io.github.pawel_bogdan.servlets;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.pawel_bogdan.services.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "FootballMatch", urlPatterns = {"/matches/*"})
public class FootballMatchServlet extends HttpServlet {
    private final Logger logger = LoggerFactory.getLogger(FootballMatchServlet.class);
    private final ObjectMapper mapper = new ObjectMapper();
    private final Service service;
    private final String INCORRECT_URL_MSG = "URL must starts with: https://www.efortuna.pl/zaklady-bukmacherskie/pilka-nozna/.";
    public FootballMatchServlet() {
        service = new Service();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        logger.info("Got request: " + request.getRequestURI());
        response.setContentType("application/json;charset=UTF-8");
        var requestURI = request.getRequestURI();
        try {
            var matches = service.getMatchesAndUpdateDB(requestURI.replace("/matches/", ""));
            mapper.writeValue(response.getOutputStream(), matches);
        } catch (IllegalArgumentException e) {
            response.setStatus(400);
            mapper.writeValue(response.getOutputStream(), INCORRECT_URL_MSG);
        }
    }
}
