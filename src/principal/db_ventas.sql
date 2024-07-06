-- --------------------------------------------------------
-- Host:                         127.0.0.1
-- Versión del servidor:         8.0.30 - MySQL Community Server - GPL
-- SO del servidor:              Win64
-- HeidiSQL Versión:             12.1.0.6537
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- Volcando estructura de base de datos para db_ventas
CREATE DATABASE IF NOT EXISTS `db_ventas` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `db_ventas`;

-- Volcando estructura para tabla db_ventas.categoria
CREATE TABLE IF NOT EXISTS `categoria` (
  `id_categoria` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id_categoria`),
  UNIQUE KEY `nombre` (`nombre`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla db_ventas.categoria: ~10 rows (aproximadamente)
INSERT INTO `categoria` (`id_categoria`, `nombre`) VALUES
	(9, 'BORRADOR'),
	(7, 'CARTUCHERAS ACTUALIZADO'),
	(11, 'CERA'),
	(8, 'CORRECTOR'),
	(1, 'CUADERNOS'),
	(4, 'LAPICEROS'),
	(3, 'MOCHILAS'),
	(2, 'PLUMONES'),
	(5, 'REGLAS'),
	(6, 'TEMPERAS'),
	(12, 'TIJERAS');

-- Volcando estructura para procedimiento db_ventas.get_categoria
DELIMITER //
CREATE PROCEDURE `get_categoria`()
BEGIN
	SELECT * FROM categoria;
END//
DELIMITER ;

-- Volcando estructura para procedimiento db_ventas.get_categorias
DELIMITER //
CREATE PROCEDURE `get_categorias`()
BEGIN
    SELECT * FROM marca;
END//
DELIMITER ;

-- Volcando estructura para procedimiento db_ventas.insertar_categoria
DELIMITER //
CREATE PROCEDURE `insertar_categoria`(IN cNombre VARCHAR(100))
BEGIN
    DECLARE categoriaExiste INT;

    -- Verifica si el nombre de la categoría ya existe
    SELECT COUNT(*) INTO categoriaExiste
    FROM categoria
    WHERE nombre = cNombre;

    IF categoriaExiste > 0 THEN
        -- Si el nombre ya existe, lanza un error
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El nombre de la categoría ya existe.';
    ELSE
        -- Si el nombre no existe, inserta la nueva categoría
        INSERT INTO categoria (nombre) VALUES (cNombre);
    END IF;
END//
DELIMITER ;

-- Volcando estructura para procedimiento db_ventas.insert_categoria
DELIMITER //
CREATE PROCEDURE `insert_categoria`(IN cNombre VARCHAR(100))
BEGIN    
   INSERT INTO categoria (nombre) VALUES (cNombre);
END//
DELIMITER ;

-- Volcando estructura para tabla db_ventas.marca
CREATE TABLE IF NOT EXISTS `marca` (
  `id_marca` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  PRIMARY KEY (`id_marca`)
) ENGINE=InnoDB AUTO_INCREMENT=40 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla db_ventas.marca: ~28 rows (aproximadamente)
INSERT INTO `marca` (`id_marca`, `nombre`) VALUES
	(2, 'artesco'),
	(3, 'PILOT'),
	(4, 'LORO'),
	(5, 'NAVARRETE'),
	(6, 'ALPA'),
	(7, 'FABER CASTELL'),
	(9, 'LULU'),
	(10, 'PAPA'),
	(11, 'Nuevo'),
	(12, 'otro'),
	(13, 'nuevo'),
	(14, 'mas'),
	(15, 'actualizado'),
	(16, 'DIEZYSEIS'),
	(18, 'NUEVO'),
	(20, 'aaaaaaaaaa'),
	(21, 'bbbbbbbbbbbb'),
	(23, 'mas'),
	(24, 'nueve'),
	(26, 'PALETA'),
	(27, 'THANKS'),
	(28, 'agggg'),
	(29, 'abcs'),
	(30, 'aaaaaaaaaabbbbbbbbbbbb'),
	(34, 'LAPIZ'),
	(35, 'NADAL'),
	(36, 'MAMA'),
	(37, 'JHONY'),
	(38, '      CON ESPACIOS       '),
	(39, 'OTRO CON ESPACIOS');

-- Volcando estructura para tabla db_ventas.producto
CREATE TABLE IF NOT EXISTS `producto` (
  `id_producto` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(200) NOT NULL,
  `descripcion` text,
  `precio` double(8,2) DEFAULT '0.00',
  `stock` int DEFAULT '0',
  `id_marca` int DEFAULT NULL,
  `id_categoria` int DEFAULT NULL,
  PRIMARY KEY (`id_producto`),
  KEY `id_marca` (`id_marca`),
  KEY `id_categoria` (`id_categoria`),
  CONSTRAINT `producto_ibfk_1` FOREIGN KEY (`id_marca`) REFERENCES `marca` (`id_marca`),
  CONSTRAINT `producto_ibfk_2` FOREIGN KEY (`id_categoria`) REFERENCES `categoria` (`id_categoria`)
) ENGINE=InnoDB AUTO_INCREMENT=28 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- Volcando datos para la tabla db_ventas.producto: ~7 rows (aproximadamente)
INSERT INTO `producto` (`id_producto`, `nombre`, `descripcion`, `precio`, `stock`, `id_marca`, `id_categoria`) VALUES
	(1, 'PLUMON PARA PIZARRA', 'PLUMON PARA PIZARRA COLOR ROJO DE 200ML', 2.50, 50, 2, 2),
	(2, 'BORRADOR', 'BORRADOR AZUL Y ROJO 1', 1.00, 100, 2, 8),
	(3, 'LAPIZ', 'LAPIZ VERDE 2B', 2.00, 100, 28, 4),
	(4, 'Tijera pequeña', 'Tijera de color naranja pequeña', 3.00, 50, 2, 2),
	(5, 'colores pequeño de 6 unidades', 'colores pequeño de 6 unidades especial para niños de 0 a 2 años', 8.00, 12, 2, 2),
	(6, 'Regla', 'Regla de 30 cm transparente	', 2.00, 200, 2, 5),
	(7, 'TAJADOR', 'TAJADOR PARA NIÑOS CON DOBLE ENTRADA', 3.00, 50, 2, 4),
	(16, 'TIJERA MANGO NARANJA', 'DE 2.5" TIJERA BASICA', 5.00, 50, 2, 2),
	(17, 'REGLA GENERICA', 'REGLAGEN', 5.00, 50, 3, 3),
	(23, 'ELEMENTOS', 'NuevoElementos', 1.00, 1, 5, 11),
	(25, 'VERDE 60 HOJAS', 'cuadeno de 50 hojas', 5.00, 100, 4, 1);

-- Volcando estructura para procedimiento db_ventas.update_categoria
DELIMITER //
CREATE PROCEDURE `update_categoria`(IN cId INT, IN cNombre VARCHAR(100))
BEGIN
	UPDATE categoria SET nombre = cNombre WHERE id_categoria = cId; 
END//
DELIMITER ;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
