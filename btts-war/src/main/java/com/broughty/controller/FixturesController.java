package com.broughty.controller;

import com.broughty.model.FixtureData;
import com.broughty.model.WeekData;
import com.broughty.util.BTTSHelper;
import com.broughty.util.CacheHelper;
import com.broughty.util.ResultsWebPageEnum;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 27/12/13.
 */
@Controller
@RequestMapping("/fixtures")
public class FixturesController {

    private static final Logger log = Logger.getLogger(FixturesController.class.getName());


    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<FixtureData> getFixturesInJSON() {
        log.info("json getFixturesInJSON");
        List<FixtureData> fixtureDataList;
        if (CacheHelper.getCache() != null) {
            if (!CacheHelper.getCache().containsKey(CacheHelper.CURRENT_FIXTURES_LST)) {
                log.info("CurrentFixtures not in map.....");
                fixtureDataList = (List<FixtureData>) CacheHelper.getCache().put(CacheHelper.CURRENT_FIXTURES_LST, getFixtures());
            }
            fixtureDataList = (List<FixtureData>) CacheHelper.getCache().get(CacheHelper.CURRENT_FIXTURES_LST);
        } else {
            log.info("Cache not available for CurrentFixtures");
            fixtureDataList = getFixtures();
        }
        return fixtureDataList;
    }


    List<FixtureData> getFixtures() {
        log.info("request for current fixtures ");
        List<FixtureData> fixtureDataList = new ArrayList<FixtureData>();

        for (ResultsWebPageEnum fixture : ResultsWebPageEnum.values()) {
            log.info("Getting current fixtures for url " + fixture.getPage());
            WeekData weekData = BTTSHelper.getCurrentWeekData();
            List<FixtureData> urlFixtureDataList = new ArrayList<FixtureData>();
            try {
                urlFixtureDataList.addAll(BTTSHelper.getFixtures(fixture.getPage(), weekData.getStartDate(), weekData.getEndDate()));
            } catch (Throwable t) {
                log.log(Level.WARNING, "Problem getting fixtures from URL - skipping " + fixture.getPage());
            }
            log.info("Adding  " + urlFixtureDataList.size() + " fixtures from URL " + fixture.getPage());
            fixtureDataList.addAll(urlFixtureDataList);
        }

        log.info("Returning  " + fixtureDataList.size() + " fixtures.");


        return fixtureDataList;

    }


    @RequestMapping(value = "simple", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<String> getBasicFixturesInJSON() {
        log.info("json getBasicFixturesInJSON");
        List<String> fixtureStrList = new ArrayList<String>();


        for(FixtureData fixtureData : getFixturesInJSON()){
            fixtureStrList.add(fixtureData.getHomeTeam());
        }

        Collections.sort(fixtureStrList);

        return fixtureStrList;
    }


}