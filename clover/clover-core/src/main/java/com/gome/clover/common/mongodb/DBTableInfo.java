package com.gome.clover.common.mongodb;

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
 * Module Desc:DB Table Info
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class DBTableInfo {

    /*表中公用列字段 开始*/

    /**
     * 表 clover_job
     */
    public  static  final String TBL_CLOVER_JOB = "clover_job";

    /**
     * 表 clover_job_temp
     */
    public  static  final String TBL_CLOVER_JOB_TEMP = "clover_job_temp";

    /**
     * 表 clover_contact
     */
    public  static  final String TBL_CLOVER_CONTACT = "clover_contact";

    /**
     * 表 clover_log
     */
    public  static  final String TBL_CLOVER_LOG = "clover_log";

    /**
     * 表 clover_config
     */
    public  static  final String TBL_CLOVER_CONFIG = "clover_config";

    /**
     * DB 列  ID
     */
    public  static  final String COL_ID= "_id";

    /**
     * DB 列  jobKey
     */
    public  static  final String COL_JOB_KEY = "jobKey";


    /**
     * DB 列  client ip
     */
    public  static  final String COL_CLIENT_IP= "clientIp";
    /**
     * DB 列  server ip
     */
    public  static  final String COL_SERVER_IP= "serverIp";

    /**
     * DB 列  ts
     */
    public  static  final String COL_TS = "ts";

    /*表中公用列字段 结束*/


    /*job表列字段 开始*/

    /**
     * DB 列  jobType
     */
    public  static  final String COL_JOB_TYPE = "jobType";

    /**
     * DB 列  executeType
     */
    public  static  final String COL_EXECUTE_TYPE = "executeType";

    /**
     * DB 列  jobStrategy
     */
    public  static  final String COL_JOB_STRATEGY = "jobStrategy";

    /**
     * DB 列  job 执行时间
     */
    public  static  final String COL_START_TIME = "startTime";

    /**
     * DB 列  job cron执行表达式
     */
    public  static  final String COL_CRON_EXPRESSION = "cronExpression";

    /**
     * DB 列  jobInfo
     */
    public  static  final String COL_JOB_INFO = "jobInfo";

    /**
     * DB 列 fixedClientIps
     */
    public  static  final String COL_FIXED_CLIENT_IPS = "fixedClientIps";
    /**
     * DB 列 fixedServerIps
     */
    public  static  final String COL_FIXED_SERVER_IPS = "fixedServerIps";

    /**
     * DB 列  execute start time
     */
    public  static  final String COL_EXECUTE_START_TIME = "executeStartTime";

    /**
     * DB 列 execute end time
     */
    public  static  final String COL_EXECUTE_END_TIME = "executeEndTime";
    /**
     * DB 列 times
     */
    public  static  final String COL_TIMES = "times";

    /**
     * DB 列 failTimes
     */
    public  static  final String COL_FAIL_TIMES = "failTimes";

    /**
     * DB 列  status
     */
    public  static  final String COL_STATUS = "status";

    /*job表列字段 结束*/

     /*contact表列字段 开始*/

    /**
     * DB 列  contacter
     */
    public  static  final String COL_CONTACTER = "contacter";

    /**
     * DB 列  email
     */
    public  static  final String COL_EMAIL = "email";

    /**
     * DB 列  mobile
     */
    public  static  final String COL_MOBILE = "mobile";

    /*contact表列字段 结束*/

    /*log表列字段 开始*/

    /**
     * DB 列  systemId
     */
    public  static  final String COL_SYSTEM_ID = "systemId";

    /**
     * DB 列  ip
     */
    public  static  final String COL_IP = "ip";

    /**
     * DB 列  execMethod
     */
    public  static  final String COL_EXEC_METHOD = "execMethod";

    /**
     * DB 列  execResult
     */
    public  static  final String COL_EXEC_RESULT = "execResult";

    /**
     * DB 列  execTime
     */
    public  static  final String COL_EXEC_TIME = "execTime";

    /*log表列字段 结束*/

     /*config表列字段 开始*/
    /**
     * DB 列  key
     */
    public  static  final String COL_CONFIG_KEY = "key";

    /**
     * DB 列  value
     */
    public  static  final String COL_CONFIG_VALUE = "value";

    /**
     * DB 列  type
     */
    public  static  final String COL_CONFIG_TYPE = "type";
     /*config表列字段 结束*/


}
