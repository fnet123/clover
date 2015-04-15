<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%@ page import="com.gome.clover.common.tools.CommonConstants" %>
<%@ page import="com.gome.clover.common.tools.StringUtil" %>
<%@ page import="com.gome.clover.common.zk.ZKUtil" %>
<%@ page import="com.mongodb.BasicDBObject" %>
<%@ page import="org.apache.curator.framework.CuratorFramework" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="java.util.List" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path;

    String qDest = request.getParameter("qDest");
    String qHostName = request.getParameter("qHostName");
    String qIp = request.getParameter("qIp");
    String qJobClass = request.getParameter("qJobClass");
    String qPort = request.getParameter("qPort");
    CuratorFramework curatorFramework = ZKUtil.create();
    if (!curatorFramework.isStarted()) curatorFramework.start();

    String serverPathStr = CommonConstants.ZK_ROOT_PATH + "/" + CommonConstants.MODULE_TYPE_SERVER;
    String clientPathStr = CommonConstants.ZK_ROOT_PATH + "/" + CommonConstants.MODULE_TYPE_CLIENT;

    List serverNodeList = null;
    List clientNodeList = null;

    if ("1".equals(qDest)) {
        serverNodeList = ZKUtil.getChilds(curatorFramework, serverPathStr);
    } else if ("2".equals(qDest)) {
        clientNodeList = ZKUtil.getChilds(curatorFramework, clientPathStr);
    } else {
        serverNodeList = ZKUtil.getChilds(curatorFramework, serverPathStr);
        clientNodeList = ZKUtil.getChilds(curatorFramework, clientPathStr);
    }

    List<BasicDBObject> filterServerDataList = null;
    if (null != serverNodeList && serverNodeList.size() > 0) {
        filterServerDataList = new ArrayList<BasicDBObject>();
        for (int i = 0; (serverNodeList != null) && (i < serverNodeList.size()); i++) {
            String id = (String) serverNodeList.get(i);
            String c = ZKUtil.getData(curatorFramework, serverPathStr + "/" + id);
            if (c == null) {
                continue;
            }
            BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
            String tempId = (String) record.get("id");
            String tempPort = (String) record.get("port");
            String tempIp = (String) record.get("ip");
            if (!StringUtil.isEmpty(qHostName) && !StringUtil.isEmpty(qIp) &&
                    !StringUtil.isEmpty(qPort)) {
                if (tempId.contains(qHostName) && tempIp.equals(qIp) && tempPort.equals(qPort)) {
                    filterServerDataList.add(record);
                }
            } else if (!StringUtil.isEmpty(qHostName) && !StringUtil.isEmpty(qIp) &&
                    StringUtil.isEmpty(qPort)) {
                if (tempId.contains(qHostName) && tempIp.equals(qIp)) {
                    filterServerDataList.add(record);
                }
            } else if (!StringUtil.isEmpty(qHostName) && StringUtil.isEmpty(qIp) &&
                    StringUtil.isEmpty(qPort)) {
                if (tempId.contains(qHostName)) {
                    filterServerDataList.add(record);
                }
            } else {
                filterServerDataList.add(record);
            }
        }
    }

    List<BasicDBObject> filterClientDataList = null;
    if (null != clientNodeList && clientNodeList.size() > 0) {
        filterClientDataList = new ArrayList<BasicDBObject>();
        for (int i = 0; (clientNodeList != null) && (i < clientNodeList.size()); i++) {
            String id = (String) clientNodeList.get(i);
            String c = ZKUtil.getData(curatorFramework, clientPathStr + "/" + id);
            if (c == null) {
                continue;
            }
            BasicDBObject record = (BasicDBObject) com.mongodb.util.JSON.parse(c);
            String tempId = (String) record.get("id");
            String tempPort = (String) record.get("port");
            String tempIp = (String) record.get("ip");
            List<String> jobClassList = (List<String>) record.get("jobClass");
            if (!StringUtil.isEmpty(qHostName) && !StringUtil.isEmpty(tempIp) && !StringUtil.isEmpty(tempPort)
                    && (null != jobClassList && jobClassList.size() > 0)) {
                if (tempId.contains(qHostName) && tempIp.equals(qIp) && tempPort.equals(qPort) && jobClassList.contains(qJobClass)) {
                    filterClientDataList.add(record);
                }
            } else if (!StringUtil.isEmpty(tempId) && !StringUtil.isEmpty(tempIp)&& !StringUtil.isEmpty(tempPort)
                    && (null == jobClassList || jobClassList.size() == 0)) {
                if (tempId.contains(qHostName) && tempIp.equals(qIp) && tempPort.equals(qPort)) {
                    filterClientDataList.add(record);
                }
            } else if (!StringUtil.isEmpty(tempId) && StringUtil.isEmpty(tempIp) && StringUtil.isEmpty(tempPort)
                    && (null == jobClassList || jobClassList.size() == 0)) {
                if (tempId.contains(qHostName) && tempIp.equals(qIp)) {
                    filterClientDataList.add(record);
                }
            } else if (!StringUtil.isEmpty(tempId) && StringUtil.isEmpty(tempIp) && StringUtil.isEmpty(tempPort)
                    && (null == jobClassList || jobClassList.size() == 0)) {
                if (tempId.contains(qHostName)) {
                    filterClientDataList.add(record);
                }
            } else {
                filterClientDataList.add(record);
            }
        }
    }
    curatorFramework.close();
