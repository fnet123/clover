package com.gome.clover.common.rocketmq;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.common.tools.ProgressUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * Module Desc:Producer Util
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */
public class ProducerUtil {
    private static  final Logger logger = LoggerFactory.getLogger(ProducerUtil.class);
    private static DefaultMQProducer producer;
    static {
        producer = new DefaultMQProducer("producer-clover-group");
        producer.setNamesrvAddr("10.58.50.204:9876;10.58.50.205:9876");
        producer.setInstanceName("ProducerInstance_" + ProgressUtil.getPid());
        try {
            producer.start();
        } catch (MQClientException e) {
           if(logger.isDebugEnabled()) e.printStackTrace();
            logger.error("ProducerUtil-->>producer.start() error",e);
        }
    }

    public static SendResult send(Message msg) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        return producer.send(msg);
    }

    public static void shutdown(){
        producer.shutdown();
    }
    public  static  void main(String args[]){
        try{
            Message msg = new Message();
            msg.setTopic(CommonConstants.TOPIC_CLOVER_SERVER);
            String ip  = IpUtil.getLocalIP();
            msg.setTags(IpUtil.getLocalIP().replace(".","-"));
            msg.setKeys(CommonConstants.MODULE_TYPE_CLIENT);
            msg.setBody("sb".getBytes());
            System.err.println("SendResult"+ProducerUtil.send(msg));
            shutdown();
        }catch (Exception e){

        }

    }
}
