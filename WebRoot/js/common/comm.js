/**
 * 数据列表
 * @param {} requestUrl
 * @param {} tcolumn
 * @param {} tbar
 * @param {} id
 */
function queryInit(requestUrl,tcolumn,tbar,id){
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

//判断值是否为“-”
function checknull(val){
	if(val=='-'){
	   val="";
    }
	return val;
}
		
//关闭 strs div id 如：#divid1
function openDiv(id){
	setWindow(id);
	$('#'+id).css('display','block');
	$('#'+id).window('open');
}

//关闭 strs div id 如：#divid1
function closeDiv(id){
	$('#'+id).window('close');
}

//设置窗体
function setWindow(id){
	var height=$('#'+id).height();
	var width=$('#'+id).width();
	$('#'+id).window({
//	 top:($(document).height()-height) * 0.5,
	 top:100,
//     left:($(document).width()-width) * 0.5,
	 left:30,
     modal:true,
     draggable:true,
     shadow:true
	});
}
           
//参数加密
function escapeParam(value){
   return escape(escape(value));
}

//重置  id 表单id
function resetForm(id) {
	$('#'+id).each(function(){
		this.reset();
	});
} 


/**
 * 格式化性别
 * @param {} val
 * @param {} rec
 * @return {}
 */
function formatSex(val,rec){  
    if(rec.sex==0){
    	val = "男";  
    }else if(rec.sex==1){
    	val = "女";  
    }
	return val ;
}