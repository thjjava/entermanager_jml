<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>手机端每天登录统计</title>
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
		<script type="text/javascript" src="../js/My97DatePicker/WdatePicker.js"></script>
		<script type="text/javascript" src="../js/echarts.min.js"></script>
	</head>
	<body>
		<div style="height:30px;padding-bottom:10px;padding-left:100px;border-bottom:1px solid #e6e6e6;font-size:14px;">
			<!-- <label>统计类型：</label>
			<select id="selectType" style="width:150px;height:25px;margin-right:30px;" onchange="changeType();">
				<option value="0">按天统计</option>
				<option value="1">按月统计</option>
				<option value="2">按年统计</option>
			</select> -->
			<label>组织：</label>
			<input type="hidden" name="groupId" id="groupId">
			<input type="text" name="groupName" id="groupName" readonly="readonly" onclick="createGroupTree();"style="width:150px;height:20px;margin-right:30px;cursor: pointer;">
			<label> 日期：</label>
			<input type="text" readonly="readonly" id="addTimeStart" name="addTimeStart" onfocus="WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',alwaysUseStartDate:true})" class="Wdate" style="width:180px;"/>
			<a id="search" class="easyui-linkbutton" href="javascript:void(0)" iconCls="icon-search" onclick="query();" >查询</a>
		</div>
		
		<div id="main" style="width: 600px;height:400px;padding:10px;"></div>
		
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
						onclick="closeDiv('groupWindow');">确认</a>
				</div>
			</div>
		</div>
		<script type="text/javascript">
			var myChart = echarts.init(document.getElementById('main'));
			$(function(){
				var queryTime = $("#addTimeStart").val();
				var groupId = $("#groupId").val();
				showChart(queryTime,groupId);
				self.setInterval("query()",30*1000);
			});
			
			function showChart(queryTime,groupId){
				myChart.showLoading();
				$.post("<%=path%>/devLog_devLoginCount.do",{"queryTime":queryTime,"groupId":groupId},function(result){
					var data = eval("("+result+")");
					if(data==''){
						window.parent.location.href = window.location.protocol+'//'+window.location.host+'<%=path%>';
					}
					myChart.hideLoading();
					myChart.setOption(
						option = {
						    title : {
						        text: '手机端登录数统计',
						        subtext: '默认统计当天登录数',
						        x:'center'
						    },
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    series : [
						        {
						            name: '手机端当天登录数统计',
						            type: 'pie',
						            radius : '55%',
						            center: ['50%', '60%'],
						            data:data,
						            itemStyle: {
						                emphasis: {
						                    shadowBlur: 10,
						                    shadowOffsetX: 0,
						                    shadowColor: 'rgba(0, 0, 0, 0.5)'
						                }
						            }
						        }
						    ],
						    color:['green','red','yellow','blue']
						});
				});
			}
			
			function query(){
				var queryTime = $("#addTimeStart").val();
				/* if(queryTime == ''){
					alert("请选择查询日期!");
					return;
				} */
				var groupId = $("#groupId").val();
				myChart.clear();
				showChart(queryTime,groupId);
			}
			
			function createGroupTree(){
				$.ajax({
					type:'POST',
					url:'<%=path%>/companyGroup_getTree.do',
					success:function(data){
						if(data !=''){
							var arry = eval('('+data+')');
							$.fn.zTree.init($("#grouptree"), groupsetting, arry);
							openDiv('groupWindow');
						}
					}
			  	});
			}
			
			var groupsetting = {
				check: {
					enable: true,
					chkStyle: "radio",
					radioType: "all"
				},
				view: {
					dblClickExpand: false
				},
				data: {
					simpleData: {
						enable: true
					}
				},
				callback: {
					onClick: onGroupClick,
					onCheck: onGroupCheck
				}
			};
			
			function onGroupClick(e, treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("grouptree");
				zTree.checkNode(treeNode, !treeNode.checked, null, true);
				return false;
			}
			
			function onGroupCheck(e, treeId, treeNode) {
				var zTree = $.fn.zTree.getZTreeObj("grouptree"),
				nodes = zTree.getCheckedNodes(true),
				names = '',ids='';
				for (var i=0, l=nodes.length; i<l; i++) {
					names += nodes[i].name + ',';
					ids += nodes[i].id + ','
				}
				if (names.length > 0 ) names = names.substring(0, names.length-1);
				if (ids.length > 0 ) ids = ids.substring(0, ids.length-1);
				$("#groupName").val(names);
				$('#groupId').val(ids);
			}
			
			/* function changeType(){
				var type = $("#selectType").val();
				var addTime = document.getElementById("addTimeStart");
				switch (type) {
				case '0':
					addTime.onfocus = function(){WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',alwaysUseStartDate:true});};
					break;
				case '1':
					addTime.onfocus = function(){WdatePicker({startDate:'%y-%M',dateFmt:'yyyy-MM',maxDate:'%y-%M',alwaysUseStartDate:true});};
					break;
				case '2':
					addTime.onfocus = function(){WdatePicker({startDate:'%y',dateFmt:'yyyy',maxDate:'%y',alwaysUseStartDate:true});};
					break;
				default:
					addTime.onfocus = function(){WdatePicker({startDate:'%y-%M-%d',dateFmt:'yyyy-MM-dd',maxDate:'%y-%M-%d',alwaysUseStartDate:true});};
					break;
				}
			} */
		</script>
	</body>
</html>