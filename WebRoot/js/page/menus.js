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
			{field:'name',title:'名称',width:120,sortable:true,align:'center'},
			{field:'type',title:'类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.type==0)
						return '<font color="green">菜单</font>';
					else if(rec.type==1)
						return '<font color="red">按钮</font>';
					else
						return '';
				}
			},
			{field:'pId',title:'关系',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.type==0){
						if(rec.pId == 0){
							return '<font color="red">父菜单</font>';
						}else {
							return '<font color="green">子菜单</font>';
						}
					}
					
				}},
			/*{field:'url',title:'路径',width:120,sortable:true,align:'center'},*/
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
	queryInit(path+'/menu_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
	$.post(path+'/menu_getParentMenus.do?timestamp=' + new Date().getTime(),function(data){
		var obj = eval('('+data+')');
		if(obj !=null){
			var html='';
			$("#pId").html('<option value="0">请选择</option>');
			for(var i=0;i<obj.length;i++){
				html+='<option value="'+obj[i].id+'">'+obj[i].name+'</option>';
			}
			$("#pId").append(html);
		}
	});
}

/**
 * 查询
 */
function query(){
	var queryName=$('#queryName').val();
	$('#'+render).datagrid('reload', {"queryName":queryName});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/menu_save.do";
		}else{
			url=path+"/menu_update.do";
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
		     	}else if('hased'==data){
		     		$.messager.alert('提示',"该菜单已经存在!");
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
				$('input[name="menus.id"]').val(arry.id);
				$('select[name="menus.type"]').val(arry.type);
				$('input[name="menus.name"]').val(arry.name);
				$('input[name="menus.url"]').val(arry.url);
				$('select[name="menus.pId"]').val(arry.pId);
				$('input[name="menus.sort"]').val(arry.sort);
				$('input[name="menus.addTime"]').val(arry.addTime);
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
	var queryUrl=path+'/menu_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/menu_deletebyids.do",{"ids":ids},function(data){
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

