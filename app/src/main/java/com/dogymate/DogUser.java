package com.dogymate;

import java.util.ArrayList;

public class DogUser {

    private String username;
    private String mobile;
    private String dogname;
    private String dogbreed;
    private String dogfrequency;
    private String doghost;
    private ArrayList<String> dogcharacter;
    private String dogage;
    private String dogsex;
    private double lat;
    private double lon;
    private String sprayed;
    private String about;
    private String vak;
    private String dogdistance;

    public DogUser()
    {

    }

    public DogUser(String us,String m,String dn,String db,String df,String dh,ArrayList<String> dc,String da,String ds,String sp,String ab,String vk,double la,double lo,String dd)
    {
        this.username=us;
        this.mobile=m;
        this.dogname=dn;
        this.dogage=da;
        this.dogbreed=db;
        this.dogsex=ds;
        this.dogfrequency=df;
        this.dogcharacter=dc;
        this.doghost=dh;
        this.lat=la;
        this.lon=lo;
        this.sprayed=sp;
        this.about=ab;
        this.vak=vk;
        this.dogdistance=dd;
    }

    public String getUsername()
    {
        return this.username;
    }
    public String getMobile()
    {
        return this.mobile;
    }
    public String getDogname()
    {
        return this.dogname;
    }
    public String getDogbreed()
    {
        return this.dogbreed;
    }
    public String getDogfrequency()
    {
        return this.dogfrequency;
    }
    public String getDoghost()
    {
        return this.doghost;
    }
    public String getDogage()
    {
        return this.dogage;
    }
    public String getDogsex()
    {
        return this.dogsex;
    }
    public double getLat()
    {
        return lat;
    }
    public double getLon()
    {
        return lon;
    }
    public String getSprayed()
    {
        return sprayed;
    }
    public String getAbout()
    {
        return about;
    }
    public ArrayList<String> getDogcharacter()
    {
        return dogcharacter;
    }
    public String getVak()
    {
        return vak;
    }
    public String getDogdistance()
    {
        return this.dogdistance;
    }
}
