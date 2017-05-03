/**
 * 
 */
function checkForm() {
	var userName = document.getElementById("userName").value;
	var password = document.getElementById("password").value;
	if (userName == null || userName == "") {
		document.getElementById("error").innerHTML = "用户名不能为空！"; // id为error的位置显示字符串
		return false; // 表单不提交
	}
	if (password == null || password == "") {
		document.getElementById("error").innerHTML = "密码不能为空！"; // id为error的位置显示字符串
		return false; // 表单不提交
	}
	return true;
}