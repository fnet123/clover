package com.gome.clover;

import com.gome.clover.common.compress.CompressUtil;
import com.gome.clover.common.tools.ClassUtil;
import com.gome.clover.common.tools.CommonConstants;
import com.gome.clover.core.job.ClientJob;
import com.gome.clover.core.job.ClientJobBuilder;
import com.gome.clover.core.job.ServerJob;
import com.gome.clover.job.MyJob;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;

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
 * Date: 2014/11/30
 * Time: 10:48
 */
public class ObjectToByte {
    @Test
    public  void toObj(){
        String msg="rO0ABXNyACJjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuU2VydmVySm9its+L9tdn6kgCAAB4cgAi\n" +
                "Y29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYg0GalC4zmMwAgAJTAALZXhlY3V0ZVR5\n" +
                "cGV0ADBMY29tL2dvbWUvY2xvdmVyL2NvcmUvam9iL0NsaWVudEpvYiRFeGVjdXRlVHlwZTtbAA5m\n" +
                "aXhlZENsaWVudElwc3QAE1tMamF2YS9sYW5nL1N0cmluZztbAA5maXhlZFNlcnZlcklwc3EAfgAD\n" +
                "TAACaXB0ABJMamF2YS9sYW5nL1N0cmluZztMAAhqb2JDbGFzc3QAEUxqYXZhL2xhbmcvQ2xhc3M7\n" +
                "TAAMam9iQ2xhc3NOYW1lcQB+AARMAAlqb2JEZXRhaWx0ABZMb3JnL3F1YXJ0ei9Kb2JEZXRhaWw7\n" +
                "TAAHam9iVHlwZXQALExjb20vZ29tZS9jbG92ZXIvY29yZS9qb2IvQ2xpZW50Sm9iJEpvYlR5cGU7\n" +
                "TAAHdHJpZ2dlcnQAFExvcmcvcXVhcnR6L1RyaWdnZXI7eHB+cgAuY29tLmdvbWUuY2xvdmVyLmNv\n" +
                "cmUuam9iLkNsaWVudEpvYiRFeGVjdXRlVHlwZQAAAAAAAAAAEgAAeHIADmphdmEubGFuZy5FbnVt\n" +
                "AAAAAAAAAAASAAB4cHQAA0FERHBwdAANMTAuMTQ0LjMzLjIxMnB0ACZjb20uZ29tZS5jbG92ZXIu\n" +
                "Y29yZS5qb2IuY2xpZW50Lk15Sm9iMXNyAB1vcmcucXVhcnR6LmltcGwuSm9iRGV0YWlsSW1wbKvD\n" +
                "yuwBWlSvAgAHWgAKZHVyYWJpbGl0eVoADXNob3VsZFJlY292ZXJMAAtkZXNjcmlwdGlvbnEAfgAE\n" +
                "TAAFZ3JvdXBxAH4ABEwACGpvYkNsYXNzcQB+AAVMAApqb2JEYXRhTWFwdAAXTG9yZy9xdWFydHov\n" +
                "Sm9iRGF0YU1hcDtMAARuYW1lcQB+AAR4cAAAcHQAC3JlbW90ZS1qb2JzdnEAfgAAc3IAFW9yZy5x\n" +
                "dWFydHouSm9iRGF0YU1hcJ+wg+i/qbDLAgAAeHIAJm9yZy5xdWFydHoudXRpbHMuU3RyaW5nS2V5\n" +
                "RGlydHlGbGFnTWFwggjow/vFXSgCAAFaABNhbGxvd3NUcmFuc2llbnREYXRheHIAHW9yZy5xdWFy\n" +
                "dHoudXRpbHMuRGlydHlGbGFnTWFwE+YurSh2Cs4CAAJaAAVkaXJ0eUwAA21hcHQAD0xqYXZhL3V0\n" +
                "aWwvTWFwO3hwAXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkA\n" +
                "CXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAAHam9iSW5mb3NxAH4AAXEAfgAMcHBxAH4A\n" +
                "DnZyACZjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuY2xpZW50Lk15Sm9iMShpJZ9gwd+ZAgAAeHBx\n" +
                "AH4AD3NxAH4AEAAAcHQAB0RFRkFVTFRxAH4AH3B0AAVNeUpvYn5yACpjb20uZ29tZS5jbG92ZXIu\n" +
                "Y29yZS5qb2IuQ2xpZW50Sm9iJEpvYlR5cGUAAAAAAAAAABIAAHhxAH4AC3QABlJFTU9URXNyAChv\n" +
                "cmcucXVhcnR6LmltcGwudHJpZ2dlcnMuQ3JvblRyaWdnZXJJbXBsiAb06o3becICAAVMAAZjcm9u\n" +
                "RXh0ABtMb3JnL3F1YXJ0ei9Dcm9uRXhwcmVzc2lvbjtMAAdlbmRUaW1ldAAQTGphdmEvdXRpbC9E\n" +
                "YXRlO0wADG5leHRGaXJlVGltZXEAfgAoTAAQcHJldmlvdXNGaXJlVGltZXEAfgAoTAAJc3RhcnRU\n" +
                "aW1lcQB+ACh4cgAob3JnLnF1YXJ0ei5pbXBsLnRyaWdnZXJzLkFic3RyYWN0VHJpZ2dlcsnRVzsN\n" +
                "4PXuAgALSQASbWlzZmlyZUluc3RydWN0aW9uSQAIcHJpb3JpdHlaAAp2b2xhdGlsaXR5TAAMY2Fs\n" +
                "ZW5kYXJOYW1lcQB+AARMAAtkZXNjcmlwdGlvbnEAfgAETAAOZmlyZUluc3RhbmNlSWRxAH4ABEwA\n" +
                "BWdyb3VwcQB+AARMAApqb2JEYXRhTWFwcQB+ABFMAAhqb2JHcm91cHEAfgAETAAHam9iTmFtZXEA\n" +
                "fgAETAAEbmFtZXEAfgAEeHAAAAAAAAAABQBwcHBxAH4AIXBxAH4AIXB0AAd0cmlnZ2Vyc3IAGW9y\n" +
                "Zy5xdWFydHouQ3JvbkV4cHJlc3Npb24AAAAC5H4vDwIAAkwADmNyb25FeHByZXNzaW9ucQB+AARM\n" +
                "AAh0aW1lWm9uZXQAFExqYXZhL3V0aWwvVGltZVpvbmU7eHB0AA4wLzEwICogKiAqICogP3NyABpz\n" +
                "dW4udXRpbC5jYWxlbmRhci5ab25lSW5mbyTR084AHXGbAgAISQAIY2hlY2tzdW1JAApkc3RTYXZp\n" +
                "bmdzSQAJcmF3T2Zmc2V0SQANcmF3T2Zmc2V0RGlmZloAE3dpbGxHTVRPZmZzZXRDaGFuZ2VbAAdv\n" +
                "ZmZzZXRzdAACW0lbABRzaW1wbGVUaW1lWm9uZVBhcmFtc3EAfgAxWwALdHJhbnNpdGlvbnN0AAJb\n" +
                "SnhyABJqYXZhLnV0aWwuVGltZVpvbmUxs+n1d0SsoQIAAUwAAklEcQB+AAR4cHQADUFzaWEvU2hh\n" +
                "bmdoYWkEKEHsAAAAAAG3dAAAAAAAAHVyAAJbSU26YCZ26rKlAgAAeHAAAAAEAbd0AAG80wAB7mKA\n" +
                "ADbugHB1cgACW0p4IAS1ErF1kwIAAHhwAAAAE//f2uAdwAAB/+y2KMAQAAD/8mp2XcAAMv/ykRKg\n" +
                "2AAA//LGhApAADL/8waNs5gAAAAHgHYvAAAyAAerQZzYAAAAB+7cg0AAMgAIIGpJ2AAAAAhkBTBA\n" +
                "ADIACJWS9tgAAAAI226lgAAyAAkM/GwYAAAACVCXUoAAMgAJgiUZGAAAAAnFv/+AADIACfdNxhgA\n" +
                "AAAexJMywAAAcHBwc3IADmphdmEudXRpbC5EYXRlaGqBAUtZdBkDAAB4cHcIAAABSf6cuCB4eAB0\n" +
                "AA1ERUZBVUxULk15Sm9icQB+ACRzcQB+ACYAAAAAAAAABQBwcHBxAH4AE3BxAH4AE3QADURFRkFV\n" +
                "TFQuTXlKb2J0AA1ERUZBVUxULk15Sm9ic3EAfgAsdAAOMC8xMCAqICogKiAqID9zcQB+ADB0AA1B\n" +
                "c2lhL1NoYW5naGFpBChB7AAAAAABt3QAAAAAAAB1cQB+ADYAAAAEAbd0AAG80wAB7mKAADbugHB1\n" +
                "cQB+ADgAAAAT/9/a4B3AAAH/7LYowBAAAP/yanZdwAAy//KREqDYAAD/8saECkAAMv/zBo2zmAAA\n" +
                "AAeAdi8AADIAB6tBnNgAAAAH7tyDQAAyAAggaknYAAAACGQFMEAAMgAIlZL22AAAAAjbbqWAADIA\n" +
                "CQz8bBgAAAAJUJdSgAAyAAmCJRkYAAAACcW//4AAMgAJ903GGAAAAB7EkzLAAABwcHBzcQB+ADp3\n" +
                "CAAAAUn+nLggeA==";
        ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(msg));
        System.err.println("server:"+serverJob);
        ClientJob clientJob = (ClientJob) serverJob.getJobDetail().getJobDataMap().get(CommonConstants.CLIENT_JOB_INFO);
        serverJob.getJobDetail().getJobDataMap().put(CommonConstants.CLIENT_JOB_INFO,Base64.encodeBase64String(ClassUtil.ObjectToBytes(clientJob))) ;
        String newMsg=     Base64.encodeBase64String(ClassUtil.ObjectToBytes(serverJob));
        System.err.println("newMsg:"+newMsg);

    }
    @Test
    public  void toObj1(){
        String msg="rO0ABXNyACJjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuQ2xpZW50Sm9iDQZqULjOYzACAApaABJp\n" +
                "c1JlY292ZXJKb2JGcm9tREJMAAtleGVjdXRlVHlwZXQAMExjb20vZ29tZS9jbG92ZXIvY29yZS9q\n" +
                "b2IvQ2xpZW50Sm9iJEV4ZWN1dGVUeXBlO1sADmZpeGVkQ2xpZW50SXBzdAATW0xqYXZhL2xhbmcv\n" +
                "U3RyaW5nO1sADmZpeGVkU2VydmVySXBzcQB+AAJMAAJpcHQAEkxqYXZhL2xhbmcvU3RyaW5nO0wA\n" +
                "CGpvYkNsYXNzdAARTGphdmEvbGFuZy9DbGFzcztMAAxqb2JDbGFzc05hbWVxAH4AA0wACWpvYkRl\n" +
                "dGFpbHQAFkxvcmcvcXVhcnR6L0pvYkRldGFpbDtMAAdqb2JUeXBldAAsTGNvbS9nb21lL2Nsb3Zl\n" +
                "ci9jb3JlL2pvYi9DbGllbnRKb2IkSm9iVHlwZTtMAAd0cmlnZ2VydAAUTG9yZy9xdWFydHovVHJp\n" +
                "Z2dlcjt4cAF+cgAuY29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYiRFeGVjdXRlVHlw\n" +
                "ZQAAAAAAAAAAEgAAeHIADmphdmEubGFuZy5FbnVtAAAAAAAAAAASAAB4cHQABlVQREFURXBwdAAK\n" +
                "MTAuNTguMTEuMXZyADZjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuY2xpZW50LkRlbGV0ZUdvbWVW\n" +
                "aXBBZFBsYW5Kb2IAAAAAAAAAAAAAAHhwdAA2Y29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLmNsaWVu\n" +
                "dC5EZWxldGVHb21lVmlwQWRQbGFuSm9ic3IAHW9yZy5xdWFydHouaW1wbC5Kb2JEZXRhaWxJbXBs\n" +
                "q8PK7AFaVK8CAAdaAApkdXJhYmlsaXR5WgANc2hvdWxkUmVjb3ZlckwAC2Rlc2NyaXB0aW9ucQB+\n" +
                "AANMAAVncm91cHEAfgADTAAIam9iQ2xhc3NxAH4ABEwACmpvYkRhdGFNYXB0ABdMb3JnL3F1YXJ0\n" +
                "ei9Kb2JEYXRhTWFwO0wABG5hbWVxAH4AA3hwAABwdAADZ2NjcQB+AA9zcgAVb3JnLnF1YXJ0ei5K\n" +
                "b2JEYXRhTWFwn7CD6L+psMsCAAB4cgAmb3JnLnF1YXJ0ei51dGlscy5TdHJpbmdLZXlEaXJ0eUZs\n" +
                "YWdNYXCCCOjD+8VdKAIAAVoAE2FsbG93c1RyYW5zaWVudERhdGF4cgAdb3JnLnF1YXJ0ei51dGls\n" +
                "cy5EaXJ0eUZsYWdNYXAT5i6tKHYKzgIAAloABWRpcnR5TAADbWFwdAAPTGphdmEvdXRpbC9NYXA7\n" +
                "eHABc3IAEWphdmEudXRpbC5IYXNoTWFwBQfawcMWYNEDAAJGAApsb2FkRmFjdG9ySQAJdGhyZXNo\n" +
                "b2xkeHA/QAAAAAAADHcIAAAAEAAAAAJ0AARhZElkdAAVdGJsXzE0MTc2NjA4MzQxMjgxNTI3dAAG\n" +
                "YWRDb2RldAALc2lkZUZvY3VzQWR4AHQALERlbGV0ZUdvbWVWaXBBZFBsYW5Kb2JAdGJsXzE0MTc2\n" +
                "NjA4MzQxMjgxNTI3fnIAKmNvbS5nb21lLmNsb3Zlci5jb3JlLmpvYi5DbGllbnRKb2IkSm9iVHlw\n" +
                "ZQAAAAAAAAAAEgAAeHEAfgAKdAAFTE9DQUxzcgAqb3JnLnF1YXJ0ei5pbXBsLnRyaWdnZXJzLlNp\n" +
                "bXBsZVRyaWdnZXJJbXBszCch6qQCbqMCAAhaAAhjb21wbGV0ZUkAC3JlcGVhdENvdW50SgAOcmVw\n" +
                "ZWF0SW50ZXJ2YWxJAA50aW1lc1RyaWdnZXJlZEwAB2VuZFRpbWV0ABBMamF2YS91dGlsL0RhdGU7\n" +
                "TAAMbmV4dEZpcmVUaW1lcQB+ACVMABBwcmV2aW91c0ZpcmVUaW1lcQB+ACVMAAlzdGFydFRpbWVx\n" +
                "AH4AJXhyAChvcmcucXVhcnR6LmltcGwudHJpZ2dlcnMuQWJzdHJhY3RUcmlnZ2VyydFXOw3g9e4C\n" +
                "AAtJABJtaXNmaXJlSW5zdHJ1Y3Rpb25JAAhwcmlvcml0eVoACnZvbGF0aWxpdHlMAAxjYWxlbmRh\n" +
                "ck5hbWVxAH4AA0wAC2Rlc2NyaXB0aW9ucQB+AANMAA5maXJlSW5zdGFuY2VJZHEAfgADTAAFZ3Jv\n" +
                "dXBxAH4AA0wACmpvYkRhdGFNYXBxAH4AEkwACGpvYkdyb3VwcQB+AANMAAdqb2JOYW1lcQB+AANM\n" +
                "AARuYW1lcQB+AAN4cAAAAAAAAAAFAHBwcHEAfgAUcHEAfgAUcQB+ACBxAH4AIAAAAAAAAAAAAAAA\n" +
                "AAAAAAAAcHNyABJqYXZhLnNxbC5UaW1lc3RhbXAmGNXIAVO/ZQIAAUkABW5hbm9zeHIADmphdmEu\n" +
                "dXRpbC5EYXRlaGqBAUtZdBkDAAB4cHcIAAABSi0lgAB4AAAAAHBxAH4AKg==\n";
        ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64(msg));
        System.err.println("server:"+clientJob);


    }
    @Test
    public void testNewMsg(){
        String newMsg="rO0ABXNyACJjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuU2VydmVySm9its+L9tdn6kgCAAB4cgAiY29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYg0GalC4zmMwAgAJTAALZXhlY3V0ZVR5cGV0ADBMY29tL2dvbWUvY2xvdmVyL2NvcmUvam9iL0NsaWVudEpvYiRFeGVjdXRlVHlwZTtbAA5maXhlZENsaWVudElwc3QAE1tMamF2YS9sYW5nL1N0cmluZztbAA5maXhlZFNlcnZlcklwc3EAfgADTAACaXB0ABJMamF2YS9sYW5nL1N0cmluZztMAAhqb2JDbGFzc3QAEUxqYXZhL2xhbmcvQ2xhc3M7TAAMam9iQ2xhc3NOYW1lcQB+AARMAAlqb2JEZXRhaWx0ABZMb3JnL3F1YXJ0ei9Kb2JEZXRhaWw7TAAHam9iVHlwZXQALExjb20vZ29tZS9jbG92ZXIvY29yZS9qb2IvQ2xpZW50Sm9iJEpvYlR5cGU7TAAHdHJpZ2dlcnQAFExvcmcvcXVhcnR6L1RyaWdnZXI7eHB+cgAuY29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYiRFeGVjdXRlVHlwZQAAAAAAAAAAEgAAeHIADmphdmEubGFuZy5FbnVtAAAAAAAAAAASAAB4cHQAA0FERHBwdAANMTAuMTQ0LjMzLjIxMnB0ACZjb20uZ29tZS5jbG92ZXIuY29yZS5qb2IuY2xpZW50Lk15Sm9iMXNyAB1vcmcucXVhcnR6LmltcGwuSm9iRGV0YWlsSW1wbKvDyuwBWlSvAgAHWgAKZHVyYWJpbGl0eVoADXNob3VsZFJlY292ZXJMAAtkZXNjcmlwdGlvbnEAfgAETAAFZ3JvdXBxAH4ABEwACGpvYkNsYXNzcQB+AAVMAApqb2JEYXRhTWFwdAAXTG9yZy9xdWFydHovSm9iRGF0YU1hcDtMAARuYW1lcQB+AAR4cAAAcHQAC3JlbW90ZS1qb2JzdnEAfgAAc3IAFW9yZy5xdWFydHouSm9iRGF0YU1hcJ+wg+i/qbDLAgAAeHIAJm9yZy5xdWFydHoudXRpbHMuU3RyaW5nS2V5RGlydHlGbGFnTWFwggjow/vFXSgCAAFaABNhbGxvd3NUcmFuc2llbnREYXRheHIAHW9yZy5xdWFydHoudXRpbHMuRGlydHlGbGFnTWFwE+YurSh2Cs4CAAJaAAVkaXJ0eUwAA21hcHQAD0xqYXZhL3V0aWwvTWFwO3hwAXNyABFqYXZhLnV0aWwuSGFzaE1hcAUH2sHDFmDRAwACRgAKbG9hZEZhY3RvckkACXRocmVzaG9sZHhwP0AAAAAAAAx3CAAAABAAAAABdAAHam9iSW5mb3QKMHJPMEFCWE55QUNKamIyMHVaMjl0WlM1amJHOTJaWEl1WTI5eVpTNXFiMkl1UTJ4cFpXNTBTbTlpRFFacVVMak9ZekFDQUFsTUFBdGxlR1ZqZFhSbFZIbHdaWFFBTUV4amIyMHZaMjl0WlM5amJHOTJaWEl2WTI5eVpTOXFiMkl2UTJ4cFpXNTBTbTlpSkVWNFpXTjFkR1ZVZVhCbE8xc0FEbVpwZUdWa1EyeHBaVzUwU1hCemRBQVRXMHhxWVhaaEwyeGhibWN2VTNSeWFXNW5PMXNBRG1acGVHVmtVMlZ5ZG1WeVNYQnpjUUIrQUFKTUFBSnBjSFFBRWt4cVlYWmhMMnhoYm1jdlUzUnlhVzVuTzB3QUNHcHZZa05zWVhOemRBQVJUR3BoZG1FdmJHRnVaeTlEYkdGemN6dE1BQXhxYjJKRGJHRnpjMDVoYldWeEFINEFBMHdBQ1dwdllrUmxkR0ZwYkhRQUZreHZjbWN2Y1hWaGNuUjZMMHB2WWtSbGRHRnBiRHRNQUFkcWIySlVlWEJsZEFBc1RHTnZiUzluYjIxbEwyTnNiM1psY2k5amIzSmxMMnB2WWk5RGJHbGxiblJLYjJJa1NtOWlWSGx3WlR0TUFBZDBjbWxuWjJWeWRBQVVURzl5Wnk5eGRXRnlkSG92VkhKcFoyZGxjanQ0Y0g1eUFDNWpiMjB1WjI5dFpTNWpiRzkyWlhJdVkyOXlaUzVxYjJJdVEyeHBaVzUwU205aUpFVjRaV04xZEdWVWVYQmxBQUFBQUFBQUFBQVNBQUI0Y2dBT2FtRjJZUzVzWVc1bkxrVnVkVzBBQUFBQUFBQUFBQklBQUhod2RBQURRVVJFY0hCMEFBMHhNQzR4TkRRdU16TXVNakV5ZG5JQUptTnZiUzVuYjIxbExtTnNiM1psY2k1amIzSmxMbXB2WWk1amJHbGxiblF1VFhsS2IySXhLR2tsbjJEQjM1a0NBQUI0Y0hRQUptTnZiUzVuYjIxbExtTnNiM1psY2k1amIzSmxMbXB2WWk1amJHbGxiblF1VFhsS2IySXhjM0lBSFc5eVp5NXhkV0Z5ZEhvdWFXMXdiQzVLYjJKRVpYUmhhV3hKYlhCc3E4UEs3QUZhVks4Q0FBZGFBQXBrZFhKaFltbHNhWFI1V2dBTmMyaHZkV3hrVW1WamIzWmxja3dBQzJSbGMyTnlhWEIwYVc5dWNRQitBQU5NQUFWbmNtOTFjSEVBZmdBRFRBQUlhbTlpUTJ4aGMzTnhBSDRBQkV3QUNtcHZZa1JoZEdGTllYQjBBQmRNYjNKbkwzRjFZWEowZWk5S2IySkVZWFJoVFdGd08wd0FCRzVoYldWeEFINEFBM2h3QUFCd2RBQUhSRVZHUVZWTVZIRUFmZ0FQY0hRQUJVMTVTbTlpZm5JQUttTnZiUzVuYjIxbExtTnNiM1psY2k1amIzSmxMbXB2WWk1RGJHbGxiblJLYjJJa1NtOWlWSGx3WlFBQUFBQUFBQUFBRWdBQWVIRUFmZ0FLZEFBR1VrVk5UMVJGYzNJQUtHOXlaeTV4ZFdGeWRIb3VhVzF3YkM1MGNtbG5aMlZ5Y3k1RGNtOXVWSEpwWjJkbGNrbHRjR3lJQnZUcWpkdDV3Z0lBQlV3QUJtTnliMjVGZUhRQUcweHZjbWN2Y1hWaGNuUjZMME55YjI1RmVIQnlaWE56YVc5dU8wd0FCMlZ1WkZScGJXVjBBQkJNYW1GMllTOTFkR2xzTDBSaGRHVTdUQUFNYm1WNGRFWnBjbVZVYVcxbGNRQitBQnRNQUJCd2NtVjJhVzkxYzBacGNtVlVhVzFsY1FCK0FCdE1BQWx6ZEdGeWRGUnBiV1Z4QUg0QUczaHlBQ2h2Y21jdWNYVmhjblI2TG1sdGNHd3VkSEpwWjJkbGNuTXVRV0p6ZEhKaFkzUlVjbWxuWjJWeXlkRlhPdzNnOWU0Q0FBdEpBQkp0YVhObWFYSmxTVzV6ZEhKMVkzUnBiMjVKQUFod2NtbHZjbWwwZVZvQUNuWnZiR0YwYVd4cGRIbE1BQXhqWVd4bGJtUmhjazVoYldWeEFINEFBMHdBQzJSbGMyTnlhWEIwYVc5dWNRQitBQU5NQUE1bWFYSmxTVzV6ZEdGdVkyVkpaSEVBZmdBRFRBQUZaM0p2ZFhCeEFINEFBMHdBQ21wdllrUmhkR0ZOWVhCeEFINEFFa3dBQ0dwdllrZHliM1Z3Y1FCK0FBTk1BQWRxYjJKT1lXMWxjUUIrQUFOTUFBUnVZVzFsY1FCK0FBTjRjQUFBQUFBQUFBQUZBSEJ3Y0hFQWZnQVVjSEVBZmdBVWNIUUFCM1J5YVdkblpYSnpjZ0FaYjNKbkxuRjFZWEowZWk1RGNtOXVSWGh3Y21WemMybHZiZ0FBQUFMa2ZpOFBBZ0FDVEFBT1kzSnZia1Y0Y0hKbGMzTnBiMjV4QUg0QUEwd0FDSFJwYldWYWIyNWxkQUFVVEdwaGRtRXZkWFJwYkM5VWFXMWxXbTl1WlR0NGNIUUFEakF2TVRBZ0tpQXFJQ29nS2lBL2MzSUFHbk4xYmk1MWRHbHNMbU5oYkdWdVpHRnlMbHB2Ym1WSmJtWnZKTkhUemdBZGNac0NBQWhKQUFoamFHVmphM04xYlVrQUNtUnpkRk5oZG1sdVozTkpBQWx5WVhkUFptWnpaWFJKQUExeVlYZFBabVp6WlhSRWFXWm1XZ0FUZDJsc2JFZE5WRTltWm5ObGRFTm9ZVzVuWlZzQUIyOW1abk5sZEhOMEFBSmJTVnNBRkhOcGJYQnNaVlJwYldWYWIyNWxVR0Z5WVcxemNRQitBQ1JiQUF0MGNtRnVjMmwwYVc5dWMzUUFBbHRLZUhJQUVtcGhkbUV1ZFhScGJDNVVhVzFsV205dVpUR3o2ZlYzUkt5aEFnQUJUQUFDU1VSeEFINEFBM2h3ZEFBTlFYTnBZUzlUYUdGdVoyaGhhUVFvUWV3QUFBQUFBYmQwQUFBQUFBQUFkWElBQWx0SlRicGdKbmJxc3FVQ0FBQjRjQUFBQUFRQnQzUUFBYnpUQUFIdVlvQUFOdTZBY0hWeUFBSmJTbmdnQkxVU3NYV1RBZ0FBZUhBQUFBQVQvOS9hNEIzQUFBSC83TFlvd0JBQUFQL3lhblpkd0FBeS8vS1JFcURZQUFELzhzYUVDa0FBTXYvekJvMnptQUFBQUFlQWRpOEFBRElBQjZ0Qm5OZ0FBQUFIN3R5RFFBQXlBQWdnYWtuWUFBQUFDR1FGTUVBQU1nQUlsWkwyMkFBQUFBamJicVdBQURJQUNRejhiQmdBQUFBSlVKZFNnQUF5QUFtQ0pSa1lBQUFBQ2NXLy80QUFNZ0FKOTAzR0dBQUFBQjdFa3pMQUFBQndjSEJ6Y2dBT2FtRjJZUzUxZEdsc0xrUmhkR1ZvYW9FQlMxbDBHUU1BQUhod2R3Z0FBQUZKL3B5NElIZz14AHQADURFRkFVTFQuTXlKb2J+cgAqY29tLmdvbWUuY2xvdmVyLmNvcmUuam9iLkNsaWVudEpvYiRKb2JUeXBlAAAAAAAAAAASAAB4cQB+AAt0AAZSRU1PVEVzcgAob3JnLnF1YXJ0ei5pbXBsLnRyaWdnZXJzLkNyb25UcmlnZ2VySW1wbIgG9OqN23nCAgAFTAAGY3JvbkV4dAAbTG9yZy9xdWFydHovQ3JvbkV4cHJlc3Npb247TAAHZW5kVGltZXQAEExqYXZhL3V0aWwvRGF0ZTtMAAxuZXh0RmlyZVRpbWVxAH4AJEwAEHByZXZpb3VzRmlyZVRpbWVxAH4AJEwACXN0YXJ0VGltZXEAfgAkeHIAKG9yZy5xdWFydHouaW1wbC50cmlnZ2Vycy5BYnN0cmFjdFRyaWdnZXLJ0Vc7DeD17gIAC0kAEm1pc2ZpcmVJbnN0cnVjdGlvbkkACHByaW9yaXR5WgAKdm9sYXRpbGl0eUwADGNhbGVuZGFyTmFtZXEAfgAETAALZGVzY3JpcHRpb25xAH4ABEwADmZpcmVJbnN0YW5jZUlkcQB+AARMAAVncm91cHEAfgAETAAKam9iRGF0YU1hcHEAfgARTAAIam9iR3JvdXBxAH4ABEwAB2pvYk5hbWVxAH4ABEwABG5hbWVxAH4ABHhwAAAAAAAAAAUAcHBwcQB+ABNwcQB+ABN0AA1ERUZBVUxULk15Sm9idAANREVGQVVMVC5NeUpvYnNyABlvcmcucXVhcnR6LkNyb25FeHByZXNzaW9uAAAAAuR+Lw8CAAJMAA5jcm9uRXhwcmVzc2lvbnEAfgAETAAIdGltZVpvbmV0ABRMamF2YS91dGlsL1RpbWVab25lO3hwdAAOMC8xMCAqICogKiAqID9zcgAac3VuLnV0aWwuY2FsZW5kYXIuWm9uZUluZm8k0dPOAB1xmwIACEkACGNoZWNrc3VtSQAKZHN0U2F2aW5nc0kACXJhd09mZnNldEkADXJhd09mZnNldERpZmZaABN3aWxsR01UT2Zmc2V0Q2hhbmdlWwAHb2Zmc2V0c3QAAltJWwAUc2ltcGxlVGltZVpvbmVQYXJhbXNxAH4ALlsAC3RyYW5zaXRpb25zdAACW0p4cgASamF2YS51dGlsLlRpbWVab25lMbPp9XdErKECAAFMAAJJRHEAfgAEeHB0AA1Bc2lhL1NoYW5naGFpBChB7AAAAAABt3QAAAAAAAB1cgACW0lNumAmduqypQIAAHhwAAAABAG3dAABvNMAAe5igAA27oBwdXIAAltKeCAEtRKxdZMCAAB4cAAAABP/39rgHcAAAf/stijAEAAA//Jqdl3AADL/8pESoNgAAP/yxoQKQAAy//MGjbOYAAAAB4B2LwAAMgAHq0Gc2AAAAAfu3INAADIACCBqSdgAAAAIZAUwQAAyAAiVkvbYAAAACNtupYAAMgAJDPxsGAAAAAlQl1KAADIACYIlGRgAAAAJxb//gAAyAAn3TcYYAAAAHsSTMsAAAHBwcHNyAA5qYXZhLnV0aWwuRGF0ZWhqgQFLWXQZAwAAeHB3CAAAAUn+nLggeA==";

         ServerJob serverJob = (ServerJob) ClassUtil.BytesToObject(Base64.decodeBase64(newMsg));
         ClientJob clientJob = (ClientJob) ClassUtil.BytesToObject(Base64.decodeBase64((String)serverJob.getJobDetail().getJobDataMap().get(CommonConstants.CLIENT_JOB_INFO)));
         System.err.println("server:"+serverJob);
         System.err.println("clientJob:"+clientJob);
    }

    @Test
    public void testCompress(){
        ClientJob clientJob = ClientJobBuilder.quickBuildLocalCronJob("jobGroup","jobGroup", MyJob.class,"0/10 * * * * ?");
        byte[] bytes= ClassUtil.ObjectToBytes(clientJob);
        System.err.println("compress 前："+bytes.length);
        byte[] compressBytes = CompressUtil.compress(bytes);
        System.err.println("compress 后："+compressBytes.length);
        byte[] uncompressBytes = CompressUtil.uncompress(compressBytes);
        ClientJob clientJob2 = (ClientJob) ClassUtil.BytesToObject(uncompressBytes);
        System.err.println("clientJob2:"+clientJob2);
    }



}
