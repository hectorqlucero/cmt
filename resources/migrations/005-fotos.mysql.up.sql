create table fotos (
  id int unsigned not null auto_increment primary key,
  fecha date,
  enlace text
) engine=innodb default charset=utf8mb3;
