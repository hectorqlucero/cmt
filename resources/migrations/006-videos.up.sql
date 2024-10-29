create table videos (
  id int unsigned not null auto_increment primary key,
  fecha date,
  titulo varchar(255),
  enlace text
) engine=innodb default charset=utf8mb3;
