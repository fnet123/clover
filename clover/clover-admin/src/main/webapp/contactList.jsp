<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="com.gome.clover.common.mongodb.DBTableInfo" %>
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
	DBCursor dbCursor  = MongoDBUtil.INSTANCE.findByCondition(condition,DBTableInfo.TBL_CLOVER_CONTACT);
%>
<!DOCTYPE html>
<html>
 <jsp:include page="/common/head.jsp"/>
  <body>
 	<jsp:include page="/common/nav.jsp"/>
	<div class="container-fluid">
		<table class="table table-bordered">
			<tbody>
			<form action="<%=basePath%>contactList.jsp" method="post">
				<tr>
					<th>System Id:</th>
					<td>
						<input  type="text" name="qSystemId" placeholder="System Id" value="<%=null!=qSystemId?qSystemId:""%>">
					</td>
					<th>ip:</th>
					<td>
						<input  type="text" name="qIp" placeholder="ip" value="<%=null!=qIp?qIp:""%>">
					</td>
					<th>Job Key:</th>
					<td>
						<input  type="text" name="qJobKey" placeholder="Job Key" value="<%=null!=qJobKey?qJobKey:""%>">
					</td>
					<th>Exec Method:</th>
					<td>
					<input  type="text" name="qExecMethod" placeholder="Exec Method" value="<%=null!=qExecMethod?qExecMethod:""%>">
					</td>
				</tr>
				<tr>
					<td colspan="8">
						<center>
							<button class="btn btn-success" type="submit">查询</button>
							<button class="btn" onclick="clearQuery()">清空</button>
                            <a class="btn btn-primary" data-toggle="modal" onclick="toAddContact()" href="#">
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
				<th style="width: 10%; text-align: center;" nowrap>systemId</th>
				<th style="width: 10%; text-align: center;" nowrap>ip</th>
				<th style="width: 10%; text-align: center;" nowrap>jobKey</th>
				<th style="width: 20%; text-align: center;" nowrap>contacter</th>
				<th style="width: 10%; text-align: center;" nowrap>email</th>
				<th style="width: 10%; text-align: center;" nowrap>mobile</th>
				<th style="width: 10%; text-align: center;" nowrap>ts</th>
				<th style="width: 10%;text-align: center;" nowrap>操作</th>
			</tr>
			</thead>
			<tbody>
            <%
                if(null!=dbCursor && dbCursor.size()>0){
                    while (dbCursor.hasNext()) {
                        DBObject tempDBObject = dbCursor.next();
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
                    <%=tempDBObject.get(DBTableInfo.COL_CONTACTER)%>
                </td>
                <td style="text-align: center;" nowrap>
                    <%=tempDBObject.get(DBTableInfo.COL_EMAIL)%>
                </td>
                <td style="text-align: center;" nowrap>
                    <%=tempDBObject.get(DBTableInfo.COL_MOBILE)%>
                </td>
                <td style="text-align: center;" nowrap>
                    <%=tempDBObject.get(DBTableInfo.COL_TS)%>
                </td>
                <td style="text-align: center;" nowrap>
                    <a data-toggle="modal" onclick="toUpdateContact('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')" href="#">
                        <i class="icon-edit"></i>修改</a>
                    <a data-toggle="modal" onclick="toDeleteContact('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')" href="#">
                        <i class="icon-remove"></i>删除</a>
                    <a data-toggle="modal" onclick="toDetailContact('<%=tempDBObject.get(DBTableInfo.COL_ID).toString() %>')" href="#">
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

	<!-- 新建弹出层开始 -->
	<div id="addDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>联系人新建</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tr>
					<th>System ID:</th>
					<td>
						<input class="form-inline span3" type="text" id="addSystemId" name="addSystemId"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>IP:</th>
					<td>
						<input class="form-inline span3" type="text" id="addIp"name="addIp" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Key:</th>
					<td>
						<input class="form-inline span3" type="text" id="addJobKey"name="addJobKey"/>
						<font color="red">* 格式:jobGroup.jobName</font>
					</td>
				</tr>

				<tr>
					<th>contacter:</th>
					<td>
						<input class="form-inline span3" type="text" id="addContacter" name="addContacter"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>email:</th>
					<td>
						<input class="form-inline span3" type="text" id="addEmail"name="addEmail" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>mobile:</th>
					<td>
						<input class="form-inline span3" type="text" id="addMobile" name="addMobile"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<center>
							<button class="btn btn-success" onclick="addContact()" >提交</button>
							<button class="btn" data-dismiss="modal">关闭</button>
						</center>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!--  新建弹出层结束 -->

	<!-- 修改弹出层开始 -->
	<div id="updateDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>联系人更新</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tr>
					<th>System ID:</th>
					<td>
						<input  type="hidden" id="updateId" name="updateId"/>
						<input class="form-inline span3" type="text" id="updateSystemId" name="updateSystemId"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>IP:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateIp"name="updateIp" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Key:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateJobKey"name="updateJobKey"/>
						<font color="red">*<%--格式:jobGroup.jobName--%></font>
					</td>
				</tr>

				<tr>
					<th>contacter:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateContacter" name="updateContacter"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>email:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateEmail"name="updateEmail" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>mobile:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateMobile" name="updateMobile"/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>TS:</th>
					<td>
						<input class="form-inline span3" type="text" id="updateTs"name="updateTs" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<td colspan="2">
						<center>
							<button class="btn btn-success" onclick="updateContact()" >提交</button>
							<button class="btn" data-dismiss="modal">关闭</button>
						</center>
					</td>
				</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 更新弹出层结束 -->


	<!-- 详情弹出层开始 -->
	<div id="detailDiv" class="modal hide fade">
		<div class="modal-header">
			<a class="close" data-dismiss="modal" >&times;</a>
			<h3>联系人详细</h3>
		</div>
		<div class="modal-body">
			<table class="table table-bordered table-hover">
				<tbody>
				<tr>
					<th>System ID:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailSystemId" name="detailSystemId"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>IP:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailIp"name="detailIp" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>Job Key:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailJobKey"name="detailJobKey"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>contacter:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailContacter" name="detailContacter"/>
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>email:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailEmail"name="detailEmail" />
						<font color="red">*</font>
					</td>
				</tr>

				<tr>
					<th>mobile:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailMobile" name="detailMobile"/>
						<font color="red">*</font>
					</td>
				</tr>
				<tr>
					<th>TS:</th>
					<td>
						<input class="form-inline span3" type="text" id="detailTs"name="detailTs" />
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
	<!-- 详情弹出层结束 -->

    <!-- 删除弹出层开始 -->
    <div id="deleteDiv" class="modal hide fade">
        <div class="modal-header">
            <a class="close" data-dismiss="modal" >&times;</a>
            <h3>联系人删除</h3>
        </div>
        <div class="modal-body">
            <p>您确定删除该联系人</p>
        </div>
        <div class="modal-footer">
            <center>
                <button class="btn" data-dismiss="modal" aria-hidden="true">关闭</button>
                <button class="btn btn-success" onclick="deleteContact()">删除</button>
            </center>
        </div>
    </div>
    <!-- 删除弹出层结束 -->
  </body>
</html>



<script type="text/javascript">

    function clearContact(optType){
        $("#"+optType+"SystemId").val("");
        $("#"+optType+"Ip").val("");
        $("#"+optType+"JobKey").val("");
        $("#"+optType+"Contacter").val("");
        $("#"+optType+"Email").val("");
        $("#"+optType+"Mobile").val("");
        $("#"+optType+"Ts").val("");
    }

    /*格式校验 开始*/
    function validateContact(optType){
        var systemId = $.trim($("#"+optType+"SystemId").val());
        if(systemId == "" || 0== systemId.length){
            alert("系统ID，请填写");
            return false;
        }
        var ip = $.trim( $("#"+optType+"Ip").val());
        if(ip == "" || 0== ip.length){
            alert("IP，请填写");
            return false;
        }
        var jobKey = $.trim( $("#"+optType+"JobKey").val());
        if(jobKey == "" || 0== jobKey.length){
            alert("Job Key，请填写");
            return false;
        }
        var contacter = $.trim( $("#"+optType+"Contacter").val());
        if(contacter == "" || 0== contacter.length){
            alert("联系人，请填写");
            return false;
        }
        var email = $.trim( $("#"+optType+"Email").val());
        if(email == "" || 0== email.length){
            alert("email，请填写");
            return false;
        }
        return true;
    }
    /*格式校验 结束*/

	function clearQuery() {
		$("#qSystemId").val("");
		$("#qJobKey").val("");
		$("#qIp").val("");
		$("#qExecMethod").val("");
	}

	function toAddContact(){
        clearContact("add");
		$("#addDiv").modal({backdrop: 'static', keyboard: false});
	}
	function addContact(){
        var optType = 'add';
        if(validateContact(optType)){
			$.ajax({
				type:"POST",
				url:"servlet/cloverServletHandle.do?action=handleContactAdd&r="+Math.random(),
				data:{
					"systemId":$.trim($("#"+optType+"SystemId").val()),
					"ip":$.trim($("#"+optType+"Ip").val()),
					"jobKey":$.trim($("#"+optType+"JobKey").val()),
					"contacter":$.trim($("#"+optType+"Contacter").val()),
					"email":$.trim($("#"+optType+"Email").val()),
					"mobile":$.trim($("#"+optType+"Mobile").val())
				},
				success:function (data){
					if(1==data){
						clearContact('add');
						window.location.reload();
					} else {
						alert("系统出现异常，请联系管理员");
						return;
					}
				}
			});
		}
	}

	function toUpdateContact(_id){
        var url="servlet/cloverServletHandle.do?action=handleContactDetail&id="+_id+"&r="+Math.random();
        $.get(url, null, function(data){
            if(null==data ||data.length==0){
                alert("系统出现异常，请联系管理员");
                return;
            }
            eval("var resultObj = "+data);

            $("#updateId").val(_id);
            $("#updateSystemId").val(resultObj.systemId);
            $("#updateIp").val(resultObj.ip);
            $("#updateJobKey").val(resultObj.jobKey);
            $("#updateContacter").val(resultObj.contacter);
            $("#updateEmail").val(resultObj.email);
            $("#updateMobile").val(resultObj.mobile);
            $("#updateTs").val(resultObj.ts);
            $("#updateDiv").modal({backdrop: 'static', keyboard: false});
        });
	}
	function updateContact(_id){
        var optType = 'update';
        if(validateContact(optType)){
			$.ajax({
				type:"POST",
				url:"servlet/cloverServletHandle.do?action=handleContactUpdate&r="+Math.random(),
				data:{
					"id":$.trim($("#updateId").val()),
					"systemId":$.trim($("#"+optType+"SystemId").val()),
					"ip":$.trim($("#"+optType+"Ip").val()),
					"jobKey":$.trim($("#"+optType+"JobKey").val()),
					"contacter":$.trim($("#"+optType+"Contacter").val()),
					"email":$.trim($("#"+optType+"Email").val()),
					"mobile":$.trim($("#"+optType+"Mobile").val())
				},
				success:function (data){
					if(1==data){
						clearContact('update');
						window.location.reload();
					} else {
						alert("系统出现异常，请联系管理员");
						return;
					}
				}
			});
		}
	}
	function toDetailContact(_id){
        var url="servlet/cloverServletHandle.do?action=handleContactDetail&id="+_id+"&r="+Math.random();
        $.get(url, null, function(data){
            if(null==data ||data.length==0){
                alert("系统出现异常，请联系管理员");
                return;
            }
            eval("var resultObj = "+data);
            $("#detailSystemId").val(resultObj.systemId);
            $("#detailIp").val(resultObj.ip);
            $("#detailJobKey").val(resultObj.jobKey);
            $("#detailContacter").val(resultObj.contacter);
            $("#detailEmail").val(resultObj.email);
            $("#detailMobile").val(resultObj.mobile);
            $("#detailTs").val(resultObj.ts);
            $("#detailDiv").modal({backdrop: 'static', keyboard: false});
        });
	}

    function toDeleteContact(_id){
        $("#updateId").val(_id);
        $("#deleteDiv").modal({backdrop: 'static', keyboard: false});
    }

    function deleteContact(){
        $.ajax({
            type:"POST",
            url:"servlet/cloverServletHandle.do?action=handleContactDelete&r="+Math.random(),
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