/**
 * zTree控件树形菜单公共属性设置
 * */
 
 var commonSetting = {
	check: {
		enable: true,
		chkboxType: {"Y":"ps", "N":"ps"}
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
		beforeClick: beforeClick,
		onCheck: onCheck
	}
};
function beforeClick(treeId, treeNode) {
	var zTree = $.fn.zTree.getZTreeObj(treeId);
	zTree.checkNode(treeNode, !treeNode.checked, null, true);
	return false;
}



