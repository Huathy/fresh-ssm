-- 创建数据库
create database fresh default character set utf8 collate utf8_bin;

-- 切换库
user fresh;

-- 如果存在则先删除
drop table if exists adminInfo;

-- 如果不存在则创建
-- 管理员信息表
create table if not exists adminInfo(
	aid int primary key auto_increment, -- 管理员编号
	aname varchar(100) not null unique, -- 管理员昵称
	pwd varchar(100) not null, -- 管理员密码
	tel varchar(15) unique -- 手机号码
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;

insert into adminInfo values(0, 'navy', password('123'), '15096098010');

-- 会员信息表
create table if not exists menberInfo(
	mno int primary key auto_increment,
	nickName varchar(100) not null unique, -- 昵称
	realName varchar(100), -- 真实姓名
	pwd varchar(100) not null, -- 密码
	tel varchar(15) not null unique, -- 手机号码
	email varchar(100) not null unique, -- 邮箱
	photo varchar(100), -- 图像
	regDate datetime, -- 注册日期
	status int
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;

-- 修改自增列的起始值
alter table menberInfo auto_increment=1001;

-- 商品类型表
create table if not exists goodsType(
	tno int primary key auto_increment,
	tname varchar(100) not null, -- 商品类型名
	pic varchar(100), -- 类型图片
	status int
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;

insert into goodsType values(0,'新鲜水果', 1),(0,'海鲜水产',1),(0,'猪牛羊肉',1),(0,'禽类蛋品',1),(0,'新鲜蔬菜',1),(0,'速冻食品',1);

-- 商品信息表
create table if not exists goodsInfo(
	gno int primary key auto_increment,
	gname varchar(100) not null, -- 商品名称
	tno int, -- 所属类型
	price decimal(8,2), -- 商品单价
	intro varchar(400), -- 简介
	balance int, -- 库存量
	pics varchar(1000), -- 图片
	unit varchar(50), -- 单位
	qperied varchar(50), -- 保质期
	weight varchar(50), -- 净重
	descr text, -- 描述
	constraint FK_goodsInfo_tno foreign key(tno) references goodsType(tno)
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin; 


-- 购物车表
create table if not exists cartInfo(
	cno int primary key auto_increment,
	mno int,
	gno int,
	num int,
	constraint FK_cartInfo_mno foreign key(mno) references menberInfo(mno),
	constraint FK_cartInfo_gno foreign key(gno) references goodsInfo(gno)
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;

-- 收货地址表
create table if not exists addrInfo(
	ano varchar(100) primary key,
	mno int,
	name varchar(100) not null, -- 收货人姓名
 	tel varchar(15) not null, -- 收货人联系方式
	province varchar(100) not null, -- 省
	city varchar(100) not null, -- 市
	area varchar(100) not null, -- 区
	addr varchar(100) not null,
	flag int, -- 是否为默认收货地址
	status int, -- 状态
	constraint FK_addrInfo_mno foreign key(mno) references menberInfo(mno)
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;

-- 订单表
create table if not exists orderInfo(
	ono varchar(100) primary key,
	odate datetime, -- 下单日期
	ano varchar(100), -- 收货地址
	sdate datetime, -- 发货日期
	rdate datetime, -- 收货日期
	status int, -- 订单状态
	price decimal(10,2), -- 订单总额
	invoice int, -- 是否已开发票
	constraint FK_orderInfo_ano foreign key(ano) references addrInfo(ano)
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;

-- 订单详细
create table if not exists orderItemInfo(
	ino int primary key auto_increment,
	ono varchar(100), -- 订单编号
	gno int, -- 商品编号
	nums int, -- 购买数量
	price decimal(10,2), -- 购买单价
	status int,
	constraint FK_orderItemInfo_gno foreign key(gno) references goodsInfo(gno),
	constraint FK_orderItemInfo_ono foreign key(ono) references orderInfo(ono)
)ENGINE=InnoDB default charset=utf8 collate=utf8_bin;
