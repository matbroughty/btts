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

/**
 * Created by matbroughty on 27/12/13.
 */
@Controller
@RequestMapping("/league")
public class JSONController {

    @RequestMapping(value = "{name}", method = RequestMethod.GET)
    public
    @ResponseBody
    LeagueTableData getPlayerPointsInJSON(@PathVariable String name) {
        BTTSHelper.getPlayerPoints(name);
        LeagueTableData table = new LeagueTableData(name, BTTSHelper.getPlayerPoints(name));
        return table;

    }

    @RequestMapping(method = RequestMethod.GET)
    public
    @ResponseBody
    List<LeagueTableData> getPointsInJSON() {
        List<LeagueTableData> tableDataList = new ArrayList<LeagueTableData>();

        for(PlayerEnum player : PlayerEnum.values()){
            tableDataList.add(new LeagueTableData(player.getName(), BTTSHelper.getPlayerPoints(player.getName())));

        }
        return tableDataList;
    }


}