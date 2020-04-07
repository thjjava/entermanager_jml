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
            {field:'higherGroupName',title:'大区',width:120,sortable:true,align:'center'},
            {field:'parentGroupName',title:'市级部',width:120,sortable:true,align:'center'},
            {field:'curGroupName',title:'当前组织',width:120,sortable:true,align:'center'},
			{field:'dev',title:'会议组织人',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.dev!=null)
						return rec.dev.devName;
					else
						return '无';
				}
			},
			{field:'answer1',title:'唱营销之歌',width:120,sortable:true,align:'center'},
//			{field:'answer2',title:'昨日追踪',width:120,sortable:true,align:'center'},
			{field:'answer6',title:'业绩汇总、收入分析',width:120,sortable:true,align:'center'},
			{field:'answer3',title:'早会汇总表、日线路检讨',width:120,sortable:true,align:'center'},
			{field:'answer4',title:'经销商/小老板七大工法日检讨',width:120,sortable:true,align:'center'},
			{field:'answer7',title:'早展售直播',width:120,sortable:true,align:'center'},
			{field:'answer5',title:'参与沟通人数',width:120,sortable:true,align:'center'},
			{field:'timeLen',title:'会议时长(分钟)',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.timeLen > 30){
						val = rec.timeLen / 60;
						return val.toFixed(0);
					}else {
						return 1;
					}
				}},
			{field:'score',title:'得分',width:120,sortable:true,align:'center'},
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
	queryInit(path+'/userQuestion_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {'addTimeStart':$('#addTimeStart').val(),"addTimeEnd":$('#addTimeEnd').val(),"groupId":$("#groupId").val()});
}

/**
 * 导出Excel
*/
function exportExcel(){
	var startTime = $('#addTimeStart').val();
	var endTime = $('#addTimeEnd').val();
	var groupId=$("#groupId").val();
	window.location.href=path+'/userQuestion_exportExcel.do?startTime='+startTime+'&endTime='+endTime+'&groupId='+groupId;
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