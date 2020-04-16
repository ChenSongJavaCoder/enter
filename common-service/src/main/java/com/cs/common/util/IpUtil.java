package com.cs.common.util;

import java.net.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;

/**
 * @Author: CS
 * @Date: 2020/4/16 4:56 下午
 * @Description:
 */
public class IpUtil {

    public static String getIPV4Address() {
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            ArrayList<String> ipv4Result = new ArrayList();
            ArrayList ipv6Result = new ArrayList();

            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = (NetworkInterface) enumeration.nextElement();
                Enumeration en = networkInterface.getInetAddresses();

                while (en.hasMoreElements()) {
                    InetAddress address = (InetAddress) en.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(normalizeHostAddress(address));
                        } else {
                            ipv4Result.add(normalizeHostAddress(address));
                        }
                    }
                }
            }

            if (!ipv4Result.isEmpty()) {
                Iterator var8 = ipv4Result.iterator();

                String ip;
                do {
                    if (!var8.hasNext()) {
                        return (String) ipv4Result.get(0);
                    }

                    ip = (String) var8.next();
                } while (ip.startsWith("127.0") || ip.startsWith("192.168"));

                return ip;
            } else if (!ipv6Result.isEmpty()) {
                return (String) ipv6Result.get(0);
            } else {
                InetAddress localHost = InetAddress.getLocalHost();
                return normalizeHostAddress(localHost);
            }
        } catch (UnknownHostException | SocketException var6) {
            var6.printStackTrace();
            return null;
        }
    }

    private static String normalizeHostAddress(InetAddress localHost) {
        return localHost instanceof Inet6Address ? "[" + localHost.getHostAddress() + "]" : localHost.getHostAddress();
    }

    public static void main(String[] args) {
        System.out.println(getIPV4Address());
    }
}
