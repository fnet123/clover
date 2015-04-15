package com.gome.clover.common.tools;

import com.gome.clover.common.file.ConfigFile;

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
 * Module Desc:公共常量类
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class CommonConstants {
    /**
     * MODULE_TYPE Server
     */
    public  static  final String MODULE_TYPE_SERVER = "server";
    /**
     * MODULE_TYPE Client
     */
    public  static  final String MODULE_TYPE_CLIENT = "client";
    /**
     * MODULE_TYPE Server With Admin
     */
    public  static  final String MODULE_TYPE_SERVER_WITH_ADMIN = "serverWithAdmin";

    /**
     * MODULE_TYPE Client With Admin
     */
    public  static  final String MODULE_TYPE_CLIENT_WITH_ADMIN = "clientWithAdmin";

    /**
     * MODULE_TYPE Server With Monitor
     */
    public  static  final String MODULE_TYPE_SERVER_WITH_MONITOR = "serverWithMonitor";

    /**
     * zero mq 服务端 启动消费者 端口
     */
    public  static  final String ZMQ_SERVER_PORT;

    /**
     * NETTY 服务端 启动消费者 端口
     */
    public  static  final int NETTY_SERVER_PORT;

    /**
     * job type local:本地job
     */
    public  static  final String JOB_TYPE_LOCAL = "LOCAL";
    /**
     * job type remote:分布式job
     */
    public  static  final String JOB_TYPE_REMOTE = "REMOTE";

    /**
     * job status 1:初始化状态
     */
    public  static  final String JOB_STATUS_1 = "1";
    /**
     * job status 2:运行中状态
     */
    public  static  final String JOB_STATUS_2 = "2";

    /**
     * job status 3:结束状态
     */
    public  static  final String JOB_STATUS_3 = "3";

    public  static final String ALIVE = "alive";

    public  static final String ALIVE_STATUS_0 = "0";//死亡
    public  static final String ALIVE_STATUS_1 = "1";//存活


    public static final long SERVER_DIFFER_MILLI_SECONDS; //服务端HeartBeat间隔时间2分钟

    public static  final long CLIENT_DIFFER_MILLI_SECONDS; //客户端HeartBeat间隔时间2分钟

    public static  final long MONITOR_DIFFER_MILLI_SECONDS; //monitor HeartBeat间隔时间2分钟

    public  static  final String TOPIC_CLOVER_SERVER = "TOPIC_CLOVER_SERVER";
    public  static  final String TOPIC_CLOVER_CLIENT = "TOPIC_CLOVER_CLIENT";

    public  static  final int port = 8888;//netty port

    public  static  final String DEFAULT_COMPANY_EMAIL;
    public  static  final String DEFAULT_PRIVATE_EMAIL;
    public  static  final String SYSTEM_ID_CLOVER = "clover";

    public static final  String REMOTE_JOB_GROUP = "remote-jobs";
    public static final  String SPLIT_CHARACTER_FALG = "_#_";
    public static final String SERVER_JOB_INFO = "serverJobInfo";
    public static final String CLIENT_JOB_INFO = "clientJobInfo";
    public static final String CLIENT_JOB_PATH = "clientJobPath";

    public static  String ZK_CONNECT_STRING =null;
    public static  String ZK_ROOT_PATH = null;
    public static  String ZK_USER_NAME = null;
    public static  String ZK_PASSWORD = null;
    public static int ZOO_KEEPER_TIMEOUT;

    public static long ZMQ_SLEEP_CLIENT_MILLIS; //zeromq消费者端 等待一分钟后启动
    public static long ZMQ_SLEEP_SERVER_MILLIS ;//zeromq生产者端 等待一分钟后启动

    public static String token;

    public static String DISABLED_DB; //是否启动DB
    public static String TYPE_DB = "typeDB"; //DB类型

    public static int POOL_SIZE;

    public static int MAX_FAIL_TIMES; //max fail times
    public static double MAX_MEM_RATIO; //max mem ratio
    public static double MAX_CPU_RATIO; //max cpu ratio
    public static String SERVER_JOB_STRATEGY; //server job strategy

    public static String ID = "id";
    public static String JOB_CLASS = "jobClass";
    public static String PORT = "port";
    public static String IP = "ip";
    public static String TS = "ts";
    public static String MEM_RATIO = "memRatio";
    public static String CPU_RATIO = "cpuRatio";
    public static String TOTAL_THREAD = "totalThread";

    public static String SUCCESS = "success";
    public static String ERROR_CODE = "errorCode";

    public static String ERROR_CODE_101 = "101"; //serverInfo |clientInfo is null
    public static String ERROR_CODE_102 = "102"; //over max mem ratio
    public static String ERROR_CODE_103 = "103"; // other error

    static {
        ZMQ_SERVER_PORT = ConfigFile.commonConfig().getItem("zmqServerPort","1688");
        NETTY_SERVER_PORT = ConfigFile.commonConfig().getIntItem("nettyServerPort","8087");
        SERVER_DIFFER_MILLI_SECONDS = ConfigFile.commonConfig().getLongItem("serverDifferMilliSeconds", "2*60*1000");
        CLIENT_DIFFER_MILLI_SECONDS = ConfigFile.commonConfig().getLongItem("clientDifferMilliSeconds","2*60*1000");
        MONITOR_DIFFER_MILLI_SECONDS = ConfigFile.commonConfig().getLongItem("monitorDifferMilliSeconds","2*60*1000");
        DEFAULT_COMPANY_EMAIL = ConfigFile.commonConfig().getItem("defaultCompanyEmail", "xiaoxiangxu@yolo24.com");
        DEFAULT_PRIVATE_EMAIL = ConfigFile.commonConfig().getItem("defaultPrivateEmail", "zhutouzan@163.com");
        ZMQ_SLEEP_CLIENT_MILLIS = ConfigFile.commonConfig().getLongItem("zmqSleepClientMillis", "1000");
        ZMQ_SLEEP_SERVER_MILLIS = ConfigFile.commonConfig().getLongItem("zmqSleepServerMillis", "1000");
        token = ConfigFile.commonConfig().getItem("token", "6fb8535d703f2492704aefc212b7cd41");
        DISABLED_DB = ConfigFile.commonConfig().getItem("disabledDB", "disabledDB");
        POOL_SIZE = ConfigFile.commonConfig().getIntItem("poolSize", "100");
        MAX_FAIL_TIMES = ConfigFile.commonConfig().getIntItem("maxFailTimes", "5");
        MAX_MEM_RATIO = ConfigFile.commonConfig().getDoubleItem("maxMemRatio", "0.95");
        MAX_CPU_RATIO = ConfigFile.commonConfig().getDoubleItem("maxCpuRatio", "0.95");
        SERVER_JOB_STRATEGY = ConfigFile.commonConfig().getItem("serverJobStrategy", "SYSTEM_CAPACITY");

        ZK_CONNECT_STRING = ConfigFile.zkConfig().getItem("zkConnectString","127.0.0.1:2181");
        ZK_ROOT_PATH = ConfigFile.zkConfig().getItem("rootPath","/clover");
        ZK_USER_NAME = ConfigFile.zkConfig().getItem("userName","cloverAdmin");
        ZK_PASSWORD = ConfigFile.zkConfig().getItem("password","password");
        ZOO_KEEPER_TIMEOUT = ConfigFile.zkConfig().getIntItem("zkSessionTimeout", "60000000");
    }

}
