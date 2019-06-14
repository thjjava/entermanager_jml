<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>设备管理</title>
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
		<script type="text/javascript" src="../js/page/dev.js"></script>
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
				<form method="post" id="queryForm" action="<%=path%>/queryList_userAction.do" onkeydown="if(event.keyCode==13){return false;}">
					<table style="width: 100%; height: 100%" cellspacing=1
						cellpadding=0 border=0 bgcolor="#99bbe8">
						<tr>
							<td bgcolor="#ffffff" align="center">
								组织：
								<input type="hidden" name="groupId" id="groupId">
								<input type="text" name="groupName" id="groupName" readonly="readonly" onclick="createGroupTree2();" style="width:150px;height:20px;margin-right:30px;cursor: pointer;">
							</td>
							<td bgcolor="#ffffff" align="center">
								设备号：
								<input type="text" id="queryDevNo" style="width: 180px;">
							</td>
							<td bgcolor="#ffffff" align="center">
								设备名称：
								<input type="text" id="queryDevName" style="width: 180px;">
							</td>
							<td bgcolor="#ffffff" align="center">
								是否分配组织：
								<select id="queryIsGroup" style="width: 180px;">
									<option value="">--全部--</option>
									<option value="yes">已分配</option>
									<option value="no">未分配</option>
								</select>
							</td>
							<td bgcolor="#ffffff" align="center">
								是否启用：
								<select id="queryIsAble" style="width: 180px;">
									<option value="">--全部--</option>
									<option value="0">启用</option>
									<option value="1">禁用</option>
								</select>
							</td>
							<td bgcolor="#ffffff" align="center">
								<a class="easyui-linkbutton" href="javascript:void(0)"
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
		<div id="addWindow" class="easyui-window" title="设备信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false" iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/dev_save.do" method="post"
						id="saveForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr style="display: none">
								<td align="right">
									ID：
								</td>
								<td>
									<input type="text" name="dev.id" id="id">
									<input type="text" name="dev.imsi" id="imsi">
									<input type="text" name="dev.phone" id="phone">
									<input type="text" name="dev.audioRtpPort" id="audioRtpPort">
									<input type="text" name="dev.audioRtcpPort" id="audioRtcpPort">
									<input type="text" name="dev.videoRtpPort" id="videoRtpPort">
									<input type="text" name="dev.videoRtcpPort" id="videoRtcpPort">
									<input type="text" name="dev.serverId" id="serverId">
									<input type="text" name="dev.drId" id="drId">
									<input type="text" name="dev.onLines" id="onLines">
									<input type="text" name="dev.isAble" id="isAble">
									<input type="text" name="dev.lastLoginTime" id="lastLoginTime">
									<input type="text" name="dev.addTime" id="addTime">
									<input type="text" name="dev.group.id" id="group">
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									企业：
								</td>
								<td>
									<select name="dev.company.id" id="company" disabled="disabled">
										<option value="">--请选择--</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right">
									设备名称：
								</td>
								<td>
									<input type="text" name="dev.devName" id="devName"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr>
								<td align="right">
									设备号：
								</td>
								<td>
									<input type="text" name="dev.devNo" id="devNo"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="长度为50...">
								</td>
							</tr>
							<tr id="trp1">
								<td align="right">
									密码：
								</td>
								<td>
									<input type="password" name="dev.devKey" id="devKey"
										class="easyui-validatebox" required="true"
										validType="length[1,50]" invalidMessage="请输入密码...">
								</td>
							</tr>
							<tr id="trp2">
								<td align="right">
									确认密码：
								</td>
								<td>
									<input type="password" name="pwd1" id="pwd1"
										class="easyui-validatebox" required="true"
										validType="same['devKey']" invalidMessage="请输入密码...">
								</td>
							</tr>
							<tr>
								<td align="right">
									视频发布地址：
								</td>
								<td>
									<input type="text" name="dev.publishUrl"
										id="publishUrl" class="easyui-validatebox" 
										validType="length[1,300]"
										invalidMessage="请输入地址...">
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
		<!-- 组织 -->
		<div id="groupWindow" class="easyui-window" title="组织" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			style="width: 300px; height: 450px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<ul id="grouptree" class="ztree"></ul>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="fenpei();">提交</a>
				</div>
			</div>
		</div>
		
		<!-- 组织2 -->
		<div id="groupWindow2" class="easyui-window" title="组织" closed="true"
			collapsible="false" minimizable="false" maximizable="false"
			iconCls="icon-add"
			style="width: 300px; height: 350px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<ul id="grouptree2" class="ztree"></ul>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('groupWindow2');">确认</a>
				</div>
			</div>
		</div>
		
		<!-- 批量导入开始 -->
		<div id="importWindow" class="easyui-window" title="设备信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false" iconCls="icon-add"
			style="width: 500px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<table width="100%" border="0" style="font-size: 13">
						<tr>
							<td align="right">&nbsp;</td>
			    			<td>
			    				<span style="color:red;">请先下载模板，按照模板要求批量导入数据</span>
			    			</td>
						</tr>
						<tr>
							<td align="right">文件：</td>
			    			<td>
			    				<form method="post" id="uploadForm" action="<%=path%>/dev_upload.do" enctype="multipart/form-data">
			    					<input type="file" id="upload" name="upload" value="浏览">
			    				</form>
			    			</td>
						</tr>
					</table>
				</div>
				<div region="south" border="false"
					style="text-align: center; height: 30px; line-height: 30px;">
					<a id="downBtn" class="easyui-linkbutton" href="javascript:void(0)"
						onclick="javascript:location.href='<%=path%>/excel/dev.xls'">下载模版</a>
					<a id="subBtn" class="easyui-linkbutton" href="javascript:void(0)"
						onclick="importExcel();">导入</a>
					<a class="easyui-linkbutton" href="javascript:void(0)"
						onclick="closeDiv('importWindow');">取消</a>
				</div>
			</div>
		</div>
		<!-- 批量导入结束 -->
		
	</body>
</html>