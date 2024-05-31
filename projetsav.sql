-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : ven. 31 mai 2024 à 16:55
-- Version du serveur : 8.0.31
-- Version de PHP : 8.0.26


SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `projetsav`
--

-- --------------------------------------------------------

--
-- Structure de la table `dossier_sav`
--

DROP TABLE IF EXISTS `dossier_sav`;
CREATE TABLE IF NOT EXISTS `dossier_sav` (
  `nom` varchar(30) NOT NULL,
  `prenom` varchar(30) NOT NULL,
  `telephone` varchar(30) NOT NULL,
  `reclamation` varchar(250) NOT NULL,
  `date` date NOT NULL,
  `numero_ticket` int NOT NULL AUTO_INCREMENT,
  `etat_ticket` varchar(10) NOT NULL,
  PRIMARY KEY (`numero_ticket`)
) ENGINE=InnoDB AUTO_INCREMENT=660 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Déchargement des données de la table `dossier_sav`
--

INSERT INTO `dossier_sav` (`nom`, `prenom`, `telephone`, `reclamation`, `date`, `numero_ticket`, `etat_ticket`) VALUES
('Synyshyn', 'Pavlo', '32323232', 'batterie', '2023-10-11', 1, 'Ouvert'),
('ZDZ', 'TEST', 'TEST', 'PAVLO', '2023-10-09', 630, 'Fermé'),
('TEST1', 'TEST1', '23923892', 'AAAAAAAAAAAAA', '2023-12-22', 652, 'En cours'),
('TEST1', 'TEST1', '23923892', '232323AAEZEZTEST', '2023-12-22', 653, 'En cours'),
('TEST2', 'TEST2', '238923', 'RIEN', '2023-12-23', 654, 'Fermé'),
('TEST2', 'TEST2', '238923', 'RIEN', '2023-12-23', 655, 'Ouvert'),
('TEST 3', 'TEST 3', '2323829', 'Oui', '2023-12-15', 656, 'Ouvert'),
('TEST 3', 'TEST 3', '2323829', 'Oui', '2023-12-15', 657, 'Ouvert'),
('TEST 4', '23232', '323232', 'NON', '2023-12-16', 658, 'En Cours'),
('TEST 4', '23232', '323232', 'NON', '2023-12-16', 659, 'Fermé');
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
