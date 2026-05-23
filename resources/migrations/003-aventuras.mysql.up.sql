create table aventuras (
  id int unsigned not null auto_increment primary key,
  aventura text,
  fecha date,
  leader_email varchar(255),
  enlace text,
  enlacev text,
  cmt_id int unsigned not null default 0,
  key fk_aventuras_cmt (cmt_id),
  constraint fk_aventuras_cmt foreign key (cmt_id) references cmt (id) on delete cascade
) engine=innodb default charset=utf8mb3;
