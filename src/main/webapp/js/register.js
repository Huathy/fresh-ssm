// JavaScript Document

//获取元素
var  $nickname = $("#nickname");
//用户名验证
//失去焦点事件
$("#nickname").blur (function(){
	check_nickname();
});

//聚焦事件
$("#nickname").focus(function(){
	//隐藏
	$(this).next().hide();
})
function check_nickname(){
	//获取用户输入的用户名
	var nickname = $("#nickname").val();
	//判断用户名的格式
	if(nickname == '' ){
		//给当前对象的下一个标签设值
		$(this).next().html("用户不能为空");
		//未设值前该标签是隐藏的  此时就必须显示来
		$(this).next().show();
		return false;
	}
	//正则表达式
	//创建一个规则
	var reg = /^\w{6,15}$/;
	//判断
	/*
	var flag = reg.test(nickname);
	console.log(flag);
	*/
	if( !reg.test(nickname)){
		//给当前对象的下一个标签设值
		$(this).next().html("用户名必须是6-15位的英文或数字");
		//未设值前该标签是隐藏的  此时就必须显示来
		$(this).next().show();
		return false;
	}
	return true;
}


//密码验证	
//失焦事件
$("#pwd").blur(function(){
	check_pwd();	
});

//聚焦事件
$("#pwd").focus(function(){
	$(this).next().hide();
});
	
function check_pwd(){
	//获取密码
	var pwd = $("#pwd").val();
	//规则
	var reg = /^[\w@!#$%^&*~]{6,15}$/;
	//判断
	if(!reg.test(pwd)){
		$("#pwd").next().html("密码不符合规范");
		$("#pwd").next().show();
		return false;
	}
	return true;
}

//确认密码验证
//失焦事件
$("#cpwd").blur(function(){
	check_cpwd();	
});

//聚焦事件
$("#cpwd").focus(function(){
	$(this).next().hide();
});
	
function check_cpwd(){
	//获取原密码
	var pwd = $("#pwd").val();
	//获取再次输入的密码
	var cpwd = $("#cpwd").val();
	
	//判断
	if(cpwd == ''){
		$("#cpwd").next().html("确认密码不能为空");
		$("#cpwd").next().show();
		return false;
	}
	if(cpwd != pwd){
		$("#cpwd").next().html("两次输入的密码不一致");
		$("#cpwd").next().show();
		return false;
	}
	return true;
}

//手机号码验证
//失焦事件
$("#tel").blur(function(){
	check_tel();	
});

//聚焦事件
$("#tel").focus(function(){
	$("#tel").next().hide();
});

function check_tel(){
	//获取电话号码
	var tel = $("#tel").val();
	//规则
	var reg = /^[1][3,4,5,7,8,9][0-9]{9}$/;
	if( !reg.test(tel)){
		$("#tel").next().html("手机号码格式错误");
		$("#tel").next().show();
		return false;
	}
	return true;
}

/****邮箱校验************************************************/
//失焦事件
$("#email").blur(function(){
	check_email();	
});

//聚焦事件
$("#email").focus(function(){
	$("#email").next().next().hide();
});
	
function check_email(){
	//获取邮箱
	var email = $("#email").val();
	//规则
	var reg = /^[A-Za-z0-9_-]+@[A-Za-z0-9_-]+\.[A-Za-z0-9]+$/;
	
	if( !reg.test(email)){
		$("#email").next().next().html("邮箱格式错误");
		$("#email").next().next().show();
		return false;
	}
	return true;
}


//获取验证码
function sendCode(){
	//获取邮箱
	var email = $("#email").val();
	//非空判断
	if( email == ''){
		layer.msg("邮箱不可为空！");
	}
	
	//请求方式  地址  参数  回调函数
	$.post("code/email",{email:email},function(data){
		data = parseInt($.trim(data));
		if(data == -1){
			layer.msg("邮箱不可为空...");
		}else if(data == 1){
			layer.msg("验证码发送成功！请前往邮箱查看！");
		}else{
			layer.alert("验证码发送失败！");
		}
	})
}
	

//协议
$("#allow").click(function(){
	//判断checkbox是否被选中
	if(!$(this).is(":checked")){
		$(this).next().next().html("您必须同意公司的协议");
		$(this).next().next().show();
		//给注册按钮设置禁用属性
		$("#reg").attr("disabled","disabled");
	}else{
		$(this).next().next().html("");
		$(this).next().next().hide();
		//移除注册按钮的禁用属性
		$("#reg").removeAttr("disabled");
	}
})
	
	
//注册
function register(){
	var code = $('#yzm').val();
	var flag = $("#slider").data("flag");
	
	if( !check_nickname() ){
		layer.msg("昵称不合法");
		return ;
	}
	if( !check_pwd() ){
		layer.msg("密码为空");
		return ;
	}
	if( !check_cpwd() ){
		layer.msg("两次密码不一致");
		return ;
	}
	if( !check_tel() ){
		layer.msg("电话格式错误");
		return ;
	}
	if( !check_email() ){
		layer.msg("邮箱为空");
		return ;
	}
	if(code==null || code==''){
		layer.msg("验证码为空");
		return ;
	}
	if( !flag ){
		layer.msg("请拖动滑块验证");
	}
	//只有当所有的校验都合法时，才可执行
	var regForm = $("#reg_form").serialize();	//序列化表单
	$.post("menber/reg" , regForm , function(data){
		data = parseInt($.trim(data));
		if( data == -1 ){
			layer.msg('请检查个人信息...');
		}else if(data == -2){
			layer.msg('验证码过期,请重新获取..');
		}else if(data == -3){
			layer.msg('验证码错误，请重新输入..');
		}else if(data == -101){
			layer.msg('用户名已被占用！');
		}else if(data == -102){
			layer.msg('该邮箱已注册！');
		}else if(data == -103){
			layer.msg('该手机号码已注册！');
		}else if(data > 0){
			layer.msg('注册成功！');
			$("#reg_form")[0].reset();
			location.href="login.html";
		}else{
			layer.msg('注册失败，请稍后..');
		}
	},"json");
}

function code(){
	var email = $("#email").val();
	if( !check_email() ){
		layer.msg("请输入合法邮箱");
		return ;
	}
	$.post("code/email",{email:email},function(data){
		data = parseInt($.trim(data));
		if(data > 0){
			layer.msg("验证码发送成功，请前往邮箱查看！");
		}else {
			layer.msg("验证码发送失败，请稍后重试！");
		}
	});
}