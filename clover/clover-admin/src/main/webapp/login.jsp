<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <style type="text/css">
      body {
        padding-top: 60px;
        padding-bottom: 40px;
      }
      .sidebar-nav {
        padding: 9px 0;
      }
    </style>
      <link rel="stylesheet" type="text/css" href="css/bootstrap.min.css" />
      <link rel="stylesheet" type="text/css" href="css/bootstrap-responsive.min.css" />
      <link rel="stylesheet" type="text/css" href="css/style.css" />
      <!--[if lt IE 9]>
      <script type="text/javascript" src="/js/html5.js"></script>
      <![endif]-->
      <script type="text/javascript" src="js/jquery-1.7.1.min.js"></script>
      <script type="text/javascript" src="js/bootstrap.min.js"></script>
    <script>
        $(document).ready(function(){

     // <!--登录方法开始-->
          $("#employeeCode").blur(function(){
              var employeeCode= $("#employeeCode").val();
              if(employeeCode.length==0&&employeeCode==''){
                  $("#employeeCodeAlert").html("<font size='4' color='red'>×</font>");
                  $("#employeeCode").focus();
              }else{
                  $("#employeeCodeAlert").html("<font size='4' color='green'>√</font>");
          }
          });
           $("#employeeCode").click(function(){
               $("#employeeCodeAlert").html("");
           });


          $("#password").blur(function(){
              var password= $("#password").val();
              if(password.length==0&&password==''){
                  $("#passwordAlert").html("<font size='4' color='red'>×</font>");
              }else{
                  $("#passwordAlert").html("<font size='4' color='green'>√</font>");
              }
          });

          $("#password").click(function(){
              $("#passwordAlert").html("");
          });

      $("#login").click(function(){
              var employeeCode= $("#employeeCode").val();
              var password=$("#password").val();
              if(employeeCode.length>0&&employeeCode!=''&&password.length>0&&password!=''){
                   return true;
              }else{
                  return false;
              }

          });
      
      $("#register").click(function(){
    	  window.self.location="employee_toRegister.action";
      });
      
      
     //<!--登录方法结束-->


            function submitForm(eventObj){
                var e;
                var code;
                if(eventObj!=null){
                    e=eventObj;
                }else{
                    e=window.event || event;
                }
                if(jQuery.browser.msie){
                    code=e.keyCode;
                }else{
                    code=e.which;
                }
                if (code == 13){
                    jQuery("#login").click();
                }
            }

        });
        
        

    </script>
  </head>

  <body>
    <div class="navbar navbar-fixed-top">
      <div class="navbar-inner">
        <div class="container-fluid">
          <a class="btn btn-navbar" data-toggle="collapse" data-target=".nav-collapse">
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </a>
          <a class="brand" href="#">clover manager system</a>
          
        </div>
      </div>
    </div>

    <div class="container-fluid">
          
   <form  method="post" action="login.action">
      <table >
        <thead>
         <tr align="center">
           <th  style="text-algin:center">员工登录页面</th>
           
         </tr>
        </thead>
        <tbody>
            <tr>
                <td>
                    <font color="red">*</font>员工账号:
                </td>
                <td align="left">
                    <input type="text" id="employeeCode" name="employeeCode"  placeholder="员工账号" value="<s:property value="employeeCode"/>" class="span2" /> <span id="employeeCodeAlert"></span>
                </td>
            </tr>

                <tr>
                    <td>
                        <font color="red">*</font>密码:
                    </td>
                    <td>
                        <input type="password" id="password" name="password" placeholder="密码" class="span2" onkeyup="submitForm(event)" /> &nbsp;<span id="passwordAlert"></span>
                    </td>
                </tr>
                  <tr align="center">

                      <td>
                          <button id="login" type="submit" class="btn btn-primary">登录</button>
                      </td>
                      <td>
                          <button type="reset" class="btn">取消</button>
                      </td>
                  </tr>
                   <tr align="center">
                       <td>
	                       <button id="register" type="button" class="btn btn-primary">注册</button>
                       </td>
                       <td>
                       </td>
                   </tr>
        </tbody>
      </table>
    </form>
    </div>
  </body>
</html>
