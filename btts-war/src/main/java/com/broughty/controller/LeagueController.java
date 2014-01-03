package com.broughty.controller;

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
@RequestMapping("/league")
public class LeagueController {

    private static final Logger log = Logger.getLogger(LeagueController.class.getName());


    @RequestMapping(value = "{name}", method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    LeagueTableData getPlayerPointsInJSON(@PathVariable String name) {
        log.info("request for player points for player " + name);
        BTTSHelper.getPlayerPoints(name);
        LeagueTableData table = new LeagueTableData(name, BTTSHelper.getPlayerPoints(name));
        log.info("request for player points for player " + name + " results " + table.toString());
        return table;

    }



    @RequestMapping(method = RequestMethod.GET,  produces = "application/json")
    public
    @ResponseBody
    List<LeagueTableData> getPointsInJSON() {
        log.info("request for player points for all players ");
        List<LeagueTableData> tableDataList = new ArrayList<LeagueTableData>();

        for(PlayerEnum player : PlayerEnum.values()){
            tableDataList.add(new LeagueTableData(player.getName(), BTTSHelper.getPlayerPoints(player.getName())));

        }

        log.info("request for all player results " + tableDataList.toString());
        return tableDataList;
    }


}