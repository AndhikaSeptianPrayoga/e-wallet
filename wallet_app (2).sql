-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 13, 2024 at 07:24 AM
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
(62, 5000.00, '2024-06-12 23:36:18', 'Transfer', 16);

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(45) DEFAULT NULL,
  `password` varchar(45) DEFAULT NULL,
  `current_balance` decimal(10,2) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `password`, `current_balance`) VALUES
(16, 'Andhika', '123123', 755000.00),
(17, 'Ummam234', '123123', 150000.00),
(18, 'Rofi123', '123123', 150000.00),
(19, 'Rezky123', '123123', 100000.00),
(22, 'asp123', '123123', 0.00);

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
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=63;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=23;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
