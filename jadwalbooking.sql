
Create Database `jadwalbooking` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci */;
SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


CREATE TABLE `booking` (
  `id_booking` int(11) NOT NULL,
  `jadwal_id` int(11) NOT NULL,
  `mahasiswa_id` int(11) NOT NULL,
  `topik_skripsi` text DEFAULT NULL,
  `lokasi` varchar(255) DEFAULT NULL,
  `status_booking` enum('PENDING','DISETUJUI','DITOLAK','SELESAI') DEFAULT 'PENDING',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `booking`
--

INSERT INTO `booking` (`id_booking`, `jadwal_id`, `mahasiswa_id`, `topik_skripsi`, `lokasi`, `status_booking`, `created_at`) VALUES
(1, 1, 3, 'pemilihan judul', 'gedung b', 'DISETUJUI', '2026-06-26 13:21:03'),
(2, 1, 3, 'saya', NULL, 'DITOLAK', '2026-06-26 14:44:40'),
(3, 1, 5, 'aduh', NULL, 'PENDING', '2026-06-26 14:46:25');

-- --------------------------------------------------------

--
-- Table structure for table `jadwal_konsultasi`
--

CREATE TABLE `jadwal_konsultasi` (
  `id_jadwal` int(11) NOT NULL,
  `dosen_id` int(11) NOT NULL,
  `tanggal` date NOT NULL,
  `jam_mulai` time NOT NULL,
  `jam_selesai` time NOT NULL,
  `status` enum('TERSEDIA','TERBOOKING') DEFAULT 'TERSEDIA',
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `jadwal_konsultasi`
--

INSERT INTO `jadwal_konsultasi` (`id_jadwal`, `dosen_id`, `tanggal`, `jam_mulai`, `jam_selesai`, `status`, `created_at`) VALUES
(1, 2, '2026-07-25', '09:00:00', '11:00:00', 'TERBOOKING', '2026-06-25 04:29:45');

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `password` varchar(255) NOT NULL,
  `role` enum('ADMIN','DOSEN','MAHASISWA') NOT NULL,
  `nim_nip` varchar(20) DEFAULT NULL,
  `created_at` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `users`
--

INSERT INTO `users` (`id`, `username`, `email`, `password`, `role`, `nim_nip`, `created_at`) VALUES
(1, 'admin', 'admin@kampus.ac.id', '12345', 'ADMIN', NULL, '2026-06-24 06:19:32'),
(2, 'dosen1', 'dosen1@kampus.ac.id', '12345', 'DOSEN', 'NIP123', '2026-06-24 06:19:32'),
(3, 'mhs1', 'mhs1@kampus.ac.id', '12345', 'MAHASISWA', 'NIM001', '2026-06-24 06:19:32'),
(4, 'dosen2', 'dosen2@gmail.com', '12345', 'DOSEN', '23456', '2026-06-25 04:36:11'),
(5, 'anisa', 'anisa@gmail.com', '12345', 'MAHASISWA', 'f1g124', '2026-06-25 05:19:19');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `booking`
--
ALTER TABLE `booking`
  ADD PRIMARY KEY (`id_booking`),
  ADD KEY `jadwal_id` (`jadwal_id`),
  ADD KEY `mahasiswa_id` (`mahasiswa_id`);

--
-- Indexes for table `jadwal_konsultasi`
--
ALTER TABLE `jadwal_konsultasi`
  ADD PRIMARY KEY (`id_jadwal`),
  ADD KEY `dosen_id` (`dosen_id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `username` (`username`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `booking`
--
ALTER TABLE `booking`
  MODIFY `id_booking` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=4;

--
-- AUTO_INCREMENT for table `jadwal_konsultasi`
--
ALTER TABLE `jadwal_konsultasi`
  MODIFY `id_jadwal` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;

--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=6;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `booking`
--
ALTER TABLE `booking`
  ADD CONSTRAINT `booking_ibfk_1` FOREIGN KEY (`jadwal_id`) REFERENCES `jadwal_konsultasi` (`id_jadwal`) ON DELETE CASCADE,
  ADD CONSTRAINT `booking_ibfk_2` FOREIGN KEY (`mahasiswa_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;

--
-- Constraints for table `jadwal_konsultasi`
--
ALTER TABLE `jadwal_konsultasi`
  ADD CONSTRAINT `jadwal_konsultasi_ibfk_1` FOREIGN KEY (`dosen_id`) REFERENCES `users` (`id`) ON DELETE CASCADE;
COMMIT;

