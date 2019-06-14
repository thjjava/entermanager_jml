<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>晨会自评问题管理</title>
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
		<script type="text/javascript" src="../js/page/question.js"></script>
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
				<form method="post" id="queryForm" onkeydown="if(event.keyCode==13){return false;}"
					action="<%=path%>/queryList_userAction.do">
					<table style="width: 100%; height: 100%" cellspacing=1
						cellpadding=0 border=0 bgcolor="#99bbe8">
						<tr>
							<td bgcolor="#ffffff" align="center">
								类型：
								<select name="queryType" id="queryType" style="width: 120px;">
										<option value="">全部</option>
										<option value="0">是非题</option>
										<option value="1">问答题</option>
										<option value="2">选择题</option>
									</select>
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
		<div id="addWindow" class="easyui-window" title="企业信息" closed="true"
			collapsible="false" minimizable="false" maximizable="false" iconCls="icon-add"
			style="width: 650px; height: 300px; display: none;" resizable="false">
			<div class="easyui-layout" fit="true">
				<div region="center" border="false"
					style="padding: 10px; background: #fff; border: 1px solid #ccc;">
					<form action="<%=path%>/question_save.do" method="post"
						id="saveForm">
						<table width="100%" border="0" style="font-size: 13">
							<tr style="display: none">
								<td align="right">
									ID：
								</td>
								<td>
									<input type="text" name="question.id" id="id">
									<input type="text" name="question.comId" id="comId">
									<input type="text" name="question.status" id="status">
									<input type="text" name="question.editUser" id="editUser">
									<input type="text" name="question.addTime" id="addTime">
									<input type="text" name="question.answerNo" id="answerNo">
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									类型：
								</td>
								<td>
									<select name="question.type" id="type" onchange="showOrhide();">
										<option value="0">是非题</option>
										<option value="1">问答题</option>
										<option value="2">选择题</option>
									</select>
								</td>
							</tr>
							<tr>
								<td align="right" width="30%;">
									问题：
								</td>
								<td>
									<input type="text" name="question.question" id="question">
								</td>
							</tr>
							<!-- <tr id="tr1">
								<td align="right" width="30%;">
									答案1：
								</td>
								<td>
									<input type="text" name="question.answer1" id="answer1">
								</td>
							</tr>
							<tr id="tr2">
								<td align="right" width="30%;">
									答案2：
								</td>
								<td>
									<input type="text" name="question.answer2" id="answer2">
								</td>
							</tr>
							<tr id="tr3">
								<td align="right" width="30%;">
									答案3：
								</td>
								<td>
									<input type="text" name="question.answer3" id="answer3">
								</td>
							</tr>
							<tr id="tr4">
								<td align="right" width="30%;">
									答案4：
								</td>
								<td>
									<input type="text" name="question.answer4" id="answer4">
								</td>
							</tr> -->
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