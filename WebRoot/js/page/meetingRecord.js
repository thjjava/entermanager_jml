var render="datalist";
var win="addWindow";
var wform="saveForm";
var qform="queryForm";
var path = $('#path').val();

function queryInit1(requestUrl,tcolumn,tbar,id){
	$("#"+id).datagrid({
		title:"数据列表",
		iconCls:'icon-search',
		pageSize: 10,
		pageList: [5,10,15,20,25,30],
		nowrap: false,
		striped: true,
		collapsible:true,
		url:requestUrl,
		loadMsg:'数据加载中......',
		sortOrder: 'desc',
		remoteSort: false,
		fitColumns:true,
		singleSelect:false,
		idField:'id',  
		frozenColumns:[[{field:'id',checkbox:true}]],
		columns:tcolumn,
		pagination:true,
		rownumbers:true,
		toolbar:tbar
	});
	$("#"+id).datagrid('getPager').pagination({
	    beforePageText: '第',//页数文本框前显示的汉字
	    afterPageText: '页    共 {pages} 页',
	    displayMsg: '当前显示 {from} - {to} 条记录   共 {total} 条记录',  
		onBeforeRefresh:function(pageNumber, pageSize){
			$(this).pagination('loading');
			$(this).pagination('loaded');
		}
	});
}

/**
 * 数据列表
 * @type 
 */
var tcolumn=[[
			{field:'userAccount',title:'会议发起者账号',width:120,sortable:true,align:'center'},
			{field:'userName',title:'会议发起者名称',width:120,sortable:true,align:'center'},
			{field:'topic',title:'会议主题',width:120,sortable:true,align:'center'},
			{field:'status',title:'会议状态',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.status == 0){
						return '<font color="red">已结束</font>';
					}else{
						return '<font color="green">正进行</font>';
					}
				}},
			{field:'startTime',title:'开始时间',width:120,sortable:true,align:'center'},
			{field:'endTime',title:'结束时间',width:120,sortable:true,align:'center'},
			/*{field:'end_time',title:'结束时间',width:120,sortable:true,align:'center',
					formatter:function(val,rec){
						if(typeof(rec.end_time) != 'undefined'){
							var crtTime = new Date(rec.end_time);
						    return dateFtt("yyyy-MM-dd hh:mm:ss",crtTime);
						}else{
							return '';
						}
					}},*/
			{field:'participants',title:'参会人数',width:120,sortable:true,align:'center'},
			{field:'oper',title:'操作',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					return '<a href="javascript:void(0);" onclick="getMeetingInfo(\''+rec.meetingId+'\');">详情</a>';
				}
			}
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
			id:'btnremove',
			text:'删除',
			iconCls:'icon-remove',
			handler:deleteobj
		 }];
		 
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
	queryInit(path+'/meetingRecord_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {'addTimeStart':$('#addTimeStart').val(),"addTimeEnd":$('#addTimeEnd').val(),"userAccount":$("#userAccount").val(),"userName":$("#userName").val()});
}

/**
 * 删除
 */
function deleteobj(){
	$.messager.confirm('系统提示', '您确定要删除吗?', function(r) {
        if (r) {
            var rows = $('#'+render).datagrid('getSelections');
            var ids="";
			if(rows.length>0){
				for(var i=0;i<rows.length;i+=1){
					if(i==0){
						ids=rows[i].id;
					}else{
						ids+="_"+rows[i].id;
					}
				}
				$.post(path+"/meetingRecord_deletebyids.do",{"ids":ids},function(data){
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}else{
			     		$.messager.alert('提示',"删除失败!");
			     	}
				});
			}
        }
    });
}

/**
 * 导出Excel
*/
function exportExcel(){
	var startTime = $('#addTimeStart').val();
	var endTime = $('#addTimeEnd').val();
//	var devName = $('#queryDevName').val();
//	var groupId=$("#groupId").val();
	window.location.href=path+'/meetingRecord_exportExcel.do?startTime='+startTime+'&endTime='+endTime;
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

function dateFtt(fmt, date) {
	var o = {
		"M+" : date.getMonth() + 1, //月份   
		"d+" : date.getDate(), //日 
		"h+" : date.getHours(), //小时
		"m+" : date.getMinutes(), //分   
		"s+" : date.getSeconds(), //秒  
		"q+" : Math.floor((date.getMonth() + 3) / 3), //季度   
		"S" : date.getMilliseconds() //毫秒     
	};
	if (/(y+)/.test(fmt))
		fmt = fmt.replace(RegExp.$1, (date.getFullYear() + "")
				.substr(4 - RegExp.$1.length));
	for ( var k in o)
		if (new RegExp("(" + k + ")").test(fmt))
			fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k])
					: (("00" + o[k]).substr(("" + o[k]).length)));
	return fmt;
}

var tcolumn1=[[
  			{field:'user_name',title:'用户名',width:80,sortable:true,align:'center'},
  			{field:'device',title:'使用设备',width:80,sortable:true,align:'center'},
  			{field:'city',title:'所在城市',width:80,sortable:true,align:'center'},
  			{field:'join_time',title:'加入时间',width:100,sortable:true,align:'center',
  				formatter:function(val,rec){
  					if(typeof(rec.join_time) != 'undefined'){
  						var crtTime = new Date(rec.join_time);
  					    return dateFtt("yyyy-MM-dd hh:mm:ss",crtTime);
  					}else{
  						return '';
  					}
  				}},
  			{field:'leave_time',title:'离开时间',width:100,sortable:true,align:'center',
				formatter:function(val,rec){
					if(typeof(rec.leave_time) != 'undefined'){
						var crtTime = new Date(rec.leave_time);
					    return dateFtt("yyyy-MM-dd hh:mm:ss",crtTime);
					}else{
						return '';
					}
				}}
  			]];

function getMeetingInfo(id){
	queryInit(path+'/meetingRecord_getMeetingInfo.do?meetingId='+id,tcolumn1,"","meetinglist");
	openDiv('meetingInfoWindow');
}
