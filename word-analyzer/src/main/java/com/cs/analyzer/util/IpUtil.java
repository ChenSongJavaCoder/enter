package com.cs.analyzer.util;

import com.maxmind.db.CHMCache;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import org.springframework.util.Assert;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Objects;

/**
 * @Author: CS
 * @Date: 2020/7/1 3:09 下午
 * @Description:
 */
public class IpUtil {

    /**
     * 测试阶段-使用城市精度，文件有些大
     * 可以按照业务需求选择不同精度的数据库文件
     */
    private static DatabaseReader reader;

    /**
     * 初始化加载数据库文件-城市精度
     */
    static {
        String path = IpUtil.class.getResource("/GeoLite2-City.mmdb").getPath();
        File database = new File(path);
        // This creates the DatabaseReader object. To improve performance, reuse
        // the object across lookups. The object is thread-safe.
        try {
            reader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据IP地址获取所在经纬度，使用","分隔
     *
     * @param ip IP地址
     * @return
     */
    public static String getPositionByIp(String ip) {
        CityResponse city = getCityDetailByIp(ip);
        if (Objects.nonNull(city)) {
            Location location = city.getLocation();
            return location.getLongitude() + "," + location.getLatitude();
        }
        return null;
    }

    /**
     * 获取ip所在城市的详细信息
     *
     * @param ip
     * @return
     */
    private static CityResponse getCityDetailByIp(String ip) {
        Assert.hasText(ip, "ip is required!");
        InetAddress ipAddress = null;
        try {
            ipAddress = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        // Replace "city" with the appropriate method for your database, e.g.,
        // "country".
        CityResponse response = null;
        try {
            response = reader.city(ipAddress);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeoIp2Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    public static void main(String[] args) {
        String testIp = "192.168.153.158";

        try {
            String position = getPositionByIp(testIp);
            System.out.println(position);
            System.out.println(getCityDetailByIp(testIp).toJson());


//            testAsn(testIp);
//            testCity(testIp);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void testAsn(String testIp) throws Exception {
        //GeoIP2数据库文件，把数据库文件直接放d盘下(可以随意喜欢的位置)
        String path = IpUtil.class.getResource("/GeoLite2-ASN.mmdb").getPath();
        File database = new File(path);

        // 创建 DatabaseReader对象
        DatabaseReader reader = new DatabaseReader.Builder(database).build();

        InetAddress ipAddress = InetAddress.getByName(testIp);

        AsnResponse response = reader.asn(ipAddress);

        System.out.println(response.getAutonomousSystemOrganization());
        System.out.println(response.getAutonomousSystemNumber());

    }

    private static void testCity(String testIp) throws Exception {
        // A File object pointing to your GeoIP2 or GeoLite2 database

        String path = IpUtil.class.getResource("/GeoLite2-City.mmdb").getPath();
        File database = new File(path);

        // This creates the DatabaseReader object. To improve performance, reuse
        // the object across lookups. The object is thread-safe.
        DatabaseReader reader = new DatabaseReader.Builder(database).withCache(new CHMCache()).build();
        InetAddress ipAddress = InetAddress.getByName(testIp);

        // Replace "city" with the appropriate method for your database, e.g.,
        // "country".
        CityResponse response = reader.city(ipAddress);

        Country country = response.getCountry();
        System.out.println(country.getIsoCode());
        // 'US'
        System.out.println(country.getName());
        // 'United States'
        System.out.println(country.getNames().get("zh-CN"));
        // '美国'

        Subdivision subdivision = response.getMostSpecificSubdivision();
        System.out.println(subdivision.getName());
        // 'Minnesota'
        System.out.println(subdivision.getIsoCode());
        // 'MN'

        City city = response.getCity();
        System.out.println(city.getName());
        // 'Minneapolis'

        Postal postal = response.getPostal();
        System.out.println(postal.getCode());
        // '55455'

        Location location = response.getLocation();
        System.out.println(location.getLatitude());
        // 44.9733
        System.out.println(location.getLongitude());
        // -93.2323
    }


}
