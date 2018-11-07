$(function() {

	// 绑定按钮事件-控制编辑区显隐
	$("#cancelBtn,#formBtn").on("click", function() {
		formViewDisplsy();
	});
	// 绑定按钮事件-提交表单
	$("#submitBtn").on("click", function() {
		submitForm();
	});

	// 控制编辑区显隐
	$("#form").css("display", "none");
	function formViewDisplsy() {
		if ($("#view").css("display") == "block") {
			$("#view").css("display", "none");
			$("#form").css("display", "block");
		} else {
			$("#view").css("display", "block");
			$("#form").css("display", "none");
		}
	}

	// 清空密码
	$("#password").val("");
	// 表单校验
	$("#editForm").validate({
		rules: {
			name: {
				required: true,
				maxlength: 16
			}, password: {
				maxlength: 32
			}, confirm_password: {
				equalTo: "#password",
				maxlength: 32
			}, phone: {
				maxlength: 11,
				digits: true
			}, mobile: {
				maxlength: 11,
				digits: true
			}, email: {
				maxlength: 32
			}
		}
	});

});

/* 表单提交 */
function submitForm() {
	// md5加密
	$("#password").val(md5($("#password").val()));
	$("#confirm_password").val($("#password").val());
	
	$("#editForm").submit();
}