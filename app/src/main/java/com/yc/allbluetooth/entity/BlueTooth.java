package com.yc.allbluetooth.entity;

/**
 * Date:2023/2/10 15:52
 * author:jingyu zheng
 */
/**
 * Date:2022/11/3 14:18
 * author:jingyu zheng
 */
public class BlueTooth {
    private int id;
    private String name;
    private String address;
    private String type;

    public BlueTooth() {
    }
    public BlueTooth(int id, String name, String address, String type) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "BlueTooth{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}