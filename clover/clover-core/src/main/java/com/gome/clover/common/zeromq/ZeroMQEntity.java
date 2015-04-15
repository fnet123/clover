package com.gome.clover.common.zeromq;

import com.gome.clover.common.netty.client.SubReqClientHandel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
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
 * Module Desc:Zero Zero MQ Entity
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */


public class ZeroMQEntity  implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(ZeroMQEntity.class);
    /**
     * 执行任务类型(server|client)
     */
    private String destServer;

    /**
     *  执行机器IP地址
     */
    private String destIp;

    /**
     * Job 信息 Base64 encode字符串
     */
    private String jobInfo;

    /**
     *  msg
     */
    private String msg;

    public ZeroMQEntity() {}

    public ZeroMQEntity(String destServer, String destIp, String jobInfo) {
        this.destServer = destServer;
        this.destIp = destIp;
        this.jobInfo = jobInfo;
        logger.error("ZeroMQEntity.length()"+jobInfo.length());
    }
    public ZeroMQEntity(String destServer, String destIp, String jobInfo, String msg) {
        this.destServer = destServer;
        this.destIp = destIp;
        this.jobInfo = jobInfo;
        this.msg = msg;
    }

    public String getDestServer() {
        return destServer;
    }

    public void setDestServer(String destServer) {
        this.destServer = destServer;
    }

    public String getJobInfo() {
        return jobInfo;
    }

    public void setJobInfo(String jobInfo) {
        this.jobInfo = jobInfo;
    }

    public String getDestIp() {
        return destIp;
    }

    public void setDestIp(String destIp) {
        this.destIp = destIp;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
