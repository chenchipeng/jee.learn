
/*将文本内容写进文件并下载*/
function downloadContentFile(fileName, content) {
	var aLink = document.createElement('a');
	var blob = new Blob([ content ]);
	var evt = document.createEvent("HTMLEvents");
	evt.initEvent("click", false, false);// initEvent 不加后两个参数在FF下会报错
	aLink.download = fileName;
	aLink.href = URL.createObjectURL(blob);
	aLink.dispatchEvent(evt);
}