//初始化Packing list_uploader对象
var Packing_list_uploader = WebUploader.create({
	// swf文件路径
	swf : 'Uploader.swf',
	// 文件接收服务端。
	server : 'UploadOrderFile.action',
	// 选择文件的按钮。可选。
	// 内部根据当前运行是创建，可能是input元素，也可能是flash.
	pick : {
		"id" : '#packinglist_show_falsh',
		"multiple" : false
	// 禁止多选。
	},

	// 文件进入队列后自动开始上传
	auto : true,
	// 分片上传设置
	chunked : true, // 允许分片
	chunkSize : 2 * 1024 * 1024, // 每片大小2M
	chunkRetry : 3, // 分片上传失败之后的重试次数
	threads : 5, // 上传并发数。允许同时最大3个上传进程
	// 去重
	duplicate : true,

	// 上传文件个数限制
	fileNumLimit : 20,
	// 单个文件大小限制 20M
	fileSingleSizeLimit : 20 * 1024 * 1024,
	// 传入文件格式限制，只能上传doc,docx,pdf格式的文件
	accept : {
		title : 'Applications',
		extensions : 'doc,docx,pdf',
		mimeTypes : 'application/pdf, application/msword'
	},

	// 重要参数:跟后台文件组件的对接参数，后台文件组件叫做upload。
	// fileVal : "upload",

	// 传入参数。这两个参数会跟文件一起传给后台，用于跟后台对接，确认文件的来源。
	formData : {
		// 这里的两个参数是为了跟后台对接，让后台知道上传的文件存在哪里。
		"ttype" : "qc_packinglist",
		"task" : "createqc"
	},
});
