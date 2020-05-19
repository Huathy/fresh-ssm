//定义 函数, 创建XML解析器, 加载 XML 文件
function loadXML(xmlFile) {
	// alert(xmlFile);  //  1. 测试
	//1. , 根据不同的浏览器 构建文档解析器,创建 XMLDOC
	var xmlDoc = null;

	//判断浏览器的类型
	if (window.ActiveXObject) { //支持IE浏览器的创建xmlDoc对象
		xmlDoc = new ActiveXObject('Microsoft.XMLDOM');
		xmlDoc.async = false;
		xmlDoc.load(xmlFile);
	} else if (document.implementation
			&& document.implementation.createDocument) { //支持火狐等浏览器的xmlDoc对象的创建
		try {
			xmlDoc = document.implementation.createDocument('', '', null);
			xmlDoc.async = false;
			xmlDoc.load(xmlFile);
		} catch (e) { //由于Chrome为了安全性考虑,不支持本地的加载,所有智能有XMLHttpRequest对象去发送请求,加载XML文件
			var xmlhttp = new window.XMLHttpRequest();
			xmlhttp.onreadystatechange = function() {
				if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
					xmlDoc = xmlhttp.responseXML.documentElement;
				}
			}
			xmlhttp.open("GET", xmlFile, false);
			xmlhttp.send(null);
		}
	} else {
		return null;
	}
	return xmlDoc;
}

// 用javascript,在页面加载的时候解析 ,读取这个XML文件的内容,获取省份
var provinces;
function showProvince() {
	//firefox会忽略无参数的alert
	//window.alert("sss"); 
	//alert("show");

	//1. 加载XML
	var myXmlDoc = loadXML("xmlFile/areas.xml");	//这里市xml的路径
	//alert(myXmlDoc);
	//2. 解析XML内容
	provinces = myXmlDoc.getElementsByTagName("province"); //根据xml的标签,获取一组元素
	//alert("省份："+provinces.length); //获取同名标签的 元素的 个数
	// 如何逐个 获取 这个标签的 文本信息, 这里<province name="江苏省">,name是属性
	for (var i = 0; i < provinces.length; i++) {
		// 获取 name属性值
		var p = provinces[i].getAttribute("name");
		//console.log(p);
		//到此,再进行绑定
		//  构建  Option对象
		var op = new Option(p, p);
		//console.log(op);
		//给下拉框 执行添加 选项, 使用javascript的 HTML DOM操作
		document.getElementById("province").options.add(op);
	}
}

//创建 方法,更改省份,显示对应城市,实现级联
var citys;
function showCity() {
	//alert("ok");
	//1. 首先获取当前 选择的是哪个省?
	//  js---> html
	var currentProvince = document.getElementById("province").value;
	//alert(currentProvince);

	//下面是: js ---> XML     关于 js-->css ,回去自己总结,对比

	//2. 根据这个省份,查询XML,返回对应的城市信息
	//2.1. 加载XML
	//var myXmlDoc = loadXML("areas.xml");		
	//2.2 解析XML内容
	//var  provinces=myXmlDoc.getElementsByTagName("province"); 
	//2.3 逐个判断比较
	//根据上面provinces遍历,找到指定的省份
	for (var i = 0; i < provinces.length; i++) {
		// 获取 name属性值
		//alert("for");
		//alert(provinces[i]);
		var p = provinces[i].getAttribute("name");
		if (p == currentProvince) {
			//alert("当前省份,在XML中的下标 顺序(从0开始):"+i);

			// 匹配 一致后,获取当前省份的 所有子元素,也就是所有的城市
			citys = provinces[i].childNodes;
			//alert(citys);
			//alert("所有的城市的个数:"+citys.length);
			//alert(citys.length);

			// 循环迭代 城市列表
			document.getElementById("city").length = 1;
			document.getElementById("area").length = 1
			for (var j = 0; j < citys.length; j++) {
				//alert(citys[j].nodeType);
				//逐个弹出 城市							
				//alert(citys[j].childNodes[0].nodeValue);

				// alert(provinces[i].childNodes[j].text); 等价上述

				// 把这个城市的 列表,绑定到 城市的下拉列表框
				if (citys[j].nodeType == 3)
					continue;
				var city = citys[j].getAttribute("name");

				document.getElementById("city").add(new Option(city, city));
				// 留给大家 思考.......  
			}
			break;
		}
	}
	//3. 得到城市信息,绑定到 城市的下拉框

}
function showArea() {
	//alert(citys);
	//1. 首先获取当前 选择的是哪个省?
	//  js---> html
	var currentCity = document.getElementById("city").value;
	//var  currentProvince= document.getElementById("province").value; 

	//alert(currentProvince);

	//下面是: js ---> XML     关于 js-->css ,回去自己总结,对比

	//2. 根据这个省份,查询XML,返回对应的城市信息
	//2.1. 加载XML
	// var myXmlDoc = loadXML("省市县信息.xml");		
	//2.2 解析XML内容
	//var provinces=myXmlDoc.getElementsByTagName("province"); 
	// var citys=showCity().childNodes;
	//2.3 逐个判断比较
	for (var i = 0; i < citys.length; i++) {
		// 获取 name属性值
		//alert(citys[i]);
		if (citys[i].nodeType == 3)
			continue;
		var c = citys[i].getAttribute("name");
		if (c == currentCity) {
			//alert("当前省份,在XML中的下标 顺序(从0开始):"+i);

			// 匹配 一致后,获取当前省份的 所有子元素,也就是所有的城市
			var areas = citys[i].childNodes;
			//alert("所有的城市的个数:"+citys.length);
			//alert(citys.length);

			// 循环迭代 城市列表
			document.getElementById("area").length = 1;
			for (var j = 0; j < areas.length; j++) {
				//alert(citys[j].nodeType);
				//逐个弹出 城市							
				//alert(citys[j].childNodes[0].nodeValue);

				// alert(provinces[i].childNodes[j].text); 等价上述

				// 把这个城市的 列表,绑定到 城市的下拉列表框
				if (areas[j].nodeType == 3)
					continue;
				var area = areas[j].getAttribute("name");

				document.getElementById("area").add(new Option(area, area));
				// 留给大家 思考.......  
			}
			break;
		}
	}

}