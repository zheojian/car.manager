create database car_manager default charset=utf8;

use car_manager;

create table userinfo(
	uid int not null auto_increment primary key,
	user_name varchar(20) not null,
	password varchar(64) not null,
	mobile varchar(15),
	remark varchar(1024),
	identity varchar(18) not null,
	driving_license varchar(20),
	driving_license_date date,
	home_tel varchar(15),
	home_addr varchar(255),
	status int not null default 0,
	pms int not null default 0
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into userinfo values(null,'admin',md5('123456'),'12345678901',null,'123456789012345678','driving_license',now(),null,null,0,2);
 
create table dept(
	did int not null auto_increment primary key,
	dept_name varchar(20) not null
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

insert into dept values(null,'办公室');

/* 	正常0  停用 1  保养 2  派出 3
*/
create table carinfo(
	cid int not null auto_increment primary key,
	license varchar(8) not null,
	uid int not null,
	annual_inspect_date date not null,
	insurance_date date not null,
	engine_no varchar(50) not null,
	driving_license varchar(20) not null,
	owner varchar(20) not null,
	remark varchar(255),
	status int not null default 0,
	mileage int not null default 0,
	unique (license),
	foreign key ( uid ) references userinfo(uid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 	申请(待审核)0 -> 批准(已批准)1  -> 完成2
				        否决3
*/
create table outinfo(
	oid int not null auto_increment primary key,
	cid int not null,
	apply_uid int not null,
	out_date date not null,
	come_date date,
	budget_day int not null default 1,
	cost_day int,
	destination varchar(255) not null,
	begin_mile int not null,
	end_mile int,
	driver_uid int not null, 
	dept_id int not null,
	retinue varchar(255),
	reason varchar(255),
	remark varchar(255),
	approve_uid int,
	status int not null default 0,
	apply_date date not null,
	foreign key ( cid ) references carinfo(cid),
	foreign key ( apply_uid ) references userinfo(uid),
	foreign key ( driver_uid ) references userinfo(uid),
	foreign key ( dept_id ) references dept(did)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

/* 	申请(待审核)0 -> 批准(待保养)1  -> 完成2
				        否决3
*/
create table maintain(
	mid int not null auto_increment primary key,
	cid int not null,
	apply_uid int not null,
	maintain_item varchar(255) not null,
	maintain_date date not null,
	maintain_co varchar(255),
	maintain_fee decimal(8,2),
	remark varchar(255),
	status int not null default 0,
	approve_uid int not null,
	maintain_reason varchar(255) not null,
	maintain_type varchar(100),
	foreign key ( cid ) references carinfo(cid),
	foreign key ( apply_uid ) references userinfo(uid),
	foreign key ( approve_uid ) references userinfo(uid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

create table refuel(
	rid int not null auto_increment primary key,
	cid int not null,
	uid int not null,
	refuel_date date not null,
	refuel_fee decimal(8,2) not null,
	mileage int not null,
	remark varchar(255),
	foreign key ( uid ) references userinfo(uid),
	foreign key ( cid ) references carinfo(cid)
)ENGINE=InnoDB DEFAULT CHARSET=utf8;
