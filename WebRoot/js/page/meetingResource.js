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
			{field:'account',title:'账号',width:120,sortable:true,align:'center'},
			{field:'nickName',title:'名称',width:120,sortable:true,align:'center'},
			{field:'participants',title:'会议方数',width:120,sortable:true,align:'center'},
			{field:'status',title:'状态',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.status == '1')
						return '<font color="red">已分配</font>';
					else
						return '<font color="green">空闲</font>';
				}
			},
			{field:'isMeeting',title:'是否在开会',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.isMeeting == '1')
						return '<font color="green">是</font>';
					else
						return '<font color="red">否</font>';
				}
			},
			{field:'usedAccount',title:'被分配用户',width:120,sortable:true,align:'center'},
			{field:'usedTime',title:'被分配时间',width:120,sortable:true,align:'center'},
			{field:'addTime',title:'添加日期',width:120,sortable:true,align:'center'},
			{field:'editTime',title:'修改日期',width:120,sortable:true,align:'center'}
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
	queryInit(path+'/meetingResource_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
    var queryAccount=$('#queryAccount').val();
	$('#'+render).datagrid('reload', {"queryAccount":queryAccount});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/meetingResource_save.do";
		}else{
			url=path+"/meetingResource_update.do";
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
		     	}else if("account"==data){
		     		$.messager.alert('提示',"该账号已存在!");
		     	}else if("accountFalse"==data){
		     		$.messager.alert('提示',"请重新输入正确的会易通账号!");
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
				$('input[name="meetingResource.id"]').val(arry.id);
				$('input[name="meetingResource.comId"]').val(arry.comId);
				$('input[name="meetingResource.account"]').val(arry.account);
				$('input[name="meetingResource.password"]').val(arry.password);
				$('input[name="meetingResource.zcode"]').val(arry.zcode);
				$('input[name="meetingResource.participants"]').val(arry.participants);
				$('input[name="meetingResource.status"]').val(arry.status);
				$('input[name="meetingResource.usedAccount"]').val(arry.usedAccount);
				$('input[name="meetingResource.usedTime"]').val(arry.usedTime);
				$('input[name="meetingResource.addTime"]').val(arry.addTime);
				$('input[name="meetingResource.nickName"]').val(arry.nickName);
				$('input[name="meetingResource.isMeeting"]').val(arry.isMeeting);
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
	var queryUrl=path+'/meetingResource_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/meetingResource_deletebyids.do",{"ids":ids},function(data){
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