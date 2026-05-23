create table talleres (
  id int unsigned not null auto_increment primary key,
  nombre varchar(255),
  direccion varchar(255),
  telefono varchar(255),
  horarios text,
  sitio text,
  direcciones text,
  historia text
) engine=innodb default charset=utf8mb3;
