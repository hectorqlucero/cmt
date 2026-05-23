create table cmt (
  id int unsigned not null auto_increment primary key,
  nombre varchar(255),
  comments text,
  maximo int DEFAULT 0
) engine=innodb default charset=utf8mb3;
