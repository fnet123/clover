<%@ page import="com.gome.clover.common.tools.CommonConstants" %>
<%@ page import="com.gome.clover.common.tools.IpUtil" %>
<%@ page import="com.gome.clover.common.tools.PropertiesUtil" %>
<%@ page import="com.gome.clover.common.tools.StringUtil" %>
<%@ page import="com.gome.clover.common.zk.ZKManager" %>
<%@ page import="com.mongodb.BasicDBObject" %>
<%@ page import="com.mongodb.util.JSON" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

	ZKManager zkManager = new ZKManager(PropertiesUtil.loadProperties());
	String serverDataStr = zkManager.getData(CommonConstants.ZK_ROOT_PATH + "/monitor/server/"+ IpUtil.getLocalIP());
	String cloverServerStatus = "0";
	String cloverServerTS = "";
	String serverHeartBeatStatus = "0";
	String serverHeartBeatTS = "";
	String monitorHeartBeatStatus = "0";
	String monitorHeartBeatTS = "";
	if(!StringUtil.isEmpty(serverDataStr)){
		BasicDBObject serverData = (BasicDBObject) JSON.parse(serverDataStr);
		if(serverData.containsField("cloverServerStatus")){
			cloverServerStatus = (String) serverData.get("cloverServerStatus");
		}
		if(serverData.containsField("cloverServerTS")){
			cloverServerTS = (String) serverData.get("cloverServerTS");
		}

		if(serverData.containsField("serverHeartBeatStatus")){
			serverHeartBeatStatus = (String) serverData.get("serverHeartBeatStatus");
		}
		if(serverData.containsField("serverHeartBeatTS")){
			serverHeartBeatTS = (String) serverData.get("serverHeartBeatTS");
		}

		if(serverData.containsField("monitorHeartBeatStatus")){
			monitorHeartBeatStatus = (String) serverData.get("monitorHeartBeatStatus");
		}
		if(serverData.containsField("monitorHeartBeatTS")){
			monitorHeartBeatTS = (String) serverData.get("monitorHeartBeatTS");
		}
	}
	zkManager.close();

