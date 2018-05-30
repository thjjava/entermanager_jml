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
            {field:'higherGroupName',title:'上上级组织',width:120,sortable:true,align:'center'},
			{field:'parentGroupName',title:'上级组织',width:120,sortable:true,align:'center'},
			{field:'curGroupName',title:'当前组织',width:120,sortable:true,align:'center'},
			{field:'devNo',title:'设备编号',width:120,sortable:true,align:'center'},
			{field:'devName',title:'设备名称',width:120,sortable:true,align:'center'},
			{field:'onLines',title:'是否上线',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if($('#queryOnLine').val()== 0){
						return '<span style="color:green">已上线</span>';
					}else{
						return '<span style="color:red">未上线</span>';
					}
				}
			},
			{field:'addTime',title:'最近登录日期',width:120,sortable:true,align:'center'},
			{field:'lastLoginTime',title:'上次登录日期',width:120,sortable:true,align:'center'}
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
	queryInit(path+'/dev_queryOnLineList.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {'onLine':$('#queryOnLine').val(),'addTimeStart':$('#addTimeStart').val(),"addTimeEnd":$('#addTimeEnd').val(),"queryDevNo":$('#queryDevNo').val(),"groupId":$("#groupId").val()});
}

/**
 * 导出Excel
*/
function exportExcel(){
	var startTime = $('#addTimeStart').val();
	var endTime = $('#addTimeEnd').val();
	var devNo = $('#queryDevNo').val();
	var onLine = $('#queryOnLine').val();
	var groupId=$("#groupId").val();
	window.location.href=path+'/dev_exportExcel.do?startTime='+startTime+'&endTime='+endTime+'&onLine='+onLine+'&groupId='+groupId;
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

