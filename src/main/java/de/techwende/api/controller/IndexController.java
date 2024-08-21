package de.techwende.api.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller to deliver html
 */
@Controller
@RequestMapping("/")
class IndexController {

    /**
     * Redirects to the startpage view
     *
     * @return the html document
     */
    @GetMapping(path = "", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        return "redirect:start";
    }

    /**
     * Returns the startpage view
     *
     * @return the html document
     */
    @GetMapping(path = "start/**", produces = MediaType.TEXT_HTML_VALUE)
    public String startpage() {
        return "startpage";
    }

    /**
     * Returns the login view
     *
     * @return the html document
     */
    @GetMapping(path = "login/**", produces = MediaType.TEXT_HTML_VALUE)
    public String login() {
        return "login";
    }

    /**
     * Returns the dashboard view
     *
     * @return the html document
     */
    @GetMapping(path = "dashboard/**", produces = MediaType.TEXT_HTML_VALUE)
    public String dashboard() {
        return "dashboard";
    }

}
