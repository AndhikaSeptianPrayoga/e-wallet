-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 14, 2024 at 11:00 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `wallet_app`
--

-- --------------------------------------------------------

--
-- Table structure for table `transactions`
--

CREATE TABLE `transactions` (
  `id` int(11) NOT NULL,
  `transaction_amount` decimal(10,2) DEFAULT NULL,
  `transaction_date` datetime DEFAULT NULL,
  `transaction_type` varchar(45) DEFAULT NULL,
  `user_id` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `transactions`
--

INSERT INTO `transactions` (`id`, `transaction_amount`, `transaction_date`, `transaction_type`, `user_id`) VALUES
(52, 1000000.00, '2024-05-29 10:15:31', 'Deposit Dana', 16),
(53, -50000.00, '2024-05-29 10:15:41', 'Penarikan Dana', 16),
(54, -150000.00, '2024-05-29 10:16:09', 'Transfer', 16),
(55, 150000.00, '2024-05-29 10:16:09', 'Transfer', 17),
(56, -50000.00, '2024-06-04 20:12:37', 'Penarikan Dana', 20),
(57, -50000.00, '2024-06-04 20:16:02', 'Transfer', 16),
(58, 50000.00, '2024-06-04 20:16:02', 'Transfer', 16),
(59, 10000.00, '2024-06-12 23:35:59', 'Deposit Dana', 22),
(60, -5000.00, '2024-06-12 23:36:04', 'Penarikan Dana', 22),
(61, -5000.00, '2024-06-12 23:36:18', 'Transfer', 22),
(62, 5000.00, '2024-06-12 23:36:18', 'Transfer', 16),
(63, 1000000.00, '2024-06-13 12:55:16', 'Deposit Dana', 23),
(64, -500000.00, '2024-06-13 12:55:33', 'Transfer', 23),
(65, 500000.00, '2024-06-13 12:55:33', 'Transfer', 23),
(66, -25000.00, '2024-06-13 13:22:01', 'Transfer', 23),
(67, 25000.00, '2024-06-13 13:22:01', 'Transfer', 23),
(68, -2500.00, '2024-06-13 13:22:54', 'Penarikan Dana', 23),
(69, -20000.00, '2024-06-13 23:38:58', 'Tarik Dana', 16),
(70, -100000.00, '2024-06-13 23:39:52', 'Tarik Dana', 16),
(71, -25000.00, '2024-06-13 23:40:40', 'Tarik Dana', 25),
(72, -20000.00, '2024-06-13 23:41:12', 'Transfer', 25),
(73, 20000.00, '2024-06-13 23:41:12', 'Transfer', 23),
(74, 50000.00, '2024-06-14 12:29:45', 'Deposit Dana', 26),
(75, -10000.00, '2024-06-14 12:30:19', 'Transfer', 26),
(76, 10000.00, '2024-06-14 12:30:19', 'Transfer', 17),
(77, -10000.00, '2024-06-14 12:31:00', 'Tarik Dana', 26),
(78, 50000.00, '2024-06-14 13:31:59', 'Deposit Dana', 27),
(79, -10000.00, '2024-06-14 13:32:32', 'Tarik Dana', 27),
(80, -25000.00, '2024-06-14 13:33:40', 'Transfer', 27),
(81, 25000.00, '2024-06-14 13:33:40', 'Transfer', 28),
(82, -23123.00, '2024-06-14 13:49:18', 'Transfer', 31),
(83, 23123.00, '2024-06-14 13:49:18', 'Transfer', 26),
(84, 50000.00, '2024-06-14 15:53:14', 'Deposit Dana', 36),
(85, -10000.00, '2024-06-14 15:53:56', 'Transfer', 36),
(86, 10000.00, '2024-06-14 15:53:56', 'Transfer', 34);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `current_balance` decimal(10,2) NOT NULL,
  `role` int(1) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `phone`, `password`, `current_balance`, `role`) VALUES
(19, 'Rezky123', '', '', 500000.00, 0),
(23, 'Andhika123', '', '123123', 120000.00, 0),
(26, 'ROFIUL', '', 'ROFI123', 53123.00, 0),
(27, 'test123', '', '123123', 15000.00, 0),
(28, 'test456', '', '123123', 25000.00, 0),
(29, 'dashboardtest123', '', '123123', 100000.00, 0),
(30, '123213', '', '123123', 123123.00, 0),
(31, 'andhika321', '', '123123', 100000.00, 0),
(33, 'admin', '', 'admin', 0.00, 1),
(34, 'test-sub', '08123123123', '123123', 133123.00, 0),
(36, 'revisi', '089123123123', 'revisi', 40000.00, 0);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `transactions`
--
ALTER TABLE `transactions`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `transactions`
--
ALTER TABLE `transactions`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=87;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
