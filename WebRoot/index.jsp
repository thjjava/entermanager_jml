<%@page import="java.util.Calendar"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@page import="com.sttri.pojo.TblUser"%>
<%@page import="com.sttri.util.WorkUtil"%>
<% 
	String path=request.getContextPath();
	boolean flag = false;
	TblUser u = WorkUtil.getCurrUser(request);
	if(u!=null)
		flag = true;
	boolean isAdmin = false;
	isAdmin = (Boolean)request.getSession().getAttribute("isAdmin");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>视频直播企业管理系统</title>
		<link rel="stylesheet" type="text/css" href="<%=path %>/css/default.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css" href="<%=path %>/js/themes/icon.css" />
		<script type="text/javascript" src="<%=path %>/js/jquery.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="<%=path %>/js/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src='<%=path %>/js/outlook2.js'></script>
		<script type="text/javascript">
			var flag = '<%=flag%>';
			if(flag!='true')
				window.location.href = window.location.protocol+'//'+window.location.host+'<%=path%>';
				
			<%-- var isAdmin = '<%=isAdmin%>';
			if(isAdmin == 'true'){
				var _menus = {"menus":[
							{"menuid":"1","icon":"icon-sys","menuname":"系统管理",
							  		"menus":[
							  			{"menuname":"角色管理","icon":"icon-users","url":"<%=path%>/page/role.jsp"},
							  			{"menuname":"菜单管理","icon":"icon-users","url":"<%=path%>/page/menus.jsp"},
							  			{"menuname":"企业组织","icon":"icon-users","url":"<%=path%>/page/companyGroup.jsp"},
							  			{"menuname":"企业设备","icon":"icon-users","url":"<%=path%>/page/dev.jsp"},
							  			{"menuname":"用户管理","icon":"icon-users","url":"<%=path%>/page/user.jsp"},
							  			{"menuname":"设备日志","icon":"icon-users","url":"<%=path%>/page/devLog.jsp"},
							  			{"menuname":"设备登录统计","icon":"icon-users","url":"<%=path%>/page/devLoginCount.jsp"}
									  ]
							 }
					     ]};
			}else{
				var _menus = {"menus":[
							{"menuid":"1","icon":"icon-sys","menuname":"系统管理",
							  		"menus":[
							  			{"menuname":"企业设备","icon":"icon-users","url":"<%=path%>/page/dev.jsp"},
							  			{"menuname":"用户管理","icon":"icon-users","url":"<%=path%>/page/user.jsp"},
							  			{"menuname":"设备日志","icon":"icon-users","url":"<%=path%>/page/devLog.jsp"}
									  ]
							 }
					     ]};
			} --%>
			
			function openDiv(id){
				var height=$('#'+id).height();
				var width=$('#'+id).width();
				$('#'+id).window({
				 top:($(document).height()-height) * 0.5,
			     left:($(document).width()-width) * 0.5,
			     modal:true,
			     draggable:true,
			     shadow:true
				});
				$('#'+id).css('display','block');
				$('#'+id).window('open');
			}
			
			function closeDiv(id){
				$('#'+id).window('close');
			}
			
			function submitForm(){
				if($('#modifyForm').form('validate')){
					var newPwd=$('#newPwd').val();
					if(newPwd==''){
						$.messager.alert('提示',"请输入新密码!");
						return;
					}
					var rePwd=$('#rePwd').val();
					if(rePwd==''){
						$.messager.alert('提示',"请输入确认密码!");
						return;
					}else if(newPwd != rePwd){
						$.messager.alert('提示',"确认密码不等于新密码!");
						return;
					}
					var url="<%=path%>/user_modifyPwd.do?newPwd="+newPwd;
					$('#modifyForm').form('submit', {
					    url:url,
					    onSubmit: function(){
					    },   
						success:function(data){
					     	if("success"==data){
					     		$.messager.alert('提示',"修改密码成功!");
								closeDiv('modifyPwdWindow');
								window.location.href="<%=path %>/login_logout.do";
					     	}else{
					     		$.messager.alert('提示',"修改密码失败!");
					     	}
					    }
					});
				}
			}
		</script>
	</head>
	<body class="easyui-layout">
		<div region="north" border="false" split="true"
			style="height: 80px; background: #E0ECFF; width: 100%;">
			<div style="float: left;height: 100%;width: 50%;">
				<span style="color: white;font-size: 25px;padding-top: 20px;float: left;padding-left: 25px;font-style: 楷体;">
				视频直播企业管理系统
				</span>
			</div>
			<div style="float: right;display: ;font-style: 楷体;">
				<span style="valign: middle; float: right; padding-right: 10px; margin-top: 15px; color: white;  display: inline-block;">
					<a id="modifyPwd" href="javascript:void(0);" onclick="openDiv('modifyPwdWindow');" style='color: white;cursor:pointer;text-decoration: none;'>修改密码</a>
				</span><br />
				<span style="valign: middle; float: right; padding-right: 10px; margin-top: 25px; color: white;  display: inline-block;"
					class="head">
					<a id="loginOut" href="<%=path %>/login_logout.do" style='color: white;cursor:poin
					ter;text-decoration: none;'>安全退出</a>
				</span>
			</div>
		</div>
		<div region="west" split="true" title="菜单" style="width: 180px;">
			<div class="easyui-accordion" fit="true" border="false">
				导航内容
			</div>
		</div>
		<!-- 
		<div region="east" split="true" title="East" style="width:100px;padding:10px;"></div>
		 -->
		<div region="south" split="true"
			style="height: 35px; text-align: center;">
			<font style="font-size: 20px;color: white;">©2015-<%=Calendar.getInstance().get(Calendar.YEAR) %> 视频服务产品（上海）运营中心 版权所有</font>
		</div>
		<div region="center" title="" style="overflow-y: hidden;">
			<div id="tabs" class="easyui-tabs" fit="true" border="false">
				<!-- <div title="欢迎使用" style="overflow: hidden;" id="home">
					<img src="<%=path %>/images/ehome.jpg" style="width: 100%;height: 100%;"/>
				</div> -->
			</div>
		</div>
		<!--加载内容页面结束-->
		<div id="mm" class="easyui-menu" style="width: 150px;">
			<div id="mm-tabclose">
				关闭
			</div>
			<div id="mm-tabcloseall">
				全部关闭
			</div>
			<div id="mm-tabcloseother">
				除此之外全部关闭
			</div>
			<div class="menu-sep"></div>
			<div id="mm-tabcloseright">
				当前页右侧全部关闭
			</div>
			<div id="mm-tabcloseleft">
				当前页左侧全部关闭
			</div>
			<div class="menu-sep"></div>
			<div id="mm-exit">
				退出
			</div>
		</div>
		<!-- 修改密码 -->
		<div id="modifyPwdWindow" class="easyui-window" title="设备信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 300px; height: 200px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/user_modifyPwd.do" method="post"
						id="modifyForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr>
								<td align="right">
									新密码：
								</td>
								<td>
									<input type="password" id="newPwd" />
								</td>
							</tr>
							<tr>
								<td align="right">
									确认密码：
								</td>
								<td>
									<input type="password" id="rePwd" />
								</td>
							</tr>
						</table>
					</form>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="submitForm()">提交</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('modifyPwdWindow');">取消</a>
				</div>
			</div>
		</div>
	</body>
</html>