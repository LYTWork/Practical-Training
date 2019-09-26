
//验证码验证
function isRightCode() {
	var code = $("#veryCode").attr("value");
	//alert(code);
	code = "c=" + code;
	$.ajax( {
		type : "POST",
		url : "ResultServlet",
		data : code,
		success : callback
	});
}
//验证以后处理提交信息或错误信息
function callback(data) {
	if(data.toString()==1)
	{
		$("#info").html("xw素材网提醒您：成功了！");

	}else
	{
		$("#info").html(data);

	}
}  
