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
			{field:'sourceType',title:'采集或观看',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.sourceType==1)
						return '采集';
					else if(rec.sourceType==2)
						return '观看';
					else
						return '无';
				}
			},
			{field:'conType',title:'设备类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.conType==1)
						return '安卓';
					else if(rec.conType==2)
						return '苹果';
					else if(rec.conType==3)
						return 'pc电脑';
					else if(rec.conType==4)
						return '专业';
					else
						return '无';
				}
			},
			{field:'upgradeStatus',title:'升级状态',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.upgradeStatus==1)
						return '提示升级';
					else if(rec.upgradeStatus==2)
						return '强制升级';
					else
						return '无';
				}
			},
			{field:'conName',title:'名称',width:120,sortable:true,align:'center'},
			{field:'conVer',title:'版本',width:120,sortable:true,align:'center'},
			{field:'conPushTime',title:'发布时间',width:120,sortable:true,align:'center'}
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
	queryInit(path+'/control_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
    var queryComName=$('#queryComName').val();
	$('#'+render).datagrid('reload', {"queryComName":queryComName});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/control_save.do";
		}else{
			url=path+"/control_update.do";
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
				$('input[name="control.id"]').val(arry.id);
				$('input[name="control.conName"]').val(arry.conName);
				$('select[name="control.sourceType"]').val(arry.sourceType);
				$('select[name="control.conType"]').val(arry.conType);
				$('select[name="control.upgradeStatus"]').val(arry.upgradeStatus);
				$('input[name="control.conVer"]').val(arry.conVer);
				$('input[name="control.conPushTime"]').val(arry.conPushTime);
				$('input[name="control.addTime"]').val(arry.addTime);
				$('input[name="control.conPath"]').val(arry.conPath);
				
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
	var queryUrl=path+'/control_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/control_deletebyids.do",{"ids":ids},function(data){
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

function upload(){
	var url=path+'/control_upload.do';
	$('#uploadForm').form('submit', {
	    url:url,
	    onSubmit: function(){
	    },   
		success:function(data){
	    	if(data!=''){
	    		var d = eval('('+data+')');
	    		if(d!=null){
	    			if(d.key=='success'){
						$('#conPath').val(d.value);
	    				$.messager.alert('提示',"文件上传成功!");
						closeDiv('uploadWindow');
	    			}else if(d.key=='pictype'){
	    				$.messager.alert('提示',"文件格式不支持,请上传apk/exe/msi/ipa格式文件!");
	    			}else{
	    				$.messager.alert('提示',"文件上传失败!");
	    			}
	    		}
	    	}
	    }
	});
}