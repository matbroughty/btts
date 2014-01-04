package com.broughty.controller;

import com.broughty.model.PlayerData;
import com.broughty.util.BTTSHelper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 27/12/13.
 */
@Controller
@RequestMapping("/players")
public class PlayersController {

    private static final Logger log = Logger.getLogger(PlayersController.class.getName());

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public
    @ResponseBody
    List<PlayerData> getPlayersInJSON() {
        log.info("json request list of players ");
        return BTTSHelper.getPlayers();
    }


}