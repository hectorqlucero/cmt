CREATE TABLE cmt (
    id int unsigned NOT NULL AUTO_INCREMENT,
    nombre varchar(100) DEFAULT NULL,
    comments text,
    maximo int(11) DEFAULT 0,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb3
