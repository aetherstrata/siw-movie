package dev.aest.siw.movie.controller;

import dev.aest.siw.movie.SiwMovie;
import dev.aest.siw.movie.service.CredentialsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalController
{
    @ModelAttribute("requestUrl")
    public String addCurrentAddress(HttpServletRequest request){
        return request.getServletPath();
    }

    @ModelAttribute("project")
    public String addProjectName(){
        return SiwMovie.PROJECT_NAME;
    }
}
