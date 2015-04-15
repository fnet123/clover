package com.gome.clover.common.rocketmq;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import com.alibaba.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import com.alibaba.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.common.tools.IpUtil;
import com.gome.clover.common.tools.ProgressUtil;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.module.ModuleSchedulerClient;
import com.gome.clover.core.module.ModuleSchedulerServer;
import org.apache.commons.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.List;
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
 * Module Desc:Consumer Util
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/21
 * Time: 14:27
 */

public class ConsumerUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConsumerUtil.class);
/**
    public static void startup(String namesrvAddr, String topic, String subExpression) throws MQClientException {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("consumer-clover-group");
        consumer.setInstanceName("ConsumerInstance_" + ProgressUtil.getPid());
        consumer.setNamesrvAddr(namesrvAddr);
        consumer.subscribe(topic, subExpression);
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public
            consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                System.out.println("consumeMessage");
                MessageExt msg = msgs.get(0);
                if (null == msg) return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                if (CommonConstants.MODULE_TYPE_SERVER.equals(msg.getKeys())) {
                    if(msg.getTags() != null && msg.getTags().equals(IpUtil.getLocalIP().replace(".", "-"))){
                        System.err.println("MODULE_TYPE_SERVER--------------");
                        ClientJob clientJob = null;
                        try {
                            clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64(new String(msg.getBody(), "ISO-8859-1")));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        System.err.println("clientJob"+clientJob.getTrigger());
                        ModuleSchedulerServer server = ModuleSchedulerServer.getInstance();
                        //server.handlerJob(clientJob);
                    }

                } else if (CommonConstants.MODULE_TYPE_CLIENT.equals(msg.getKeys())) {
                    if (msg.getTags() != null && msg.getTags().equals(IpUtil.getLocalIP().replace(".", "-"))) {
                        System.err.println("MODULE_TYPE_CLIENT--------------");
                        ClientJob clientJob = null;
                        try {
                            clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64(new String(msg.getBody(), "ISO-8859-1")));
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        System.err.println("clientJob" + clientJob.getTrigger());
                        ModuleSchedulerClient client = ModuleSchedulerClient.getInstance();
                        client.handlerJob(clientJob);
                    }
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });

        consumer.start();
        System.out.println("Consumer Started.");
    }

    public static void main(String args[]) {
        try {
            ConsumerUtil.startup("10.58.50.204:9876;10.58.50.205:9876", CommonConstants.TOPIC_CLOVER_SERVER, IpUtil.getLocalIP().replace(".", "-"));
        } catch (Exception e) {

        }

    }
**/
}
