/*******************************************************************************
 * 
 * jQuery Validate扩展验证方法 (chenchipeng)
 ******************************************************************************/

/*******************************************************************************
 * 
 * 注意: 以"is"开头的可直接放进"class"里面使用, 其它则建议放js里面以便传入参数值
 ******************************************************************************/

// 字节大小验证, 中文字两个字节
jQuery.validator.addMethod("byteRangeLength",
		function(value, element, param) {
			var length = value.length;
			for (var i = 0; i < value.length; i++) {
				if (value.charCodeAt(i) > 127) {
					length++;
				}
			}
			return this.optional(element)
					|| (length >= param[0] && length <= param[1]);
		}, $.validator.format("请确保输入的值在{0}-{1}个字节之间(一个中文字算2个字节)"));

// 小数验证, 只能输入数字. 参数格式[最小值, 最大值, 小数点后几位]
$.validator.addMethod("decimal", function(value, element, params) {

	if (params != undefined && typeof (params) == "string"
			&& params.startsWith("[")) {
		params = eval(params);
	}
	// debugger;
	if (isNaN(params[0])) {
		console.error("参数错误，decimal验证的min只能为数字");
		return false;
	}
	if (isNaN(params[1])) {
		console.error("参数错误，decimal验证的max只能为数字");
		return false;
	}
	if (isNaN(params[2])) {
		console.error("参数错误，decimal验证的accuracy只能为数字");
		return false;
	}
	if (isNaN(value)) {
		return false;
	}
	var min = Number(params[0]);
	var max = Number(params[1]);
	var testVal = Number(value);
	var testAbsVal = Math.abs(testVal);
	if (typeof (params[2]) == undefined || params[2] == 0) {
		var regX = /^\d+$/;
	} else {
		var regxStr = "^\\d+(\\.\\d{1," + params[2] + "})?$";
		var regX = new RegExp(regxStr);
	}
	return this.optional(element)
			|| ((testVal + "") == value && regX.test(testAbsVal)
					&& testVal >= min && testVal <= max);
}, $.validator.format("请输入在{0}到{1}之间且保留小数点后{2}位的值"));

// 整数验证, 只能输入数字. 参数格式[最小值, 最大值]
$.validator.addMethod("int", function(value, element, params) {

	if (params != undefined && typeof (params) == "string"
			&& params.startsWith("[")) {
		params = eval(params);
	}
	// debugger;
	if (isNaN(params[0])) {
		console.error("参数错误，decimal验证的min只能为数字");
		return false;
	}
	if (isNaN(params[1])) {
		console.error("参数错误，decimal验证的max只能为数字");
		return false;
	}
	if (isNaN(value)) {
		return false;
	}
	var min = Number(params[0]);
	var max = Number(params[1]);
	var testVal = Number(value);
	testVal = parseInt(testVal);
	var testAbsVal = Math.abs(testVal);
	return this.optional(element)
			|| ((testVal + "") == value && testVal >= min && testVal <= max);
}, $.validator.format("请输入在{0}到{1}之间的整数"));

// 邮政编码验证
jQuery.validator.addMethod("isZipCode", function(value, element) {
	var tel = /^[0-9]{6}$/;
	return this.optional(element) || (tel.test(value));
}, "请正确填写邮政编码");

// 条码验证
jQuery.validator.addMethod("isBarcode", function(value, element) {
	var result = false;
	var length = value.length;
	var d = /^\d{13}$/.test(value);
	if (length == 13 && d) {
		var c1 = parseInt(value.charAt(0)) + parseInt(value.charAt(2))
				+ parseInt(value.charAt(4)) + parseInt(value.charAt(6))
				+ parseInt(value.charAt(8)) + parseInt(value.charAt(10));
		var c2 = (parseInt(value.charAt(1)) + parseInt(value.charAt(3))
				+ parseInt(value.charAt(5)) + parseInt(value.charAt(7))
				+ parseInt(value.charAt(9)) + parseInt(value.charAt(11))) * 3;
		var c3 = parseInt(value.charAt(12))
		var cc = 10 - (c1 + c2) % 10;
		if (cc == 10) {
			cc = 0;
		}
		if (cc == c3) {
			result = true;
		}
	}
	return this.optional(element) || result;
}, "请正确填写条码");

// 手机号码验证
jQuery.validator
		.addMethod(
				"isMobile",
				function(value, element) {
					var length = value.length;
					var mobile = /^(13[0-9]{9})|(18[0-9]{9})|(14[0-9]{9})|(17[0-9]{9})|(15[0-9]{9})$/;
					return this.optional(element)
							|| (length == 11 && mobile.test(value));
				}, "请正确填写手机号码");

/* 只允许输入字母 */
$.validator.addMethod("isAlphabet", function(value, element) {
	return this.optional(element) || /^[a-zA-Z]+$/.test(value);
}, $.validator.format("只允许输入字母"));

/* 只允许输入字母或数字 */
$.validator.addMethod("isAlphabetDigits", function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9]+$/.test(value);
}, $.validator.format("只允许输入字母或数字"));

/* 只允许输入字母或数字或下划线 */
$.validator.addMethod("isAlphabetDigitsUnderLine", function(value, element) {
	return this.optional(element) || /^[a-zA-Z0-9_]+$/.test(value);
}, $.validator.format("只允许输入字母或数字或下划线"));
