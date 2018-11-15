$(function() {

	// 绑定按钮事件-点击头像上传新头像
	$("#photo").on("click", function() {
		$(".webuploader-element-invisible").click();
	});
	// 绑定按钮事件-取消提交表单
	$("#cancelBtn").on("click", function() {
		window.history.go(-1);
	});
	// 绑定按钮事件-提交表单
	$("#submitBtn").on("click", function() {
		submitForm();
	});
	// 清空密码
	$("#password").val("");
	// 表单校验
	$("#editForm").validate({
		rules : {
			name : {
				required : true,
				maxlength : 16
			},
			password : {
				maxlength : 32
			},
			confirm_password : {
				equalTo : "#password",
				maxlength : 32
			},
			phone : {
				maxlength : 11,
				digits : true
			},
			mobile : {
				maxlength : 11,
				digits : true
			},
			email : {
				maxlength : 32
			}
		}
	});

	// ////// 文件上传 ////////

	// 服务器地址
	var fileServer = $("#fileServer").html(),
	// swf文件路径
	swfPath = $("#swfPath").html(),
	// 文件类型
	fileExtensions = "jpg,jpeg,png", fileMimeTypes = "image/jpeg,image/png",
	// 分片GUID
	filePicker = "#photoSelecter";

	// 初始化 webuploader, 参数在html中传值
	var uploader = getUploader(fileServer, swfPath, fileExtensions,
			fileMimeTypes, filePicker);

	// 当文件上传成功时触发
	uploader.on('uploadSuccess', function(file, response) {
		if (response.c === "200") {
			// 更新图片和记录
			let isOK = updatePicRecord(response.d.path);
//			if(isOK){
//				layer.msg('上传成功！');
//			}else{
//				layer.msg('上传异常！');
//			}
			layer.msg('上传成功！');
		} else {
			layer.msg('上传失败！ ' + response.e);
			console.info(JSON.stringify(response));
		}
	});
	// 当文件上传出错时触发
	uploader.on('uploadError', function(file, reason) {
		layer.msg('文件上传出错，请联系开发员！');
	});
	// 不管成功或者失败，文件上传完成时触发
	uploader.on('uploadComplete', function(file) {
	});

});

/* 控制编辑区显隐 */
function formViewDisplsy() {
	if ($("#view").css("display") == "block") {
		$("#view").css("display", "none");
		$("#form").css("display", "block");
	} else {
		$("#view").css("display", "block");
		$("#form").css("display", "none");
	}
}

/* 表单提交 */
function submitForm() {
	// md5加密
	$("#password").val(md5($("#password").val()));
	$("#confirm_password").val($("#password").val());

	$("#editForm").submit();
}

/* 更新头像图片和记录 */
function updatePicRecord(path) {
	// 换头像
	$("#photo").attr("src", path);
	// 存记录
	let josn = {"path" : path};
	$.ajax({
    	url: $("#updatePic").html(),
    	type: 'post',
    	contentType: 'application/json;charset=UTF-8',
    	data: path,
        dataType: 'json',
        success: function(data){
        	if(data.c != "200"){
        		layer.msg("更新头像图片和记录失败, 请联系开发员");
        		console.info(data);
        	}
        }
	});
}
