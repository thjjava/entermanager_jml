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
			{field:'group',title:'组织',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.group!=null)
						return rec.group.groupName;
					else
						return '无';
				}
			},
			{field:'devName',title:'设备名称',width:120,sortable:true,align:'center'},
			{field:'devNo',title:'设备号',width:120,sortable:true,align:'center'},
			//{field:'imsi',title:'imsi',width:120,sortable:true,align:'center'},
			//{field:'phone',title:'手机号',width:120,sortable:true,align:'center'},
			{field:'publishUrl',title:'发布地址',width:120,sortable:true,align:'center'},
			{field:'onLines',title:'是否在线',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.onLines==0)
						return '在线';
					else
						return '离线';
				}
			},
			{field:'isAble',title:'启用状态',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.isAble==0)
						return '启用';
					else
						return '禁用';
				}
			},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'},
			{field:'oper',title:'操作',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					var value = '<a href="javascript:void(0);" style="text-decoration: none;" onclick="resetPwd(\''+rec.id+'\');">重置密码</a>|';
					if(rec.onLines==1){
						if(rec.isAble==0){
							return value+'<a href="javascript:void(0);" style="text-decoration: none;" onclick="isAble(\''+rec.id+'\','+rec.onLines+',1);">禁用</a>';
						}else{
							return value+'<a href="javascript:void(0);" style="text-decoration: none;" onclick="isAble(\''+rec.id+'\','+rec.onLines+',0);">启用</a>';
						}
					}else{
						return value+'无操作';
					}
				}
			}
			]];
			
/**
 * 菜单栏
 */
var tbar=[{
			id:'btnedit',
			text:'分配组织',
			iconCls:'icon-edit',
			handler:createGroupTree
		 },'-',
		 { 
			id:'btnedit',
			text:'修改',
			iconCls:'icon-edit',
			handler:edit
		 },'-',
		 { 
			id:'btnadd',
			text:'批量导出',
			iconCls:'icon-add',
			handler:exportExcel},'-',
		{ 
			id:'btnadd',
			text:'批量导入',
			iconCls:'icon-add',
			handler:function(){
				openDiv('importWindow');
			}
		}];
		 
/**
 * 加载初始化
 */
$(function(){
	init();
	$.post(path+'/dev_queryCom.do',function(data){
		var data = eval('('+data+')');
		if(data !=null){
			var html='';
			for(var i=0;i<data.length;i++){
				html+='<option value="'+data[i].id+'">'+data[i].comName+'</option>';
			}
//			$("#channelid").html('<option value="'+-1+'">'+"--请选择通道编号--"+'</option>');
			$("#company").append(html);
		}
	});
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/dev_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
    var queryDevNo=$('#queryDevNo').val();
    var groupId=$("#groupId").val();
	$('#'+render).datagrid('reload', {"queryDevNo":queryDevNo,"queryDevName":$('#queryDevName').val(),'queryIsGroup':$('#queryIsGroup').val(),"groupId":groupId,'queryIsAble':$('#queryIsAble').val()});
}

function isAble(id,online,isAble){
	$.post(path+"/dev_isAble.do",{"id":id,"online":online,"isAble":isAble},function(data){ 
		if(data==null || data==''){
			$.messager.alert('提示',"操作错误!");
			return;
		}
		if(data=='success'){
			$.messager.alert('提示',"更新数据成功!");
			init();
		}
	});
}

var ids='';

function fenpei(){
	if(groupId==''){
		$.messager.alert('提示',"请选择分组!");
		return;
	}
	$.post(path+"/dev_group.do",{"ids":ids,'groupId':groupId},function(data){
		if("success"==data){
			closeDiv('groupWindow');
			$('#'+render).datagrid('clearSelections');
     		$.messager.alert('提示',"更新数据成功!");
     		init();
     	}else{
     		$.messager.alert('提示',"更新数据失败!");
     	}
	});
}


function createGroupTree(){
	var rows = $('#'+render).datagrid('getSelections');
	if(rows.length==0){
		$.messager.alert('提示',"请选择设备!");
		return;
	}
	ids='';
	for(var i=0;i<rows.length;i+=1){
		if(i==0){
			ids=rows[i].id;
		}else{
			ids+="_"+rows[i].id;
		}
	}
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
		dblClickExpand: false,
		showIcon: false
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
		ids += nodes[i].id + ',';
	}
	if (ids.length > 0 ) ids = ids.substring(0, ids.length-1);
	groupId = ids;
}

var groupId = '';

/**
 * 修改
 */
function edit(){
	resetForm(wform);
	var rows = $('#'+render).datagrid('getSelections');
	if(rows.length==0){
		alert("请选择一条记录！");
		return;
	}
	var queryUrl=path+'/dev_getbyid.do?id='+rows[0].id;
	$('#'+render).datagrid('clearSelections');
	queryObjectbyID(queryUrl);
}


