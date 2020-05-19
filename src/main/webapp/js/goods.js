var pageSize = 5;
$(function() {
	var search = location.search;
	var params = search.substring(1); // 获取参数
	if (search == null || search == '' || params == null || params == undefined) {
		errorToIndex();		//非法请求,跳转首页
	}
	var kw_pn = params.split("kw=")[1];
	var tp_pn = params.split("tp=")[1];
	
	// ***判断请求是否合法**************************************/
	if ((kw_pn==null || kw_pn=='') && (tp_pn==null || tp_pn=='')) {
		errorToIndex();
	}
	if(tp_pn!=null && tp_pn!=''){
		var pn = parseInt(tp_pn.split("pn=")[1]); // 当前页数
		var tp = parseInt(tp_pn.split("&")[0]); // 当前类型
		if(isNaN(tp) || tp<=0){
			errorToIndex();
		}
		if (isNaN(pn) || pn <= 0) {
			location.href = "goods.html?tp=" + tp + "pn=" + 1;
		}
		getDataByTp(tp,pn);
	}else if(kw_pn != null && kw_pn!=''){
		var pn = parseInt(kw_pn.split("pn=")[1]); // 当前页数
		var kw = sessionStorage.getItem("searchKeyWord"); //关键字
		if(kw==null || kw==''){
			errorToIndex();
		}
		if (isNaN(pn) || pn <= 0) {
			location.href = "goods.html?kw=" + kw + "pn=" + 1;
		}
		getDataByKW(kw,pn);
		$("#keyWord").val(kw);
	}else{
		//errorToIndex();
	}
	
// ****渲染类型列表**********************************************************************************/
	var types = JSON.parse(sessionStorage.getItem("types")); // 从session中获取类型，并将json字符串对象转为json数组格式
	var typeList = '';
	$.each(types,function(index, item) { //
		if (tp == item.tno) {
			$("#currType").html(item.tname);
		}
		typeList += '<li><img style="height:80%;position:relative;margin-left:-110px;margin-top:5px;" src="' + item.pic + '"/>';
		typeList += '<a style="float:left;z-index:2;" href="goods.html?tp='+item.tno+'&pn=1">' + item.tname + '</a></li>';
	});
	$("#typeList").html(typeList);

	//热门查询动态样式
	//给我们热门类型查询做点击事件
	$(".type").on("click", function() {
		//默认查询方式 
		var type = "id";
		//移除原来的active的样式
		$(".type").removeClass("active");
		//获取此时点击的对象的文本是什么
		var check = $(this).html();
		//判断
		if (check == "默认") {
			$("#normal").addClass("active");
		}
		if (check == "价格") {
			$("#price").addClass("active");
			type = "gprice"
		}
		if (check == "人气") {
			$("#manUse").addClass("active");
			type = "gsales"
		}
	})
})

//***搜索方法,请求数据*********************************************************/
function getDataByKW(kw,pn){
	$.post("goods/searchGoodsByPage",{kw:kw,page:pn,pageSize:pageSize},function(data){
		showGoodsList(data);
		var pageBtns = '';
		if (pn == 1) { // 若当前页面已经是第一页，就不要使按钮可以点击
			pageBtns += '<li><a href="javascript:void(0)" style="disabled">上一页</a></li>';
		} else {
			pageBtns += '<li><a href="goods.html?kw=' + kw + '&pn=' + (pn - 1) + '">上一页</a></li>';
		}
		
		var totalPage = data.count%pageSize==0 ? parseInt(data.count / pageSize) : parseInt(data.count / pageSize) + 1; // 总页数
		if (pn > totalPage) { // 对当前页面进行判断，是否为合法的请求页面
			location.href = 'goods.html?kw=' + kw + '&pn=' + totalPage;
		}
		for (var i = 1; i <= totalPage; i++) {
			if (pn == i) {
				pageBtns += '<li><a href="javascript:void(0)" class="active">' + i + '</a></li>';
			} else {
				pageBtns += '<li><a href="goods.html?kw=' + kw + '&pn=' + i + '" >' + i + '</a></li>';
			}
		}
		if (pn == totalPage) { // 若当前页面已经是末页，就不要使按钮可以点击
			pageBtns += '<li><a href="javascript:void(0)" style="disabled">下一页</a></li>';
		} else {
			pageBtns += '<li><a href="goods.html?kw=' + kw + '&pn=' + (pn + 1) + '">下一页</a></li>';
		}
		$("#pageBtns").html(pageBtns);
	});
}

