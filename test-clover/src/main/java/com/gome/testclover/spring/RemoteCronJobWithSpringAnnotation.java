package com.gome.testclover.spring;

import com.gome.clover.common.annotation.CloverJobAnnotation;
import com.gome.clover.common.tools.DateUtils;
import com.gome.clover.core.job.ClientJob;
import org.quartz.JobExecutionContext;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

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
 * Module Desc:Log4j ConfigListener
 * User: wangyue-ds6 || stark_summer@qq.com
 * Date: 2014/11/25
 * Time: 10:55
 */
@Component
@CloverJobAnnotation(jobGroup = "RemoteCronJobWithSpringAnnotationGroup", jobName = "RemoteCronJobWithSpringAnnotationName",
        jobType = ClientJob.JobType.REMOTE, executeType = ClientJob.ExecuteType.ADD,jobStrategy =ClientJob.JobStrategy.SYSTEM_CAPACITY
        ,cronExpression = "0/50 * * * * ?")
public class RemoteCronJobWithSpringAnnotation extends com.gome.clover.client.job.RemoteJob {
    @Override
    public void executeJob(JobExecutionContext arg0) {
        System.err.println("RemoteJobWithSpringAnnotation--->>>executeJob(JobExecutionContext) @" + DateUtils.currentDateTime());
        try {
            String filePath = File.separatorChar + "home" + File.separatorChar + "wangyue-ds6";
            File outFile = new File(filePath + File.separatorChar + "RemoteCronJobWithSpringAnnotation" + File.separatorChar + DateUtils.format(new Date(), "yyyy-MM-dd"));
            if (!outFile.getParentFile().exists()) {
                outFile.getParentFile().mkdirs();
            }
            FileWriter fw = new FileWriter(outFile, true);
            BufferedWriter bw = new BufferedWriter(fw);
            StringBuffer sb = new StringBuffer();
            sb.append(" \n call executeJob(JobExecutionContext) method").append(" Time:" + DateUtils.currentDateTime());
            bw.write(sb.toString());
            bw.flush();
            fw.close();
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
