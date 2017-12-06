package com.acmed.his.util;

/**
 * 计算经纬度距离
 *
 * Created by Darren on 2017-12-06
 **/
public class LngLatUtil {


//    public static final double EARTH_RADIUS = 6371.393;//km 地球半径 平均值，千米
        public static final double EARTH_RADIUS = 6378.137;
    /**
     *
     * @param lng1  经度1
     * @param lat1  纬度1
     * @param lng2  经度2
     * @param lat2  纬度2
     *
     * @return 距离（公里、千米）
     */
    public static double Distance(double lng1,double lat1, double lng2,double lat2) {
        //用haversine公式计算球面两点间的距离。
        //经纬度转换成弧度
        lat1 = ConvertDegreesToRadians(lat1);
        lng1 = ConvertDegreesToRadians(lng1);
        lat2 = ConvertDegreesToRadians(lat2);
        lng2 = ConvertDegreesToRadians(lng2);

        //差值
        double vLon = Math.abs(lng1 - lng2);
        double vLat = Math.abs(lat1 - lat2);

        //h is the great circle distance in radians, great circle就是一个球体上的切面，它的圆心即是球心的一个周长最大的圆。
        double h = HaverSin(vLat) + Math.cos(lat1) * Math.cos(lat2) * HaverSin(vLon);
        double distance = 2 * EARTH_RADIUS * Math.asin(Math.sqrt(h));
        return distance;
    }

    public static double HaverSin(double theta) {
        double v = Math.sin(theta / 2);
        return v * v;
    }

    /**
     * 将角度换成弧度
     * @param degrees
     * @return
     */
    public static double ConvertDegreesToRadians(double degrees) {
        return degrees * Math.PI / 180;
    }

    public static void main(String[] args) {
        System.err.println(Distance(116.32793,39.94607, 121.42575, 31.24063));
        System.err.println(Distance(110,39, 110.15, 39 ));
        System.err.println(Distance(110,39, 110, 39.15 ));

    }

}
