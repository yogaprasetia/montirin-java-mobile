package com.montirin.app.model;

public class UsersItem {
    String name,phone,jasa,desc;
    Double latitude,longitude;

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    UsersItem(){

    }

    public UsersItem(String name, String phone, String jasa, String desc, Double latitude, Double longitude) {
        this.name = name;
        this.phone = phone;
        this.jasa = jasa;
        this.desc = desc;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getJasa() {
        return jasa;
    }

    public void setJasa(String jasa) {
        this.jasa = jasa;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