/**
 * 获取详细信息
 * @param {} url
 */
function queryObjectbyID(url){
	$.ajax({
		type:'POST',
		url:url,
		success:function(msg){
			if(msg !=''){
				var arry = eval("("+msg+")");
				$('select[name="dev.company.id"]').val(arry.company.id);
				$('input[name="dev.id"]').val(arry.id);
				$('input[name="dev.devName"]').val(arry.devName);
				$('input[name="dev.devNo"]').val(arry.devNo);
				$('input[name="dev.devKey"]').val(arry.devKey);
				$('input[name="pwd1"]').val(arry.devKey);
				$('input[name="dev.publishUrl"]').val(arry.publishUrl);
				$('input[name="dev.imsi"]').val(arry.imsi);
				$('input[name="dev.phone"]').val(arry.phone);
				$('input[name="dev.audioRtpPort"]').val(arry.audioRtpPort);
				$('input[name="dev.audioRtcpPort"]').val(arry.audioRtcpPort);
				$('input[name="dev.videoRtpPort"]').val(arry.videoRtpPort);
				$('input[name="dev.videoRtcpPort"]').val(arry.videoRtcpPort);
				$('input[name="dev.serverId"]').val(arry.serverId);
				$('input[name="dev.drId"]').val(arry.drId);
				$('input[name="dev.onLines"]').val(arry.onLines);
				$('input[name="dev.isAble"]').val(arry.isAble);
				$('input[name="dev.lastLoginTime"]').val(arry.lastLoginTime);
				$('input[name="dev.addTime"]').val(arry.addTime);
				if(arry.group != null){
					$('input[name="dev.group.id"]').val(arry.group.id);
				}
				openDiv(win);
			}else{
				$.messager.alert('提示','信息不存在！');
			}
		}
  	});
}

/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/dev_save.do";
		}else{
			url=path+"/dev_update.do";
		}
		$('#'+wform).form('submit', {
		    url:url,
		    onSubmit: function(){
		    },   
			success:function(data){
		     	if("success"==data){
		     		$.messager.alert('提示',"更新数据成功!");
			     	resetForm(wform);
					closeDiv(win);
					init();
		     	}else if("devNo"==data){
		     		$.messager.alert('提示',"当前设备编号已经存在!");
		     	}else if("devName"==data){
		     		$.messager.alert('提示',"当前设备名称已经存在!");
		     	}else{
		     		$.messager.alert('提示',"更新数据失败!");
		     	}
		    }
		});
	}
}

/**
 * 
 * 重置设备密码
 * 
 */
function resetPwd(devId){
	$.post(path+"/dev_resetPwd.do",{"id":devId},function(data){ 
		if(data==null || data==''){
			$.messager.alert('提示',"操作错误!");
			return;
		}
		if(data=='success'){
			$.messager.alert('提示',"设备密码重置成功!");
//			init();
		}
	});
}

//查询条件组织
function createGroupTree2(){
	$.ajax({
		type:'POST',
		url:path+'/companyGroup_getTree.do',
		success:function(data){
			if(data !=''){
				var arry = eval('('+data+')');
				$.fn.zTree.init($("#grouptree2"), groupsetting2, arry);
				openDiv('groupWindow2');
			}
		}
  	});
}

var groupsetting2 = {
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
		onClick: onGroupClick2,
		onCheck: onGroupCheck2
	}
};

function onGroupClick2(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("grouptree2");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onGroupCheck2(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("grouptree2"),
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

/**
 * 导出Excel
*/
function exportExcel(){
	var devNo = $('#queryDevNo').val();
	var devName = $('#queryDevName').val();
	var isGroup = $('#queryIsGroup').val();
	var groupId=$("#groupId").val();
	window.location.href=path+'/dev_exportDevsToExcel.do?devNo='+devNo+'&devName='+devName+'&groupId='+groupId+"&isGroup"+isGroup;
}

/**
 * 将excel数据导入到数据库中
 */
function importExcel(){
	var file=$("#upload").val();
	if(file==''){
		alert("请选择上传的文件!");
		return;
	}
	var url=path+'/dev_upload.do';
	$('#uploadForm').form('submit', {
	    url:url,
	    onSubmit: function(){
	    },   
		success:function(data){
	    	if(data!=''){
	    		var d = eval('('+data+')');
	    		if(d!=null){
	    			if(d.key=='success'){
	    				var msg = d.msg;
	    				if(msg.split(',').length >1){
	    					$.messager.alert('提示',msg);
	    				}else{
	    					$.messager.alert('提示',"导入成功!");
	    				}
	    				closeDiv('importWindow');
	    				init();
	    			}else if(d.key=='pictype'){
	    				$.messager.alert('提示',"文件格式不支持,请导入xls格式文件!");
	    			}else{
	    				$.messager.alert('提示',"导入失败!");
	    			}
	    		}
	    	}
	    }
	})
}