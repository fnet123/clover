package com.gome.testclover.spring;

import com.gome.clover.client.job.RemoteJob;
import com.gome.clover.common.tools.DateUtils;
import org.quartz.JobExecutionContext;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class RemoteJob1WithSpring extends RemoteJob {

	@Override
	public void executeJob(JobExecutionContext arg0) {
		System.err.println("RemoteJob1WithSpring--->>>executeJob(JobExecutionContext) @"+ DateUtils.currentDateTime());
		try {
			String filePath = File.separatorChar + "home" + File.separatorChar + "wangyue-ds6";
			File outFile = new File(filePath + File.separatorChar + "RemoteJob1WithSpring" + File.separatorChar + DateUtils.format(new Date(), "yyyy-MM-dd"));
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
