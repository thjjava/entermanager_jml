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
			{field:'roleName',title:'角色名称',width:120,sortable:true,align:'center'},
			{field:'roleType',title:'角色类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.roleType==1){
						return '<font color="red">用户</font>';
					}else{
						return '<font color="green">管理员</font>';
					}
				}},
			{field:'remark',title:'角色说明',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'创建时间',width:120,sortable:true,align:'center'},
			{field:'oper',title:'操作',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					return '<a href="javascript:void(0);" style="text-decoration: none;" onclick="getMenus(\''+rec.id+'\',\''+rec.roleType+'\');">分配权限</a>';
				}
			}
			]];
			
/**
 * 菜单栏
 */
var tbar=[{ 
			id:'btnadd',
			text:'新增',
			iconCls:'icon-add',
			handler:function(){
				$('#comtr').hide();
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
	queryInit(path+'/role_query.do?timestamp='+ new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	var queryRoleName=$('#queryRoleName').val();
	$('#'+render).datagrid('reload', {"queryRoleName":queryRoleName});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/role_save.do";
		}else{
			url=path+"/role_update.do";
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
		     	}else if('account'){
		     		$.messager.alert('提示',"帐号已经存在!");
		     	}else{
		     		$.messager.alert('提示',"更新数据失败!");
		     	}
		    }
		});
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
				$('input[name="role.id"]').val(arry.id);
				$('input[name="role.roleName"]').val(arry.roleName);
				$('input[name="role.remark"]').val(arry.remark);
				$('input[name="role.addTime"]').val(arry.addTime);
				$('select[name="role.roleType"]').val(arry.roleType);
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
	if(rows.length==0){
		alert("请选择一条记录！");
		return;
	}
	var queryUrl=path+'/role_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/role_deletebyids.do",{"ids":ids},function(data){
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


var menus='',roleId='';

function setPermisson(){
	$.post(path+"/role_setPermisson.do",{"menus":menus,'roleId':roleId},function(data){
		if("success"==data){
			closeDiv('menusWindow');
			$('#'+render).datagrid('clearSelections');
     		$.messager.alert('提示',"更新数据成功!");
     		init();
     	}else{
     		$.messager.alert('提示',"更新数据失败!");
     	}
	});
}


function getMenus(id,type){
	roleId = id;
	$.ajax({
		type:'POST',
		url:path+'/menu_queryMenus.do',
		data:"roleId="+id+"&roleType="+type,
		success:function(data){
			if(data !=''){
				var arry = eval('('+data+')');
				$.fn.zTree.init($("#menustree"), groupsetting, arry);
				openDiv('menusWindow');
			}
		}
  	});
}


var groupsetting = {
	check: {
		enable: true,
		chkStyle: "checkbox",
		cascadeCheck: false
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
	var zTree = $.fn.zTree.getZTreeObj("menustree");
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}

function onGroupCheck(e, treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj("menustree"),
	nodes = zTree.getCheckedNodes(true),
	ids='';
	for (var i=0, l=nodes.length; i<l; i++) {
		ids += nodes[i].id + ',';
	}
	if (ids.length > 0 ) ids = ids.substring(0, ids.length-1);
	menus = ids;
}

