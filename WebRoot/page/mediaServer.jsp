<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>流媒体管理</title>
		<link rel="stylesheet" type="text/css"
			href="../js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="../js/themes/icon.css" />
		<link rel="stylesheet" type="text/css"
			href="../css/commonCss.css">
		<script type="text/javascript" src="../js/jquery.min.js"></script>
		<script type="text/javascript" src="../js/jquery.easyui.min.js"></script>
		<script type="text/javascript" src="../js/easyui-lang-zh_CN.js"></script>
		<script type="text/javascript" src="../js/common/comm.js"></script>
		<script type="text/javascript" src="../js/common/util.js"></script>
		<script type="text/javascript" src="../js/page/mediaServer.js"></script>
	</head>
	<body>
		<div class="query" style="display: none;">
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
				<form method="post" id="queryForm" onkeydown="if(event.keyCode==13){return false;}"
					action="<%=path%>/queryList_userAction.do">
					<table style="width: 100%; height: 100%" cellspacing=1
						cellpadding=0 border=0 bgcolor="#99bbe8">
						<tr>
							<td bgcolor="#ffffff" align="center">
								企业名称：
								<input type="text" id="queryComName" style="width: 180px;">
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
		<!-- 新增和修改 -->
		<div id="addWindow" class="easyui-window" title="流媒体信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/company_save.do" method="post"
						id="saveForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr style="display: none">
								<td align="right">
									ID：
								</td>
								<td>
									<input type="text" name="mediaServer.id" id="id">
									<input type="text" name="mediaServer.cpuUsed" id="cpuUsed">
									<input type="text" name="mediaServer.memoryUsed" id="memoryUsed">
									<input type="text" name="mediaServer.devNum" id="devNum">
									<input type="text" name="mediaServer.playNum" id="playNum">
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									企业：
								</td>
								<td>
									<select name="mediaServer.company.id" id="company">
										
									</select>
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									服务器名称：
								</td>
								<td>
									<input type="text" name="mediaServer.serverName" id="serverName">
								</td>
							</tr>
							<tr>
								<td align="right">
									实时播放地址：
								</td>
								<td>
									<input type="text" name="mediaServer.realPlayUrl" id="realPlayUrl"
										class="easyui-validatebox" required="true"
										validType="length[1,200]" invalidMessage="长度为200...">
								</td>
							</tr>
							<tr>
								<td align="right">
									录像地址：
								</td>
								<td>
									<input type="text" name="mediaServer.recordPlayUrl" id="recordPlayUrl"
										class="easyui-validatebox" required="true"
										validType="length[1,200]" invalidMessage="长度为200...">
								</td>
							</tr>
							<tr>
								<td align="right">
									录像服务地址：
								</td>
								<td>
									<input type="text" name="mediaServer.recordWebServiceUrl" id="recordWebServiceUrl"
										class="easyui-validatebox" required="true"
										validType="length[1,200]" invalidMessage="长度为200...">
								</td>
							</tr>
							<tr>
								<td align="right">
									录像下载地址：
								</td>
								<td>
									<input type="text" name="mediaServer.recordDownUrl" id="recordDownUrl"
										class="easyui-validatebox" required="true"
										validType="length[1,100]" invalidMessage="长度为100...">
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
						onclick="closeDiv('addWindow');">取消</a>
				</div>
			</div>
		</div>
	</body>
</html>