%>
<!DOCTYPE html>
<html>
<jsp:include page="/common/head.jsp"/>
<body>
<jsp:include page="/common/nav.jsp"/>
<div class="container-fluid">

    <table class="table table-bordered ">
        <tbody>
        <form action="<%=basePath%>/zkList.jsp" method="post">
            <tr>
                <td >
                    <select id="qDest" name="qDest">
                        <%
                            if ("1".equals(qDest)) {
                        %>
                        <option value="">请选择查询目标</option>
                        <option value="1" selected>server</option>
                        <option value="2">client</option>
                        <%
                        } else if ("2".equals(qDest)) {
                        %>
                        <option value="">请选择查询目标</option>
                        <option value="1">server</option>
                        <option value="2" selected>client</option>
                        <%
                        } else {
                        %>
                        <option value="">请选择查询目标</option>
                        <option value="1">server</option>
                        <option value="2">client</option>
                        <%
                            }
                        %>
                    </select>
                </td>
                <th>Host Name:</th>
                <td>
                    <input type="text" id="qHostName" name="qHostName" class="form-inline span2" placeholder="Host Name"
                           value="<%=null!=qHostName?qHostName:""%>"/>
                </td>
                <th>ip:</th>
                <td>
                    <input type="text" id="qIp" name="qIp" placeholder="ip" value="<%=null!=qIp?qIp:""%>"/>
                </td>
                <th>Job Class:</th>
                <td>
                    <input type="text" id="qJobClass" name="qJobClass" placeholder="Job Class"
                           value="<%=null!=qJobClass?qJobClass:""%>"/>
                </td>
                <th>port:</th>
                <td>
                    <input type="text" id="qPort" name="qPort" placeholder="port" value="<%=null!=qPort?qPort:""%>"/>
                </td>

            </tr>
            <tr>
                <td colspan="9">
                    <center>
                        <button class="btn btn-success" type="submit">查询</button>
                        <button class="btn" onclick="clearQuery()">清空</button>
                        <%--<a style="float: right" id="startupZeroMqBtn" class="btn btn-success" data-toggle="modal"
                           onclick="startupZeroMq()" href="#">
                            启动zeromq</a>--%>
                    </center>
                </td>
            </tr>
        </form>
        </tbody>
    </table>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th style="width: 20%; text-align: center;" nowrap>Server Name</th>
            <th style="width: 10%; text-align: center;" nowrap>ip</th>
            <th style="width: 10%; text-align: center;" nowrap>port</th>
            <th style="width: 10%; text-align: center;" nowrap>ts</th>
            <th style="width: 10%; text-align: center;" nowrap>Mem Ratio</th>
            <th style="width: 10%; text-align: center;" nowrap>Cpu Ratio</th>
            <th style="width: 10%; text-align: center;" nowrap>Total Thread</th>
            <%-- <th style="width: 20%;text-align: center;" nowrap>操作</th>--%>
        </tr>
        </thead>
        <tbody>
        <%
            if (null != filterServerDataList && filterServerDataList.size() > 0) {

                for (BasicDBObject tempServerData : filterServerDataList) {
        %>
        <tr>
            <td style="text-align: center;" nowrap>
                <%=(String) tempServerData.get("id")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=tempServerData.get("ip")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=tempServerData.get("port")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=tempServerData.get("ts")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=tempServerData.get("memRatio")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=tempServerData.get("cpuRatio")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=tempServerData.get("totalThread")%>
            </td>
            <%--  <td style="text-align: center;" nowrap>
                  <a data-toggle="modal" onclick="toUpdateServer('<%=tempFilterServerDataStrs[0]%>')"
                     href="#">
                      <i class="icon-edit"></i>更新
                  </a>
                  <a data-toggle="modal" onclick="toDeleteServer('<%=tempFilterServerDataStrs[0]%>')"
                     href="#">
                      <i class="icon-remove"></i>删除
                  </a>
              </td>--%>
        </tr>
        <%
            }
        } else {
        %>
        <tr>
            <td colspan="7" style="text-align: center;" nowrap>暂无相关记录</td>
        </tr>
        <%
            }
        %>
        </tbody>
    </table>

    <table class="table table-bordered table-striped">
        <thead>
        <tr>
            <th style="width: 20%; text-align: center;" nowrap>Client Name</th>
            <th style="width: 30%; text-align: center;" nowrap>Job Class</th>
            <th style="width: 10%; text-align: center;" nowrap>ip</th>
            <th style="width: 5%; text-align: center;" nowrap>port</th>
            <th style="width: 10%; text-align: center;" nowrap>ts</th>
            <th style="width: 10%; text-align: center;" nowrap>Mem Ratio</th>
            <th style="width: 10%; text-align: center;" nowrap>Cpu Ratio</th>
            <th style="width: 5%; text-align: center;" nowrap>Total Thread</th>
            <%-- <th style="width: 20%;text-align: center;" nowrap>操作</th>--%>
        </tr>
        </thead>
        <tbody>
        <%
            if (null != filterClientDataList && filterClientDataList.size() > 0) {
                for (BasicDBObject tempClientData : filterClientDataList) {
        %>
        <tr>
            <td style="text-align: center;" nowrap>
                <%=(String) tempClientData.get("id")%>
            </td>

            <td style="text-align: center;" nowrap>
              <a href="#" data-toggle="tooltip" data-placement="bottom"
                 title='<%=tempClientData.get("jobClass")%>'>
                    <%=(tempClientData.get("jobClass")).toString().substring(0, 80)%></a>
            </td>

            <td style="text-align: center;" nowrap>
                <%=(String) tempClientData.get("ip")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=(String) tempClientData.get("port")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%=(String) tempClientData.get("ts")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%= tempClientData.get("memRatio")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%= tempClientData.get("cpuRatio")%>
            </td>
            <td style="text-align: center;" nowrap>
                <%= tempClientData.get("totalThread")%>
            </td>
            <%-- <td style="text-align: center;" nowrap>
                 <a data-toggle="modal" onclick="toUpdateClient('<%=clientMap.get(DBTableInfo.COL_IP)%>')"
                    href="#">
                     <i class="icon-edit"></i>更新
                 </a>
                 <a data-toggle="modal" onclick="toDeleteClient('<%=clientMap.get(DBTableInfo.COL_IP)%>')"
                    href="#">
                     <i class="icon-remove"></i>删除
                 </a>
             </td>--%>
        </tr>
        <%
            }
        } else {
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

</body>
</html>

<script type="text/javascript">
    var isClickZeroMq = false;
    function clearQuery() {
        $("#qDest").val("");
        $("#qIp").val("");
        $("#qAlive").val("");
    }

    function toUpdateServer(ip) {
        $.ajax({
            type: "POST",
            url: "servlet/cloverServletHandle.do?action=handleUpdateZkServerIp&ip=" + ip,
            data: {
                "ip": ip
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
    function toDeleteServer(ip) {
        $.ajax({
            type: "POST",
            url: "servlet/cloverServletHandle.do?action=handleDeleteZkServerIp&ip=" + ip,
            data: {
                "ip": ip
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
    function toUpdateClient(ip) {
        $.ajax({
            type: "POST",
            url: "servlet/cloverServletHandle.do?action=handleUpdateZkClientIp&ip=" + ip,
            data: {
                "ip": ip
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
    function toDeleteClient(ip) {
        $.ajax({
            type: "POST",
            url: "servlet/cloverServletHandle.do?action=handleDeleteZkClientIp&ip=" + ip,
            data: {
                "ip": ip
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

    function startupZeroMq() {
        if (!isClickZeroMq) {
            var url = "servlet/cloverServletHandle.do?action=startupZeroMq";
            $.get(url, null, function (data) {
                if (null == data || data.length == 0) {
                    alert("系统出现异常，请联系管理员");
                    return;
                }
            });
            $('#startupZeroMqBtn').removeClass("btn-success");
            $("#startupZeroMqBtn").html("zeromq已启动");
            $("#startupZeroMqBtn").disabled();
            isClickZeroMq = true;
        }
    }
</script>