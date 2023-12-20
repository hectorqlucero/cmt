CREATE TABLE `talleres` (
  `id` int unsigned NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) DEFAULT NULL,
  `direccion` varchar(200) DEFAULT NULL,
  `telefono` varchar(100) DEFAULT NULL,
  `horarios` text,
  `sitio` text CHARACTER SET utf8 COLLATE utf8_general_ci,
  `direcciones` text,
  `historia` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3
