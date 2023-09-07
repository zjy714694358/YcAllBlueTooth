package com.yc.allbluetooth.std.entity;

/**
 * Date:2022/11/15 10:37
 * author:jingyu zheng
 */
public class TestSet {
    private String dl;
    private String xw;
    private String dz;
    private String zc;

    public TestSet() {
    }
    public TestSet(String dl, String xw, String dz, String zc) {
        this.dl = dl;
        this.xw = xw;
        this.dz = dz;
        this.zc = zc;
    }

    public String getDl() {
        return dl;
    }

    public void setDl(String dl) {
        this.dl = dl;
    }

    public String getXw() {
        return xw;
    }

    public void setXw(String xw) {
        this.xw = xw;
    }

    public String getDz() {
        return dz;
    }

    public void setDz(String dz) {
        this.dz = dz;
    }

    public String getZc() {
        return zc;
    }

    public void setZc(String zc) {
        this.zc = zc;
    }
}
