function checkNull(params,msg){
	if(params.length <= 0){
		throw "参数为空！";
	}
	if(msg.length <= 0){
		throw "提示信息为空！";
	}
	if(msg.length != params.length){
		throw "参数与提示信息不匹配！";
	}
	for(var i=0;i<params.length;i++){
		var param = $.trim(params[i]);
		if(param==undefined || param==null || param=='' || param==""){
			alert(msg[i]+"不可为空！");
			return msg;
		}
	}
	return false;
}

//将json对象完全转为字符串格式，包括数值，null值
function fullToString(objs){
	for(var i in objs){
		var obj = objs[i]
		for(var key in obj){
			obj[key] = String(obj[key]);
		}
	}
	return objs;
}