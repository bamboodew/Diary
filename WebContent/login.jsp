<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>

<html lang="zh">
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>个人日记本登录</title>
  
  <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap.css" rel="stylesheet">
  <link href="${pageContext.request.contextPath}/bootstrap/css/bootstrap-responsive.css" rel="stylesheet">
  <script src="${pageContext.request.contextPath}/bootstrap/js/jQuery.js"></script>
  <script src="${pageContext.request.contextPath}/bootstrap/js/bootstrap.js"></script>

  <style type="text/css">@IMPORT url("login.css");</style>
  <script type="text/javascript" src="checkForm.js"></script>
</head>
<body>
  <div class="container">
    <form name="myForm" class="form-signin" action="login" method="post">
      <h2 class="form-signin-heading">屌丝日记本</h2>

      <input id="userName" name="userName" value="${user.userName }" type="text" class="input-block-level" placeholder="屌丝名...">
      <!-- input-block-level 块级元素，独占一行 -->
      <!-- placeholder 占位符 -->
      <!-- value="${user.userName }"登录之后显示输入值 -->

      <input id="password" name="password" value="${user.password }" type="password" class="input-block-level" placeholder="屌丝码...">
      <label class="checkbox"> <input id="remember" name="remember" type="checkbox" value="remember-me">记住我
        &nbsp;&nbsp;&nbsp;&nbsp; <font id="error" color="red">${error }</font>
      </label>

      <button class="btn btn-large btn-primary" type="submit">登录</button>
      <!-- btn 普通按钮 -->

      &nbsp;&nbsp;&nbsp;&nbsp;
      <button class="btn btn-large btn-primary" type="button">重置</button>
      <p align="center" style="padding-top: 15px;">
        版权所有 2014 Java知识分享网 <a href="http://www.java1234.com" target="_blank">http://www.java1234.com</a>
      </p>
    </form>
  </div>
</body>
</html>