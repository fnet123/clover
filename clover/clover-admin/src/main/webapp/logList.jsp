<%@ page language="java" contentType="text/html; charset=UTF-8"
		 pageEncoding="UTF-8" %>
<%@page import="com.gome.clover.common.mongodb.DBTableInfo" %>
<%@ page import="com.gome.clover.common.mongodb.MongoDBUtil" %>
<%@ page import="com.gome.clover.common.tools.StringUtil" %>
<%@ page import="com.mongodb.BasicDBObject" %>
<%@ page import="com.mongodb.DBCursor" %>
<%@ page import="com.mongodb.DBObject" %>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	String qSystemId = request.getParameter("qSystemId");
	String qJobKey = request.getParameter("qJobKey");
	String qIp = request.getParameter("qIp");
	String qExecMethod = request.getParameter("qExecMethod");
	DBObject condition = new BasicDBObject();
	if (!StringUtil.isEmpty(qSystemId)) condition.put(DBTableInfo.COL_SYSTEM_ID, qSystemId);
	if (!StringUtil.isEmpty(qJobKey)) condition.put(DBTableInfo.COL_JOB_KEY, qJobKey);
	if (!StringUtil.isEmpty(qIp)) condition.put(DBTableInfo.COL_IP, qIp);
	if (!StringUtil.isEmpty(qExecMethod)) condition.put(DBTableInfo.COL_EXEC_METHOD, qExecMethod);
	DBCursor dbCursor;
	if(StringUtil.isEmpty(qSystemId) && StringUtil.isEmpty(qJobKey) && StringUtil.isEmpty(qIp) && StringUtil.isEmpty(qExecMethod)){
		dbCursor = null;
	}else {
		dbCursor = MongoDBUtil.INSTANCE.findByCondition(condition,DBTableInfo.TBL_CLOVER_LOG);
	}

