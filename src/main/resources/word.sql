create table t_project(
	id bigint primary key auto_increment comment '主键:项目ID,自动增长',
	pro_name varchar(50) not null comment '项目名字',
	path varchar(50) not null comment '路径',
	remark varchar(500) comment '备注'
);

create table t_program_file(
	id bigint primary key auto_increment comment '程序文件表：主键ID，自动增长',
	file_name varchar(50) not null comment '文件名字',
	state varchar(2) not null comment '文件状态 1 开源文件 2 已归档',
	imprint varchar(500) comment '版本说明：每一次归档要求说明本次修改的内容',
	remark varchar(50) comment '备注'
);


create table t_pro_file(
  id bigint primary key auto_increment,
	pro_code bigint comment '外键:引用项目表t_project(id)',
	f_id bigint comment '外键:引用程序文件表t_program_file(id)',
	FOREIGN key(pro_code) REFERENCES  t_project(id),
	FOREIGN key(f_id) REFERENCES  t_program_file(id)
);


# 查询所有开源程序查询出来
select b.id,c.pro_name,a.file_name,a.state,c.path from t_program_file a ,t_pro_file b,t_project c where a.state = '1' and a.id = b.f_id and c.id = b.pro_code;



select * from t_project;
select * from t_program_file;
select * from t_pro_file;

select * from rest_rtable;


delete from t_pro_file where  pro_code not in(42,43);
delete from t_project where id not in(42,43);
delete from t_program_file where id not in(40,41);


-- drop table t_pro_file