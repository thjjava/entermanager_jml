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
			{field:'type',title:'类型',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.type == '0'){
						return '是非题';
					}else if(rec.type == '1'){
						return '问答题';
					}else if(rec.type == '2'){
						return '选择题';
					}else {
						return '其他';
					}
				}	
			},
			{field:'question',title:'问题',width:120,sortable:true,align:'center'},
			{field:'status',title:'状态',width:120,sortable:true,align:'center',
				formatter:function(val,rec){
					if(rec.status == '0'){
						return '<font color="green">正常</font>';
					}else{
						return '<font color="red">作废</font>';
					}
				}	
			},
			{field:'editUser',title:'创建者',width:120,sortable:true,align:'center'},
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
				$("#tr1,#tr2,#tr3,#tr4").hide();
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
			text:'作废',
			iconCls:'icon-remove',
			handler:setInvalid
		}
		 /*{ 
			id:'btnremove',
			text:'删除',
			iconCls:'icon-remove',
			handler:deleteobj
		 }*/];
		 
/**
 * 加载初始化
 */
$(function(){
	init();
	$("#tr1,#tr2,#tr3,#tr4").hide();
}); 

/**
 * 刷新列表
 */
function init(){
	queryInit(path+'/question_query.do?timestamp=' + new Date().getTime(),tcolumn,tbar,render);
}

/**
 * 查询
 */
function query(){
	$('#'+render).datagrid('reload', {"type":$("#queryType").val()});
}


/**
 * 增加和修改操作
 */
function submitForm(){
	if($('#'+wform).form('validate')){
		var url="";
		var id=$('#id').val();
		if(id==''){
			url=path+"/question_save.do";
		}else{
			url=path+"/question_update.do";
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
				$('input[name="question.id"]').val(arry.id);
				$('input[name="question.comId"]').val(arry.comId);
				$('input[name="question.question"]').val(arry.question);
				$('input[name="question.status"]').val(arry.status);
				$('input[name="question.editUser"]').val(arry.editUser);
				$('select[name="question.type"]').val(arry.type);
				$('input[name="question.addTime"]').val(arry.addTime);
				$('input[name="question.answer1"]').val(arry.answer1);
				$('input[name="question.answer2"]').val(arry.answer2);
				$('input[name="question.answer3"]').val(arry.answer3);
				$('input[name="question.answer4"]').val(arry.answer4);
				if(arry.type==2){
					$("#tr1,#tr2,#tr3,#tr4").show();
				}else{
					$("#tr1,#tr2,#tr3,#tr4").hide();
				}
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
	var queryUrl=path+'/question_getbyid.do?id='+rows[0].id;
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
				$.post(path+"/question_deletebyids.do",{"ids":ids},function(data){
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

function setInvalid(){
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
		$.post(path+"/question_setInvalid.do",{"ids":ids},function(data){
			if("success"==data){
				$('#'+render).datagrid('clearSelections');
	     		$.messager.alert('提示',"更新数据成功!");
	     		init();
	     	}
		}); 
	}
}

function showOrhide(){
	var type = $("#type").val();
	if(type==2){
		$("#tr1,#tr2,#tr3,#tr4").show();
	}else{
		$("#tr1,#tr2,#tr3,#tr4").hide();
	}
}