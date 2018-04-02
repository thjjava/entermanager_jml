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
			{field:'groupName',title:'当前组织',width:120,sortable:true,align:'center'},
			{field:'parentName',title:'上级组织',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'}
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
			id:'btnadd',
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				resetForm(wform);
				openDiv(win);
			}
		 },'-',{ 
			id:'btnedit',
			text:'修改',
			iconCls:'icon-edit',
			handler:edit
		 },'-',{ 
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
	queryInit(path+'/companyGroup_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
    var queryGroupName=$('#queryGroupName').val();
	var groupId=$("#groupId").val();
	$('#'+render).datagrid('reload', {"queryGroupName":queryGroupName,"groupId":groupId});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/companyGroup_save.do";
		}else{
			url=path+"/companyGroup_update.do";
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
		     	}else if("hased"==data){
		     		$.messager.alert('提示',"该组织名已存在!");
		     	}else{
		     		$.messager.alert('提示',"更新数据失败!");
		     	}
		    }
		})
	}
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
				$('input[name="group.id"]').val(arry.id);
				$('input[name="group.groupName"]').val(arry.groupName);
				$('input[name="group.pid"]').val(arry.pid);
				$('input[name="parentName"]').val(arry.parentName);
				$('input[name="group.addTime"]').val(arry.addTime);
				openDiv(win);
			}else{
				$.messager.alert('提示','信息不存在！');
			}
		}
  	});
}

/**
 * 修改
 */
function edit(){
	resetForm(wform);
	var rows = $('#'+render).datagrid('getSelections');
	if(rows.length==0 || rows[0].id == undefined){
		alert("请选择一条记录！");
		return;
	}
	var queryUrl=path+'/companyGroup_getbyid.do?id='+rows[0].id;
	$('#'+render).datagrid('clearSelections');
	queryObjectbyID(queryUrl);
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
				$.post(path+"/companyGroup_deletebyids.do",{"ids":ids},function(data){ 
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}
				});
			}
        }
    });
}

function isAble(id,online,isAble){
	$.post(path+"/dev_isAble.do",{"id":id,"online":online,"isAble":isAble},function(data){ 
		if(data==null || data==''){
			$.messager.alert('提示',"操作错误!");
			return;
		}
		var d = eval("("+data+")");
		if(d==null){
			$.messager.alert('提示',"操作错误!");
			return;
		}
		if(d.key=='success'){
			$.messager.alert('提示',"更新数据成功!");
			init();
		}
	});
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
	$("#parentName").val(names);
	$('#pid').val(ids);
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