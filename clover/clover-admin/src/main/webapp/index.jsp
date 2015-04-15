<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path;
%>
<!DOCTYPE html>
<html>
 <jsp:include page="/common/head.jsp"/>
 <script src="<%=basePath%>/js/highcharts.js"></script>
 <script src="<%=basePath%>/js/exporting.js"></script>
 <script src="<%=basePath%>/js/Map.js"></script>
  <body>
 	<jsp:include page="/common/nav.jsp"/>
    <div class="container-fluid" >
        <div id="container" style="min-width:700px;height:400px"></div>
    </div>
  </body>

<script type="text/javascript">
    $(function () {
        $('#container').highcharts({
            title: {
                text: 'Job Monitor',
                x: -20 //center
            },
            subtitle: {
                text: 'Source From:Clover',
                x: -20
            },
            xAxis: {
                categories: ['2014-11-24 16:13', '2014-11-24 16:14', '2014-11-24 16:15', '2014-11-24 16:16', '2014-11-24 16:17', '2014-11-24 16:18','2014-11-24 16:19', '2014-11-24 16:20', '2014-11-24 16:21', '2014-11-24 16:21', '2014-11-24 16:13', '2014-11-24 16:13']
            },
            yAxis: {
                title: {
                    text: 'QPS'
                },
                plotLines: [{
                    value: 0,
                    width: 1,
                    color: '#808080'
                }]
            },
            tooltip: {
                valueSuffix: '次'
            },
            legend: {
                layout: 'vertical',
                align: 'right',
                verticalAlign: 'middle',
                borderWidth: 0
            },
            series: [{
                name: 'Job1',
                data: [70, 6, 9, 14, 18, 21, 25, 26, 23, 18, 13, 9]
            }, {
                name: 'Job2',
                data: [2, 8, 5, 11, 17, 22, 24, 24, 20, 14, 60, 50]
            }, {
                name: 'Job3',
                data: [90, 60, 30, 80, 13, 17, 18, 17, 14, 90, 30, 10]
            }, {
                name: 'Job4',
                data: [3, 40, 50, 80, 11, 15, 17, 16, 14, 10, 60, 80]
            }]
        });
    });

</script>
</html>