%>
<!DOCTYPE html>
<html>
 <jsp:include page="/common/head.jsp"/>
  <body>
 	<jsp:include page="/common/nav.jsp"/>
	<div class="container-fluid">
		<table class="table table-bordered table-striped">
			<thead>
			<tr>
				<th style="width: 10%; text-align: center;" nowrap>Service Name</th>
				<th style="width: 10%; text-align: center;" nowrap>Service Status</th>
				<th style="width: 20%; text-align: center;" nowrap>ts</th>
				<th style="width: 50%; text-align: center;" nowrap>comment</th>
				<th style="width: 10%;text-align: center;" nowrap>操作</th>
			</tr>
			</thead>
			<tbody>
            <tr>
                <td style="text-align: center;" nowrap>
                    Clover Server
                </td>
                <td style="text-align: center;" nowrap>
					<%
						if("0".equals(cloverServerStatus)){
					%>
							未启动
					<%
						} else if("1".equals(cloverServerStatus)){
					%>
							已启动
					<%
						}else {
					%>
							 未启动
					<%
						}
					%>
                </td>
				<td style="text-align: center;" nowrap>
					<%=cloverServerTS%>
				</td>

				<td style="text-align: center;" nowrap>
					启动 Clover Server
				</td>

                <td style="text-align: center;" nowrap>
					<%
						if("0".equals(cloverServerStatus)){
					%>
					<a data-toggle="modal" onclick="toStartupCloverServer()" href="#"><i class="icon-edit"></i>启动</a>
					<%
					} else if("1".equals(cloverServerStatus)){
					%>
					<a data-toggle="modal" onclick="toStopCloverServer()" href="#"><i class="icon-edit"></i>停止</a>
					<%
					}else {
					%>
					<a data-toggle="modal" onclick="toStartupCloverServer()" href="#"><i class="icon-edit"></i>启动</a>
					<%
						}
					%>
                </td>
            </tr>
			<tr>
				<td style="text-align: center;" nowrap>
					Server Heart Beat
				</td>
				<td style="text-align: center;" nowrap>
					<%
						if("0".equals(serverHeartBeatStatus)){
					%>
						未启动
					<%
					} else if("1".equals(serverHeartBeatStatus)){
					%>
						已启动
					<%
					}else {
					%>
						未启动
					<%
						}
					%>
				</td>

				<td style="text-align: center;" nowrap>
					<%=serverHeartBeatTS%>
				</td>

				<td style="text-align: center;" nowrap>
					启动 Server Heart Beat，每隔指定（默认：2分钟）时间发送内存、cpu、活跃线程数等信息到ZK
				</td>

				<td style="text-align: center;" nowrap>
					<%
						if("0".equals(serverHeartBeatStatus)){
					%>
					<a data-toggle="modal" onclick="toStartupServerHeartBeat()" href="#"><i class="icon-edit"></i>启动</a>
					<%
					} else if("1".equals(serverHeartBeatStatus)){
					%>
					<a data-toggle="modal" onclick="toStopServerHeartBeat()" href="#"><i class="icon-edit"></i>停止</a>
					<%
					}else {
					%>
					<a data-toggle="modal" onclick="toStartupServerHeartBeat()" href="#"><i class="icon-edit"></i>启动</a>
					<%
						}
					%>

				</td>
			</tr>

			<tr>
				<td style="text-align: center;" nowrap>
					Monitor Heart Beat
				</td>
				<td style="text-align: center;" nowrap>
					<%
						if("0".equals(monitorHeartBeatStatus)){
					%>
					未启动
					<%
					} else if("1".equals(monitorHeartBeatStatus)){
					%>
					已启动
					<%
					}else {
					%>
					未启动
					<%
						}
					%>
				</td>

				<td style="text-align: center;" nowrap>
					<%=monitorHeartBeatTS%>
				</td>

				<td style="text-align: center;" nowrap>
					启动 Monitor Heart Beat，监控所有server节点，当server中一台不可用，
					将会把不可用server中的所有job信息转移到另一台可用server中；当所有server不可用，那就报警通知相关server负责人
				</td>

				<td style="text-align: center;" nowrap>
					<%
						if("0".equals(monitorHeartBeatStatus)){
					%>
					<a data-toggle="modal" onclick="toStartupMonitorHeartBeat()" href="#"><i class="icon-edit"></i>启动</a>
					<%
					} else if("1".equals(monitorHeartBeatStatus)){
					%>
					<a data-toggle="modal" onclick="toStopMonitorHeartBeat()" href="#"><i class="icon-edit"></i>停止</a>
					<%
					}else {
					%>
					<a data-toggle="modal" onclick="toStartupMonitorHeartBeat()" href="#"><i class="icon-edit"></i>启动</a>
					<%
						}
					%>

				</td>
			</tr>
			</tbody>
		</table>
	</div>

    <!-- 启动Clover Server弹出层开始 -->
    <div id="startupCloverServerDiv" class="modal hide fade">
        <div class="modal-header">
            <a class="close" data-dismiss="modal" >&times;</a>
            <h3>Start Up Clover Server</h3>
        </div>
        <div class="modal-body">
            <p>Are you sure start up clover server? </p>
        </div>
        <div class="modal-footer">
            <center>
                <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                <button class="btn btn-success" onclick="startupCloverServer()">启动</button>
            </center>
        </div>
    </div>
    <!-- 启动Clover Server弹出层结束 -->
  </body>
 <!-- 停止Clover Server弹出层开始 -->
 <div id="stopCloverServerDiv" class="modal hide fade">
	 <div class="modal-header">
		 <a class="close" data-dismiss="modal" >&times;</a>
		 <h3>Stop Up Clover Server</h3>
	 </div>
	 <div class="modal-body">
		 <p>Are you sure stop up clover server? </p>
	 </div>
	 <div class="modal-footer">
		 <center>
			 <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			 <button class="btn btn-success" onclick="stopCloverServer()">停止</button>
		 </center>
	 </div>
 </div>
 <!-- 停止Clover Server弹出层结束 -->

 <!-- 启动Server Heart Beat弹出层开始 -->
 <div id="startupServerHeartBeatDiv" class="modal hide fade">
	 <div class="modal-header">
		 <a class="close" data-dismiss="modal" >&times;</a>
		 <h3>Start Up Server Heart Beat</h3>
	 </div>
	 <div class="modal-body">
		 <p>Are you sure start up server heart beat? </p>
	 </div>
	 <div class="modal-footer">
		 <center>
			 <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			 <button class="btn btn-success" onclick="startupServerHeartBeat()">启动</button>
		 </center>
	 </div>
 </div>
 <!-- 启动Server Heart Beat弹出层开始 -->

 <!-- 停止Server Heart Beat弹出层结束 -->
 <div id="stopServerHeartBeatDiv" class="modal hide fade">
	 <div class="modal-header">
		 <a class="close" data-dismiss="modal" >&times;</a>
		 <h3>Stop Up Server Heart Beat</h3>
	 </div>
	 <div class="modal-body">
		 <p>Are you sure stop up server heart beat? </p>
	 </div>
	 <div class="modal-footer">
		 <center>
			 <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			 <button class="btn btn-success" onclick="stopServerHeartBeat()">停止</button>
		 </center>
	 </div>
 </div>
 <!-- 停止Server Heart Beat弹出层结束 -->


 <!-- 启动Monitor Heart Beat弹出层开始 -->
 <div id="startupMonitorHeartBeatDiv" class="modal hide fade">
	 <div class="modal-header">
		 <a class="close" data-dismiss="modal" >&times;</a>
		 <h3>Start Up Monitor Heart Beat</h3>
	 </div>
	 <div class="modal-body">
		 <p>Are you sure start up monitor heart beat? </p>
	 </div>
	 <div class="modal-footer">
		 <center>
			 <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			 <button class="btn btn-success" onclick="startupMonitorHeartBeat()">启动</button>
		 </center>
	 </div>
 </div>
 <!-- 启动Monitor Heart Beat弹出层开始 -->

 <!-- 停止 Monitor Heart Beat弹出层结束 -->
 <div id="stopMonitorHeartBeatDiv" class="modal hide fade">
	 <div class="modal-header">
		 <a class="close" data-dismiss="modal" >&times;</a>
		 <h3>Stop Up Monitor Heart Beat</h3>
	 </div>
	 <div class="modal-body">
		 <p>Are you sure stop up monitor heart beat? </p>
	 </div>
	 <div class="modal-footer">
		 <center>
			 <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
			 <button class="btn btn-success" onclick="stopMonitorHeartBeat()">停止</button>
		 </center>
	 </div>
 </div>
 <!-- 停止 Monitor Heart Beat弹出层结束 -->

 </body>
