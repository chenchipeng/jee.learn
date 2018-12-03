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

			let body = top.layer.getChildFrame('body', index);
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
				top.layer.close(index);
			}
			
		},
		btn2 : function(index, layero) {
			layer.close(index);
		}
	});
}
