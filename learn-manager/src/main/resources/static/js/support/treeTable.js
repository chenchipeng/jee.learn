//初始化bootstrap-table的内容
var $table;
function initTreeTable(url, treeShowField, parentIdField) {
	// 记录页面bootstrap-table全局变量$table，方便应用
	var queryUrl = url;
	queryUrl += (queryUrl.indexOf("?") == -1 ? "?": "&");
	queryUrl += 'r=' + Math.random();
	$table = $('#contentTable').bootstrapTable({
		url : queryUrl,
		method : 'POST',
		striped : true,
		cache : false,
		pagination : false,
		sidePagination : "server",
		idField : "id",
		showRefresh : true,
		showColumns : true,
		minimumCountColumns : 2,
		toolbar : "#toolbar",
		treeShowField : treeShowField,
		parentIdField : parentIdField,
		height : initTableHeight(),
		onLoadSuccess : function() {
			$table.treegrid({
				initialState : 'collapsed',// 收缩
				treeColumn : 1,// 指明第几列数据改为树形
				expanderExpandedClass : 'glyphicon glyphicon-triangle-bottom',
				expanderCollapsedClass : 'glyphicon glyphicon-triangle-right',
				onChange : function() {
					$table.bootstrapTable('resetWidth');
				}
			});
		},
		onLoadError : function() {
			layer.msg("数据加载失败！");
		}
	});
};

// 初始化表格高度,以保证分页工具栏始终显示
function initTableHeight() {
	// 拿到父窗口的centerTabs高度(这是iframe子页面拿到父窗口元素的方法，需要根据自己项目所使用的框架自行修改元素的id)
	let panelH = $("#content-main", parent.document).height();
	if (panelH == null) {
		panelH = $(window).height();
	}
	// 拿到查询条件区域高度
	let selectParamH = $("#selectParam").height();
	if (selectParamH == null) {
		selectParamH = 0;
	}
	// 拿到顶部工具栏高度
	let toolBarH = $(".fixed-table-toolbar").height();
	// 计算表格container该设置的高度
	let height = panelH - selectParamH - toolBarH - 91 - 50;
	return height;
}

// TODO 格式化状态 如何动态的从字典获取
function statusFormatter(value, row, index) {
	if (value === '1') {
		return '是';
	} else {
		return '否';
	}
}

// 按钮添加(每一行的按钮)
function operateFormatter(value, row, index) {
	let operAry = new Array();
	$("#rowOperation").children().each(function() {
		operAry.push($(this).html() + "&nbsp;");
	});
	return operAry.join("");
}

// 按钮事件
window.operateEvents = {
	'click .edit' : function(e, value, row, index) {
		alert(row.id);
	},
	'click .del' : function(e, value, row, index) {
		alert(row.id);
	}
};