package com.mrefive.freebay;

/**
 * Created by mrefive on 1/7/16.
 */
public class OwnOffers {

    private String name, descr;

    public OwnOffers(String name, String descr) {
        this.setName(name);
        this.setDescr(descr);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }
}
