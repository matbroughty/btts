package com.broughty.controller;

import com.broughty.model.WeekData;
import com.broughty.util.BTTSHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.logging.Logger;

/**
 * Created by matbroughty on 27/12/13.
 */
@Controller
@RequestMapping("/")
public class MetaController {

    private static final Logger log = Logger.getLogger(MetaController.class.getName());

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    WeekData getWeekDataInJSON() {
        log.info("json request current week data");
        return BTTSHelper.getCurrentWeekData();
    }

}