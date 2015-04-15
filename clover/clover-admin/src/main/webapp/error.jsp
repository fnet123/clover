<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
 <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">

<div class="m_404">
	<div class="error">
        <h3>
            <s:if test="exception">
               <s:property value="exception.printStackTrace()"/>
               <s:if test="exception.class.simpleName=='NoSuchMethodException'">
               		<s:property value="response.setStatus(404)"/>
               		 404! 您请求的内容不存在
               </s:if>
               <s:elseif test="exception.message">
               		     出错了：  <s:property value="exception.message"/>
               </s:elseif>
               <s:else>
               	出错了系统错误！
               </s:else>
            </s:if>
        </h3>
    </div>
</div>