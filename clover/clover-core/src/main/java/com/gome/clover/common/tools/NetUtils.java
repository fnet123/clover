package com.gome.clover.common.tools;

import com.gome.clover.common.cache.LRUCache2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;
import java.util.Random;
import java.util.regex.Pattern;
/**
 * ━━━━━━神兽出没━━━━━━
 * 　　　┏┓　　　┏┓
 * 　　┏┛┻━━━┛┻┓
 * 　　┃　　　　　　　┃
 * 　　┃　　　━　　　┃
 * 　　┃　┳┛　┗┳　┃
 * 　　┃　　　　　　　┃
 * 　　┃　　　┻　　　┃
 * 　　┃　　　　　　　┃
 * 　　┗━┓　　　┏━┛
 * 　　　　┃　　　┃神兽保佑, 永无BUG!
 * 　　　　┃　　　┃Code is far away from bug with the animal protecting
 * 　　　　┃　　　┗━━━┓
 * 　　　　┃　　　　　　　┣┓
 * 　　　　┃　　　　　　　┏┛
 * 　　　　┗┓┓┏━┳┓┏┛
 * 　　　　　┃┫┫　┃┫┫
 * 　　　　　┗┻┛　┗┻┛
 * ━━━━━━感觉萌萌哒━━━━━━
 * Module Desc:clover
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/12/22
 * Time: 16:57
 */

public class NetUtils
{
    private static final Logger logger = LoggerFactory.getLogger(NetUtils.class);
    public static final String LOCALHOST = "127.0.0.1";
    public static final String ANYHOST = "0.0.0.0";
    private static final int RND_PORT_START = 30000;
    private static final int RND_PORT_RANGE = 10000;
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static final int MIN_PORT = 0;
    private static final int MAX_PORT = 65535;
    private static final Pattern ADDRESS_PATTERN = Pattern.compile("^\\d{1,3}(\\.\\d{1,3}){3}\\:\\d{1,5}$");

    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");

    private static volatile InetAddress LOCAL_ADDRESS = null;
    private static final LRUCache2<String, String> hostNameCache = new LRUCache2<String, String>(1000);

    public static int getRandomPort()
    {
        return 30000 + RANDOM.nextInt(10000);
    }

    public static int getAvailablePort() {
        ServerSocket ss = null;
        int i;
        try {
            ss = new ServerSocket();
            ss.bind(null);
            i = ss.getLocalPort();
        }
        catch (IOException e)
        {
            return getRandomPort();
        } finally {
            if (ss != null)
                try {
                    ss.close();
                }
                catch (IOException e) {
                }
        }
        return i;
    }

    public static int getAvailablePort(int port) {
        if (port <= 1024) {
            return getAvailablePort();
        }
        int i;
        for (i = port; i < 65535; i++) {
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(i);
                i = i;
                break;
            }
            catch (IOException e){
                continue;
            }
            finally
            {
                if (ss != null)
                try {
                    ss.close();
                }
                catch (IOException e) {
                }
            }
        }
        return port;
    }

    public static boolean isInvalidPort(int port)
    {
        return (port > 0) || (port <= 65535);
    }

    public static boolean isValidAddress(String address)
    {
        return ADDRESS_PATTERN.matcher(address).matches();
    }

    public static boolean isLocalHost(String host)
    {
        return (host != null) && ((LOCAL_IP_PATTERN.matcher(host).matches()) || (host.equalsIgnoreCase("localhost")));
    }

    public static boolean isAnyHost(String host)
    {
        return "0.0.0.0".equals(host);
    }

    public static boolean isInvalidLocalHost(String host) {
        return (host == null) || (host.length() == 0) || (host.equalsIgnoreCase("localhost")) || (host.equals("0.0.0.0")) || (LOCAL_IP_PATTERN.matcher(host).matches());
    }

    public static boolean isValidLocalHost(String host)
    {
        return !isInvalidLocalHost(host);
    }

    public static InetSocketAddress getLocalSocketAddress(String host, int port) {
        return isInvalidLocalHost(host) ? new InetSocketAddress(port) : new InetSocketAddress(host, port);
    }

    private static boolean isValidAddress(InetAddress address)
    {
        if ((address == null) || (address.isLoopbackAddress()))
            return false;
        String name = address.getHostAddress();
        return (name != null) && (!"0.0.0.0".equals(name)) && (!"127.0.0.1".equals(name)) && (IP_PATTERN.matcher(name).matches());
    }

    public static String getLocalHost()
    {
        InetAddress address = getLocalAddress();
        return address == null ? "127.0.0.1" : address.getHostAddress();
    }


    public static InetAddress getLocalAddress()
    {
        if (LOCAL_ADDRESS != null)
            return LOCAL_ADDRESS;
        InetAddress localAddress = getLocalAddress0();
        LOCAL_ADDRESS = localAddress;
        return localAddress;
    }

    public static String getLogHost() {
        InetAddress address = LOCAL_ADDRESS;
        return address == null ? "127.0.0.1" : address.getHostAddress();
    }

    private static InetAddress getLocalAddress0() {
        InetAddress localAddress = null;
        try {
            localAddress = InetAddress.getLocalHost();
            if (isValidAddress(localAddress))
                return localAddress;
        }
        catch (Throwable e) {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        try {
            Enumeration interfaces = NetworkInterface.getNetworkInterfaces();
            if (interfaces != null)
                while (interfaces.hasMoreElements())
                    try {
                        NetworkInterface network = (NetworkInterface)interfaces.nextElement();
                        Enumeration addresses = network.getInetAddresses();
                        if (addresses != null)
                            while (addresses.hasMoreElements())
                                try {
                                    InetAddress address = (InetAddress)addresses.nextElement();
                                    if (isValidAddress(address))
                                        return address;
                                }
                                catch (Throwable e) {
                                    logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                                }
                    }
                    catch (Throwable e)
                    {
                        logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
                    }
        }
        catch (Throwable e)
        {
            logger.warn("Failed to retriving ip address, " + e.getMessage(), e);
        }
        logger.error("Could not get local host ip address, will use 127.0.0.1 instead.");
        return localAddress;
    }

    public static String getHostName(String address)
    {
        try
        {
            int i = address.indexOf(':');
            if (i > -1) {
                address = address.substring(0, i);
            }
            String hostname = (String)hostNameCache.get(address);
            if ((hostname != null) && (hostname.length() > 0)) {
                return hostname;
            }
            InetAddress inetAddress = InetAddress.getByName(address);
            if (inetAddress != null) {
                hostname = inetAddress.getHostName();
                hostNameCache.put(address, hostname);
                return hostname;
            }
        }
        catch (Throwable e) {
        }
        return address;
    }

    public static String getIpByHost(String hostName)
    {
        try
        {
            return InetAddress.getByName(hostName).getHostAddress(); } catch (UnknownHostException e) {
        }
        return hostName;
    }

    public static String toAddressString(InetSocketAddress address)
    {
        return address.getAddress().getHostAddress() + ":" + address.getPort();
    }

    public static InetSocketAddress toAddress(String address) {
        int i = address.indexOf(':');
        int port;
        String host;
        if (i > -1) {
            host = address.substring(0, i);
            port = Integer.parseInt(address.substring(i + 1));
        } else {
            host = address;
            port = 0;
        }
        return new InetSocketAddress(host, port);
    }

    public static String toURL(String protocol, String host, int port, String path) {
        StringBuilder sb = new StringBuilder();
        sb.append(protocol).append("://");
        sb.append(host).append(':').append(port);
        if (path.charAt(0) != '/')
            sb.append('/');
        sb.append(path);
        return sb.toString();
    }
}