$(function(){
	showPersonInfo();
	var hash = location.hash;
	var op = hash.substring(1);
	setTimeout(function(){
		if(op == 'addrManage'){
			showAddrInfo();
		}
	},100);
});
var layer;
layui.use('layer', function() {
	layer = layui.layer;
});
var laytpl;
layui.use('laytpl', function() {
	laytpl = layui.laytpl;
});

//校验是否登录
var user = checkLogin();
if (user == null || user == '') {
	location.href = "login.html";
}

//***显示用户个人信息******************************************************************************/
function showPersonInfo() {
	$.post("menber/getInfoByMno",{mno : user.mno},function(data){
		var data = { 'list' : data };
		var getTpl = personInfoModel.innerHTML, view = document.getElementById('showView');
		//渲染
		laytpl(getTpl).render(data, function(html) {
			view.innerHTML = html;
		});
	});
}

var addrs;
//***显示地址信息*******************************************************************/
function showAddrInfo(){
	var html = '<h3 class="common_title2">地址列表<button type="button" class="layui-btn layui-btn-normal layui-btn-sm" onclick="showModifyView()" '
		+' style="float:right">添加新地址</button></h3><div style="margin-top:20px;"><table class="layui-hide" id="addrTable" lay-filter="addrTable"></table></div>';
	$("#showView").html(html);
	$.post("addr/getAddrByMno",{mno : user.mno},function(data){
		var res = fullToString(data);	//调用完全转为string的方法
		addrs = data;
		renderAddrTable(res);
	});
}
//修改默认地址
function changeDefaultAddr(ano){
	$.post("addr/changeDefault",{mno:user.mno,ano:ano},function(data){
		data = $.trim(data);
		if(data > 0){
			$.each(addrs,function(index,item){
				item.flag = "0";
				if(item.ano==ano){
					item.flag = "1";
				}
			});
			renderAddrTable(addrs);
		}else{
			layer.msg("修改默认地址失败,请稍后重试");
		}
	});
}
//显示数据表格方法
function renderAddrTable(res){
	layui.use('table', function(){
		var table = layui.table;
		table.render({
			elem: '#addrTable'
			,cols: [[
				{field:'ano', width:'8%', title: '序号' ,templet:function(res){
					return res.LAY_INDEX;
				}}
				,{field:'name', width:'12%', title: '收件人姓名'}
				,{field:'tel', width:'13%', title: '联系电话'}
				,{field:'province', width:'8%', title: '省份' }
				,{field:'city', width:'10%', title: '城市' }
				,{field:'area', width:'10%', title: '地区' }
				,{field:'addr', width:'18%', title: '寄送地址' }
				,{field:'flag', width:'10%', title: '是否默认' , templet:function(res){
					if( parseInt(res.flag)==1){
						return '<a class="layui-btn layui-btn-warm layui-btn-xs">默认地址</a>';
					}else{
						return '<a class="layui-btn layui-btn-primary layui-btn-xs" onclick="changeDefaultAddr('+res.ano+')">设为默认</a>';
					}
				}}
				,{field:'right', width:'12%', title: '操作' ,  toolbar: '#barDemo' }
			]]
			,data : res
		});
		//监听行工具事件
		table.on('tool(addrTable)', function(obj){
			var data = obj.data;
			if(obj.event === 'del'){
				layer.confirm('真的删除行么', function(index){
					layer.close(index);
					$.post("addr/delByAno",{ano:data.ano},function(data){
						data = $.trim(data);
						if(data>0){
							layer.msg('删除成功',{icon:1});
							obj.del();
						}else{
							layer.msg('删除失败',{icon:5});
						}
					});
				});
			} else if(obj.event === 'edit'){
				layer.confirm('<div id="modifyView"></div>', {	//定义修改默认地址的视图
					btn: ['确认修改','取消修改'] //按钮
					,area:['560px','420px']
				}, function(index){
					var name=$("#name").val() , tel=$("#tel").val(), prov=$("#province").val() 
						, city=$("#city").val() , area=$("#area").val() , addr=$("#addr").val();
					var params = [name , tel , prov , city , area , addr];
					var msg = ['收件人姓名','收件人联系方式','省份','城市','区域','详细地址'];
					if(checkNull(params,msg)){
						return;
					}
					$.post("addr/modifyAddr",{
						ano:obj.data.ano, name:name, tel:tel, province:prov, city:city, area:area, addr:addr
					},function(data){
						data = $.trim(data);
						if(data>0){
							layer.msg('修改成功!');
							layer.close(index);
							obj.update({	//更新表格
								name:name, tel:tel, province:prov, city:city, area:area, addr:addr
							});
						}else{
							layer.msg('修改失败');
						}
					});
				});
				//渲染修改默认地址的视图
				showModifyView(obj.data)
			}
		});
	});
}

