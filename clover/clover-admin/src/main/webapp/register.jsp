<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
    <title></title>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script type="text/javascript" src="js/DatePicker/WdatePicker.js"></script>
    
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
        	
        	$("#employeeCode").blur(function(){
    			var employeeCode=$("#employeeCode").val();
    			if(employeeCode.length>0&&employeeCode!=''){
    				var pattern =  /^(ps|PS)+\w{4,19}$/;
                    if(!pattern.exec(employeeCode))
                    {
                         $("#employeeCode").focus();
                        $("#employeeCodeAlert").html("<font size='5' color='red'>账号格式(PS(ps)+字母或者数字,长度大于6位)</font>");
                        return false;
                    }else {
                    	
	    				//$("#employeeCodeAlert").html("<font size='5' color='green'>√</font>");
                    	jQuery.ajax({
            				type : "post",
            				url : "employeeJson_checkEmployeeCode.action",
            				dataType : 'text',
            				data : {"employeeCode":employeeCode},
            				success : function(json) {

            					if (json != null && json != '') {
            						var result = eval(json); //包数据解析为json 格 式 
            						if('ok'==result){
            							$("#employeeCodeAlert").html("<font size='5' color='green'>√</font>");
            						}else if('exist'==result){
            							$("#employeeCodeAlert").html("<font color='red'>该账号已经存在</font>");
            							$("#employeeCode").focus();
            						
            						}else{
            							$("#employeeCodeInfo").html("<font  color='red'>"+result+"</font>");
            						} 
            						
            					} else {
            						alert("异步处理异常，返回值为空！");
            					}
            				}
            			});
                    }
    				
    			}else{
    				$("#employeeCodeAlert").html("<font size='5' color='red'>×</font>");
    				$("#employeeCode").focus();
    			}
    		});
    		$("#employeeCode").click(function(){
	    			$("#employeeCodeAlert").html("");
    			
    		});
    		
    		$("#employeeName").blur(function(){
    			var employeeName = $("#employeeName").val();
    			if (employeeName.length == 0 || employeeName == "") {
    				$("#employeeNameAlert").html("<font size='5' color='red'>×</font>");
    			}else{
    				$("#employeeNameAlert").html("<font size='5' color='green'>√</font>");
    				
    			}
    		});
    		
    		$("#employeeName").click(function(){
    			$("#employeeNameAlert").html("");
    			
    		});
    		
    		$("#password").blur(function(){
    			var password = $("#password").val();
    			if (password.length == 0 || password == "") {
    				$("#passwordAlert").html("<font size='5' color='red'>×</font>");
    			}else{
    				$("#passwordAlert").html("<font size='5' color='green'>√</font>");
    				
    			}
    		});
    		
    		$("#password").click(function(){
    			$("#passwordAlert").html("");
    			
    		});
    		
    		$("#mobile").blur(function(){
    			var mobile = $("#mobile").val();
    			if (mobile.length == 0 || mobile == "") {
    				$("#mobileAlert").html("<font size='5' color='red'>×</font>");
    			}else{
    				  var reg =/^((\+86)|(86))?(1)\d{10}$/;
                      if(!mobile.match(reg))
                      {
                          $("#mobile").focus();
                          $("#mobileAlert").html("<font size='5' color='red'>×</font>");
                          return false;
                      }else {
	    				$("#mobileAlert").html("<font size='5' color='green'>√</font>");
                      }
    			}
    				
    		});
    		
    		$("#mobile").click(function(){
    			$("#mobileAlert").html("");
    			
    		});
    		$("#email").blur(function(){
    			var email = $("#email").val();
    			if (email.length == 0 || email == "") {
    				$("#emailAlert").html("<font size='5' color='red'>×</font>");
    				$("#email").focus();
    			}else{
    				 var pattern =/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
                     if(!pattern.exec(email))
                     {
                         $("#email").focus();
                         $("#emailAlert").html("<font size='5' color='red'>×</font>");
                         return false;
                     }else {
 	    				$("#emailAlert").html("<font size='5' color='green'>√</font>");
                     }
    			}
    				
    		});
    		
    		$("#email").click(function(){
    			$("#emailAlert").html("");
    		});
    		
    		
    		$("#idCard").click(function(){
    			$("#idCardAlert").html("");
    		});
    		
    		
    		$("#birthday").click(function(){
    			$("#birthdayAlert").html("");
    			
    		});
    		
    		$("#address").blur(function(){
    			var address = $("#address").val();
    			if (address.length == 0 || address == "") {
    				$("#addressAlert").html("<font size='5' color='red'>×</font>");
    			}else{
    				$("#addressAlert").html("<font size='5' color='green'>√</font>");
    				
    			}
    		});
    		
    		$("#address").click(function(){
    			$("#addressAlert").html("");
    			
    		});
    		
    		$("#zipCode").blur(function(){
    			var zipCode = $("#zipCode").val();
    			if (zipCode.length == 0 || zipCode == "") {
    				$("#zipCodeAlert").html("<font size='5' color='red'>×</font>");
    			}else{
    				var pattern =/^[1-9][0-9]{5}$/;
                    if(!pattern.exec(zipCode))
                    {
                        $("#zipCode").focus();
                        $("#zipCodeAlert").html("<font size='5' color='red'>×</font>");
                        return false;
                    }else {
	    				$("#zipCodeAlert").html("<font size='5' color='green'>√</font>");
                    }
    				
    			}
    		});
    		
    		$("#zipCode").click(function(){
    			$("#zipCodeAlert").html("");
    			
    		});
    		
    		
    		
    		$("#register").click(function() {
    			var employeeCode = $("#employeeCode").val();
    			if (employeeCode.length == 0 || employeeCode == "") {
    				$("#employeeCodeInfo").show();
    				$("#employeeCodeAlert").html("<font size='5' color='red'>×</font>");
    				$("#employeeCode").focus();
    				return false;
    			}else{
    				$("#employeeCodeInfo").hide();
    				$("#employeeCodeAlert").html("<font size='5' color='green'>√</font>");
    				
    			}


    			var employeeName = $("#employeeName").val();
    			if (employeeName.length == 0 || employeeName == "") {
    				$("#employeeNameAlert").html("<font size='5' color='red'>×</font>");
    				$("#employeeName").focus();
    				return false;
    			}else{
    				$("#employeeNameAlert").html("<font size='5' color='green'>√</font>");
    				
    			}
    			var password = $("#password").val();
    			if (password.length == 0 || password == "") {
    				$("#passwordAlert").html("<font size='5' color='red'>×</font>");
    				$("#password").focus();
    				return false;
    			}else{
    				$("#passwordAlert").html("<font size='5' color='green'>√</font>");
    				
    			}
    			var mobile = $("#mobile").val();
    			if (mobile.length == 0 || mobile == "") {
    				$("#mobileAlert").html("<font size='5' color='red'>×</font>");
    				$("#mobile").focus();
    				return false;
    			}else{
    				  var reg =/^((\+86)|(86))?(1)\d{10}$/;
                      if(!mobile.match(reg))
                      {
                          $("#mobile").focus();
                          $("#mobileAlert").html("<font size='5' color='red'>×</font>");
                          return false;
                      }else {
	    				$("#mobileAlert").html("<font size='5' color='green'>√</font>");
                      }
    			}
    			var email = $("#email").val();
    			if (email.length == 0 || email == "") {
    				$("#emailAlert").html("<font size='5' color='red'>×</font>");
    				$("#email").focus();
    				return false;
    			}else{
    				 var pattern =/^[a-zA-Z0-9_\-]{1,}@[a-zA-Z0-9_\-]{1,}\.[a-zA-Z0-9_\-.]{1,}$/;
                     if(!pattern.exec(email))
                     {
                         $("#email").focus();
                         $("#emailAlert").html("<font size='5' color='red'>×</font>");
                         return false;
                     }else {
 	    				$("#emailAlert").html("<font size='5' color='green'>√</font>");
                     }
    				
    			}
    			var address = $("#address").val();
    			if (address.length == 0 || address == "") {
    				$("#addressAlert").html("<font size='5' color='red'>×</font>");
    				$("#address").focus();
    				return false;
    			}else{
    				$("#addressAlert").html("<font size='5' color='green'>√</font>");
    				
    			}
    			
    			var zipCode = $("#zipCode").val();
    			if (zipCode.length == 0 || zipCode == "") {
    				$("#zipCodeAlert").html("<font size='5' color='red'>×</font>");
    				$("#zipCode").focus();
    				return false;
    			}else{
    				var pattern =/^[1-9][0-9]{5}$/;
                    if(!pattern.exec(zipCode))
                    {
                        $("#zipCode").focus();
                        $("#zipCodeAlert").html("<font size='5' color='red'>×</font>");
                        return false;
                    }else {
	    				$("#zipCodeAlert").html("<font size='5' color='green'>√</font>");
                    }
    			}
    		});
    		
    		
    		 $("#backLogin").click(function(){
    	    	  window.self.location="toLogin.action";
    	      });
        	
        	
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
                 
                 
    <form  method="post" action="employee_register.action">

      <table >
        <thead>
         <tr align="center">
           <th  style="text-algin:center">员工注册页面</th>
         </tr>
        </thead>
        <tbody>
        
            <tr>
                <td>
                    <font color="red">*</font>员工账号:
                </td>
                <td align="left">
                    <input type="text" id="employeeCode" name="employeeCode" placeholder="员工账号" class="span2" /> <span id="employeeCodeAlert"></span>
                </td>
            </tr>
            
             <tr>
                <td>
                    <font color="red">*</font>员工名:
                </td>
                <td align="left">
                    <input type="text" id="employeeName" name="employeeName" placeholder="员工名" class="span2" /> <span id="employeeNameAlert"></span>
                </td>
            </tr>

               <tr>
                   <td>
                       <font color="red">*</font>密码:
                   </td>
                   <td>
                       <input type="password" id="password" name="password" placeholder="密码" class="span2"  /> &nbsp;<span id="passwordAlert"></span>
                   </td>
               </tr>
               
                <tr>
	                <td>
	                    <font color="red">*</font>性别:
	                </td>
	                <td align="left">
				                    男<input type="radio" id="sex" name="sex" value="0" checked="checked"  /> 
				                    女<input type="radio" id="sex" name="sex" value="1"  /> 
	                </td>
	            </tr>
                
                 <tr>
	                <td>
	                    <font color="red">*</font>手机号码:
	                </td>
	                <td align="left">
	                    <input type="text" id="mobile" name="mobile" placeholder="手机号码" class="span2" /> <span id="mobileAlert"></span>
	                </td>
	            </tr>
            
             <tr>
                <td>
                    <font color="red">*</font>邮箱:
                </td>
                <td align="left">
                    <input type="text" id="email" name="email" placeholder="邮箱" class="span2" /> <span id="emailAlert"></span>
                </td>
            </tr>
            
             <tr>
                <td>
                    <font color="red"></font>身份证号码:
                </td>
                <td align="left">
                    <input type="text" id="idCard" name="idCard" placeholder="身份证号码" class="span2" /> <span id="idCardAlert"></span>
                </td>
            </tr>
            
            
           <%--   <tr>
                <td>
                    <font color="red"></font>生日:
                </td>
                <td align="left">
                    <input type="text" id="birthday" name="birthday" placeholder="生日" 
                    onFocus="WdatePicker({isShowClear:true,readOnly:true,lang:'<s:property value="datePickerLocale"/>'})"
														readonly class="span2" /> <span id="birthdayAlert"></span>
                </td>
            </tr> --%>
      
	      <tr>
               <td>
                   <font color="red">*</font>地址:
               </td>
               <td align="left">
                   <input type="text" id="address" name="address" placeholder="地址" class="span2" /> <span id="addressAlert"></span>
               </td>
           </tr>
           
            <tr>
               <td>
                   <font color="red">*</font>邮编:
               </td>
               <td align="left">
                   <input type="text" id="zipCode" name="zipCode" placeholder="邮编" class="span2" /> <span id="zipCodeAlert"></span>
               </td>
           </tr>
    
          
             <tr align="center">

                 <td>
                     <button id="register" type="submit" class="btn btn-primary">注册</button>
                 </td>
                 <td>
                     <button type="reset" class="btn">重置</button>
                 </td>
             </tr>
             
             <tr align="center">
                       <td>
	                       <button id="backLogin" type="button" class="btn btn-primary">返回登录页面</button>
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
