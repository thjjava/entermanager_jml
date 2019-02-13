<%@ page language="java" pageEncoding="UTF-8"%>
<%@ include file="/comm/ContextPath.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>视频直播企业管理系统</title>
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/js/themes/default/easyui.css" />
		<link rel="stylesheet" type="text/css"
			href="<%=path%>/js/themes/icon.css" />
		<script type="text/javascript" src="<%=path%>/js/jquery-1.11.1.min.js"></script>
		<script type="text/javascript" src="<%=path%>/js/base64.js"></script>
		<script>
			function checkLogin(){
				var form = document.forms[0];
				var password=form.password;
				var code=form.code;
				var account=form.account;
			    if(account.value==""){
			    	account.focus();
			    	$("#errorinfo").html("请输入帐号！");
				    return false;
			    }else if(password.value==""){
			    	password.focus();
			   	    $("#errorinfo").html("请输入密码！");
				    return false;
			    }else if(code.value==""){
			    	code.focus();
			    	$("#errorinfo").html("请输入效验码！");
			    	 return false;
			    }else{
				    var pwd = $("#password").val();
					var b = new Base64();  
					$("#pwd").val(b.encode(pwd));
					$("#password").val(b.encode(pwd));
			    	return true;
			  	}
			}
			
			$(document).ready(function(){
				refurbishImgCode();
			});
			
			//验证码
			function refurbishImgCode() {
				var url = '<%=path%>/servlet/vn?sign='+Math.round(Math.random()*10000000000)+'000000000000000000000000000000';
				document.getElementById('picrandcode').src = url;
			}
		</script>
	</head>
	<body style="background-color: #f3f3f4;margin: 0 0 0 0;">
		<form method="post" name="form" id="form"
			action="<%=path%>/login_login.do" onsubmit="return checkLogin();">
			<input type="hidden" name="user.pwd" id="pwd" />
			<div style="width: 100%;height: auto;text-align: center;">
				<div style="width: 100%;height: 266px;background-image: url('images/login/login_bg_1.jpg');">
					<div style="width: 90%;height: 266px;margin-left: auto;margin-right: auto;">
						<div style="width:864px;height: 266px;background-image: url('images/login/login_bg_2.png');">
						</div>
					</div>
				</div>
				<div style="width: 100%;height: 445px;margin-top: -140px;">
					<div style="width: 100%;height: 42px;font-size: 35px;line-height: 42px;color: #fff;">
						<div style="width: 270px;height: 42px;margin-left:auto;margin-right:auto;
							background-image: url('images/login/login_name.png');display: none;">
						</div>
						视频直播企业管理系统
					</div>
					<div style="width: 100%;height: 20px;"></div>
					<div style="width: 100%;height: 361px;">
						<div style="width: 708px;height: 361px;margin-left:auto;margin-right:auto;
							background-image: url('images/login/login_bg_3.png');">
							<div style="width: 100%;height: 361px;">
								<div style="width: 235px;height: 361px;float: left;"></div>
								<div style="width: 60px;height: 361px;float: left;"></div>
								<div style="width: 340px;height: 361px;float: left;">
									<div style="width: 100%;height: 75px;"></div>
									<div style="width: 100%;height: 18px;text-align: left;">
										<font size="3px" color="red" id="errorinfo">${errorinfo }</font>
									</div>
									<div style="width: 100%;height: 46px;line-height:46px;
										background-image: url('images/login/login_user_pwd.png');">
										帐&nbsp;&nbsp;号
											<input style="width: 250px;height: 46px;border: 0px;
												background-color: transparent;font-size: 15px;"
												 type="text" name="user.account" id="account" value="${account}"/>
									</div>
									<div style="width: 100%;height: 46px;margin-top: 12px;line-height:46px;
										background-image: url('images/login/login_user_pwd.png');">
										密&nbsp;&nbsp;码
											<input style="width: 250px;height: 46px;border: 0px;
												background-color: transparent;font-size: 15px;"
												 type="password" name="password" id="password" autocomplete="off" value="${password}"/>
									</div>
									<div style="width: 100%;height: 46px;margin-top: 12px;">
										<div style="width: 200px;height: 46px;float:left;line-height:46px;
											background-image: url('images/login/login_code.png');">
											验证码&nbsp;
											<input style="width: 110px;height: 46px;border: 0px;
												background-color: transparent;font-size: 15px;"
												 type="text" name="code" id="code"/>
										</div>
										<div style="width: 140px;height: 46px;float: right;">
											<img id="picrandcode" style="width: 130px; height : 45px; " src="" align="absmiddle" onclick="javascript:refurbishImgCode()"/>
										</div>
									</div>
									<div style="width: 100%;height: 36px;margin-top: 20px;text-align: left;">
										<input type="image" src="images/login/login_login_but.jpg"/>
									</div>
								</div>
								<div style="width: 70px;height: 361px;float: left;"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</form>
	</body>
</html>
