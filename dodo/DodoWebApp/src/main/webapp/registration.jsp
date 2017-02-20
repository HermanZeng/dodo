<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>DODO -registration</title>
<link rel="stylesheet" type="text/css"
	href="resources/js/jquery-easyui-1.4.5/themes/default/easyui.css">
<link rel="stylesheet" type="text/css"
	href="resources/js/jquery-easyui-1.4.5/themes/icon.css">
<link rel="stylesheet" type="text/css"
	href="resources/js/jquery-easyui-1.4.5/demo/demo.css">

<script type="text/javascript" src="resources/js/jquery-2.2.2.js"></script>
<script type="text/javascript"
	src="resources/js/jquery-easyui-1.4.5/jquery.easyui.min.js"></script>

</head>
<body background="resources/images/bg.jpg">
	<div align="right">
		<a href="login_old.jsp" class="easyui-linkbutton"
		   style="width: 40px; height: 20px;"> Back </a>
	</div>

	<h1 align="center">Welcome to join dodo!</h1>
	<br>
	<br>
	<br>
	<br>

	<div id="reg-dlg" class="easyui-dialog"
		style="width: 320px; height: 280px; padding: 0px 20px" title="Sign up"
		closed="false">
		<s:form action="Signup">
			<div class="fitem">
				<s:textfield name="signupUser.email" label="Email"
					cssClass="easyui-textbox" prompt="your email address"
					validType="email" validateOnCreate="false"
					cssStyle="width:170px;height:25px;" />
			</div>
			<div class="fitem">
				<s:textfield name="signupUser.firstName" label="First Name"
					cssClass="easyui-textbox" prompt="your first name"
					validateOnCreate="false" cssStyle="width:170px;height:25px;" />
			</div>
			<div class="fitem">
				<s:textfield name="signupUser.lastName" label="Last Name"
					cssClass="easyui-textbox" prompt="your last name"
					validateOnCreate="false" cssStyle="width:170px;height:25px;" />
			</div>
			<div class="fitem">
				<s:password name="signupUser.password" label="Password"
					cssClass="easyui-textbox" prompt="password"
					validateOnCreate="false" cssStyle="width:170px;height:25px;" />
			</div>
			<br>
			<br>
			<br>

			<s:submit align="right" value="Sign up" cssClass="easyui-linkbutton"
				iconCls="icon-ok" cssStyle="width:80px;height:25px;" />

		</s:form>
	</div>

</body>
</html>
