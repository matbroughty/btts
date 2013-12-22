package com.broughty.util;

import com.twilio.sdk.TwilioRestClient;
import com.twilio.sdk.resource.factory.SmsFactory;
import com.twilio.sdk.resource.instance.Sms;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 22/12/13.
 */
public class SmsHelper {

    private static final Logger log = Logger.getLogger(SmsHelper.class.getName());


    public static final String SMS_ACCOUNT_SID = "AC72bc71950d5db7125e5669b797a9ea26";

    public static final String SMS_AUTH_TOKEN = "9ef3f6d72fffc0bf21283e1e668aba93";


    public static void mobileAlert(StringBuilder alertString, boolean globalAlert, PlayerEnum playerEnum) {
        try {

            TwilioRestClient client = new TwilioRestClient(SMS_ACCOUNT_SID, SMS_AUTH_TOKEN);


            Map<String, String> params = new HashMap<String, String>();

            params.put("Body", alertString.toString());

            params.put("To", playerEnum.getMobile());

            params.put("From", "+441604422945");

            SmsFactory messageFactory = client.getAccount().getSmsFactory();

            if (StringUtils.isBlank(playerEnum.getMobile())) {
                log.info(playerEnum.getName() + " doesn't have a mobile set.");
            } else {
                Sms message = messageFactory.create(params);
                log.info("Sent SMS message to player " + playerEnum.getName() + " mobile " +
                        playerEnum.getMobile() + " price = " + message.getPrice() + " message->" + alertString.toString());
            }


            if (globalAlert) {
                for (PlayerEnum player : PlayerEnum.values()) {
                    if (!StringUtils.isBlank(player.getEmail())) {
                        params.put("To", player.getMobile());
                        Sms message = messageFactory.create(params);
                        log.info("Sent SMS message to player " + player.getName() + " mobile " +
                                player.getMobile() + " price = " + message.getPrice() + " message->" + alertString.toString());

                    }


                }
            }

        } catch (Throwable t) {
            log.log(Level.SEVERE, "An unhandled mobileAlert error message for alert -> " + alertString, t);
        }
    }

}
