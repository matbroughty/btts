package com.broughty.controller;

import com.broughty.model.WeekData;
import com.broughty.util.BTTSHelper;
import com.broughty.util.CacheHelper;
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
        WeekData weekData;
        if (CacheHelper.getCache() != null) {
            if (!CacheHelper.getCache().containsKey(CacheHelper.CURRENT_WEEK_WEEKDATA)) {
                log.info("WeekData not in map.....");
                CacheHelper.getCache().put(CacheHelper.CURRENT_WEEK_WEEKDATA, BTTSHelper.getCurrentWeekData());
            }
            weekData = (WeekData) CacheHelper.getCache().get(CacheHelper.CURRENT_WEEK_WEEKDATA);
        } else {
            log.info("Cache not available for WeekData");
            weekData = BTTSHelper.getCurrentWeekData();
        }

        return weekData;
    }

}