// *****根据类型编号请求数据*********************************************/
function getDataByTp(tp,pn){
	$.post( "goods/getGoodsByTidPage",{ tno : tp, page : pn, pageSize : pageSize },function(data) {
		if (data.count == 0 || data.goods.length <= 0) {
			layer.msg("该页暂无商品数据！", { icon : 5 });
			window.history.back(-1);
		}
		showGoodsList(data);

		// ****渲染分页按钮****************************************************/
		var pageBtns = '';
		if (pn == 1) { // 若当前页面已经是第一页，就不要使按钮可以点击
			pageBtns += '<li><a href="javascript:void(0)" style="disabled">上一页</a></li>';
		} else {
			pageBtns += '<li><a href="goods.html?tp=' + tp + '&pn=' + (pn - 1) + '">上一页</a></li>';
		}
		
		var totalPage = data.count%pageSize==0 ? parseInt(data.count / pageSize) : parseInt(data.count / pageSize) + 1; // 总页数
		if (pn > totalPage) { // 对当前页面进行判断，是否为合法的请求页面
			location.href = 'goods.html?tp=' + tp + '&pn=' + totalPage;
		}
		for (var i = 1; i <= totalPage; i++) {
			if (pn == i) {
				pageBtns += '<li><a href="javascript:void(0)" class="active">' + i + '</a></li>';
			} else {
				pageBtns += '<li><a href="goods.html?tp=' + tp + '&pn=' + i + '" >' + i + '</a></li>';
			}
		}
		if (pn == totalPage) { // 若当前页面已经是末页，就不要使按钮可以点击
			pageBtns += '<li><a href="javascript:void(0)" style="disabled">下一页</a></li>';
		} else {
			pageBtns += '<li><a href="goods.html?tp=' + tp + '&pn=' + (pn + 1) + '">下一页</a></li>';
		}
		$("#pageBtns").html(pageBtns);
	});
}
//****渲染数据********************************************************/
function showGoodsList(data){
	//搜索关键字标红
	var goods = data.goods;
	if(data.kws!=undefined && data.kws!=null && data.kws.length>0){
		$.each(goods,function(index,item){
			var gname = item.gname;
			for(var key in item){
				$.each(data.kws,function(idx,kw){
					if( item.gname.indexOf(kw) != -1){
						var str = item.gname;
						var i = item.gname.indexOf(kw);
						console.log(item.gname[i]);
						if(key=='gname'){
							item[key] = item.gname.replace(item.gname[i],("<font color='red'>"+item.gname[i]+"</font>"));
						}
					}
				});
			}
		});
	}
	//显示商品
	var goodsList = '';
	$.each(goods,function(index, item) {
		var pic = item.pics.split("|")[0];
		goodsList += '<li><a href="details.html?tp='+item.tno+'&gno='+ item.gno+ '"><img src="'+ pic+ '"></a><h4>';
		goodsList += '<a href="details.html?tp='+item.tno+'&gno='+ item.gno+ '">'+ item.gname+ '</a>';
		goodsList += '</h4><div class="operate"><span class="prize">￥'+ item.price+ '</span>';
		goodsList += '<span class="unit">'+ item.price+ '/'+ item.weight;
		goodsList += '</span><a href="javascript:addToCart('+item.gno+',1);" class="add_goods" title="加入购物车"></a></div></li>';
	});
	$("#goodsList").html(goodsList);
}

//非法请求,跳转首页
function errorToIndex(){
	layer.msg("非法请求！");
	setTimeout(function() {
		location.href = "index.html";
	}, 1000);
}