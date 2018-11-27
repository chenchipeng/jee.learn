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
};

// layer iframe 弹出层
function showView(url, title, width, height) {

	layer.open({
		type : 2,
		title : title,
		shadeClose : false,
		shade : 0.5,
		area : [ width, height ],
		content : url, // iframe的url
		id : 'layer_iframe',
		maxmin : true
	});
}
