package com.broughty.controller;

import com.broughty.model.PlayerChoicesData;
import com.broughty.model.LeagueTableData;
import com.broughty.util.BTTSHelper;
import com.broughty.util.PlayerEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
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



    @RequestMapping(method = RequestMethod.GET,  produces = "application/json")
    public
    @ResponseBody
    List<PlayerChoicesData> getCurrentWeekChoicesInJSON() {
        log.info("json request choices for current week ");
        return BTTSHelper.getWeeksChoices(BTTSHelper.getCurrentWeek());
    }


}