
if (window.top !== window.self) {
	window.top.location = window.location;
}

/*提交时密码进行一次MD5*/
function checkForm(){
    let input_pwd = document.getElementById('pwd');
    let md5_pwd = document.getElementById('md5_pwd');
    md5_pwd.value = md5(input_pwd.value);    
    return true;
}

$(document).ready(function() {
    validateForm=$("#inputForm").validate({
    	debug: false,
    	rules:{
    		captcha:{
            	remote:{
            		url:"/img/captcha.jpg",
            		async: true
            	}
    		}
    	},
    	messages:{
    		captcha:{
            	remote:""
    		}
    	},
    	submitHandler: function(form){
    		checkForm();
    		form.submit();
    	},
    	errorContainer: "#messageBox",
    	errorPlacement: function(error, element) {
    	}
    });
	
});
