-- Script SQL untuk membuat tabel berdasarkan database.txt

-- Tabel semester_akademik
CREATE TABLE semester_akademik (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    tahun INT NOT NULL,
    semester ENUM('1', '2', '3', '4', '5', '6', '7', '8') NOT NULL,
    tanggal_mulai DATE,
    tanggal_selesai DATE,
    is_aktif BOOLEAN DEFAULT FALSE
);

-- Tabel kelas
CREATE TABLE kelas (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    mata_kuliah_id INT NOT NULL,
    semester_akademik_id INT NOT NULL,
    kurikulum_id INT NOT NULL,
    kode_kelas VARCHAR(20) NOT NULL,
    kapasitas INT,
    status ENUM('aktif', 'nonaktif') DEFAULT 'aktif'
);

-- Tabel dosen_kelas
CREATE TABLE dosen_kelas (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    kelas_id INT NOT NULL,
    dosen_id INT NOT NULL,
    peran ENUM('pengampu', 'asisten') DEFAULT 'pengampu'
);

-- Tabel mahasiswa
CREATE TABLE mahasiswa (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    nim VARCHAR(14) NOT NULL UNIQUE,
    nama VARCHAR(50) NOT NULL,
    program_studi_id INT(11) NOT NULL,
    kurikulum_id INT(11) NOT NULL,
    angkatan INT(11) NOT NULL,
    status ENUM('aktif', 'cuti', 'lulus', 'drop_out') DEFAULT 'aktif'
);

-- Tabel krs
CREATE TABLE krs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    mahasiswa_id INT NOT NULL,
    kelas_id INT NOT NULL,
    semester_akademik_id INT NOT NULL,
    status_krs ENUM('pending', 'approved', 'rejected') DEFAULT 'pending',
    nilai_huruf VARCHAR(2),
    nilai_angka FLOAT
);

-- Tabel jadwal_kelas
CREATE TABLE jadwal_kelas (
    id INT PRIMARY KEY AUTO_INCREMENT,
    kelas_id INT NOT NULL,
    ruangan_id INT NOT NULL,
    hari ENUM('senin', 'selasa', 'rabu', 'kamis', 'jumat', 'sabtu', 'minggu') NOT NULL,
    jam_mulai TIME NOT NULL,
    jam_selesai TIME NOT NULL,
    tipe_sesi ENUM('kuliah', 'praktikum', 'ujian') DEFAULT 'kuliah'
);

-- Tabel ruangan
CREATE TABLE ruangan (
    id INT PRIMARY KEY AUTO_INCREMENT,
    kode VARCHAR(12) NOT NULL UNIQUE,
    nama VARCHAR(50) NOT NULL,
    tipe ENUM('kelas', 'lab', 'auditorium') DEFAULT 'kelas',
    kapasitas INT
);
 
-- Tabel jurnal
CREATE TABLE jurnal (
    id INT(11) PRIMARY KEY AUTO_INCREMENT,
    judul VARCHAR(255) NOT NULL,
    slug VARCHAR(255) NOT NULL UNIQUE,
    gambar VARCHAR(255),
    link VARCHAR(255)
);