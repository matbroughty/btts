package com.broughty.controller;

import com.broughty.model.PlayerChoicesData;
import com.broughty.model.PlayerData;
import com.broughty.util.BTTSHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 27/12/13.
 */
@Controller
@RequestMapping("/choices")
public class ChoicesController {


    private static final Logger log = Logger.getLogger(ChoicesController.class.getName());


    @RequestMapping(value = "{week}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<PlayerChoicesData> getWeekChoicesInJSON(@PathVariable String week) {
        log.info("json request choices for week " + week);
        return BTTSHelper.getWeeksChoices(week);

    }

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<PlayerChoicesData> getCurrentWeekChoicesInJSON() {
        log.info("json request choices for current week ");
        return BTTSHelper.getWeeksChoices(BTTSHelper.getCurrentWeek());
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postCurrentWeekChoicesInJSON(@RequestBody PlayerChoicesData choice) {
        log.info("Anything?");
        log.info("post json request choices for current week " + choice);
    }

    @RequestMapping(value = "/newchoice", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postCurrentWeekChoicesInString(@RequestBody String textChoice) {
        log.info("post string request choices for current week " + textChoice);
    }

    @RequestMapping(value = "/simple", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void postSimpleJsonObject(@RequestBody PlayerData playerDataChoice) {
        log.info("postSimpleJsonObject for player data " + playerDataChoice);
    }


    @RequestMapping(value = "{week}/{name}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    PlayerChoicesData getPlayerWeekChoicesInJSON(@PathVariable String week, @PathVariable String name) {
        log.info("json request player choices for player " + name + " for week " + week);
        return BTTSHelper.getPlayersWeeksChoices(week, name);
    }


    @RequestMapping(value = "player/{name}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<PlayerChoicesData> getAllPlayersChoicesInJSON(@PathVariable String name) {
        log.info("json request for all player choices for player " + name);
        return BTTSHelper.getAllPlayersChoices(name);
    }

}