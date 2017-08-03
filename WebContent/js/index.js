$(function(){
	$("#login").click(function(){
		var userName = $("#userName").val();
		var password = $("#password").val();
		if (userName == "" || password == "") {
			return;
		}
		$.ajax({
			url:"http://localhost:8089/MyWebSite/user/login.do",
			type:"post",
			data: {"userName":userName,"password":password},
			dataType:"json",
			success:function(result){
				alert("成功！");   
			},
			error:function(XMLHttpRequest, textstatus,errorThrown) {
				alert(XMLHttpRequest.status);
				alert(XMLHttpRequest.readystate);
				alert(textstatus);
			}
		});
	});
});
