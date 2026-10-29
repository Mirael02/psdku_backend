-- Insert sample berita data
-- First, get the user_id of an admin user (adjust as needed)
-- This assumes there's already a user in the database

-- Sample Berita data
INSERT INTO berita (judul, deskripsi, kategori, tanggal, file, slug, view) VALUES
('Pengumuman Penerimaan Mahasiswa Baru 2024', 'Polinema Lumajang dengan bangga mengumumkan pembukaan pendaftaran mahasiswa baru tahun akademik 2024/2025. Pendaftaran dapat dilakukan melalui portal PMB kami.', 'Akademik', '2024-04-15', '/uploads/berita/news-1.jpg', 'pengumuman-penerimaan-2024', 150),
('Beasiswa Penuh untuk Mahasiswa Berprestasi', 'Program beasiswa penuh tersedia untuk mahasiswa dengan prestasi akademik luar biasa. Daftar sekarang dan dapatkan kesempatan emas untuk melanjutkan studi tanpa beban biaya.', 'Akademik', '2024-04-10', '/uploads/berita/news-2.jpg', 'beasiswa-penuh-2024', 280),
('Festival Kampus 2024 Akan Digelar Bulan Mei', 'Acara tahunan Festival Kampus Polinema Lumajang akan diselenggarakan pada bulan Mei 2024. Berbagai kegiatan menarik, workshop, dan hiburan akan memeriahkan acara ini.', 'Kemahasiswaan', '2024-04-08', '/uploads/berita/news-3.jpg', 'festival-kampus-2024', 320),
('Kerjasama dengan Industri Teknologi Terkemuka', 'Polinema Lumajang menjalin kerjasama strategis dengan perusahaan teknologi terkemuka untuk program magang dan penempatan lulusan.', 'Umum', '2024-04-05', '/uploads/berita/news-4.jpg', 'kerjasama-industri-2024', 195),
('Peluncuran Laboratorium Komputer Generasi Terbaru', 'Polinema Lumajang telah meluncurkan laboratorium komputer dengan perangkat terbaru untuk mendukung pembelajaran praktik mahasiswa.', 'Akademik', '2024-04-01', '/uploads/berita/news-5.jpg', 'lab-komputer-terbaru', 250);

-- Now insert approval requests for the berita
-- Note: Adjust the id_user and user_id values to match your actual user
-- This query assumes there's a user with id_user=1 (you may need to adjust this)

INSERT INTO approval_request (id_berita, nama_tabel, action, status, payload, user_id, created_at, updated_at) VALUES
(1, 'berita', 'CREATE', 'APPROVED', 
  JSON_OBJECT('judul', 'Pengumuman Penerimaan Mahasiswa Baru 2024', 'deskripsi', 'Polinema Lumajang dengan bangga mengumumkan pembukaan pendaftaran mahasiswa baru tahun akademik 2024/2025. Pendaftaran dapat dilakukan melalui portal PMB kami.', 'kategori', 'akademik', 'tanggal', '2024-04-15', 'file', '/uploads/berita/news-1.jpg', 'slug', 'pengumuman-penerimaan-2024', 'view', 150),
  1, NOW(), NOW()),
  
(2, 'berita', 'CREATE', 'APPROVED',
  JSON_OBJECT('judul', 'Beasiswa Penuh untuk Mahasiswa Berprestasi', 'deskripsi', 'Program beasiswa penuh tersedia untuk mahasiswa dengan prestasi akademik luar biasa. Daftar sekarang dan dapatkan kesempatan emas untuk melanjutkan studi tanpa beban biaya.', 'kategori', 'akademik', 'tanggal', '2024-04-10', 'file', '/uploads/berita/news-2.jpg', 'slug', 'beasiswa-penuh-2024', 'view', 280),
  1, NOW(), NOW()),
  
(3, 'berita', 'CREATE', 'APPROVED',
  JSON_OBJECT('judul', 'Festival Kampus 2024 Akan Digelar Bulan Mei', 'deskripsi', 'Acara tahunan Festival Kampus Polinema Lumajang akan diselenggarakan pada bulan Mei 2024. Berbagai kegiatan menarik, workshop, dan hiburan akan memeriahkan acara ini.', 'kategori', 'kemahasiswaan', 'tanggal', '2024-04-08', 'file', '/uploads/berita/news-3.jpg', 'slug', 'festival-kampus-2024', 'view', 320),
  1, NOW(), NOW()),
  
(4, 'berita', 'CREATE', 'APPROVED',
  JSON_OBJECT('judul', 'Kerjasama dengan Industri Teknologi Terkemuka', 'deskripsi', 'Polinema Lumajang menjalin kerjasama strategis dengan perusahaan teknologi terkemuka untuk program magang dan penempatan lulusan.', 'kategori', 'umum', 'tanggal', '2024-04-05', 'file', '/uploads/berita/news-4.jpg', 'slug', 'kerjasama-industri-2024', 'view', 195),
  1, NOW(), NOW()),
  
(5, 'berita', 'CREATE', 'APPROVED',
  JSON_OBJECT('judul', 'Peluncuran Laboratorium Komputer Generasi Terbaru', 'deskripsi', 'Polinema Lumajang telah meluncurkan laboratorium komputer dengan perangkat terbaru untuk mendukung pembelajaran praktik mahasiswa.', 'kategori', 'akademik', 'tanggal', '2024-04-01', 'file', '/uploads/berita/news-5.jpg', 'slug', 'lab-komputer-terbaru', 'view', 250),
  1, NOW(), NOW());
