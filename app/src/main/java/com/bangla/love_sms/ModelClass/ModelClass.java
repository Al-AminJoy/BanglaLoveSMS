package com.bangla.love_sms.ModelClass;

public class ModelClass {
    private String id, sms, catagory, favourite;

    public ModelClass(String id, String sms, String catagory, String favourite) {
        this.id = id;
        this.sms = sms;
        this.catagory = catagory;
        this.favourite = favourite;
    }

    public String getId() {
        return id;
    }

    public String getSms() {
        return sms;
    }

    public String getCatagory() {
        return catagory;
    }

    public String getFavourite() {
        return favourite;
    }

}
