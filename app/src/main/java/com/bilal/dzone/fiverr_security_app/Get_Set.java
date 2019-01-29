package com.bilal.dzone.fiverr_security_app;

/**
 * Created by Bilal on 16-May-17.
 */

public class Get_Set {


    private String token;
    private String Lat, Long;

    public Get_Set(String token,  String Lat, String Long) {
        this.token = token;
        this.Lat = Lat;
        this.Long = Long;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        token = token;
    }


    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String l) {
        Long = l;
    }

}
