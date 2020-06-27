package com.ntust.mycontact;

class Location {
    /**
     * Created by anildeshpande on 4/3/17.
     */

    private long id;
    private String longitude;
    private String latitude;
    private String name;

    public Location(){
        super();
    }

    public Location(long id, String name, String longitude,String latitude){
        this.id=id;
        this.name = name;
        this.longitude=longitude;
        this.latitude=latitude;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLocation() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
