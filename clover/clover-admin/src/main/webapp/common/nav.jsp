<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<div class="navbar navbar-fixed-top">
    <div class="navbar-inner">
        <div class="container-fluid"><a class="btn btn-navbar"
                                        data-toggle="collapse" data-target=".nav-collapse"> <span
                class="icon-bar"></span> <span class="icon-bar"></span> <span
                class="icon-bar"></span> </a> <a class="brand" href="<%=basePath%>/index.jsp">clover manager system</a>

            <div class="nav-collapse">
                <ul class="nav navbar-nav">
                    <li><a href="<%=basePath%>/zkList.jsp">zkmgr</a></li>
                    <li><a href="<%=basePath%>/jobList.jsp">jobmgr</a></li>
                    <li><a href="<%=basePath%>/contactList.jsp">contactmgr</a></li>
                    <li><a href="<%=basePath%>/logList.jsp">logmgr</a></li>
                    <li><a href="<%=basePath%>/monitorList.jsp">monitor</a></li>
                   <%-- <li class="dropdown active">
                        <a href=""
                           class="dropdown-toggle" data-toggle="dropdown">monitor<b class="caret"></b></a>
                        <ul class="dropdown-menu">
                            <li><a href="servlet/cloverServletHandle.do?action=startupMonitorHeartBeat">startupMonitorHeartBeat</a></li>
                            <li><a href="">stopMonitorHeartBeat</a></li>
                        </ul>
                    </li>--%>
                </ul>
            </div>
        </div>
    </div>
    </div>