%>
<!DOCTYPE html>
<html>
 <jsp:include page="/common/head.jsp"/>
  <body>
 	<jsp:include page="/common/nav.jsp"/>
	<div class="container-fluid">
	<table class="table table-bordered">
		<tbody>
		<form action="<%=basePath%>logList.jsp" method="post">
			<tr>
				<th>System Id:</th>
				<td>
					<input  type="text" id="qSystemId"name="qSystemId" placeholder="System Id" value="<%=null!=qSystemId?qSystemId:""%>">
				</td>
				<th>ip:</th>
				<td>
					<input  type="text" id="qIp"name="qIp" placeholder="ip" value="<%=null!=qIp?qIp:""%>">
				</td>
				<th>Job Key:</th>
				<td>
					<input  type="text" id="qJobKey" name="qJobKey" placeholder="Job Key" value="<%=null!=qJobKey?qJobKey:""%>">
				</td>
				<th>Exec Method:</th>
				<td>
				<input  type="text" id="qExecMethod" name="qExecMethod" placeholder="Exec Method" value="<%=null!=qExecMethod?qExecMethod:""%>">
				</td>
			</tr>
			<tr>
				<td colspan="8">
					<center>
						<button class="btn btn-success" type="submit">查询</button>
						<button class="btn" onclick="clearQuery()">清空</button>
					</center>
				</td>
			</tr>
		</form>
		</tbody>
	</table>

      <table class="table table-bordered table-striped">
      <thead>
       <tr>
		   <th style="width: 10%; text-align: center;" nowrap>SystemId</th>
		   <th style="width: 10%; text-align: center;" nowrap>ip</th>
		   <th style="width: 10%; text-align: center;" nowrap>JobKey</th>
           <th style="width: 20%; text-align: center;" nowrap>ExecMethod</th>
           <th style="width: 10%; text-align: center;" nowrap>ExecResult</th>
           <th style="width: 10%; text-align: center;" nowrap>ExecTime</th>
           <th style="width: 10%; text-align: center;" nowrap>ts</th>
          <th style="width: 10%;text-align: center;" nowrap>操作</th>
       </tr>
      </thead>
      <tbody>
	  <%
		  if(null!=dbCursor && dbCursor.size()>0){
			  int index =0;
			  while (dbCursor.hasNext()) {
                  DBObject tempDBObject = dbCursor.next();
				  System.err.println("index:"+(index++)+" tempDBObject "+tempDBObject);

	  %>
		  <tr>
			  <td style="text-align: center;" nowrap>
				  <%=tempDBObject.get(DBTableInfo.COL_SYSTEM_ID)%>
			  </td>
			  <td style="text-align: center;" nowrap>
				  <%=tempDBObject.get(DBTableInfo.COL_IP)%>
			  </td>
			  <td style="text-align: center;" nowrap>
				  <%=tempDBObject.get(DBTableInfo.COL_JOB_KEY)%>
			  </td>
			  <td style="text-align: center;" nowrap>
				  <a href="#"  data-toggle="tooltip" data-placement="bottom" title="<%=tempDBObject.get(DBTableInfo.COL_EXEC_METHOD)%>">
					  <%=(tempDBObject.get(DBTableInfo.COL_EXEC_METHOD)).toString().substring(0, 50)%></a>
			  </td>
			  <td style="text-align: center;" nowrap>
                  <a href="#" data-toggle="tooltip" data-placement="bottom" title="<%=tempDBObject.get(DBTableInfo.COL_EXEC_RESULT)%>">
                      <%=(tempDBObject.get(DBTableInfo.COL_EXEC_RESULT)).toString().substring(0,50)%></a>
			  </td>
			  <td style="text-align: center;" nowrap><%=tempDBObject.get(DBTableInfo.COL_EXEC_TIME)%></td>
			  <td style="text-align: center;" nowrap>
				  <%=tempDBObject.get(DBTableInfo.COL_TS)%>
			  </td>
			  <td style="text-align: center;" nowrap>
				  <a data-toggle="modal" onclick="toView('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')" href="#">
					  <i class="icon-eye-open"></i>查看</a>
			  </td>
		  </tr>
	  <%
		  }
	  }else {
	  %>
	  <tr>
		  <td colspan="8" style="text-align: center;" nowrap>暂无相关记录</td>
	  </tr>
	  <%
		  }
	  %>
         
      </tbody>
     </table>
    </div>
	<!-- 查询详情弹出层开始 -->
	<div id="detailDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>日志详细</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tr>
					<th>System ID:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailSystemId" name="detailSystemId" readonly/>
						<font color="red">*</font>
						</td>
				</tr>

				<tr>
					<th>IP:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailIp" name="detailIp"readonly />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Key:</th>
					<td>
						<input class="form-inline span3" type="text" name="detailJobKey"name="detailJobKey"readonly />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Exec Method:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailExecMethod" name="detailExecMethod" readonly/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Exec Result:</th>
					<td>
						<textarea id="detailExecResult" cols="5" rows="10" readonly></textarea>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Exec Time:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailExecTime" name="detailExecTime" readonly/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>TS:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailTs" name="detailTs" readonly/>
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
  </body>
</html>


<!-- 编辑弹出层结束 -->
<script type="text/javascript">
	function clearQuery() {
		$("#qSystemId").val("");
		$("#qJobKey").val("");
		$("#qIp").val("");
		$("#qExecMethod").val("");
	}

	function toView (_id){
        var url="servlet/cloverServletHandle.do?action=handleLogDetail&id="+_id+"&r="+Math.random();
        $.get(url, null, function(data){
            if(null==data ||data.length==0){
                alert("系统出现异常，请联系管理员");
                return;
            }
            eval("var resultObj = "+data);
            $("#detailSystemId").val(resultObj.systemId);
            $("#detailIp").val(resultObj.ip);
            $("#detailJobKey").val(resultObj.jobKey);
            $("#detailExecMethod").val(resultObj.execMethod);
            $("#detailExecResult").val(resultObj.execResult);
            $("#detailExecTime").val(resultObj.execTime);
            $("#detailTs").val(resultObj.ts);
            $("#detailDiv").modal({backdrop: 'static', keyboard: false});
        });
	}

</script>