package com.gome.testclover.spring;

import com.gome.clover.client.job.RemoteJob;
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

@Component
@CloverJobAnnotation(jobGroup = "RemoteJob2WithSpringAnnotationGroup",jobName = "RemoteJob2WithSpringAnnotationName",
jobType = ClientJob.JobType.REMOTE,executeType = ClientJob.ExecuteType.ADD,
		jobStrategy =ClientJob.JobStrategy.SYSTEM_CAPACITY,cronExpression="0/30 * * * * ?")
public class RemoteJob2WithSpringAnnotation extends RemoteJob {

	@Override
	public void executeJob(JobExecutionContext arg0) {
		System.err.println("RemoteJob2WithSpringAnnotation--->>>executeJob(JobExecutionContext) @"+ DateUtils.currentDateTime());
		try {
			String filePath = File.separatorChar + "home" + File.separatorChar + "wangyue-ds6";
			File outFile = new File(filePath + File.separatorChar + "RemoteJob2WithSpringAnnotation" + File.separatorChar + DateUtils.format(new Date(), "yyyy-MM-dd"));
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
