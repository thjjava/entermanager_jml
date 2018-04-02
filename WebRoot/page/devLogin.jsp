<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>人员上线统计</title>
		<link rel="stylesheet" type="text/css"
			href="../js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="../js/themes/icon.css" />
		<link rel="stylesheet" type="text/css"
			href="../css/commonCss.css">
		<link rel="stylesheet" type="text/css" href="../css/zTreeStyle/zTreeStyle.css">
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="../js/zTree/jquery.ztree.core-3.5.js"></script>
		<script type="text/javascript" src="../js/zTree/jquery.ztree.excheck-3.5.js"></script>
		<script type="text/javascript" src="../js/common/comm.js"></script>
		<script type="text/javascript" src="../js/common/util.js"></script>
		<script type="text/javascript" src="../js/page/devLogin.js"></script>
		<script type="text/javascript" src="<%=path%>/js/My97DatePicker/WdatePicker.js"></script>
	</head>
	<body>
		<div class="query">
			<div id="querydiv" class="query-title">
				<div class="query-title-background">
					<div class="query-title-icon"></div>
					<div class="query-title-font">
						查询条件设置
					</div>
					<div id="yshowdiv" class="query-title-show">
						<a
							onclick="$('#nshowdiv').show();$('#yshowdiv').hide();$('#showdiv').hide();$('#querydiv').css({'border-bottom':'1px solid #8DB2E3'});"
							href="javascript:void(0)">隐藏</a>
					</div>
					<div id="nshowdiv" class="query-title-show" style="display: none;">
						<a
							onclick="$('#yshowdiv').show();$('#nshowdiv').hide();$('#showdiv').show();$('#querydiv').css({'border-bottom':'0px solid #8DB2E3'});"
							href="javascript:void(0)">显示</a>
					</div>
				</div>
			</div>
			<div id="showdiv" style="height: 20px;">
				<form method="post" id="queryForm" action="<%=path%>/devLog_query.do" onkeydown="if(event.keyCode==13){return false;}">
					<table style="width: 100%; height: 100%" cellspacing=1
						cellpadding=0 border=0 bgcolor="#99bbe8">
						<tr>
							<!-- <td bgcolor="#ffffff" align="center">
								日志类型：
								<select id="queryLogType" style="width: 180px;">
									<option value="">--全部--</option>
									<option value="0">开始直播</option>
									<option value="1">停止直播</option>
									<option value="2">登录</option>
									<option value="3">直播异常</option>
									<option value="4">APP异常</option>
									<option value="5">登录远程客户端</option>
								</select>
							</td> -->
							<td bgcolor="#ffffff" align="center">
								是否上线：
								<select id="queryOnLine" style="width: 180px;">
									<option value="0">上线</option>
									<option value="1">未上线</option>
								</select>
							</td>
							<td bgcolor="#ffffff" align="center">
								组织：
								<input type="hidden" name="groupId" id="groupId">
								<input type="text" name="groupName" id="groupName" readonly="readonly" onclick="createGroupTree();" style="width:150px;height:20px;margin-right:30px;cursor: pointer;">
							</td>
							<!-- <td bgcolor="#ffffff" align="center">
								设备账号：
								<input type="text" id="queryDevNo" style="width: 180px;">
							</td> -->
							<td bgcolor="#ffffff" align="center">
								日期：
								<input type="text" readonly="readonly"  id="addTimeStart"  name="addTimeStart" onfocus="WdatePicker({startDate:'%y-%M-%d %H:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d',alwaysUseStartDate:true})" class="Wdate" style="width:180px;"/>
								至
								<input type="text" readonly="readonly"  id="addTimeEnd"  name="addTimeEnd" onfocus="WdatePicker({startDate:'%y-%M-%d %H:%m:%s',dateFmt:'yyyy-MM-dd HH:mm:ss',maxDate:'%y-%M-%d',alwaysUseStartDate:true})" class="Wdate" style="width:180px;"/>
							</td>
							<td bgcolor="#ffffff" align="center">
								<a id="search" class="easyui-linkbutton" href="javascript:void(0)"
									iconCls="icon-search"
									onclick="query();">查询</a>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>
		<table id="datalist"></table>
		
		<!-- 组织 -->
		<div id="groupWindow" class="easyui-window" title="组织" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 300px; height: 350px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<ul id="grouptree" class="ztree"></ul>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('groupWindow');">关闭</a>
				</div>
			</div>
		</div>
		
	</body>
</html>