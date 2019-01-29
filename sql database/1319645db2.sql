-- phpMyAdmin SQL Dump
-- version 4.8.0.1
-- https://www.phpmyadmin.net/
--
-- Host: localhost
-- Generation Time: May 24, 2018 at 11:33 AM
-- Server version: 10.2.14-MariaDB
-- PHP Version: 7.1.17

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `1319645db2`
--

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `name` varchar(50) NOT NULL,
  `number` varchar(50) NOT NULL,
  `pass` varchar(50) NOT NULL,
  `lat` varchar(50) NOT NULL,
  `lon` varchar(50) NOT NULL,
  `token` varchar(500) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `name`, `number`, `pass`, `lat`, `lon`, `token`) VALUES
(1, 'emulator', '03341158799', '', '34.0085367', '71.5749433', 'fok5lYfAkdw:APA91bFVpFfzU3_vbEDJlcPMcFmgpPxGUrMKEfjrm-CUNTGWjzyrspQlyZP9H716T7Xdnu899KlBJ7RbUHk7Z2I4vVYXqLrwrIelweVr8Jjn-7D-vFYv7NrjCzS378MXC6fQ9HNjzP5M'),
(2, 'maa g', '03456791834', '', '34.0100039', '71.6007858', 'cTEvntwfeA4:APA91bFc490pkXAngE-CMz1ThacQajVubd0mUPDZQb54niufqUkjetDGwuQMWboRmnx6qzqT1H8csyzjZGMBq4ikYggC6-MXCOEfykLtG_cAuk0Y9V-jLw46wiA4-BE3nARUMd06naBu'),
(3, 'Bilal', '03345598311', '', '34.009608', '71.592426', 'e8Vk5pGFgXI:APA91bF2jk611L1Rg0M1EIydHu63Wwhfr3DBvmtK4OQ7O1Nyb-9NBUoFB8WaM4IiopRiRhO_s3NUpZEDKeglOxMo86LWKIS1imhDmmLgx3le8teDW9_M9K69laHsV9yfpuKTLygbN7Fi');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
