package com.iliuta.strava.mvc;

import com.iliuta.strava.model.AthleteProfile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestOperations;

import javax.annotation.Resource;

/**
 * Created by apapusoi on 17/12/14.
 */
@Controller
@SessionAttributes("athleteProfile")
public class ProfileController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProfileController.class);


    @Resource(name = "stravaRestTemplate")
    private RestOperations stravaRestTemplate;

    @RequestMapping("/profile")
    public String userProfile(Model model, @ModelAttribute("athleteProfile") AthleteProfile athleteProfile) {
        //model.addAttribute("profile", athleteProfile);
        String userLocale = LocaleContextHolder.getLocale().getLanguage().toLowerCase();
        if (StringUtils.hasText(LocaleContextHolder.getLocale().getCountry())) {
            userLocale = userLocale + "-" + LocaleContextHolder.getLocale().getCountry().toLowerCase();
        }
        model.addAttribute("locale", userLocale);
        LOGGER.info("AthleteProfile retrieved: id={} {}, locale={}", athleteProfile.getId(), athleteProfile.getFirstName(), LocaleContextHolder.getLocale());
        return "strava";
    }

    @ModelAttribute("athleteProfile")
    private AthleteProfile getAthleteProfile() {
        return stravaRestTemplate
                .getForObject("https://www.strava.com/api/v3/athlete", AthleteProfile.class);
    }

    public void setStravaRestTemplate(OAuth2RestTemplate stravaRestTemplate) {
        this.stravaRestTemplate = stravaRestTemplate;
    }

    @ExceptionHandler(InvalidRequestException.class)
    protected String handleInvalidRequestException(InvalidRequestException ire) {
        LOGGER.error("OAuth2 invalid request. Redirect to this page without parameters. Message: {}", ire.getMessage());
        return "redirect:/profile";
    }

}