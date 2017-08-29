-- phpMyAdmin SQL Dump
-- version 4.6.4
-- https://www.phpmyadmin.net/
--
-- Client :  127.0.0.1
-- Généré le :  Mar 20 Juin 2017 à 04:32
-- Version du serveur :  5.7.14
-- Version de PHP :  5.6.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données :  `rentes`
--

-- --------------------------------------------------------

--
-- Structure de la table `etablissement`
--

CREATE TABLE `etablissement` (
  `ID` int(11) NOT NULL,
  `nom` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `adresse1` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `adresse2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `codepostal` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `ville` varchar(150) COLLATE utf8_unicode_ci NOT NULL,
  `telephone` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `responsable` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `etablissement`
--

INSERT INTO `etablissement` (`ID`, `nom`, `adresse1`, `adresse2`, `codepostal`, `ville`, `telephone`, `email`, `responsable`) VALUES
(1, 'Service Technique de l\'Aviation Civile', '31 avenue du Maréchal Leclerc', 'CS 30012', '94385', 'BONNEUIL SUR MARNE CEDEX', '+ 33 1 49 56 81 12', '', 'Olivier JOUANS');

-- --------------------------------------------------------

--
-- Structure de la table `grade`
--

CREATE TABLE `grade` (
  `ID` int(11) NOT NULL,
  `libel` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `grade`
--

INSERT INTO `grade` (`ID`, `libel`) VALUES
(2, 'Contractuel'),
(3, 'Administratif'),
(4, 'Cadre');

-- --------------------------------------------------------

--
-- Structure de la table `preuve`
--

CREATE TABLE `preuve` (
  `ID` int(11) NOT NULL,
  `libel` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `preuve`
--

INSERT INTO `preuve` (`ID`, `libel`) VALUES
(1, 'Attestation sur l\'honneur'),
(2, 'Certificat médical'),
(3, 'Certificat de décès'),
(4, 'Décédé(e)'),
(5, 'Autre ');

-- --------------------------------------------------------

--
-- Structure de la table `preuvevie`
--

CREATE TABLE `preuvevie` (
  `ID` int(11) NOT NULL,
  `libel` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `datedemande` date NOT NULL,
  `datereception` date DEFAULT NULL,
  `ID_PREUVE` int(11) NOT NULL,
  `ID_RENTIER` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `preuvevie`
--

INSERT INTO `preuvevie` (`ID`, `libel`, `datedemande`, `datereception`, `ID_PREUVE`, `ID_RENTIER`) VALUES
(1, 'Preuve demandée ', '2016-01-01', '2016-01-08', 2, 1),
(2, 'Autre preuve demandée', '2017-01-10', NULL, 1, 2),
(3, 'Autre', '2017-01-10', '2017-06-01', 1, 3),
(5, 'Enfant', '2015-01-10', NULL, 2, 2),
(8, 'Nouvelle preuve', '2017-05-03', '2017-05-05', 5, 3),
(9, 'Attributaire', '2017-05-03', NULL, 1, 7),
(10, 'Autre preuve demandée', '2017-05-03', NULL, 1, 8),
(11, 'demande Preuve', '2017-05-10', '2017-05-13', 1, 9),
(12, 'demandée par téléphone', '2017-06-07', NULL, 1, 6),
(13, 'Envoyé courrier', '2017-06-07', NULL, 2, 10);

-- --------------------------------------------------------

--
-- Structure de la table `profil`
--

CREATE TABLE `profil` (
  `ID` int(11) NOT NULL,
  `nom` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `role` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `profil`
--

INSERT INTO `profil` (`ID`, `nom`, `role`) VALUES
(1, 'Administrateurs', 'ROLE_ADMIN'),
(2, 'Utilisateurs', 'ROLE_USER'),
(3, 'Invités  ', 'ROLE_INVITE'),
(5, 'Superviseurs', 'ROLE_SUPERVISOR');

-- --------------------------------------------------------

--
-- Structure de la table `rente`
--

CREATE TABLE `rente` (
  `ID` int(11) NOT NULL,
  `libel` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `dateconsolidation` date NOT NULL,
  `dateaccident` date NOT NULL,
  `txippaycause` decimal(5,2) NOT NULL,
  `txippaydroit` decimal(5,2) NOT NULL,
  `mntrenteinitial` decimal(10,2) NOT NULL,
  `etatrente` date DEFAULT NULL,
  `ID_RENTIER` int(11) NOT NULL,
  `ID_VERSEMTYPE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `rente`
--

INSERT INTO `rente` (`ID`, `libel`, `dateconsolidation`, `dateaccident`, `txippaycause`, `txippaydroit`, `mntrenteinitial`, `etatrente`, `ID_RENTIER`, `ID_VERSEMTYPE`) VALUES
(1, 'Rente pour AT du pied', '2016-03-09', '2016-03-09', '40.00', '20.00', '5000.00', NULL, 1, 1),
(2, 'Rente AT dos', '2015-07-08', '2015-07-08', '60.00', '30.00', '4500.00', '2016-11-26', 2, 1),
(3, 'AT ', '2016-06-05', '2016-06-05', '50.00', '25.00', '2300.00', NULL, 6, 1),
(5, 'Autre', '2016-01-13', '2016-01-13', '50.00', '25.00', '2300.00', NULL, 3, 1),
(6, 'Enfant tt', '2015-09-16', '2015-09-16', '50.00', '25.00', '1000.00', '2017-06-15', 3, 3),
(9, 'Une autre 2eme cc', '2016-07-22', '2016-07-22', '50.00', '25.00', '2360.00', '2017-06-15', 1, 3),
(10, 'Rente pour AT du doigt', '2016-07-15', '2016-07-15', '40.00', '20.00', '1200.00', NULL, 7, 1),
(11, 'autre droit', '2016-04-05', '2016-04-05', '50.00', '25.00', '3000.00', '2017-04-01', 3, 2),
(12, 'Pour le  versement Forfaitaire', '2016-04-06', '2016-04-06', '50.00', '25.00', '4500.00', '2017-06-15', 7, 3),
(13, 'test add', '2015-06-05', '2015-06-05', '50.00', '25.00', '3000.00', NULL, 3, 1),
(28, 'Rente AT DOS', '2017-01-15', '2017-01-10', '50.00', '25.00', '1000.00', NULL, 9, 1),
(29, 'AT Main droite', '2017-04-10', '2017-04-01', '50.00', '25.00', '2300.00', NULL, 2, 2),
(30, 'Rente du pied gauche', '2016-04-01', '2016-04-01', '40.00', '20.00', '3000.00', NULL, 8, 2),
(31, 'SQ', '2017-04-01', '2015-06-05', '50.00', '25.00', '2300.00', NULL, 9, 1),
(36, 'Rente du pied gauche', '2017-01-01', '2016-12-05', '50.00', '25.00', '2000.00', NULL, 10, 1),
(37, 'rente du 11', '2017-06-01', '2017-05-01', '60.00', '30.00', '2000.00', NULL, 10, 1),
(39, 'AT du DOS', '2017-01-31', '2017-01-07', '30.00', '20.00', '1500.00', NULL, 11, 1);

-- --------------------------------------------------------

--
-- Structure de la table `renterevalorisee`
--

CREATE TABLE `renterevalorisee` (
  `ID` int(11) NOT NULL,
  `datelancement` date NOT NULL,
  `datedeb` date NOT NULL,
  `datefin` date NOT NULL,
  `montantrevalorise` decimal(12,2) NOT NULL,
  `ID_RENTE` int(11) NOT NULL,
  `ID_REVALORISATION` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `renterevalorisee`
--

INSERT INTO `renterevalorisee` (`ID`, `datelancement`, `datedeb`, `datefin`, `montantrevalorise`, `ID_RENTE`, `ID_REVALORISATION`) VALUES
(1, '2016-03-09', '2016-03-09', '2016-03-31', '5000.00', 1, 1),
(4, '2016-01-13', '2016-01-13', '2016-03-31', '2300.00', 5, 1),
(7, '2016-07-15', '2016-07-15', '2017-02-19', '1200.00', 10, 1),
(10, '2015-06-05', '2015-06-05', '2016-03-31', '3000.00', 13, 1),
(13, '2015-07-08', '2015-07-08', '2016-03-31', '4500.00', 2, 1),
(15, '2016-06-05', '2016-06-05', '2017-02-19', '2300.00', 3, 1),
(263, '2016-07-22', '2016-07-22', '2017-06-15', '2360.00', 9, 1),
(264, '2015-09-16', '2015-09-16', '2017-06-15', '1000.00', 6, 1),
(265, '2016-04-06', '2016-04-06', '2017-06-15', '4500.00', 12, 1),
(289, '2017-05-15', '2017-04-10', '2017-05-09', '2300.00', 29, 1),
(357, '2016-04-05', '2016-04-05', '2017-02-19', '3000.00', 11, 1),
(358, '2016-04-01', '2016-04-01', '2016-04-01', '3000.00', 30, 1),
(359, '2017-01-15', '2017-01-15', '2017-02-19', '1000.00', 28, 1),
(360, '2017-04-01', '2017-04-01', '2017-04-01', '2300.00', 31, 1),
(857, '2017-06-06', '2017-01-01', '2017-02-19', '2000.00', 36, 1),
(937, '2017-06-15', '2016-04-01', '2016-11-26', '4513.50', 2, 3),
(938, '2017-06-15', '2017-02-20', '2017-03-31', '3003.00', 11, 6),
(939, '2017-06-15', '2017-04-01', '2017-04-01', '3009.01', 11, 4),
(940, '2017-06-15', '2016-04-01', '2017-02-19', '5015.00', 1, 3),
(941, '2017-06-15', '2017-02-20', '2017-03-31', '5020.02', 1, 6),
(942, '2017-06-15', '2017-04-01', '2017-05-09', '5030.06', 1, 4),
(943, '2017-06-15', '2017-05-10', '3000-12-31', '5037.60', 1, 5),
(944, '2017-06-15', '2017-02-20', '2017-03-31', '2302.30', 3, 6),
(945, '2017-06-15', '2017-04-01', '2017-05-09', '2306.90', 3, 4),
(946, '2017-06-15', '2017-05-10', '3000-12-31', '2310.37', 3, 5),
(947, '2017-06-15', '2016-04-01', '2017-02-19', '2306.90', 5, 3),
(948, '2017-06-15', '2017-02-20', '2017-03-31', '2309.21', 5, 6),
(949, '2017-06-15', '2017-04-01', '2017-05-09', '2313.83', 5, 4),
(950, '2017-06-15', '2017-05-10', '3000-12-31', '2317.30', 5, 5),
(951, '2017-06-15', '2017-02-20', '2017-03-31', '1201.20', 10, 6),
(952, '2017-06-15', '2017-04-01', '2017-05-09', '1203.60', 10, 4),
(953, '2017-06-15', '2017-05-10', '3000-12-31', '1205.41', 10, 5),
(954, '2017-06-15', '2016-04-01', '2017-02-19', '3009.00', 13, 3),
(955, '2017-06-15', '2017-02-20', '2017-03-31', '3012.01', 13, 6),
(956, '2017-06-15', '2017-04-01', '2017-05-09', '3018.03', 13, 4),
(957, '2017-06-15', '2017-05-10', '3000-12-31', '3022.56', 13, 5),
(958, '2017-06-15', '2017-02-20', '2017-03-31', '1001.00', 28, 6),
(959, '2017-06-15', '2017-04-01', '2017-05-09', '1003.00', 28, 4),
(960, '2017-06-15', '2017-05-10', '3000-12-31', '1004.51', 28, 5),
(961, '2017-06-15', '2017-04-01', '2017-05-09', '2304.60', 31, 4),
(962, '2017-06-15', '2017-05-10', '3000-12-31', '2308.06', 31, 5),
(963, '2017-06-15', '2017-02-20', '2017-03-31', '2002.00', 36, 6),
(964, '2017-06-15', '2017-04-01', '2017-05-09', '2006.00', 36, 4),
(965, '2017-06-15', '2017-05-10', '3000-12-31', '2009.01', 36, 5),
(966, '2017-06-15', '2017-05-10', '3000-12-31', '2303.45', 29, 5),
(967, '2017-06-15', '2016-04-01', '2017-02-19', '3009.00', 30, 3),
(968, '2017-06-15', '2017-02-20', '2017-03-31', '3012.01', 30, 6),
(969, '2017-06-15', '2017-04-01', '2017-05-09', '3018.03', 30, 4),
(970, '2017-06-15', '2017-05-10', '3000-12-31', '3022.56', 30, 5),
(971, '2017-06-19', '2017-06-01', '3000-12-31', '2000.00', 37, 1),
(973, '2017-06-20', '2017-01-31', '2017-02-19', '1500.00', 39, 1),
(974, '2017-06-20', '2017-02-20', '2017-03-31', '1501.50', 39, 6),
(975, '2017-06-20', '2017-04-01', '2017-05-09', '1504.50', 39, 4),
(976, '2017-06-20', '2017-05-10', '3000-12-31', '1506.76', 39, 5);

-- --------------------------------------------------------

--
-- Structure de la table `rentier`
--

CREATE TABLE `rentier` (
  `ID` int(11) NOT NULL,
  `nomfamille` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `nommarital` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `prenom` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `datenaissance` date NOT NULL,
  `sexe` tinyint(4) NOT NULL,
  `adresse1` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `adresse2` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `codepostal` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `ville` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `dossier` int(11) NOT NULL,
  `banque` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `guichet` char(10) COLLATE utf8_unicode_ci NOT NULL,
  `codebanque` varchar(10) COLLATE utf8_unicode_ci NOT NULL,
  `compte` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `cle` varchar(2) COLLATE utf8_unicode_ci NOT NULL,
  `iban` varchar(25) COLLATE utf8_unicode_ci NOT NULL,
  `findroit` date DEFAULT NULL,
  `motifarret` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `ID_USERS` int(11) NOT NULL,
  `ID_TITULAIRE` int(11) NOT NULL,
  `ID_GRADE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `rentier`
--

INSERT INTO `rentier` (`ID`, `nomfamille`, `nommarital`, `prenom`, `datenaissance`, `sexe`, `adresse1`, `adresse2`, `codepostal`, `ville`, `dossier`, `banque`, `guichet`, `codebanque`, `compte`, `cle`, `iban`, `findroit`, `motifarret`, `ID_USERS`, `ID_TITULAIRE`, `ID_GRADE`) VALUES
(1, 'DUPOND', '', 'Antoine', '1965-04-07', 1, '2 rue de Paris', '', '75000', 'PARIS', 12345, 'Ma Banque', '3333', '44444', '23456789', '22', '1235678900999', NULL, '', 3, 1, 2),
(2, 'MARTIN', '', 'Paul', '1955-08-07', 1, '4 rue de Nantes', '', '77000', 'MELUN', 12344, 'Banque', '3333', '44444', '7777776', '99', '776655443322', '2017-01-01', 'depart a etranger', 1, 1, 2),
(3, 'DUFOUR', '', 'Jean Pierre', '1945-01-01', 1, '50 Rue de la marne', 'Bat 360 Etage 2 Porte 54', '78000', 'VERSAILLES', 23458, 'BPP', '12345', '33333', '888888888', '77', '55667788995544', NULL, '', 1, 3, 2),
(6, 'DUBOIS', '', 'Jacques', '1996-01-01', 1, '2 rue de Paris', '', '75000', 'PARIS', 23456, 'LA BANQUE', '12345', '999999', '7777777777', '77', '55658876999933335555', NULL, '', 1, 3, 4),
(7, 'MONTAGNE', 'PLAINE', 'MARIE', '1972-08-08', 2, '6 rue du bois', 'Bat 2 etage 3', '77000', 'MELUN', 12345, 'BBA Banque', '12345', '888888', '777', '56', '0987654321', NULL, '', 1, 2, 3),
(8, 'DUJARDIN', '', 'Simone', '1972-08-08', 2, '2 rue de Paris', '', '75000', 'PARIS', 23456, 'LA BANQUE', '12345', '999999', '7777777777', '45', '676554335789', NULL, '', 1, 1, 4),
(9, 'POMMIER', '', 'Irène', '1945-10-25', 2, '34 Rue de la paix', '', '77170', 'SOLERS', 12356, 'BBPA', '123456', '76543', '09878909', '65', '654345768', NULL, '', 1, 1, 2),
(10, 'SAINT MARTIN', '', 'Antoine', '1962-09-05', 1, '2 rue de bois', '', '65000', 'AUBERT', 1234567, 'DPD', '234567', '7777777', '1234567890', '33', '445566778899', NULL, '', 1, 1, 3),
(11, 'MONTAGNE', 'DUBOIS', 'Jean', '1963-05-01', 1, '2 rue diu lac ', '', '77170', 'SOLERS', 1111, 'LA BANQUE', '344567', '888888', '1234567890', '22', '55658876999933335555', NULL, '', 1, 1, 2);

-- --------------------------------------------------------

--
-- Structure de la table `revalorisation`
--

CREATE TABLE `revalorisation` (
  `ID` int(11) NOT NULL,
  `libel` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `coeff` decimal(7,5) DEFAULT NULL,
  `daterevalorisation` date NOT NULL,
  `directive` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `revalorisation`
--

INSERT INTO `revalorisation` (`ID`, `libel`, `coeff`, `daterevalorisation`, `directive`) VALUES
(1, 'Rente initiale', '1.00000', '1800-01-01', 'Rente initiale'),
(3, 'Reval du 01/04/2016', '1.00300', '2016-04-01', 'Directive AD-04-2017'),
(4, 'Revalorisation du 01/04/2017', '1.00200', '2017-04-01', 'Directive AD-22'),
(5, 'Reval du 15 mai 2017', '1.00150', '2017-05-10', 'Directive 10-05-2017'),
(6, 'Revalorisation Février 2017', '1.00100', '2017-02-20', 'Directive du Février 2017');

-- --------------------------------------------------------

--
-- Structure de la table `titulaire`
--

CREATE TABLE `titulaire` (
  `ID` int(11) NOT NULL,
  `libel` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `titulaire`
--

INSERT INTO `titulaire` (`ID`, `libel`) VALUES
(1, 'Attributaire'),
(2, 'Conjoint(e)'),
(3, 'Enfant  ');

-- --------------------------------------------------------

--
-- Structure de la table `userprofil`
--

CREATE TABLE `userprofil` (
  `ID_PROFIL` int(11) NOT NULL,
  `ID_USERS` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `userprofil`
--

INSERT INTO `userprofil` (`ID_PROFIL`, `ID_USERS`) VALUES
(1, 1),
(2, 3),
(1, 5),
(1, 6),
(3, 7);

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE `users` (
  `ID` int(11) NOT NULL,
  `identif` varchar(20) COLLATE utf8_unicode_ci NOT NULL,
  `mdp` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `nom` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `prenom` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `telephone` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  `email` varchar(80) COLLATE utf8_unicode_ci DEFAULT NULL,
  `actif` tinyint(1) NOT NULL,
  `ID_ETABLISSEMENT` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`ID`, `identif`, `mdp`, `nom`, `prenom`, `telephone`, `email`, `actif`, `ID_ETABLISSEMENT`) VALUES
(1, 'Admin', '$2a$10$YuX6guouiawnVIpK9LryKOs/pvTi.XdWBxE2wzUOEbLboWC8V5Lnq', 'Administrateur ', 'Administrateur', '06-06-06-06-07', 'aa@gmail.com', 1, 1),
(3, 'Gourpil', '$2a$10$FSZGeitKuXlBvVFtsJgD4u7DXNrFGB0TdMXw85VRI/MsF45J1btsK', 'GOURPIL', 'Firmin', '06-06-06-06-06', 'aaaaa@bbbbbb.com', 1, 1),
(5, 'Isambert', '$2a$10$H2tdUvIW6ln4kseYmNP6XerRGfXfzG2L1xNDgL7LzsZYI9VZ6FfH.', 'ISAMBERT', 'Nicolas', '+33101010101', 'aa@gmail.com', 1, 1),
(6, 'Faulcon', '$2a$10$6V.neGslMFUsYBA43aBFl.9d8baefMYEeowlZsy.K.6I41q7a4SLq', 'FAULCON', 'Thibaut', '0101010101', 'aa@gmail.com', 1, 1),
(7, 'invite', '$2a$10$OMuNJruNHbcarBWU4m.31uY93rZKcqVQ/O3IXFoDVj9nJobRBsXp6', 'Invités', 'invités', '+33101010101', 'aa@gmail.com', 1, 1);

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `vcreditannuel`
-- (Voir ci-dessous la vue réelle)
--
CREATE TABLE `vcreditannuel` (
`dossier` int(11)
,`nomfamille` varchar(100)
,`nommarital` varchar(100)
,`prenom` varchar(100)
,`datenaissance` date
,`dateaccident` date
,`txippaycause` decimal(5,2)
,`txippaydroit` decimal(5,2)
,`rentelibel` varchar(100)
,`etatrente` date
,`datedeb` date
,`datefin` date
,`montantrevalorise` decimal(12,2)
,`gradelibel` varchar(50)
);

-- --------------------------------------------------------

--
-- Structure de la table `versement`
--

CREATE TABLE `versement` (
  `ID` int(11) NOT NULL,
  `montant` decimal(8,2) NOT NULL,
  `dateversem` date NOT NULL,
  `periodeversee` varchar(80) COLLATE utf8_unicode_ci NOT NULL,
  `dernierjourpaye` date NOT NULL,
  `etatedite` tinyint(1) DEFAULT NULL,
  `ID_RENTEREVALORISEE` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `versement`
--

INSERT INTO `versement` (`ID`, `montant`, `dateversem`, `periodeversee`, `dernierjourpaye`, `etatedite`, `ID_RENTEREVALORISEE`) VALUES
(275, '416.67', '2017-06-15', 'Paiement trimestriel du 31/03/2016', '2016-03-31', 0, 1),
(276, '1253.75', '2017-06-15', 'Paiement trimestriel du 30/06/2016', '2016-06-30', 0, 940),
(277, '1253.75', '2017-06-15', 'Paiement trimestriel du 30/09/2016', '2016-09-30', 0, 940),
(278, '1253.75', '2017-06-15', 'Paiement trimestriel du 31/12/2016', '2016-12-31', 0, 940),
(279, '1253.75', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 940),
(280, '1125.00', '2017-06-15', 'Paiement trimestriel du 30/09/2015', '2015-09-30', 0, 13),
(281, '1125.00', '2017-06-15', 'Paiement trimestriel du 31/12/2015', '2015-12-31', 0, 13),
(282, '1125.00', '2017-06-15', 'Paiement trimestriel du 31/03/2016', '2016-03-31', 0, 13),
(283, '1128.38', '2017-06-15', 'Paiement trimestriel du 30/06/2016', '2016-06-30', 0, 937),
(284, '1128.38', '2017-06-15', 'Paiement trimestriel du 30/09/2016', '2016-09-30', 0, 937),
(285, '1128.38', '2017-06-15', 'Paiement trimestriel du 26/11/2016', '2016-11-26', 0, 937),
(286, '191.67', '2017-06-15', 'Paiement trimestriel du 30/06/2016', '2016-06-30', 0, 15),
(287, '575.00', '2017-06-15', 'Paiement trimestriel du 30/09/2016', '2016-09-30', 0, 15),
(288, '575.00', '2017-06-15', 'Paiement trimestriel du 31/12/2016', '2016-12-31', 0, 15),
(289, '575.00', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 15),
(290, '575.00', '2017-06-15', 'Paiement trimestriel du 31/03/2016', '2016-03-31', 0, 4),
(291, '576.73', '2017-06-15', 'Paiement trimestriel du 30/06/2016', '2016-06-30', 0, 947),
(292, '576.73', '2017-06-15', 'Paiement trimestriel du 30/09/2016', '2016-09-30', 0, 947),
(293, '576.73', '2017-06-15', 'Paiement trimestriel du 31/12/2016', '2016-12-31', 0, 947),
(294, '576.73', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 947),
(295, '1000.00', '2017-06-15', 'Paiement du Forfait  le 15/06/2017', '2017-06-15', 0, 264),
(296, '2360.00', '2017-06-15', 'Paiement du Forfait  le 15/06/2017', '2017-06-15', 0, 263),
(297, '300.00', '2017-06-15', 'Paiement trimestriel du 30/09/2016', '2016-09-30', 0, 7),
(298, '300.00', '2017-06-15', 'Paiement trimestriel du 31/12/2016', '2016-12-31', 0, 7),
(299, '300.00', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 7),
(300, '250.00', '2017-06-15', 'Paiement mensuel du 30/04/2016', '2016-04-30', 0, 357),
(301, '250.00', '2017-06-15', 'Paiement mensuel du 31/05/2016', '2016-05-31', 0, 357),
(302, '250.00', '2017-06-15', 'Paiement mensuel du 30/06/2016', '2016-06-30', 0, 357),
(303, '250.00', '2017-06-15', 'Paiement mensuel du 31/07/2016', '2016-07-31', 0, 357),
(304, '250.00', '2017-06-15', 'Paiement mensuel du 31/08/2016', '2016-08-31', 0, 357),
(305, '250.00', '2017-06-15', 'Paiement mensuel du 30/09/2016', '2016-09-30', 0, 357),
(306, '250.00', '2017-06-15', 'Paiement mensuel du 31/10/2016', '2016-10-31', 0, 357),
(307, '250.00', '2017-06-15', 'Paiement mensuel du 30/11/2016', '2016-11-30', 0, 357),
(308, '250.00', '2017-06-15', 'Paiement mensuel du 31/12/2016', '2016-12-31', 0, 357),
(309, '250.00', '2017-06-15', 'Paiement mensuel du 31/01/2017', '2017-01-31', 0, 357),
(310, '250.00', '2017-06-15', 'Paiement mensuel du 28/02/2017', '2017-02-28', 0, 357),
(311, '250.25', '2017-06-15', 'Paiement mensuel du 31/03/2017', '2017-03-31', 0, 938),
(312, '4500.00', '2017-06-15', 'Paiement du Forfait  le 15/06/2017', '2017-06-15', 0, 265),
(313, '250.00', '2017-06-15', 'Paiement trimestriel du 30/06/2015', '2015-06-30', 0, 10),
(314, '750.00', '2017-06-15', 'Paiement trimestriel du 30/09/2015', '2015-09-30', 0, 10),
(315, '750.00', '2017-06-15', 'Paiement trimestriel du 31/12/2015', '2015-12-31', 0, 10),
(316, '750.00', '2017-06-15', 'Paiement trimestriel du 31/03/2016', '2016-03-31', 0, 10),
(317, '752.25', '2017-06-15', 'Paiement trimestriel du 30/06/2016', '2016-06-30', 0, 954),
(318, '752.25', '2017-06-15', 'Paiement trimestriel du 30/09/2016', '2016-09-30', 0, 954),
(319, '752.25', '2017-06-15', 'Paiement trimestriel du 31/12/2016', '2016-12-31', 0, 954),
(320, '752.25', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 954),
(321, '250.00', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 359),
(322, '191.67', '2017-06-15', 'Paiement mensuel du 30/04/2017', '2017-04-30', 0, 289),
(323, '191.67', '2017-06-15', 'Paiement mensuel du 31/05/2017', '2017-05-31', 0, 289),
(324, '250.75', '2017-06-15', 'Paiement mensuel du 30/04/2016', '2016-04-30', 0, 967),
(325, '250.75', '2017-06-15', 'Paiement mensuel du 31/05/2016', '2016-05-31', 0, 967),
(326, '250.75', '2017-06-15', 'Paiement mensuel du 30/06/2016', '2016-06-30', 0, 967),
(327, '250.75', '2017-06-15', 'Paiement mensuel du 31/07/2016', '2016-07-31', 0, 967),
(328, '250.75', '2017-06-15', 'Paiement mensuel du 31/08/2016', '2016-08-31', 0, 967),
(329, '250.75', '2017-06-15', 'Paiement mensuel du 30/09/2016', '2016-09-30', 0, 967),
(330, '250.75', '2017-06-15', 'Paiement mensuel du 31/10/2016', '2016-10-31', 0, 967),
(331, '250.75', '2017-06-15', 'Paiement mensuel du 30/11/2016', '2016-11-30', 0, 967),
(332, '250.75', '2017-06-15', 'Paiement mensuel du 31/12/2016', '2016-12-31', 0, 967),
(333, '250.75', '2017-06-15', 'Paiement mensuel du 31/01/2017', '2017-01-31', 0, 967),
(334, '250.75', '2017-06-15', 'Paiement mensuel du 28/02/2017', '2017-02-28', 0, 967),
(335, '251.00', '2017-06-15', 'Paiement mensuel du 31/03/2017', '2017-03-31', 0, 968),
(336, '251.50', '2017-06-15', 'Paiement mensuel du 30/04/2017', '2017-04-30', 0, 969),
(337, '251.50', '2017-06-15', 'Paiement mensuel du 31/05/2017', '2017-05-31', 0, 969),
(338, '500.00', '2017-06-15', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 857),
(339, '375.00', '2017-06-20', 'Paiement trimestriel du 31/03/2017', '2017-03-31', 0, 973);

-- --------------------------------------------------------

--
-- Structure de la table `versemtype`
--

CREATE TABLE `versemtype` (
  `ID` int(11) NOT NULL,
  `libel` varchar(50) COLLATE utf8_unicode_ci NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

--
-- Contenu de la table `versemtype`
--

INSERT INTO `versemtype` (`ID`, `libel`) VALUES
(1, 'Versement trimestriel'),
(2, 'Versement mensuel'),
(3, 'Capital forfaitaire ');

-- --------------------------------------------------------

--
-- Doublure de structure pour la vue `vordreliquidatif`
-- (Voir ci-dessous la vue réelle)
--
CREATE TABLE `vordreliquidatif` (
`rentierid` int(11)
,`dossier` int(11)
,`nomfamille` varchar(100)
,`nommarital` varchar(100)
,`prenom` varchar(100)
,`datenaissance` date
,`adresse1` varchar(100)
,`adresse2` varchar(100)
,`codepostal` varchar(10)
,`ville` varchar(100)
,`banque` varchar(100)
,`guichet` char(10)
,`codebanque` varchar(10)
,`compte` varchar(15)
,`cle` varchar(2)
,`iban` varchar(25)
,`dateaccident` date
,`titulairelibel` varchar(50)
,`gradelibel` varchar(50)
,`renteid` int(11)
,`txippaycause` decimal(5,2)
,`txippaydroit` decimal(5,2)
,`montantrevalorise` decimal(12,2)
,`coeff` decimal(8,5)
,`daterevalorisation` date
,`directive` varchar(100)
,`versementid` int(11)
,`periodeversee` varchar(80)
,`montant` decimal(8,2)
,`dateversem` date
,`rentelibel` varchar(100)
,`etatrente` date
,`datedeb` date
,`datefin` date
,`etatedite` tinyint(1)
);

-- --------------------------------------------------------

--
-- Structure de la vue `vcreditannuel`
--
DROP TABLE IF EXISTS `vcreditannuel`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vcreditannuel`  AS  select `rentier`.`dossier` AS `dossier`,`rentier`.`nomfamille` AS `nomfamille`,`rentier`.`nommarital` AS `nommarital`,`rentier`.`prenom` AS `prenom`,`rentier`.`datenaissance` AS `datenaissance`,`rente`.`dateaccident` AS `dateaccident`,`rente`.`txippaycause` AS `txippaycause`,`rente`.`txippaydroit` AS `txippaydroit`,`rente`.`libel` AS `rentelibel`,`rente`.`etatrente` AS `etatrente`,`renterevalorisee`.`datedeb` AS `datedeb`,`renterevalorisee`.`datefin` AS `datefin`,`renterevalorisee`.`montantrevalorise` AS `montantrevalorise`,`grade`.`libel` AS `gradelibel` from (((`rentier` join `rente`) join `renterevalorisee`) join `grade`) where ((`rentier`.`ID` = `rente`.`ID_RENTIER`) and (`renterevalorisee`.`datefin` = '3000-12-31') and (`rente`.`ID` = `renterevalorisee`.`ID_RENTE`) and (`rentier`.`ID_GRADE` = `grade`.`ID`) and isnull(`rente`.`etatrente`)) order by `grade`.`libel`,`rentier`.`nomfamille`,`rentier`.`prenom` ;

-- --------------------------------------------------------

--
-- Structure de la vue `vordreliquidatif`
--
DROP TABLE IF EXISTS `vordreliquidatif`;

CREATE ALGORITHM=UNDEFINED DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vordreliquidatif`  AS  select `rentier`.`ID` AS `rentierid`,`rentier`.`dossier` AS `dossier`,`rentier`.`nomfamille` AS `nomfamille`,`rentier`.`nommarital` AS `nommarital`,`rentier`.`prenom` AS `prenom`,`rentier`.`datenaissance` AS `datenaissance`,`rentier`.`adresse1` AS `adresse1`,`rentier`.`adresse2` AS `adresse2`,`rentier`.`codepostal` AS `codepostal`,`rentier`.`ville` AS `ville`,`rentier`.`banque` AS `banque`,`rentier`.`guichet` AS `guichet`,`rentier`.`codebanque` AS `codebanque`,`rentier`.`compte` AS `compte`,`rentier`.`cle` AS `cle`,`rentier`.`iban` AS `iban`,`rente`.`dateaccident` AS `dateaccident`,`titulaire`.`libel` AS `titulairelibel`,`grade`.`libel` AS `gradelibel`,`rente`.`ID` AS `renteid`,`rente`.`txippaycause` AS `txippaycause`,`rente`.`txippaydroit` AS `txippaydroit`,`renterevalorisee`.`montantrevalorise` AS `montantrevalorise`,(`revalorisation`.`coeff` - 1) AS `coeff`,`revalorisation`.`daterevalorisation` AS `daterevalorisation`,`revalorisation`.`directive` AS `directive`,`versement`.`ID` AS `versementid`,`versement`.`periodeversee` AS `periodeversee`,`versement`.`montant` AS `montant`,`versement`.`dateversem` AS `dateversem`,`rente`.`libel` AS `rentelibel`,`rente`.`etatrente` AS `etatrente`,`renterevalorisee`.`datedeb` AS `datedeb`,`renterevalorisee`.`datefin` AS `datefin`,`versement`.`etatedite` AS `etatedite` from (((((((`rentier` join `rente`) join `renterevalorisee`) join `versement`) join `versemtype`) join `revalorisation`) join `grade`) join `titulaire`) where ((`rentier`.`ID` = `rente`.`ID_RENTIER`) and (`rentier`.`ID_TITULAIRE` = `titulaire`.`ID`) and (`rentier`.`ID_GRADE` = `grade`.`ID`) and (`rente`.`ID_VERSEMTYPE` = `versemtype`.`ID`) and (`rente`.`ID` = `renterevalorisee`.`ID_RENTE`) and (`renterevalorisee`.`ID_REVALORISATION` = `revalorisation`.`ID`) and (`versement`.`ID_RENTEREVALORISEE` = `renterevalorisee`.`ID`) and (`versement`.`etatedite` = FALSE)) order by `grade`.`libel`,`rentier`.`nomfamille`,`rentier`.`prenom` ;

--
-- Index pour les tables exportées
--

--
-- Index pour la table `etablissement`
--
ALTER TABLE `etablissement`
  ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `grade`
--
ALTER TABLE `grade`
  ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `preuve`
--
ALTER TABLE `preuve`
  ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `preuvevie`
--
ALTER TABLE `preuvevie`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKklyh5ao1eld9h8dmtlvbh32f1` (`ID_PREUVE`),
  ADD KEY `FKocfpdg9pgthnp40gculsw4tl1` (`ID_RENTIER`);

--
-- Index pour la table `profil`
--
ALTER TABLE `profil`
  ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `rente`
--
ALTER TABLE `rente`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKckwu1q5vrb8r5hmgu408mbx71` (`ID_RENTIER`),
  ADD KEY `FKmxfsmbrgyh19dtdi2nuyyh0np` (`ID_VERSEMTYPE`);

--
-- Index pour la table `renterevalorisee`
--
ALTER TABLE `renterevalorisee`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKd5jb0ntos5p5nt686ncdv4r75` (`ID_RENTE`),
  ADD KEY `FK5v3whes3421ps7fm5wqjeijah` (`ID_REVALORISATION`);

--
-- Index pour la table `rentier`
--
ALTER TABLE `rentier`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKnllk4iq91d2kie9rrlumucfqc` (`ID_GRADE`),
  ADD KEY `FKpp1dqslsx4uth1nwae351ncj5` (`ID_TITULAIRE`),
  ADD KEY `FKrrmd160v2creljo2qbpk60rjk` (`ID_USERS`);

--
-- Index pour la table `revalorisation`
--
ALTER TABLE `revalorisation`
  ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `titulaire`
--
ALTER TABLE `titulaire`
  ADD PRIMARY KEY (`ID`);

--
-- Index pour la table `userprofil`
--
ALTER TABLE `userprofil`
  ADD PRIMARY KEY (`ID_PROFIL`,`ID_USERS`),
  ADD KEY `FKex203ehwsvliqahij13fcx7t9` (`ID_USERS`);

--
-- Index pour la table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKq19013qjmyqt9qmtqg36lncv1` (`ID_ETABLISSEMENT`);

--
-- Index pour la table `versement`
--
ALTER TABLE `versement`
  ADD PRIMARY KEY (`ID`),
  ADD KEY `FKeu2quygag6eygy9wcal2yu6r8` (`ID_RENTEREVALORISEE`);

--
-- Index pour la table `versemtype`
--
ALTER TABLE `versemtype`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT pour les tables exportées
--

--
-- AUTO_INCREMENT pour la table `etablissement`
--
ALTER TABLE `etablissement`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;
--
-- AUTO_INCREMENT pour la table `grade`
--
ALTER TABLE `grade`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=5;
--
-- AUTO_INCREMENT pour la table `preuve`
--
ALTER TABLE `preuve`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT pour la table `preuvevie`
--
ALTER TABLE `preuvevie`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT pour la table `profil`
--
ALTER TABLE `profil`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;
--
-- AUTO_INCREMENT pour la table `rente`
--
ALTER TABLE `rente`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
--
-- AUTO_INCREMENT pour la table `renterevalorisee`
--
ALTER TABLE `renterevalorisee`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=977;
--
-- AUTO_INCREMENT pour la table `rentier`
--
ALTER TABLE `rentier`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;
--
-- AUTO_INCREMENT pour la table `revalorisation`
--
ALTER TABLE `revalorisation`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT pour la table `titulaire`
--
ALTER TABLE `titulaire`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- AUTO_INCREMENT pour la table `users`
--
ALTER TABLE `users`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT pour la table `versement`
--
ALTER TABLE `versement`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=340;
--
-- AUTO_INCREMENT pour la table `versemtype`
--
ALTER TABLE `versemtype`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
--
-- Contraintes pour les tables exportées
--

--
-- Contraintes pour la table `preuvevie`
--
ALTER TABLE `preuvevie`
  ADD CONSTRAINT `FK_PREUVEVIE_ID_PREUVE` FOREIGN KEY (`ID_PREUVE`) REFERENCES `preuve` (`ID`),
  ADD CONSTRAINT `FK_PREUVEVIE_ID_RENTIER` FOREIGN KEY (`ID_RENTIER`) REFERENCES `rentier` (`ID`),
  ADD CONSTRAINT `FKklyh5ao1eld9h8dmtlvbh32f1` FOREIGN KEY (`ID_PREUVE`) REFERENCES `preuve` (`ID`),
  ADD CONSTRAINT `FKocfpdg9pgthnp40gculsw4tl1` FOREIGN KEY (`ID_RENTIER`) REFERENCES `rentier` (`ID`);

--
-- Contraintes pour la table `rente`
--
ALTER TABLE `rente`
  ADD CONSTRAINT `FK_RENTE_ID_RENTIER` FOREIGN KEY (`ID_RENTIER`) REFERENCES `rentier` (`ID`),
  ADD CONSTRAINT `FK_RENTE_ID_VERSEMTYPE` FOREIGN KEY (`ID_VERSEMTYPE`) REFERENCES `versemtype` (`ID`),
  ADD CONSTRAINT `FKckwu1q5vrb8r5hmgu408mbx71` FOREIGN KEY (`ID_RENTIER`) REFERENCES `rentier` (`ID`),
  ADD CONSTRAINT `FKmxfsmbrgyh19dtdi2nuyyh0np` FOREIGN KEY (`ID_VERSEMTYPE`) REFERENCES `versemtype` (`ID`);

--
-- Contraintes pour la table `renterevalorisee`
--
ALTER TABLE `renterevalorisee`
  ADD CONSTRAINT `FK5v3whes3421ps7fm5wqjeijah` FOREIGN KEY (`ID_REVALORISATION`) REFERENCES `revalorisation` (`ID`),
  ADD CONSTRAINT `FK_RENTEREVALORISEE_ID_RENTE` FOREIGN KEY (`ID_RENTE`) REFERENCES `rente` (`ID`),
  ADD CONSTRAINT `FK_RENTEREVALORISEE_ID_REVALORISATION` FOREIGN KEY (`ID_REVALORISATION`) REFERENCES `revalorisation` (`ID`),
  ADD CONSTRAINT `FKd5jb0ntos5p5nt686ncdv4r75` FOREIGN KEY (`ID_RENTE`) REFERENCES `rente` (`ID`);

--
-- Contraintes pour la table `rentier`
--
ALTER TABLE `rentier`
  ADD CONSTRAINT `FK_RENTIER_ID_GRADE` FOREIGN KEY (`ID_GRADE`) REFERENCES `grade` (`ID`),
  ADD CONSTRAINT `FK_RENTIER_ID_TITULAIRE` FOREIGN KEY (`ID_TITULAIRE`) REFERENCES `titulaire` (`ID`),
  ADD CONSTRAINT `FK_RENTIER_ID_USERS` FOREIGN KEY (`ID_USERS`) REFERENCES `users` (`ID`),
  ADD CONSTRAINT `FKnllk4iq91d2kie9rrlumucfqc` FOREIGN KEY (`ID_GRADE`) REFERENCES `grade` (`ID`),
  ADD CONSTRAINT `FKpp1dqslsx4uth1nwae351ncj5` FOREIGN KEY (`ID_TITULAIRE`) REFERENCES `titulaire` (`ID`),
  ADD CONSTRAINT `FKrrmd160v2creljo2qbpk60rjk` FOREIGN KEY (`ID_USERS`) REFERENCES `users` (`ID`);

--
-- Contraintes pour la table `userprofil`
--
ALTER TABLE `userprofil`
  ADD CONSTRAINT `FK8s0l9bvo64biawpcbvcnsxd1h` FOREIGN KEY (`ID_PROFIL`) REFERENCES `profil` (`ID`),
  ADD CONSTRAINT `FK_USERPROFIL_ID_PROFIL` FOREIGN KEY (`ID_PROFIL`) REFERENCES `profil` (`ID`),
  ADD CONSTRAINT `FK_USERPROFIL_ID_USERS` FOREIGN KEY (`ID_USERS`) REFERENCES `users` (`ID`),
  ADD CONSTRAINT `FKex203ehwsvliqahij13fcx7t9` FOREIGN KEY (`ID_USERS`) REFERENCES `users` (`ID`);

--
-- Contraintes pour la table `users`
--
ALTER TABLE `users`
  ADD CONSTRAINT `FK_USERS_ID_ETABLISSEMENT` FOREIGN KEY (`ID_ETABLISSEMENT`) REFERENCES `etablissement` (`ID`),
  ADD CONSTRAINT `FKq19013qjmyqt9qmtqg36lncv1` FOREIGN KEY (`ID_ETABLISSEMENT`) REFERENCES `etablissement` (`ID`);

--
-- Contraintes pour la table `versement`
--
ALTER TABLE `versement`
  ADD CONSTRAINT `FK_VERSEMENT_ID_RENTEREVALORISEE` FOREIGN KEY (`ID_RENTEREVALORISEE`) REFERENCES `renterevalorisee` (`ID`),
  ADD CONSTRAINT `FKeu2quygag6eygy9wcal2yu6r8` FOREIGN KEY (`ID_RENTEREVALORISEE`) REFERENCES `renterevalorisee` (`ID`);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
