<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.gome.clover.common.mongodb.DBTableInfo" %>
<%@ page import="com.gome.clover.common.mongodb.MongoDBUtil" %>
<%@ page import="com.gome.clover.common.tools.CommonConstants" %>
<%@ page import="com.gome.clover.common.tools.DateUtil" %>
<%@ page import="com.gome.clover.common.tools.StringUtil" %>
<%@ page import="com.mongodb.BasicDBObject" %>
<%@ page import="com.mongodb.DBCursor" %>
<%@ page import="com.mongodb.DBObject" %>
<%@ page import="java.util.Date" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
	String qJobKey = request.getParameter("qJobKey");
	String qJobType = request.getParameter("qJobType");
	String qIp = request.getParameter("qIp");
	String qStatus = request.getParameter("qStatus");
	DBObject condition = new BasicDBObject();
	if (!StringUtil.isEmpty(qJobKey)) condition.put(DBTableInfo.COL_JOB_KEY, qJobKey);
	if (!StringUtil.isEmpty(qJobType)) condition.put(DBTableInfo.COL_JOB_TYPE, qJobType);
	if (!StringUtil.isEmpty(qIp)) condition.put(DBTableInfo.COL_IP, qIp);
	if (!StringUtil.isEmpty(qStatus)) condition.put(DBTableInfo.COL_STATUS, qStatus);
	DBCursor dbCursor =  MongoDBUtil.INSTANCE.findByCondition(condition,DBTableInfo.TBL_CLOVER_JOB);
