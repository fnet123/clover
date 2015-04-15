  <%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
  <%
      String path = request.getContextPath();
      String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
  %>
  <head>
    <title>clover manager system</title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/bootstrap.min.css" />
    <link href="<%=basePath%>/css/bootstrap-responsive.min.css" rel="stylesheet" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>/css/style.css" />
    <!--[if lt IE 9]>
      <script type="text/javascript" src="<%=basePath%>/js/html5.js"></script>
    <![endif]-->
    <script type="text/javascript" src="<%=basePath%>/js/jquery-1.7.1.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/bootstrap.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/jquery.validate.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/bootstrap-tooltip.js"></script>
    <script type="text/javascript" src="<%=basePath%>/js/bootstrap-popover.js"></script>
  </head>