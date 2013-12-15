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


    Bhavesh("Bhavesh","bhav@hpd.co.uk"),
    Charles("Charles","charles.fletcher@hpdsoftware.com"),
    Dave("Dave","Dave.Spruit@hpdsoftware.com"),
    Frank("Frank","frank@hpd.co.uk"),
    Gerald("Gerald","Gerald.McLaughlin@hpdsoftware.com"),
    Jason("Jason","jason.king@hpdsoftware.com"),
    Jonathon("Jonathon","Jonathan.Neighbour@hpdsoftware.com"),
    Jose("Jose","jose@hpd.co.uk"),
    Mat("Mat","matb@hpd.co.uk"),
    Pia("Pia","pia@hpd.co.uk"),
    Tim("Tim","tim@hpd.co.uk"),
    Toby("Toby","toby.low@hpdsoftware.com");

    private static final Logger log = Logger.getLogger(PlayerEnum.class.getName());
    String name;

    String email;

    PlayerEnum(String name, String email){
        this.name = name;
        this.email = email;
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


    public InternetAddress getMailAddress() throws UnsupportedEncodingException{
       return new InternetAddress(getEmail(), getName());
    }


    public static InternetAddress[] getMailAddresses(){

        List<InternetAddress> internetAddresses = new ArrayList<InternetAddress>();
        try{
        for(PlayerEnum playerEnum : PlayerEnum.values()){
            internetAddresses.add(playerEnum.getMailAddress());
        }

        }catch(Throwable t){
            log.log(Level.WARNING, "Problem parsing email addresses." ,t);
        }

        return (InternetAddress[])internetAddresses.toArray();
    }

}