%>
<!DOCTYPE html>
<html>
 <jsp:include page="/common/head.jsp"/>
  <body>
 	<jsp:include page="/common/nav.jsp"/>
    <div class="container-fluid">

		<table class="table table-bordered">
			<tbody>
			<form action="<%=basePath%>/jobList.jsp" method="post">
				<tr>
					<th>Job Key:</th>
					<td>
						<input  type="text" id="qJobKey"name="qJobKey" placeholder="Job Key"
								value="<%=null!=qJobKey?qJobKey:""%>"/>
					</td>

					<td>
						<select id="qJobType"name="qJobType">
							<%
							 if("LOCAL".equals(qJobType)){
							%>
								<option value="">请选择Job Type</option>
								<option value="LOCAL" selected>LOCAL</option>
								<option value="REMOTE" >REMOTE</option>
							<%
							 }else if("REMOTE".equals(qJobType)){
							%>
								<option value="">请选择Job Type</option>
								<option value="LOCAL" >LOCAL</option>
								<option value="REMOTE" selected >REMOTE</option>
							<%
							 }else {
							%>
								 <option value="">请选择Job Type</option>
								 <option value="LOCAL" >LOCAL</option>
								 <option value="REMOTE" >REMOTE</option>
							<%
							 }
							%>
						</select>
					</td>
					<th>ip:</th>
					<td>
						<input  type="text" id="qIp"name="qIp" placeholder="ip" value="<%=null!=qIp?qIp:""%>"/>
					</td>

					<td>
						<select id="qStatus"name="qStatus">
							<%
								if("1".equals(qStatus)){
							%>
								<option value="">请选择 Status</option>
								<option value="1" selected>初始化状态</option>
								<option value="2">运行中状态</option>
							<%
							}else if("2".equals(qStatus)){
							%>
								<option value="">请选择 Status</option>
								<option value="1">初始化状态</option>
								<option value="2" selected>运行中状态</option>
							<%
							}else {
							%>
								<option value="">请选择 Status</option>
								<option value="1">初始化状态</option>
								<option value="2">运行中状态</option>
							<%
								}
							%>

						</select>
					</td>
				</tr>
				<tr>
					<td colspan="6">
						<center>
							<button class="btn btn-success" type="submit">查询</button>
							<button class="btn" onclick="clearQuery()">清空</button>
							<a class="btn btn-primary" data-toggle="modal" onclick="toAddJob()" href="#">
								新建</a>
						</center>
					</td>
				</tr>
			</form>
			</tbody>
		</table>

		<table class="table table-bordered table-striped">
      <thead>
       <tr>
         <th style="width: 20%; text-align: center;" nowrap>Job Key</th>
          <th style="width: 5%; text-align: center;" nowrap>Job Type</th>
          <th style="width: 5%; text-align: center;" nowrap>Start Time</th>
          <th style="width: 5%; text-align: center;" nowrap>Cron Expression</th>
          <th style="width: 5%; text-align: center;" nowrap>Client Ip</th>
          <th style="width: 5%; text-align: center;" nowrap>Server Ip</th>
          <th style="width: 5%; text-align: center;" nowrap>Execute Start Time</th>
          <th style="width: 5%; text-align: center;" nowrap>Execute End Time</th>
          <th style="width: 5%; text-align: center;" nowrap>times</th>
          <th style="width: 5%; text-align: center;" nowrap>Fail Times</th>
          <th style="width: 5%; text-align: center;" nowrap>status</th>
          <th style="width: 5%; text-align: center;" nowrap>ts</th>
         <th style="width: 5%;text-align: center;" nowrap>操作</th>
       </tr>
      </thead>
      <tbody>
	  <%
	  	if(null!=dbCursor && dbCursor.size()>0){
			while (dbCursor.hasNext()) {
                DBObject tempDBObject = dbCursor.next();
				String nextValidTime = "";
				if(tempDBObject.containsField(DBTableInfo.COL_CRON_EXPRESSION) && StringUtil.isNotBlank((String)tempDBObject.get(DBTableInfo.COL_CRON_EXPRESSION))) {
					nextValidTime = DateUtil.formatWithDefaultPattern(
							DateUtil.getNextValidTimeAfter((String) tempDBObject.get(DBTableInfo.COL_CRON_EXPRESSION), new Date()));
				}
	  %>
				<tr>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_JOB_KEY)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_JOB_TYPE)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_START_TIME)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%
							if(null!= nextValidTime && nextValidTime.length()>0){
						%>
						<a href="#"  data-toggle="tooltip" data-placement="bottom"
						   title="下次执行时间:<%=nextValidTime%>" >
							<%=tempDBObject.get(DBTableInfo.COL_CRON_EXPRESSION)%></a>
						<%
							}
						%>

					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_CLIENT_IP)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_SERVER_IP)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_EXECUTE_START_TIME)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_EXECUTE_END_TIME)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_TIMES)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_FAIL_TIMES)%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=CommonConstants.JOB_STATUS_1.equals(tempDBObject.get(DBTableInfo.COL_STATUS))?
								"初始化状态":"运行中状态"%>
					</td>
					<td style="text-align: center;" nowrap>
						<%=tempDBObject.get(DBTableInfo.COL_TS)%>
					</td>
					<td style="text-align: center;" nowrap>
					<%--	<a data-toggle="modal" onclick="toUpdateJob('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')" href="#">
							<i class="icon-edit"></i>修改</a>--%>
						<a data-toggle="modal" onclick="toViewJob('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')"
						   href="#">
							<i class="icon-eye-open"></i>查看
						</a>
						<a data-toggle="modal" onclick="toDeleteJob('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')" href="#">
							<i class="icon-remove"></i>删除</a>
					</td>
				</tr>
	  <%
		  }
		}else {
	  %>
			<tr>
			<td colspan="13" style="text-align: center;" nowrap>暂无相关记录</td>
			</tr>
	  <%
		  }
	  %>

      </tbody>
     </table>
    </div>

	<!-- 添加弹出层开始 -->
	<div id="addDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>JOB添加</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tr>
					<th>Job Group:</th>
					<td>
						<input class="form-inline span3" type="text" id="addJobGroup" name="addJobGroup"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Name:</th>
					<td>
						<input class="form-inline span3" type="text" id="addJobName" name="addJobName"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Type:</th>
					<td>
						<select id ="addJobType" name ="addJobType">
							<option value="">请选择Job Type</option>
							<option value="LOCAL" >LOCAL</option>
							<option value="REMOTE">REMOTE</option>
						</select>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Execute Type:</th>
					<td>
						<select id ="addExecuteType" name ="addExecuteType">
							<option value="">请选择Execute Type</option>
							<option value="ADD" >ADD</option>
							<option value="UPDATE">UPDATE</option>
							<option value="DELETE">DELETE</option>
						</select>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Strategy:</th>
					<td>
						<select id ="addJobStrategy" name ="addJobStrategy">
							<option value="">请选择Job Strategy</option>
							<option value="HASH" >HASH</option>
							<option value="SYSTEM_CAPACITY">SYSTEM_CAPACITY</option>
						</select>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Class Name:</th>
					<td>
						<input class="form-inline span3" type="text" id="addJobClassName"name="addJobClassName"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Start Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="addStartTime"name="addStartTime"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Cron Expression:</th>
					<td>
						<input class="form-inline span3" type="text" id="addCronExpression"name="addCronExpression"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>ip:</th>
					<td>
						<input class="form-inline span3" type="text" id="addIp" name="addIp"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>port:</th>
					<td>
						<input class="form-inline span3" type="text" id="addPort" name="addPort"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Fixed Client Ips:</th>
					<td>
						<input class="form-inline span3" type="text" id="addFixedClientIps"
							   name="addFixedClientIps"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Fixed Server Ips:</th>
					<td>
						<input class="form-inline span3" type="text" id="addFixedServerIps"
							   name="addFixedServerIps"/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<center>
							<button class="btn btn-success" onclick="addJob()" >提交</button>
							<button class="btn btn-success" data-dismiss="modal">关闭</button>
						</center>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 修改弹出层结束 -->

	<!-- 修改弹出层开始 -->
	<div id="updateDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>JOB修改</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tbody>
				<tr>
					<th>Job Group:</th>
					<td>
						<input  type="hidden" id="updateId" name="updateId"/>
						<input class="form-inline span3" type="text" id="updateJobGroup" name="updateJobGroup"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Name:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateJobName" name="updateJobName"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Type:</th>
					<td>
						<select id ="updateJobType" name ="updateJobType">
							<option value="">请选择Job Type</option>
							<option value="LOCAL" >LOCAL</option>
							<option value="REMOTE">REMOTE</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>Job Class Name:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateJobClassName"name="updateJobClassName"/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>Start Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateStartTime"name="updateStartTime"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Cron Expression:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateCronExpression"name="updateCronExpression"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>ip:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateIp" name="updateIp"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>ip:</th>
					<td>
						<input class="form-inline span3" type="text" id="updatePort" name="updatePort"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Fixed Client Ips:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateFixedClientIps"
							   name="updateFixedClientIps"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Fixed Server Ips:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateFixedServerIps"
							   name="updateFixedServerIps"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>status:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateStatus"name="updateStatus"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<center>
							<button class="btn btn-success" onclick="updateJob()" >提交</button>
							<button class="btn btn-success" data-dismiss="modal">关闭</button>
						</center>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 修改弹出层结束 -->


	<!-- 查询详情弹出层开始 -->
	<div id="detailDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>JOB详情</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tr>
					<th>Job Key:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailJobKey" name="detailJobKey" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Type:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailJobType"name="detailJobType" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>Execute Type:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailExecuteType"name="detailExecuteType" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>Job Strategy:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailJobStrategy"name="detailJobStrategy" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Start Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailStartTime"name="detailStartTime" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Cron Expression:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailCronExpression"name="detailCronExpression" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Next Valid Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailNextValidTime"name="detailNextValidTime" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>ip:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailIp" name="detailIp" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Fixed Client Ips:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailFixedClientIps"
							   name="detailFixedClientIps" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Fixed Server Ips:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailFixedServerIps"
							   name="detailFixedServerIps" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Execute Start Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailExecuteStartTime"name="detailExecuteStartTime" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>Execute End Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailExecuteEndTime"name="detailExecuteEndTime" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>times:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailTimes"name="detailTimes" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>Fail Times:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailFailTimes"name="detailFailTimes" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>status:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailStatus"name="detailStatus" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>TS:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailTs"name="detailTs" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<center>
							<button class="btn btn-success" data-dismiss="modal">关闭</button>
						</center>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 查询详情弹出层结束 -->

  <!-- 删除弹出层开始 -->
    <div id="deleteJobDiv" class="modal hide fade" aria-hidden="true" ke>
        <div class="modal-header">
            <a class="close" data-dismiss="modal" >&times;</a>
            <h3>JOB删除</h3>
        </div>
        <div class="modal-body">
            <p>您确定删除该JOB</p>
        </div>
        <div class="modal-footer">
            <center>
                <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                <button class="btn btn-success" onclick="deleteJobData()">删除DB数据</button>
                <button class="btn btn-success" onclick="deleteJob()">删除</button>
            </center>
        </div>
    </div>
    <!-- 删除弹出层结束 -->
  </body>
