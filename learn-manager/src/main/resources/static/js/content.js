//获取全局URL标识
$.ajax({
	url : "/authcPath",
	contentType : "application/json;charset=UTF-8",
	success : function(result) {
		$(".gohome a").attr("href", result);
	}
});

// 判断当前页面是否在iframe中
if (top == this) {
	var gohome = '<div class="gohome"><a class="animated bounceInUp" href="#" title="返回首页"><i class="fa fa-home"></i></a></div>';
	$('body').append(gohome);

}

// animation.css
function animationHover(element, animation) {
	element = $(element);
	element.hover(function() {
		element.addClass('animated ' + animation);
	}, function() {
		// 动画完成之前移除class
		window.setTimeout(function() {
			element.removeClass('animated ' + animation);
		}, 2000);
	});
}

// 拖动面板
function WinMove() {
	var element = "[class*=col]";
	var handle = ".ibox-title";
	var connect = "[class*=col]";
	$(element).sortable({
		handle : handle,
		connectWith : connect,
		tolerance : 'pointer',
		forcePlaceholderSize : true,
		opacity : 0.8,
	}).disableSelection();
}

// 页面加载后动作...
$(document).ready(function() {
	// 下拉框基础设置
	$(".chosen-select").chosen({
		no_results_text : "没有找到结果！",// 搜索无结果时显示的提示
		search_contains : true, // 关键字模糊搜索，设置为false，则只从开头开始匹配
		allow_single_deselect : true, // 是否允许取消选择
		inherit_select_classes : true, // 继承普通select的class
		disable_search_threshold : 5
	});
	$(".chosen-select").css("max-width", "200px");
	$(".chosen-select").css("min-width", "70%");
});

// 以下为修改jQuery Validation插件兼容Bootstrap的方法，没有直接写在插件中是为了便于插件升级
$.validator.setDefaults({
	highlight : function(element) {
		$(element).closest('.form-group').removeClass('has-success').addClass(
				'has-error');
	},
	success : function(element) {
		$(element).closest('.form-group').removeClass('has-error').addClass(
				'has-success');
	},
	errorElement : "span",
	errorPlacement : function(error, element) {
		if (element.is(":radio") || element.is(":checkbox")) {
			error.appendTo(element.parent().parent().parent());
		} else {
			error.appendTo(element.parent());
		}
	},
	errorClass : "help-block m-b-none",
	validClass : "help-block m-b-none"
});

// layer 关闭弹出窗
function closeLayer() {
	let index = parent.layer.getFrameIndex(window.name);
	setTimeout(function() {
		parent.layer.close(index)
	}, 500);
}

// layer iframe 弹出层
function showView(url, title, width, height) {

	layer.open({
		type : 2,
		title : title,
		shadeClose : true,
		shade : 0.5,
		area : [ width, height ],
		content : url, // iframe的url
		id : 'layer_iframe',
		maxmin : true,
		btn : [ '关闭' ],
		yes : function(index, layero) {
			layer.close(index);
		}
	});
}

// layer iframe 弹出层
function showForm(url, title, width, height, target) {

	layer.open({
		type : 2,
		title : title,
		shadeClose : true,
		shade : 0.5,
		area : [ width, height ],
		content : url, // iframe的url
		id : 'layer_iframe',
		maxmin : true,
		btn : [ '保存', '关闭' ],
		yes : function(index, layero) {

			let body = layer.getChildFrame('body', index);
			let contentForm = body.find('#contentForm');
			let iframeWin = layero.find('iframe')[0];

			let top_iframe;
			if (target) {
				top_iframe = target;// 如果指定了iframe，则在改frame中跳转
			} else {
				top_iframe = 'J_iframe';// 主窗体
			}
			contentForm.attr("target", top_iframe);// 表单提交成功后，从服务器返回的url在当前tab中展示

			if (iframeWin.contentWindow.doSubmit()) {
				layer.close(index);
			}
		},
		btn2 : function(index, layero) {
			layer.close(index);
		}
	});
}

// 从图表库中选择图标, iconId=图标值, iconViewId=显示图标的容器
function iconSelecter(url, title, width, height, iconId, iconViewId) {

	top.layer.open({
		type : 2,
		title : title,
		shadeClose : true,
		shade : 0.5,
		area : [ width, height ],
		content : url, // iframe的url
		id : 'layer_iframe_icon',
		maxmin : true,
		btn : [ '保存', '关闭' ],
		yes : function(index, layero) {

			let body = top.layer.getChildFrame('body', index);
			let element = body.find('#icon');

			let hml = "&nbsp;&nbsp;<i class='" + element.val()
					+ "'>&nbsp;&nbsp;";
			$("#" + iconId).val(element.val());
			$("#" + iconViewId).html(hml);
			top.layer.close(index);
		},
		btn2 : function(index, layero) {
			top.layer.close(index);
		}
	});
}

// 根据类型获取字典列表
function getDictList(url, type, selectId) {
	$.ajax({
		url : url,
		type : 'post',
		contentType : 'application/json;charset=UTF-8',
		data : JSON.stringify({
			"h" : {
				"t" : "oQXWv0mE9Q0vTDNqKl2uFXMMuspI"
			},
			"d" : {
				"a" : 1,
				"t" : type
			}
		}),
		dataType : 'json',
		success : function(data) {
			if (data.c == "200") {
				let hml = "";
				for (var i = 0; i < data.d.l.length; i++) {
					hml += "<option value='" + data.d.l[i].value + "'>"
							+ data.d.l[i].label + "</option>";
				}
				$("#" + selectId).append(hml);
				$("#" + selectId).trigger("chosen:updated");
				$("#" + selectId + "_chosen .chosen-search-input").attr("name",
						selectId + "-input");
			} else {
				console.info(data);
			}
		},
		error : function(request, status, error) {
			// 当返回的数据无法转换成json时, 强制刷新页面
			if (request.statusText == "parsererror") {
				location.reload();
			}
		}
	});
}