function showModifyView(data){
	if(data == undefined || data==null || data==''){
		data = {"name":"", "tel":"", "province":"", "city":"", "area":"", "addr":""};
		layer.confirm('<div id="modifyView"></div>', {	//定义修改默认地址的视图
			btn: ['提交','取消'] //按钮
			,area:['560px','420px']
		}, function(index){
			var name=$("#name").val() , tel=$("#tel").val(), prov=$("#province").val() 
				, city=$("#city").val() , area=$("#area").val() , addr=$("#addr").val();
			var params = [name , tel , prov , city , area , addr];
			var msg = ['收件人姓名','收件人联系方式','省份','城市','区域','详细地址'];
			if(checkNull(params,msg)){
				return;
			}
			
			$.post("addr/add",{
				mno:user.mno,name:name, tel:tel, province:prov, city:city, area:area, addr:addr
			},function(data){
				data = $.trim(data);
				if(data>0){
					layer.msg('添加成功!',{icon:1});
					layer.close(index);
					addrs[addrs.length] = {name:name, tel:tel, province:prov, city:city, area:area, addr:addr,flag:"0"};  //将最新数据添加到addrs中 
					renderAddrTable(addrs);		//重新渲染
				}else{
					layer.msg('添加失败',{icon:5});
				}
			});
		});
	}
	var dataList = { 'list' : data };
	var getTpl = modifyAddrModel.innerHTML, view = document.getElementById('modifyView');
	//渲染
	laytpl(getTpl).render(dataList, function(html) {
		view.innerHTML = html;
	});
}
//修改个人信息
function modifyPersonInfo(){
	var mno = $("#mno").html() , nickName = $("#nickName").html() , realName = $("#realName").html() 
		, tel = $("#tel").html() , email = $("#email").html();
	data = { "nickName": nickName, "realName":realName, "tel":tel, "email":email };
	
	layer.confirm('<div id="modifyView"></div>', {	//定义修改个人信息的视图
		btn: ['确认修改','取消修改'] //按钮
		,area: ['500px','380px']
	}, function(index){
		var mdfNickName = $("#mdfNickName").val();
		var mdfRealName = $("#mdfRealName").val();
		var mdfTel = $("#mdfTel").val();
		var mdfEmail = $("#mdfEmail").val();
		if(mdfNickName==null || mdfNickName==''){
			layer.msg("昵称不可为空！"); return;
		}
		if(mdfRealName==null || mdfRealName==''){
			layer.msg("姓名不可为空！"); return;
		}
		if(mdfTel==null || mdfTel==''){
			layer.msg("电话不可为空！"); return;
		}
		if(mdfEmail==null || mdfEmail==''){
			layer.msg("邮箱不可为空！"); return;
		}
		$.post("menber/modifyByMno",{mno:mno,nickName:mdfNickName,realName:mdfRealName,tel:mdfTel,email:mdfEmail},function(data){
			data = parseInt($.trim(data));
			if( data > 0 ){
				//layer.close(index);
				layer.msg("个人信息修改成功！");
				$("#nickName").html(mdfNickName);
				$("#realName").html(mdfRealName);
				$("#tel").html(mdfTel);
				$("#email").html(mdfEmail);
			}else if(data == -101){
				layer.msg("该用户名已占用！");
			}else if(data == -102){
				layer.msg("该邮箱已注册！");
			}else if(data == -103){
				layer.msg("该手机号已注册！");
			}else{
				layer.msg("个人信息修改失败！");
			}
		});
	});
	var data = {"list":data}
	var getTpl = modifyPersonInfoModel.innerHTML , view = document.getElementById('modifyView');;
	laytpl(getTpl).render(data, function(html) {
		view.innerHTML = html;
	});
}

async function showRecentView(){
	var db = new Dexie('fresh');	//声明数据库
	db.version(1).stores({		//注册db
		lastView : 'id'
	});
	var result = await db.lastView.get(user.mno);
	console.log( result.gInfos );
	var data = {"list":result.gInfos};
	var getTpl = recentBrowseModel.innerHTML , view = document.getElementById('showView');
	laytpl(getTpl).render(data, function(html) {
		view.innerHTML = html;
	});
	db.close();
}

//修改选中的导航样式
function changeSelect(obj){
	var lis = $("#navBar li a");
	$(lis).removeAttr("class");
	$(obj).addClass("active");
}
