var layer;	//初始化layui组件
layui.use('layer', function(){
	layer = layui.layer;
}); 
var laytpl;
layui.use('laytpl',function(){
	laytpl = layui.laytpl;
});
$(function(){
	var search = location.search;
	var gno = parseInt($.trim(search.split("gno=")[1]));
	$.post("goods/getGoodsByGid",{gno : gno},function(data){ //"goodsPicItem"
		if(data==null || data==''){		//没有找到就跳回去
			alert("没有查找到该商品！");
			window.history.back(-1);
		}
		
		addToRecentBrower(data);	//将最近的浏览记录添加到前端数据库
		
		var getTpl = goodsInfoModel.innerHTML , view = document.getElementById('goodsInfoView');
		var listData = {'list':data};
		laytpl(getTpl).render(listData,function(html){
			view.innerHTML = html;
		});
		$("#goodsMsg").html(data.descr);
		
		var picArr = data.pics.split("|");
		var pics = '';
		$.each(picArr,function(index,item){
			pics += '<img src="'+item+'" />';
		})
		$("#goodsPicItem").html(pics);
	//***使用layui组件，渲染轮放图片用**********************************/
		layui.use('carousel', function(){
		var carousel = layui.carousel;
			//建造实例
			carousel.render({
				elem : '#goodsPics'
				,width : '100%'		//设置容器宽度
				,height : '100%'	//设置容器宽度
				,interval : 2000	//设置轮放间隔时间
				,arrow : 'always'	//始终显示箭头
				,autoplay : true	//设置自动轮放
			});
		});
	});
	
	var tp = parseInt(search.split("tp=")[1].split("&")[0]); // 当前类型
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
});
//***数量添加减少的方法***********************************************************/
function count(val){
	var num = parseInt($("#goodsNum").val());
	var price = $("#price").text();
	num += parseInt(val);
	if(num<=0){
		num=1;
	}
	$("#goodsNum").val(num);
	var sum = num*Number(price);
	$("#sum").text(sum);
}

//***加入购物车的方法************************************************/
function addToCart(gno,num){
	var mno = getUser('mno');
	if(num==undefined || num==null && num<=0){
		num = Number($("#goodsNum").val());
	}else{
		num = Number(num);
	}
	if(num<=0){
		num = 1;
	}
	if(mno==null || mno==''){
		location.href = "login.html";
		return;
	}
	$.post("cart/add",{mno : mno , gno : gno , num : num},function(data){
		data = $.trim(data);
		if(data == -1){
			layer.msg("参数错误！",{icon:2});
		}else if(data >= 1){
			layer.msg("添加成功！",{icon:1});
			$("#cart_count").text(data);
			localStorage.setItem("cartNum",data);	//更新一下本地存储
		}else if(data <= 0){
			layer.alert("添加失败！错误❌：" + data,{icon:5});
		}
	});
}

//将浏览器历史记录添加到前端数据库	//这里使用前端indexedDB数据库,以减少请求服务器次数
async function addToRecentBrower(obj){
	console.log(obj);
	var mno = getUser("mno");		//判断是否登录,若登录才存到数据库,否则不存入数据库
	if(mno==null || mno<=0 || obj==null){
		return;
	}
	
	var db = new Dexie('fresh');	//声明数据库
	db.version(1).stores({		//注册db
		lastView : 'id'
	});
	var gInfo = {gno:obj.gno , tno:obj.tno , gname:obj.gname , price:obj.price , weight:obj.weight , pic:obj.pics.split('|')[0]};
	db.lastView.add({id:mno,gInfos:[gInfo]});	//添加数据库,若数据库已有该条id的记录,则不操作
	
	var result = await db.lastView.get(mno);	//根据id获取存在数据库中的数据
	var gInfos = result.gInfos;		//获取商品详情信息
	var flag = JSON.stringify(gInfos).indexOf(JSON.stringify(gInfo));		//判断是否已经包含该对象
	if( flag == -1 ){	//数组中不存在这个编号,存一下
		gInfos[gInfos.length] = gInfo;		//把值加到数组中
		db.lastView.put({id:mno,gInfos:gInfos});	//修改数据库
	}
	db.close();
}
