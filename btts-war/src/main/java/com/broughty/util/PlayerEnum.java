package com.broughty.util;

import javax.mail.internet.InternetAddress;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by matbroughty on 12/12/13.
 */
public enum PlayerEnum {


    Bhavesh("Bhavesh", "Bhavesh.Patel@hpdsoftware.com", "07760178009"),
    Charles("Charles", "Charles.Fletcher@hpdsoftware.com", "07904026930"),
    Dave("Dave", "Dave.Spruit@hpdsoftware.com", "07956537929"),
    Frank("Frank", "Frank.Weaver@hpdsoftware.com", "07743488945"),
    Gerald("Gerald", "Gerald.McLaughlin@hpdsoftware.com", "07963386490"),
    Jason("Jason", "Jason.King@hpdsoftware.com", "07429084210"),
    Jonathon("Jonathon", "Jonathan.Neighbour@hpdsoftware.com", null),
    Jose("Jose", "Jose.Santos@hpdsoftware.com", null),
    Mat("Mat", "mat@broughty.com", "07712647785"),
    Pia("Pia", "Pia.O'Boyle@hpdsoftware.com", null),
    Tim("Tim", "Tim.Wilcox@hpdsoftware.com", "07760177037"),
    Toby("Toby", "toby.low@hpdsoftware.com", null),
    Star("Star", "mat@broughty.com", null);

    private static final Logger log = Logger.getLogger(PlayerEnum.class.getName());
    String name;

    String email;


    String mobile;


    PlayerEnum(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public InternetAddress getMailAddress() throws UnsupportedEncodingException {
        return new InternetAddress(getEmail(), getName());
    }


    public static InternetAddress[] getMailAddresses() {

        List<InternetAddress> internetAddresses = new ArrayList<InternetAddress>();
        try {
            for (PlayerEnum playerEnum : PlayerEnum.values()) {
                internetAddresses.add(playerEnum.getMailAddress());
            }

        } catch (Throwable t) {
            log.log(Level.WARNING, "Problem parsing email addresses.", t);
        }

        return internetAddresses.toArray(new InternetAddress[internetAddresses.size()]);
    }


}
