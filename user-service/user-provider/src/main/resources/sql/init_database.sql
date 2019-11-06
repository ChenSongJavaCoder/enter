create table if not exists user_cs
(
	id bigint auto_increment
		primary key,
	username varchar(50) default '' null,
	nickname varchar(50) default '' null,
	age tinyint(3) default 0 null,
	password varchar(100) default '' null,
	deleted tinyint(1) default 0 not null,
	create_time datetime default CURRENT_TIMESTAMP null,
	modify_time datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP,
	constraint user_cs_username_uindex
		unique (username)
);