var loginFlag = false;
//每次进入到网页检测有没有登录
$(function(){
	showLogin();
	getNewGoods();
})
//***显示登录，从前端的本地存储中获取（用户，购物车）数据，非关键页面，不发请求*********************************/
function showLogin(){
	var user = localStorage.getItem("currentUser");
	if(user != null && user != ''){
		user = JSON.parse(user);
		$("#loginBtn").hide();
		$("#loginInfo").show().find("em").html(user.nickName);
	}
	var cartNum = localStorage.getItem("cartNum");
	//console.log(cartNum);
	if(cartNum!=null && cartNum!=''){
		$("#cart_count").text(cartNum);
	}
}

//***检查登录，从后端的会话存储中获取（用户，购物车）数据，关键页面，发请求以校验前端数据*********************************/
function checkLogin(){	//bug太多，不知道什么bug🙃🙃🙃
	var user;
	$.ajaxSettings.async = false;	//同步请求
	$.post("menber/checkLogin",null,function(data){
		if(data==null || data==''){		//用户没有登录，清空存储在前端的session缓存
			localStorage.removeItem("currentUser");
		}else{		//用户登录，显示用户信息
			user = data;
			localStorage.setItem("currentUser",JSON.stringify(data));		//前端session在每次关闭页面就会清空，而后端session是在断开连接才清空
			$("#loginBtn").hide();
			$("#loginInfo").show().find("em").html(data.nickName);
			$.ajaxSettings.async = true;	//异步请求
			getCartCount(data.mno);		//获取购物车的计数
		}
	});
	return user;
}

function isLogin(){
	console.log(loginFlag);
}

//***获取用户信息***********************************/
function getUser(key){	//可以直接获取user对象，也可以获取user对象中的值
	var user = localStorage.getItem("currentUser");
	if(user==null || user==''){
		return null;
	}else{
		user = JSON.parse(user);	//JSON.parse()的参数不可以是空字串
	}
	
	if(key==null || key=='' || key==undefined){
		return user;
	}else{
		return user[key];
	}
	return user;
}

//***获取购物车数量*****************************************/
function getCartCount(mno){
	$.post("cart/countByMno", {mno : mno} ,function(data){
		$("#cart_count").text($.trim(data));
		localStorage.setItem("cartNum",$.trim(data));
		//console.log(data);
	});
}

//***退出登录*************************************/
function loginOut(){
	$.post('menber/loginOut',null,function(){
		var flag = confirm("确认退出");
		if(flag){
			localStorage.removeItem("currentUser");
			$("#loginBtn").show();
			$("#loginInfo").hide();
			location.reload();
		}
	});
}

function getNewGoods(){
	var view = $("#newGoodsView").html();
	if(view==undefined || view==null || view==''){
		return;		//说明该页面没有新品视图,直接返回
	}
	$.post("goods/getNewGoods",function(data){
		var goodsInfos = '<h3>新品推荐</h3>';
		$.each(data,function(index,item){
			goodsInfos += '<li><a href="details.html?tp='+item.tno+'&gno='+item.gno+'"><img src="'+item.pics.split('|')[0]+'"></a>';
			goodsInfos += '<h4><a href="details.html?tp='+item.tno+'&gno='+item.gno+'">';
			goodsInfos += item.gname+'</a></h4><div class="prize">￥'+item.price+'</div></li>';
		});
		$("#newGoodsView").html(goodsInfos);
	});
}

function doSearch(){
	var keyWord = $("#keyWord").val();
	sessionStorage.setItem("searchKeyWord",keyWord);
	location.href = "goods.html?kw="+encodeURI(encodeURI(keyWord))+"&pn=1";
}
