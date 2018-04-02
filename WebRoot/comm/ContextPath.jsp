<%@page import="com.sttri.util.Constant"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.sttri.pojo.TblUser"%>
<%@page import="com.sttri.util.WorkUtil"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<%
//String path=request.getContextPath();
String path=Constant.readKey("platformUrl");
boolean flag = false;
TblUser u = WorkUtil.getCurrUser(request);
if(u!=null)
	flag = true;
%>
<head>
	<script type="text/javascript">
		var flag = '<%=flag%>';
		if(flag!='true')
			window.parent.login();
	</script>
</head>
<body>
<input type="hidden" id="path" name="path" value="<%=path %>"/>
</body>
</html>