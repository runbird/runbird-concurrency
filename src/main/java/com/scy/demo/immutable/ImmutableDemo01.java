package com.scy.demo.immutable;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//使用final避免子类继承重写
public class ImmutableDemo01 {

    private Map<String, Location> locationMap = new ConcurrentHashMap<>();

    public void updateLocation(String carCode, Location newLocation) {
        locationMap.put(carCode, newLocation);
    }

    public Location getLocation(String carCode) {
        return locationMap.get(carCode);
    }
}

final class Location {

    // 使用final修饰，避免外部方法重写
    private final double x;
    private final double y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    //@Override
    //public double getX() {
    //     return super.getX()+1;
    //}

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }


//    public void set(double x, double y) {
//        this.x = x;
//        this.y = y;
//    }
}
