package com.gome.clover.common.systeminfo;

import com.sun.management.OperatingSystemMXBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.management.ManagementFactory;

import java.io.*;

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
 * Date: 2014/12/25
 * Time: 16:29
 */
public class SystemInfoUtil {
    private final static Logger logger = LoggerFactory.getLogger(SystemInfoUtil.class);
    private static final  OperatingSystemMXBean osmxb = (OperatingSystemMXBean) java.lang.management.ManagementFactory.getOperatingSystemMXBean();
    private static final int KB = 1024;
    private static final int FAULTLENGTH = 10;
    private static final int CPUTIME = 30;
    public static double getMemRatio(){
        long totalMemorySize = osmxb.getTotalPhysicalMemorySize()/KB;// 总的物理内存
        long usedMemory = (osmxb.getTotalPhysicalMemorySize() - osmxb.getFreePhysicalMemorySize()) / KB; // 已使用的物理内存
        return ((usedMemory*1.0/totalMemorySize));
    }

    public static int getTotalThread(){
        // 获得线程总数
        ThreadGroup parentThread;
        for (parentThread = Thread.currentThread().getThreadGroup(); parentThread
                .getParent() != null; parentThread = parentThread.getParent());
        return parentThread.activeCount();
    }

    public static double getCpuRatio(){
        double cpuRatio;
        // 操作系统
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().startsWith("windows")) {
            cpuRatio = getCpuRatioForWindows();
        }
        else {
            cpuRatio = getCpuRateForLinux();
        }
        return cpuRatio;
    }

    //获得cpu使用率(Windows)
    private static double getCpuRatioForWindows() {
        try {
            String procCmd = System.getenv("windir") + "\\system32\\wbem\\wmic.exe process get Caption,CommandLine,KernelModeTime,ReadOperationCount,ThreadCount,UserModeTime,WriteOperationCount";
            // 取进程信息
            long[] c0 = readCpu(Runtime.getRuntime().exec(procCmd));
            Thread.sleep(CPUTIME);
            long[] c1 = readCpu(Runtime.getRuntime().exec(procCmd));
            if (c0 != null && c1 != null) {
                long idleTime = c1[0] - c0[0];
                long busyTime = c1[1] - c0[1];
                return (busyTime)*1.0 / (busyTime + idleTime);
            } else {
                return 0;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }
    }
    //读取cpu相关信息
    private static long[] readCpu(final Process proc) {
        long[] retN = new long[2];
        try {
            proc.getOutputStream().close();
            InputStreamReader ir = new InputStreamReader(proc.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);
            String line = input.readLine();
            if (line == null || line.length() < FAULTLENGTH) {
                return null;
            }
            int capIdx = line.indexOf("Caption");
            int cmdIdx = line.indexOf("CommandLine");
            int rocIdx = line.indexOf("ReadOperationCount");
            int umtIdx = line.indexOf("UserModeTime");
            int kmtIdx = line.indexOf("KernelModeTime");
            int wocIdx = line.indexOf("WriteOperationCount");
            long idleTime = 0;
            long knelTime = 0;
            long userTime = 0;
            while ((line = input.readLine()) != null) {
                if (line.length() < wocIdx) {
                    continue;
                }
                // 字段出现顺序：Caption,CommandLine,KernelModeTime,ReadOperationCount,
                // ThreadCount,UserModeTime,WriteOperation
                String caption =substring(line, capIdx, cmdIdx - 1).trim();
                String cmd = substring(line, cmdIdx, kmtIdx - 1).trim();
                if (cmd.indexOf("wmic.exe") >= 0) {
                    continue;
                }
                String s1 = substring(line, kmtIdx, rocIdx - 1).trim();
                String s2 = substring(line, umtIdx, wocIdx - 1).trim();
                if (caption.equals("System Idle Process") || caption.equals("System")) {
                    if (s1.length() > 0)
                        idleTime += Long.valueOf(s1).longValue();
                    if (s2.length() > 0)
                        idleTime += Long.valueOf(s2).longValue();
                    continue;
                }
                if (s1.length() > 0)
                    knelTime += Long.valueOf(s1).longValue();
                if (s2.length() > 0)
                    userTime += Long.valueOf(s2).longValue();
            }
            retN[0] = idleTime;
            retN[1] = knelTime + userTime;
            return retN;
        } catch (Exception ex) {
            ex.printStackTrace();
            logger.error("SystemInfoUtil-->>readCpu error,"+ ex.getMessage());
        } finally {
            try {
                proc.getInputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("SystemInfoUtil-->>readCpu InputStream close error,"+ e.getMessage());
            }
        }
        return null;
    }

    /**
     * 由于String.subString对汉字处理存在问题（把一个汉字视为一个字节)，因此在 包含汉字的字符串时存在隐患，现调整如下：
     * @param src 要截取的字符串
     * @param start_idx 开始坐标（包括该坐标)
     * @param end_idx 截止坐标（包括该坐标）
     * @return
     */
    private static String substring(String src, int start_idx, int end_idx) {
        byte[] b = src.getBytes();
        String tgt = "";
        for (int i = start_idx; i <= end_idx; i++) {
            tgt += (char) b[i];
        }
        return tgt;
    }
    //获得cpu使用率(Linux)
    private static double getCpuRateForLinux(){
        double cpuUsage = 0;
        Process pro1,pro2;
        Runtime r = Runtime.getRuntime();
        try {
            String command = "cat /proc/stat";
            //第一次采集CPU时间
            long startTime = System.currentTimeMillis();
            pro1 = r.exec(command);
            BufferedReader in1 = new BufferedReader(new InputStreamReader(pro1.getInputStream()));
            String line;
            long idleCpuTime1 = 0, totalCpuTime1 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间
            while((line=in1.readLine()) != null){
                if(line.startsWith("cpu")){
                    line = line.trim();
                    String[] temp = line.split("\\s+");
                    idleCpuTime1 = Long.parseLong(temp[4]);
                    for(String s : temp){
                        if(!s.equals("cpu")){
                            totalCpuTime1 += Long.parseLong(s);
                        }
                    }
                    logger.info("IdleCpuTime: " + idleCpuTime1 + ", " + "TotalCpuTime" + totalCpuTime1);
                    break;
                }
            }
            in1.close();
            pro1.destroy();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                StringWriter sw = new StringWriter();
                e.printStackTrace(new PrintWriter(sw));
                logger.error("CpuUsage休眠时发生InterruptedException. " + e.getMessage());
                logger.error(sw.toString());
            }
            //第二次采集CPU时间
            long endTime = System.currentTimeMillis();
            pro2 = r.exec(command);
            BufferedReader in2 = new BufferedReader(new InputStreamReader(pro2.getInputStream()));
            long idleCpuTime2 = 0, totalCpuTime2 = 0;   //分别为系统启动后空闲的CPU时间和总的CPU时间
            while((line=in2.readLine()) != null){
                if(line.startsWith("cpu")){
                    line = line.trim();
                    String[] temp = line.split("\\s+");
                    idleCpuTime2 = Long.parseLong(temp[4]);
                    for(String s : temp){
                        if(!s.equals("cpu")){
                            totalCpuTime2 += Long.parseLong(s);
                        }
                    }
                    logger.info("IdleCpuTime: " + idleCpuTime2 + ", " + "TotalCpuTime" + totalCpuTime2);
                    break;
                }
            }
            if(idleCpuTime1 != 0 && totalCpuTime1 !=0 && idleCpuTime2 != 0 && totalCpuTime2 !=0){
                cpuUsage = 1 - (double)(idleCpuTime2 - idleCpuTime1)/(double)(totalCpuTime2 - totalCpuTime1);
                logger.info("本节点CPU使用率为: " + cpuUsage);
            }
            in2.close();
            pro2.destroy();
        } catch (IOException e) {
            StringWriter sw = new StringWriter();
            e.printStackTrace(new PrintWriter(sw));
            logger.error("CpuUsage发生InstantiationException. " + e.getMessage());
            logger.error(sw.toString());
        }
        return cpuUsage;
    }
    public static void main(String[] args) {
        double minMemRatioValue = SystemInfoUtil.getMemRatio();
        System.err.println(""+minMemRatioValue);
        int minTotalThreadRatioValue =SystemInfoUtil.getTotalThread();
        System.err.println(""+minTotalThreadRatioValue);

        double minCpuRatioValue = SystemInfoUtil.getCpuRatio();
        System.err.println(""+minCpuRatioValue);
        double totalValue = minMemRatioValue*minCpuRatioValue*minTotalThreadRatioValue;
        System.err.println(""+totalValue);


    }
}
