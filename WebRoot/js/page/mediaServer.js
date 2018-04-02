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
			{field:'com',title:'企业',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.company!=null)
						return rec.company.comName;
					else
						return '自建';
				}
			},
			{field:'serverName',title:'服务器名称',width:120,sortable:true,align:'center'},
			{field:'realPlayUrl',title:'实时播放地址',width:120,sortable:true,align:'center'},
			{field:'recordPlayUrl',title:'录像地址',width:120,sortable:true,align:'center'},
			{field:'recordWebServiceUrl',title:'录像服务地址',width:120,sortable:true,align:'center'},
			{field:'recordDownUrl',title:'录像下载地址',width:120,sortable:true,align:'center'},
			{field:'cpuUsed',title:'cpu百分比',width:120,sortable:true,align:'center'},
			{field:'memoryUsed',title:'内存百分比',width:120,sortable:true,align:'center'},
			{field:'devNum',title:'设备在线数',width:120,sortable:true,align:'center'},
			{field:'playNum',title:'用户观看数',width:120,sortable:true,align:'center'}
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
	var data = getObject(path+'/company_getList.do');
	if(data==null || data==''){
		return;
	}
	
	var d = eval('('+data+')');
	if(d==null || d.length==0)
		return;
	
	var html = '<option value="0">--请选择--</option>';
	for(var i=0;i<d.length;i++){
		html += '<option value="'+d[i].id+'">'+d[i].comName+'</option>';
	}
	$('#company').html(html);
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/mediaServer_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
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
			url=path+"/mediaServer_save.do";
		}else{
			url=path+"/mediaServer_update.do";
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
				$('input[name="mediaServer.id"]').val(arry.id);
				if(arry.company!=null)
					$('select[name="mediaServer.company.id"]').val(arry.company.id);
				$('input[name="mediaServer.serverName"]').val(arry.serverName);
				$('input[name="mediaServer.realPlayUrl"]').val(arry.realPlayUrl);
				$('input[name="mediaServer.recordPlayUrl"]').val(arry.recordPlayUrl);
				$('input[name="mediaServer.recordWebServiceUrl"]').val(arry.recordWebServiceUrl);
				$('input[name="mediaServer.recordDownUrl"]').val(arry.recordDownUrl);
				$('input[name="mediaServer.cpuUsed"]').val(arry.cpuUsed);
				$('input[name="mediaServer.memoryUsed"]').val(arry.memoryUsed);
				$('input[name="mediaServer.devNum"]').val(arry.devNum);
				$('input[name="mediaServer.playNum"]').val(arry.playNum);
				
				$('input[name="mediaServer.addTime"]').val(arry.addTime);
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
	var queryUrl=path+'/mediaServer_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/mediaServer_deletebyids.do",{"ids":ids},function(data){ 　　
					if("success"==data){
						$('#'+render).datagrid('clearSelections');
			     		$.messager.alert('提示',"更新数据成功!");
			     		init();
			     	}
				}) 
			}
        }
    });
}