</html>

<script type="text/javascript">

	var isClickZeroMq = false;
	function clearQuery() {
		$("#qJobKey").val("");
		$("#qDest").val("");
		$("#qIp").val("");
		$("#qStatus").val("");
	}
	function clearJob(optType){
		$("#"+optType+"JobGroup").val("");
		$("#"+optType+"JobName").val("");
		$("#"+optType+"JobType").val("");
		$("#"+optType+"ExecuteType").val("");
		$("#"+optType+"JobClassName").val("");
		$("#"+optType+"StartTime").val("");
		$("#"+optType+"CronExpression").val("");
		$("#"+optType+"Ip").val("");
		$("#"+optType+"Port").val("");
		$("#"+optType+"FixedClientIps").val("");
		$("#"+optType+"FixedServerIps").val("");
		$("#"+optType+"Status").val("");
	}

	/*格式校验 开始*/
	function validateJob(optType){
		var jobGroup = $.trim($("#"+optType+"JobGroup").val());
		if(jobGroup == "" || 0 == jobGroup.length){
			alert("job 组，请填写");
			return false;
		}
		var jobName = $.trim($("#"+optType+"JobName").val());
		if(jobName == "" || 0 == jobName.length){
			alert("job 名称，请填写");
			return false;
		}

		var jobType = $.trim($("#"+optType+"JobType").val());
		if(jobType == "" || 0 == jobType.length){
			alert("Job Type，请填写");
			return false;
		}
		var executeType = $.trim($("#"+optType+"ExecuteType").val());
		if(executeType == "" || 0 == executeType.length){
			alert("Execute Type，请填写");
			return false;
		}
		var jobStrategy = $.trim($("#"+optType+"JobStrategy").val());
		if(jobStrategy == "" || 0 == jobStrategy.length){
			alert("Job Strategy，请填写");
			return false;
		}
		var jobClassName = $.trim($("#"+optType+"JobClassName").val());
		if(jobClassName == "" || 0== jobClassName.length){
			alert("JobClassName，请填写");
			return false;
		}
		var startTime = $.trim($("#"+optType+"StartTime").val());
		var cronExpression = $.trim($("#"+optType+"CronExpression").val());
		if((startTime == "" || 0 == startTime.length) && (cronExpression == "" || (0 == cronExpression.length ))){
			alert("Start Time/Cron Expression不能同时为空，请填写");
			return false;
		}
		var ip = $.trim($("#"+optType+"Ip").val());
		if(ip == "" || 0 == ip.length){
			alert("ip，请填写");
			return false;
		}
		var port = $.trim($("#"+optType+"Port").val());
		if(port == "" || 0 == port.length){
			alert("port，请填写");
			return false;
		}
		return true;
	}
	/*格式校验 结束*/

	function toAddJob(){
		clearJob("add");
		$("#addDiv").modal({backdrop: 'static', keyboard: false});
	}
	function toUpdateJob(_id){
		clearJob("update");
		$("#updateDiv").modal({backdrop: 'static', keyboard: false});
	}
	function toDeleteJob(_id){
		$("#updateId").val(_id);
		$("#deleteJobDiv").modal({backdrop: 'static', keyboard: false});
	}
	function toViewJob(_id){
        var url="servlet/cloverServletHandle.do?action=handleJobDetail&id="+_id+"&r="+Math.random();
        $.get(url, null, function(data){
            if(null==data ||data.length==0){
                alert("系统出现异常，请联系管理员");
                return;
            }
            eval("var resultObj = "+data);
            $("#detailJobKey").val(resultObj.jobKey);
            $("#detailJobType").val(resultObj.jobType);
            $("#detailExecuteType").val(resultObj.executeType);
            $("#detailJobStrategy").val(resultObj.jobStrategy);
            $("#detailStartTime").val(resultObj.startTime);
            $("#detailCronExpression").val(resultObj.cronExpression);
            $("#detailNextValidTime").val(resultObj.nextValidTime);
            $("#detailIp").val(resultObj.ip);
            $("#detailFixedClientIps").val(resultObj.fixedClientIps);
            $("#detailFixedServerIps").val(resultObj.fixedServerIps);
            $("#detailExecuteStartTime").val(resultObj.executeStartTime);
            $("#detailExecuteEndTime").val(resultObj.executeEndTime);
            $("#detailTimes").val(resultObj.times);
            $("#detailFailTimes").val(resultObj.failTimes);
            $("#detailStatus").val(resultObj.status);
            $("#detailTs").val(resultObj.ts);
            $("#detailDiv").modal({backdrop: 'static', keyboard: false});
        });
	}
	function addJob() {
		var optType = 'add';
		if(validateJob(optType)){
			$.ajax({
				type:"POST",
				url:"servlet/cloverServletHandle.do?action=handleJobAdd&r="+Math.random(),
				data:{
					"jobGroup":$.trim($("#"+optType+"JobGroup").val()),
					"jobName":$.trim($("#"+optType+"JobName").val()),
					"jobType":$.trim($("#"+optType+"JobType").val()),
					"jobStrategy":$.trim($("#"+optType+"JobStrategy").val()),
					"executeType":$.trim($("#"+optType+"ExecuteType").val()),
					"jobClassName":$.trim($("#"+optType+"JobClassName").val()),
					"startTime":$.trim($("#"+optType+"StartTime").val()),
					"cronExpression":$.trim($("#"+optType+"CronExpression").val()),
					"ip":$.trim($("#"+optType+"Ip").val()),
					"port":$.trim($("#"+optType+"Port").val()),
					"fixedClientIps":$.trim($("#"+optType+"FixedClientIps").val()),
					"fixedServerIps":$.trim($("#"+optType+"FixedServerIps").val())
				},
				success:function (data){
					if(1==data){
						clearJob('add');
						window.location.reload();
					} else {
						alert("系统出现异常，请联系管理员");
						return;
					}
				}
			});
		}
	}
	function updateJob() {
		var optType = 'update';
		if(validateJob(optType)){
			alert("ok");
			$.ajax({
				type:"POST",
				url:"servlet/cloverServletHandle.do?action=handleJobAdd&r="+Math.random(),
				data:{
					"jobGroup":$.trim($("#"+optType+"JobGroup").val()),
					"jobName":$.trim($("#"+optType+"JobName").val()),
					"jobType":$.trim($("#"+optType+"JobType").val()),
					"executeType":$.trim($("#"+optType+"ExecuteType").val()),
					"jobClassName":$.trim($("#"+optType+"JobClassName").val()),
					"startTime":$.trim($("#"+optType+"StartTime").val()),
					"cronExpression":$.trim($("#"+optType+"CronExpression").val()),
					"ip":$.trim($("#"+optType+"Ip").val()),
					"port":$.trim($("#"+optType+"Port").val()),
					"fixedClientIps":$.trim($("#"+optType+"FixedClientIps").val()),
					"fixedServerIps":$.trim($("#"+optType+"FixedServerIps").val())
				},
				success:function (data){
					if(1==data){
						clearJob('add');
						window.location.reload();
					} else {
						alert("系统出现异常，请联系管理员");
						return;
					}
				}
			});
		}

	}

	function updateJob() {
		var optType = 'update';
		if(validateJob(optType)){
			$.ajax({
				type:"POST",
				url:"servlet/cloverServletHandle.do?action=handleJobUpdate&r="+Math.random(),
				data:{
					"jobGroup":$.trim($("#"+optType+"JobGroup").val()),
					"jobName":$.trim($("#"+optType+"JobName").val()),
					"jobType":$.trim($("#"+optType+"JobType").val()),
					"executeType":$.trim($("#"+optType+"ExecuteType").val()),
					"jobClassName":$.trim($("#"+optType+"JobClassName").val()),
					"startTime":$.trim($("#"+optType+"StartTime").val()),
					"cronExpression":$.trim($("#"+optType+"CronExpression").val()),
					"ip":$.trim($("#"+optType+"Ip").val()),
					"port":$.trim($("#"+optType+"Port").val()),
					"fixedClientIps":$.trim($("#"+optType+"FixedClientIps").val()),
					"fixedServerIps":$.trim($("#"+optType+"FixedServerIps").val())
				},
				success:function (data){
					if(1==data){
						clearJob('add');
						window.location.reload();
					} else {
						alert("系统出现异常，请联系管理员");
						return;
					}
				}
			});
		}
	}

	function deleteJobData() {
		$.ajax({
			type: "POST",
			url: "servlet/cloverServletHandle.do?action=handleJobDataDelete&r=" + Math.random(),
			data: {
				"id": $.trim($("#updateId").val())
			},
			success: function (data) {
				if (1 == data) {
					window.location.reload();
				} else {
					alert("系统出现异常，请联系管理员");
					return;
				}
			}
		});
	}
	function deleteJob(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleJobDelete&r="+Math.random(),
			data:{
				"id":$.trim($("#updateId").val())
			},
			success:function (data){
				if(1==data){
					window.location.reload();
				} else {
					alert("系统出现异常，请联系管理员");
					return;
				}
			}
		});
	}

</script>