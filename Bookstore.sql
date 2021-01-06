create table Member(
	cid  varchar(6)  not null,
	cname varchar(6) not null,

constraint member_FK
		foreign key (cid)
		references Customer (cid)
);
