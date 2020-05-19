//*******登录校验的方法**********************/
$(function(){
	var err_name = false;
	var err_pwd = false;

	var $login = $('#login');
	var $username = $('#nickname');
	var $password = $('#pwd');
	var $user_msg = $('#user_msg');
	var $pwd_msg = $("#pwd_error");
	
	$('#nickname').blur(function() {
		check_username();
	});
	$username.focus(function() {
		$username.next().css("visibility","hidden");
	});

	$('#pwd').blur(function() {
		check_password();
	});
	$password.focus(function() {
		$password.next().removeAttr("visibility");
	});

	function check_username() {
		var re = /^\w{6,15}(\@[a-z0-9]+(\.[0-9a-z]+){1,2})?$/;
		var val = $username.val();
		if (val == '') {
			$username.next().html('用户名不能为空');
			$username.next().css("visibility","visible");
			err_name = true;
			return false
		}
		if (re.test(val)) {
			$username.next().removeAttr("visibility");
			err_name = false
		} else {
			$username.next().html('用户名必须是6到十五位 或者是 邮箱');
			$username.next().css("visibility","visible");
			err_name = true;
			return false
		}
		return true;
	}

	function check_password() {
		var re = /^[\w@!#$%&^*]{6,15}$/;
		var val = $password.val();
		if (val == '') {
			$password.next().html('密码不可为空...');
			$password.next().css("visibility","visible");
			err_pwd = true;
			return false
		}

		if (re.test(val)) {
			$password.next().css("visibility","hidden");
			err_pwd = false;
		} else {
			$password.next().html('密码是6到15位字母、数字');
			$password.next().css("visibility","visible");
			err_pwd = true;
			return false
		}
		
		//*****验证码错误信息显示*****/
		$("#code_msg").focus(function(){
			$("#code_msg").attr("visibility","hidden");
		});
	}
	
	//登录请求
	$("#loginBtn").click(function(){
		var nickName = $("#nickname").val();
		var pwd = $("#pwd").val();
		var verify = $("#yzm").val();
		if(nickName==null || nickName=='' || pwd==null || pwd=='' || verify==null || verify=='' ){
			console.log('用户名密码账号不可为空!');
			return ;		//判空！
		}
		if( err_name && err_pwd ){
			return ;
		}
		$.post("menber/login",$("#loginForm").serialize(),function(data){
			var mno = parseInt($.trim(data.mno));
			if( mno >= 1 ){
				$("#loginForm")[0].reset();
				layer.msg('登录成功...');
				localStorage.setItem("currentUser",JSON.stringify(data));
				setInterval(function(){
					window.history.back(-1);	//跳转上一个页面
				},1100);
				layer.alert('<a href="javascript:location.href='+"'index.html'"+';">没有跳转,点击跳转..</a>');
			}else if(mno == -2){
				$("#code_msg").css("visibility","visible");
			}else if(mno == -3){
				layer.msg("账号或密码错误，请重新输入...");
			}else{
				layer.msg('登录失败！请稍后重试...');
			}
		},"json");
	});

});

//*****刷新图片验证码的方法********
function refreshCode(obj){
	obj.src = "code/loginCode?" + Math.random();
}


