create table aventuras_link (
  id int unsigned not null auto_increment primary key,
  aventuras_id int unsigned not null,
  comments text,
  nombre varchar(255),
  key fk_aventuras_link_aventuras (aventuras_id),
  constraint fk_aventuras_link_aventuras foreign key (aventuras_id) references aventuras (id) on delete cascade
) engine=innodb default charset=utf8mb3;
