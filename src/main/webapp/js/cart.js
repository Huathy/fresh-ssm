//***判断是否登录******************************/
var user = checkLogin();
if(user==null || user<=''){
	location.href = "login.html";
}
var mno = user.mno;

//***引入相关组件*******************************************/
var layer;		//引入layer弹框组件
layui.use('layer',function(){
	layer = layui.layer;
});
var laytpl;		//引入组件，laytpl，模板引擎
layui.use('laytpl',function(){
	laytpl = layui.laytpl;
});

//商品列表数据，使用layui模板引擎，进行渲染。首先要创建模板，然后编辑视图，最后渲染数据。
$.post("cart/getAllByMno/"+mno,null,function(data){
	console.log(data);
	var data = {"list" :  data}
	var getTpl = goodsModel.innerHTML , view = document.getElementById('goodsListView');
	laytpl(getTpl).render(data,function(html){
		view.innerHTML = html;
	});
});

//***商品数量加减方法*******************************/
function countNum(gno,val){
	var count = parseInt($.trim($("#num_"+gno).val()));
	var num = count + parseInt(val);
	if(num <= 0){
		return;		//直接结束，不要继续执行
	}
	$("#num_"+gno).val(num);
	updateNum(gno,num);
}
//***更新数量的方法*********************************/
function updateNum(gno,num){
	$.post("cart/updateByMnoGno",{mno:mno, gno:gno, num:num},function(data){
		data = parseInt($.trim(data));
		if(data>0){
			var price = parseInt($("#price_"+gno).text());
			$("#subtotal_"+gno).text(Number(price*num));
		}else{
			layer.msg('操作失败！请检查网络后重试...');
		}
	});
}

//***当enter时触发失焦事件************************/
function numInpKeyDown(obj){
	if(this.event.keyCode == 13){
		obj.blur();
	}
}
//***从购物车移除商品****************************************/
function delGoods(gno){
	$.post("cart/delByMnoGno",{mno:mno,gno:gno},function(data){
		data = parseInt($.trim(data));
		if(data > 0){
			$("#goodsInfo_"+gno).remove();
			var num = parseInt($.trim($("#cart_count").html()))-1;
			$("#cart_count").html(num)
		}else{
			layer.msg("操作失败！");
		}
	});
}

//****判断是否全部选上*并计算总计***********************************************/
function checks(){
	var checks = $(".cart_list_td ul .col01 input");
	var checkeds = $(".cart_list_td ul .col01 input:checkbox:checked");
	if(checks.length == checkeds.length){
		$("#all").prop("checked",true);
	}else{
		$("#all").prop("checked",false);
	}
	var total = 0;
	$.each(checkeds,function(index,item){
		var gno = $(item).data("gno");
		var subtotal = Number($("#subtotal_"+gno).text());
		total += subtotal;
	})
	$("#totalPrices").html(total);
	$("#totalNumbers").html(checkeds.length);
	
}

function toPay(){
	var checkeds = $(".cart_list_td ul .col01 input:checkbox:checked");
	if(checkeds.length<=0){
		layer.msg("请先选择要结算的商品");
		return;
	}
	var gInfos = [];
	$.each(checkeds,function(index,item){
		var gno = $(item).data("gno");
		var gname = $("#gname_"+gno).text();
		var pic = $("#pic_"+gno).attr("src")
		var weight = $("#weight_"+gno).text();
		var price = $("#price_"+gno).text();
		var num = $("#num_"+gno).val();
		console.log(num);
		var gInfo = {'gno':gno , 'gname':gname , 'pic':pic , 'weight':weight , 'price':price , 'num':num};
		gInfos[index] = gInfo;
	})
	sessionStorage.setItem("currentPayGinfos",JSON.stringify(gInfos));
	location.href = "pay.html";
}