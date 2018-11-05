$(function(){
    //菜单点击
    J_iframe
    $(".J_menuItem").on('click',function(){
        var url = $(this).attr('href');
        $("#J_iframe").attr('src',url);
        return false;
    });
    
    
});

// 过时的，勿用
function getLeftMenu(){
	//构造菜单
    $.ajax({
    	url: '/a/menu',
    	type: 'post',
    	contentType: 'application/json;charset=UTF-8',
        dataType: 'json',
        success: function(data){
        	let secondList = data.d.childrenList[0].childrenList;
        	let html = "";
			$(secondList).each(function(i){
				html += "<li>";
				html += "<a href='#'>";
				html += "<i class='" + secondList[i].icon + "'></i>";
				html += "<span class='nav-label'>" + secondList[i].name + "</span>";
				html += "<span class='fa arrow'></span>";
				html += "</a>";
				
				html += "<ul class='nav nav-second-level'>";
				let thredList = secondList[i].childrenList;
				$(thredList).each(function(j){
					html += "<li>";
					html += "<a class='J_menuItem' href='" + thredList[j].href + "'>" + thredList[j].name + "</a>";
					html += "</li>";
				});
				html += "</ul>";
				html += "</li>";
			});
			$("#side-menu").append(html);
        }
	});
}

// 刷新顶部菜单
function freshenNavbar(){
	$("#topNavbar").click();
}