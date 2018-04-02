var render="datalist";
var win="addWindow";
var wform="saveForm";
var qform="queryForm";
var path = $('#path').val();

/**
 * 数据列表
 * @type 
 */
var tcolumn=[[
			{field:'dev',title:'设备名称',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.dev!=null)
						return rec.dev.devName;
					else
						return '无';
				}
			},
			/*{field:'mediaServer',title:'服务器',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.mediaServer!=null)
						return rec.mediaServer.serverName;
					else
						return '无';
				}
			},
			{field:'clientIP',title:'客户端IP',width:120,sortable:true,align:'center'},
			{field:'operatorName',title:'网络运营商',width:120,sortable:true,align:'center'},*/
			{field:'logType',title:'日志类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.logType==0){
						return '<font color="green">开始直播</font>';
					}else if(rec.logType==1){
						return '<font color="red">停止直播</font>';
					}else if(rec.logType==2){
						return '<font>登录</font>';
					}else if(rec.logType==3){
						return '<font>直播异常</font>';
					}else if(rec.logType==4){
						return '<font>APP异常</font>';
					}else if(rec.logType==5){
						return '<font>登录远程客户端</font>';
					}
				}
			},
			{field:'logDesc',title:'日志描述',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'}
			
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
	id:'btnadd',
	text:'导出',
	iconCls:'icon-add',
	handler:exportExcel}];
		 
/**
 * 加载初始化
 */
$(function(){
	init();
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/devLog_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {'queryLogType':$('#queryLogType').val(),'queryIsp':$('#queryIsp').val(),'addTimeStart':$('#addTimeStart').val(),"addTimeEnd":$('#addTimeEnd').val(),"queryDevNo":$('#queryDevNo').val(),"groupId":$("#groupId").val()});
}

/**
 * 导出Excel
*/
function exportExcel(){
	var startTime = $('#addTimeStart').val();
	var endTime = $('#addTimeEnd').val();
	var devNo = $('#queryDevNo').val();
	var logType = $('#queryLogType').val();
	var groupId=$("#groupId").val();
	window.location.href=path+'/devLog_exportExcel.do?startTime='+startTime+'&endTime='+endTime+'&devNo='+devNo+'&queryLogType='+logType+'&groupId='+groupId;
}


function createGroupTree(){
	$.ajax({
		type:'POST',
		url:path+'/companyGroup_getTree.do',
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
		ids += nodes[i].id + ',';
	}
	if (names.length > 0 ) names = names.substring(0, names.length-1);
	if (ids.length > 0 ) ids = ids.substring(0, ids.length-1);
	$("#groupName").val(names);
	$('#groupId').val(ids);
}