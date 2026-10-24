CREATE DATABASE  IF NOT EXISTS `politecnicoagroindustrial` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `politecnicoagroindustrial`;
-- MySQL dump 10.13  Distrib 8.0.46, for Win64 (x86_64)
--
-- Host: localhost    Database: politecnicoagroindustrial
-- ------------------------------------------------------
-- Server version	8.4.11

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `asignatura`
--

DROP TABLE IF EXISTS `asignatura`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asignatura` (
  `id_asignatura` int NOT NULL AUTO_INCREMENT,
  `id_programa` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `creditos` int DEFAULT NULL,
  PRIMARY KEY (`id_asignatura`),
  KEY `fk_asignatura_programa` (`id_programa`),
  CONSTRAINT `fk_asignatura_programa` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asignatura`
--

LOCK TABLES `asignatura` WRITE;
/*!40000 ALTER TABLE `asignatura` DISABLE KEYS */;
INSERT INTO `asignatura` VALUES (9,1,'Bases de Datos','Diseño, modelado y administracion de bases de datos',3),(10,1,'Programacion','Fundamentos de programacion y desarrollo de aplicaciones',4),(11,1,'Ingenieria de Software','Metodologias y procesos para el desarrollo de software',3),(12,1,'Analisis de Sistemas','Analisis y especificacion de requisitos de software',3),(13,2,'Gestion Empresarial','Fundamentos de administracion y gestion organizacional',3),(14,2,'Procesos Administrativos','Planeacion, organizacion y control administrativo',3),(15,3,'Produccion Agropecuaria','Fundamentos de produccion agricola y pecuaria',4),(16,4,'Contabilidad General','Principios y procedimientos de contabilidad',4);
/*!40000 ALTER TABLE `asignatura` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `asistencia`
--

DROP TABLE IF EXISTS `asistencia`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `asistencia` (
  `id_asistencia` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_grupo` int NOT NULL,
  `fecha` date NOT NULL,
  `estado` varchar(20) NOT NULL,
  `observacion` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`id_asistencia`),
  KEY `fk_asistencia_estudiante` (`id_estudiante`),
  KEY `fk_asistencia_grupo` (`id_grupo`),
  CONSTRAINT `fk_asistencia_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`),
  CONSTRAINT `fk_asistencia_grupo` FOREIGN KEY (`id_grupo`) REFERENCES `grupo` (`id_grupo`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `asistencia`
--

LOCK TABLES `asistencia` WRITE;
/*!40000 ALTER TABLE `asistencia` DISABLE KEYS */;
INSERT INTO `asistencia` VALUES (1,25,1,'2026-05-04','Presente',NULL),(2,25,1,'2026-05-11','Presente',NULL),(3,25,1,'2026-05-18','Ausente','Inasistencia justificada'),(4,26,1,'2026-05-04','Presente',NULL),(5,26,1,'2026-05-11','Presente',NULL),(6,26,1,'2026-05-18','Presente',NULL),(7,27,1,'2026-05-04','Presente',NULL),(8,27,1,'2026-05-11','Ausente','No presento justificacion'),(9,27,1,'2026-05-18','Presente',NULL),(10,28,5,'2026-05-05','Presente',NULL),(11,28,5,'2026-05-12','Presente',NULL),(12,29,7,'2026-05-06','Presente',NULL),(13,29,7,'2026-05-13','Presente',NULL),(14,30,8,'2026-05-08','Presente',NULL),(15,30,8,'2026-05-15','Ausente','Calamidad familiar');
/*!40000 ALTER TABLE `asistencia` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `calificacion`
--

DROP TABLE IF EXISTS `calificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `calificacion` (
  `id_calificacion` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_asignatura` int NOT NULL,
  `nota` decimal(5,2) NOT NULL,
  `periodo_academico` varchar(20) DEFAULT NULL,
  `fecha_calificacion` date DEFAULT NULL,
  PRIMARY KEY (`id_calificacion`),
  KEY `fk_calificacion_estudiante` (`id_estudiante`),
  KEY `fk_calificacion_asignatura` (`id_asignatura`),
  CONSTRAINT `fk_calificacion_asignatura` FOREIGN KEY (`id_asignatura`) REFERENCES `asignatura` (`id_asignatura`),
  CONSTRAINT `fk_calificacion_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `calificacion`
--

LOCK TABLES `calificacion` WRITE;
/*!40000 ALTER TABLE `calificacion` DISABLE KEYS */;
INSERT INTO `calificacion` VALUES (1,25,9,4.50,'2026-1','2026-05-20'),(2,25,10,4.20,'2026-1','2026-05-21'),(3,25,11,4.60,'2026-1','2026-05-22'),(4,25,12,4.30,'2026-1','2026-05-23'),(5,26,9,3.80,'2026-1','2026-05-20'),(6,26,10,4.00,'2026-1','2026-05-21'),(7,26,11,3.70,'2026-1','2026-05-22'),(8,27,9,4.10,'2026-1','2026-05-20'),(9,27,12,4.40,'2026-1','2026-05-23'),(10,28,13,4.00,'2026-1','2026-05-24'),(11,28,14,4.30,'2026-1','2026-05-25'),(12,29,15,3.90,'2026-1','2026-05-25'),(13,30,16,4.50,'2026-1','2026-05-26');
/*!40000 ALTER TABLE `calificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `certificado`
--

DROP TABLE IF EXISTS `certificado`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `certificado` (
  `id_certificado` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `tipo` varchar(100) NOT NULL,
  `fecha_emision` date NOT NULL,
  `estado` varchar(20) DEFAULT 'Generado',
  PRIMARY KEY (`id_certificado`),
  KEY `fk_certificado_estudiante` (`id_estudiante`),
  CONSTRAINT `fk_certificado_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `certificado`
--

LOCK TABLES `certificado` WRITE;
/*!40000 ALTER TABLE `certificado` DISABLE KEYS */;
/*!40000 ALTER TABLE `certificado` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `detalle_matricula`
--

DROP TABLE IF EXISTS `detalle_matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `detalle_matricula` (
  `id_detalle` int NOT NULL AUTO_INCREMENT,
  `id_matricula` int NOT NULL,
  `id_asignatura` int NOT NULL,
  `estado` varchar(20) DEFAULT 'Cursando',
  PRIMARY KEY (`id_detalle`),
  KEY `fk_detalle_matricula` (`id_matricula`),
  KEY `fk_detalle_asignatura` (`id_asignatura`),
  CONSTRAINT `fk_detalle_asignatura` FOREIGN KEY (`id_asignatura`) REFERENCES `asignatura` (`id_asignatura`),
  CONSTRAINT `fk_detalle_matricula` FOREIGN KEY (`id_matricula`) REFERENCES `matricula` (`id_matricula`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `detalle_matricula`
--

LOCK TABLES `detalle_matricula` WRITE;
/*!40000 ALTER TABLE `detalle_matricula` DISABLE KEYS */;
INSERT INTO `detalle_matricula` VALUES (14,1,9,'Cursando'),(15,1,10,'Cursando'),(16,1,11,'Cursando'),(17,1,12,'Cursando'),(18,2,9,'Cursando'),(19,2,10,'Cursando'),(20,2,11,'Cursando'),(21,3,9,'Cursando'),(22,3,12,'Cursando'),(23,4,13,'Cursando'),(24,4,14,'Cursando'),(25,5,15,'Cursando'),(26,6,16,'Cursando');
/*!40000 ALTER TABLE `detalle_matricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docente`
--

DROP TABLE IF EXISTS `docente`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docente` (
  `id_docente` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `documento` varchar(20) NOT NULL,
  `especialidad` varchar(100) DEFAULT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_docente`),
  UNIQUE KEY `documento` (`documento`),
  KEY `fk_docente_usuario` (`id_usuario`),
  CONSTRAINT `fk_docente_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docente`
--

LOCK TABLES `docente` WRITE;
/*!40000 ALTER TABLE `docente` DISABLE KEYS */;
INSERT INTO `docente` VALUES (5,23,'Carlos Alberto','Ramirez','9001001001','Desarrollo de Software y Bases de Datos','3004567890'),(6,24,'Ana Lucia','Martinez','9001001002','Gestion Administrativa','3015678901');
/*!40000 ALTER TABLE `docente` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `estudiante`
--

DROP TABLE IF EXISTS `estudiante`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `estudiante` (
  `id_estudiante` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `id_programa` int NOT NULL,
  `nombres` varchar(100) NOT NULL,
  `apellidos` varchar(100) NOT NULL,
  `documento` varchar(20) NOT NULL,
  `telefono` varchar(20) DEFAULT NULL,
  `direccion` varchar(150) DEFAULT NULL,
  `fecha_nacimiento` date DEFAULT NULL,
  PRIMARY KEY (`id_estudiante`),
  UNIQUE KEY `documento` (`documento`),
  KEY `fk_estudiante_usuario` (`id_usuario`),
  KEY `fk_estudiante_programa` (`id_programa`),
  CONSTRAINT `fk_estudiante_programa` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`),
  CONSTRAINT `fk_estudiante_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `estudiante`
--

LOCK TABLES `estudiante` WRITE;
/*!40000 ALTER TABLE `estudiante` DISABLE KEYS */;
INSERT INTO `estudiante` VALUES (25,25,1,'Laura Isabella','Mosquera','1001001001','3104567890','Calle 10 # 20-15','2005-04-18'),(26,26,1,'Juan David','Torres','1001001002','3115678901','Carrera 15 # 8-20','2004-08-12'),(27,27,1,'Valentina','Rodriguez','1001001003','3126789012','Calle 25 # 12-30','2005-01-25'),(28,28,2,'Andres Felipe','Gomez','1001001004','3137890123','Carrera 8 # 14-25','2004-11-09'),(29,29,3,'Camila','Martinez','1001001005','3148901234','Calle 30 # 5-18','2005-06-21'),(30,30,4,'Sebastian','Lopez','1001001006','3159012345','Carrera 20 # 10-12','2004-03-15');
/*!40000 ALTER TABLE `estudiante` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `grupo`
--

DROP TABLE IF EXISTS `grupo`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `grupo` (
  `id_grupo` int NOT NULL AUTO_INCREMENT,
  `id_asignatura` int NOT NULL,
  `id_docente` int NOT NULL,
  `nombre_grupo` varchar(50) NOT NULL,
  `jornada` varchar(30) DEFAULT NULL,
  `cupo_maximo` int DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'Activo',
  PRIMARY KEY (`id_grupo`),
  KEY `fk_grupo_asignatura` (`id_asignatura`),
  KEY `fk_grupo_docente` (`id_docente`),
  CONSTRAINT `fk_grupo_asignatura` FOREIGN KEY (`id_asignatura`) REFERENCES `asignatura` (`id_asignatura`),
  CONSTRAINT `fk_grupo_docente` FOREIGN KEY (`id_docente`) REFERENCES `docente` (`id_docente`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `grupo`
--

LOCK TABLES `grupo` WRITE;
/*!40000 ALTER TABLE `grupo` DISABLE KEYS */;
INSERT INTO `grupo` VALUES (1,9,5,'ADS-01','Diurna',30,'Activo'),(2,10,5,'ADS-02','Diurna',30,'Activo'),(3,11,5,'ADS-03','Nocturna',25,'Activo'),(4,12,5,'ADS-04','Diurna',30,'Activo'),(5,13,6,'GA-01','Diurna',30,'Activo'),(6,14,6,'GA-02','Diurna',30,'Activo'),(7,15,5,'PA-01','Diurna',25,'Activo'),(8,16,6,'CF-01','Nocturna',25,'Activo');
/*!40000 ALTER TABLE `grupo` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `horario`
--

DROP TABLE IF EXISTS `horario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `horario` (
  `id_horario` int NOT NULL AUTO_INCREMENT,
  `id_grupo` int NOT NULL,
  `dia_semana` varchar(20) NOT NULL,
  `hora_inicio` time NOT NULL,
  `hora_fin` time NOT NULL,
  `aula` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`id_horario`),
  KEY `fk_horario_grupo` (`id_grupo`),
  CONSTRAINT `fk_horario_grupo` FOREIGN KEY (`id_grupo`) REFERENCES `grupo` (`id_grupo`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `horario`
--

LOCK TABLES `horario` WRITE;
/*!40000 ALTER TABLE `horario` DISABLE KEYS */;
INSERT INTO `horario` VALUES (1,1,'Lunes','07:00:00','09:00:00','Aula 101'),(2,1,'Miercoles','07:00:00','09:00:00','Aula 101'),(3,2,'Martes','09:00:00','11:00:00','Laboratorio 1'),(4,2,'Jueves','09:00:00','11:00:00','Laboratorio 1'),(5,3,'Lunes','18:00:00','20:00:00','Laboratorio 2'),(6,3,'Miercoles','18:00:00','20:00:00','Laboratorio 2'),(7,4,'Viernes','07:00:00','09:00:00','Aula 102'),(8,5,'Martes','07:00:00','09:00:00','Aula 201'),(9,6,'Jueves','07:00:00','09:00:00','Aula 202'),(10,7,'Miercoles','10:00:00','12:00:00','Aula Agropecuaria'),(11,8,'Viernes','18:00:00','20:00:00','Aula 301');
/*!40000 ALTER TABLE `horario` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `matricula`
--

DROP TABLE IF EXISTS `matricula`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `matricula` (
  `id_matricula` int NOT NULL AUTO_INCREMENT,
  `id_estudiante` int NOT NULL,
  `id_programa` int NOT NULL,
  `fecha_matricula` date NOT NULL,
  `periodo_academico` varchar(20) NOT NULL,
  `estado` varchar(20) DEFAULT 'Activa',
  PRIMARY KEY (`id_matricula`),
  KEY `fk_matricula_estudiante` (`id_estudiante`),
  KEY `fk_matricula_programa` (`id_programa`),
  CONSTRAINT `fk_matricula_estudiante` FOREIGN KEY (`id_estudiante`) REFERENCES `estudiante` (`id_estudiante`),
  CONSTRAINT `fk_matricula_programa` FOREIGN KEY (`id_programa`) REFERENCES `programa` (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `matricula`
--

LOCK TABLES `matricula` WRITE;
/*!40000 ALTER TABLE `matricula` DISABLE KEYS */;
INSERT INTO `matricula` VALUES (1,25,1,'2026-01-15','2026-1','Activa'),(2,26,1,'2026-01-15','2026-1','Activa'),(3,27,1,'2026-01-16','2026-1','Activa'),(4,28,2,'2026-01-17','2026-1','Activa'),(5,29,3,'2026-01-17','2026-1','Activa'),(6,30,4,'2026-01-18','2026-1','Activa');
/*!40000 ALTER TABLE `matricula` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `notificacion`
--

DROP TABLE IF EXISTS `notificacion`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `notificacion` (
  `id_notificacion` int NOT NULL AUTO_INCREMENT,
  `id_usuario` int NOT NULL,
  `asunto` varchar(150) NOT NULL,
  `mensaje` varchar(500) NOT NULL,
  `fecha` datetime NOT NULL,
  `estado` varchar(20) DEFAULT 'No leida',
  PRIMARY KEY (`id_notificacion`),
  KEY `fk_notificacion_usuario` (`id_usuario`),
  CONSTRAINT `fk_notificacion_usuario` FOREIGN KEY (`id_usuario`) REFERENCES `usuario` (`id_usuario`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `notificacion`
--

LOCK TABLES `notificacion` WRITE;
/*!40000 ALTER TABLE `notificacion` DISABLE KEYS */;
INSERT INTO `notificacion` VALUES (1,21,'Bienvenida al sistema','Bienvenido al sistema academico del Politecnico Agroindustrial.','2026-01-20 08:00:00','Leida'),(2,22,'Horario actualizado','Su horario academico ha sido actualizado.','2026-01-21 09:30:00','No leida'),(3,23,'Calificaciones disponibles','Ya puede consultar sus calificaciones del periodo.','2026-05-27 10:00:00','No leida'),(4,24,'Matricula confirmada','Su matricula para el periodo 2026-1 ha sido confirmada.','2026-01-22 08:30:00','Leida'),(5,25,'Asistencia registrada','Se ha actualizado su registro de asistencia.','2026-05-20 11:00:00','No leida'),(6,26,'Certificado disponible','Su certificado de matricula se encuentra disponible.','2026-02-04 14:00:00','Leida');
/*!40000 ALTER TABLE `notificacion` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `programa`
--

DROP TABLE IF EXISTS `programa`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `programa` (
  `id_programa` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(100) NOT NULL,
  `descripcion` varchar(200) DEFAULT NULL,
  `duracion` int DEFAULT NULL,
  `estado` varchar(20) DEFAULT 'Activo',
  PRIMARY KEY (`id_programa`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `programa`
--

LOCK TABLES `programa` WRITE;
/*!40000 ALTER TABLE `programa` DISABLE KEYS */;
INSERT INTO `programa` VALUES (1,'Analisis y Desarrollo de Software','Formacion en desarrollo de software, bases de datos y tecnologias de la informacion',27,'Activo'),(2,'Gestion Administrativa','Formacion orientada a procesos administrativos y organizacionales',24,'Activo'),(3,'Produccion Agropecuaria','Formacion en procesos de produccion y manejo agropecuario',24,'Activo'),(4,'Contabilidad y Finanzas','Formacion en procesos contables, financieros y administrativos',24,'Activo');
/*!40000 ALTER TABLE `programa` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `rol`
--

DROP TABLE IF EXISTS `rol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `rol` (
  `id_rol` int NOT NULL AUTO_INCREMENT,
  `nombre` varchar(50) NOT NULL,
  `descripcion` varchar(150) DEFAULT NULL,
  PRIMARY KEY (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `rol`
--

LOCK TABLES `rol` WRITE;
/*!40000 ALTER TABLE `rol` DISABLE KEYS */;
INSERT INTO `rol` VALUES (1,'Administrador','Gestiona usuarios y configuración del sistema'),(2,'Coordinador','Gestiona programas y procesos académicos'),(3,'Docente','Gestiona grupos, calificaciones y asistencias'),(4,'Estudiante','Consulta información académica y administrativa');
/*!40000 ALTER TABLE `rol` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `usuario`
--

DROP TABLE IF EXISTS `usuario`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `usuario` (
  `id_usuario` int NOT NULL AUTO_INCREMENT,
  `id_rol` int NOT NULL,
  `nombre` varchar(100) NOT NULL,
  `correo` varchar(100) NOT NULL,
  `contrasena` varchar(255) NOT NULL,
  `estado` varchar(20) DEFAULT 'Activo',
  PRIMARY KEY (`id_usuario`),
  UNIQUE KEY `correo` (`correo`),
  KEY `fk_usuario_rol` (`id_rol`),
  CONSTRAINT `fk_usuario_rol` FOREIGN KEY (`id_rol`) REFERENCES `rol` (`id_rol`)
) ENGINE=InnoDB AUTO_INCREMENT=31 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `usuario`
--

LOCK TABLES `usuario` WRITE;
/*!40000 ALTER TABLE `usuario` DISABLE KEYS */;
INSERT INTO `usuario` VALUES (21,1,'Administrador Sistema','admin@politecnicoagroindustrial.edu.co','Admin123*','Activo'),(22,2,'Maria Fernanda Gomez','maria.gomez@politecnicoagroindustrial.edu.co','Maria123*','Activo'),(23,3,'Carlos Alberto Ramirez','carlos.ramirez@politecnicoagroindustrial.edu.co','Carlos123*','Activo'),(24,3,'Ana Lucia Martinez','ana.martinez@politecnicoagroindustrial.edu.co','Ana123*','Activo'),(25,4,'Laura Isabella Mosquera','laura.mosquera@politecnicoagroindustrial.edu.co','Laura123*','Activo'),(26,4,'Juan David Torres','juan.torres@politecnicoagroindustrial.edu.co','Juan123*','Activo'),(27,4,'Valentina Rodriguez','valentina.rodriguez@politecnicoagroindustrial.edu.co','Valentina123*','Activo'),(28,4,'Andres Felipe Gomez','andres.gomez@politecnicoagroindustrial.edu.co','Andres123*','Activo'),(29,4,'Camila Martinez','camila.martinez@politecnicoagroindustrial.edu.co','Camila123*','Activo'),(30,4,'Sebastian Lopez','sebastian.lopez@politecnicoagroindustrial.edu.co','Sebastian123*','Activo');
/*!40000 ALTER TABLE `usuario` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-08-28 19:12:56
