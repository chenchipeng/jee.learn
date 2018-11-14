function getUploader(fileServer, swfPath, fileExtensions, fileMimeTypes,
		filePicker) {

	let fileCount = 5;// 上传文件个数限制
	let fileSize = 2; // 单文件大小(M)
	let fileTotalSize = 5; // 文件总大小(M)

	var uploader;
	// 实例化
	uploader = WebUploader.create({

		// 文件接收服务端
		server : fileServer,
		// 传入文件格式限制
		accept : {
			title : 'File',
			extensions : fileExtensions,
			mimeTypes : fileMimeTypes
		},
		// 传入参数
		formData : {
			guid : WebUploader.Base.guid()
		// 分片标识
		},
		// 上传触发控件
		pick : {
			id : filePicker,
			multiple : false
		// 禁止多选
		},
		// 大小设置
		fileNumLimit : fileCount, // 上传文件个数限制
		fileSingleSizeLimit : fileSize * 1024 * 1024, // 单个文件大小限制 2M
		fileSizeLimit : fileTotalSize * 1024 * 1024, // 总大小 5M
		// 分片上传设置
		chunked : false, // 允许分片
		chunkSize : 2 * 1024 * 1024, // 每片大小2M
		chunkRetry : 3, // 分片上传失败之后的重试次数
		threads : 5, // 上传并发数
		// 杂项
		swf : swfPath, // swf文件路径
		auto : true, // 文件进入队列后自动开始上传
		duplicate : true, // 去重
		compress : false,// 不启用压缩
		resize : false
	// 尺寸不改变

	});

	// 错误提示
	uploader.onError = function(code) {
		if (code == "Q_EXCEED_NUM_LIMIT") {
			alert('Eroor: 单次上传仅支持 ' + fileCount + ' 个文件');
		} else if (code == "Q_TYPE_DENIED") {
			alert('Eroor: 仅支持 ' + fileExtensions + ' 格式的文件');
		} else if (code == "Q_EXCEED_SIZE_LIMIT" || code == "F_EXCEED_SIZE") {
			alert('Eroor: 上传的文件过大!请控制在 ' + fileSize + ' M 以内');
		} else {
			alert('Eroor: ' + code);
		}
	};

	return uploader;
}
