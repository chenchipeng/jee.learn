
function getUploader(server, extensions, mimeTypes, elementId) {
	
	console.info(server);
	console.info(extensions);
	console.info(mimeTypes);
	console.info(elementId);
	
	// 初始化WebUploader对象
	var uploader222 = WebUploader.create({

		server : server, // 文件接收服务端
		fileVal : "file", // 后台文件接收参数的名字
		// 传入参数
		formData : {
			"foo" : ""
		},

		fileNumLimit : 5, // 上传文件个数限制
		fileSingleSizeLimit : 2 * 1024 * 1024, // 单个文件大小限制 2M
		fileSizeLimit : 1024 * 1024 * 1024, // 总大小 1024M
		// 传入文件格式限制
		accept : {
			title : 'File',
			extensions : extensions,
			mimeTypes : mimeTypes
		},

		// 分片上传设置
		chunked : false, // 允许分片
		chunkSize : 2 * 1024 * 1024, // 每片大小2M
		chunkRetry : 3, // 分片上传失败之后的重试次数
		threads : 5, // 上传并发数

		swf : 'Uploader.swf', // swf文件路径
		auto : true, // 文件进入队列后自动开始上传
		duplicate : true, // 去重
	    resize: false, // 不压缩image
		// 内部根据当前运行是创建，可能是input元素，也可能是flash.
		pick : {
			"id" : elementId,
			"multiple" : false // 禁止多选
		}

	});
}