</html>



<script type="text/javascript">

	function toStartupCloverServer(){
		$("#startupCloverServerDiv").modal({backdrop: 'static', keyboard: false});
	}
	function toStopCloverServer(){
		$("#stopCloverServerDiv").modal({backdrop: 'static', keyboard: false});
	}

	function toStartupServerHeartBeat(){
		$("#startupServerHeartBeatDiv").modal({backdrop: 'static', keyboard: false});
	}
	function toStopServerHeartBeat(){
		$("#stopServerHeartBeatDiv").modal({backdrop: 'static', keyboard: false});
	}

	function toStartupMonitorHeartBeat(){
		$("#startupMonitorHeartBeatDiv").modal({backdrop: 'static', keyboard: false});
	}
	function toStopMonitorHeartBeat(){
		$("#stopMonitorHeartBeatDiv").modal({backdrop: 'static', keyboard: false});
	}

	function startupCloverServer(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleStartupCloverServer&r="+Math.random(),
			data:{},
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
	function stopCloverServer(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleStopCloverServer&r="+Math.random(),
			data:{},
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

	function startupServerHeartBeat(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleStartupServerHeartBeat&r="+Math.random(),
			data:{},
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
	function stopServerHeartBeat(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleStopServerHeartBeat&r="+Math.random(),
			data:{},
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


	function startupMonitorHeartBeat(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleStartupMonitorHeartBeat&r="+Math.random(),
			data:{},
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
	function stopMonitorHeartBeat(){
		$.ajax({
			type:"POST",
			url:"servlet/cloverServletHandle.do?action=handleStopMonitorHeartBeat&r="+Math.random(),
			data:{},
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