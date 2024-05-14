package com.example.elearning_smkn_ngambon.api;

public class apiconfig {

    public static final String URL = "https://7eda-103-47-132-36.ngrok-free.app/";
    public static final String BASE_URL = URL+"api/";

    public static final String LOGIN_ENDPOINT = BASE_URL + "login";
    public static final String MATA_PELAJARAN = BASE_URL + "tampilMataPelajaran";
    public static final String TAMPIL_TUGAS = BASE_URL + "tampilTugas";
    public static final String TAMPIL_MATERI = BASE_URL + "tampilMateri";
    public static final String UBAH_PASSWORD_ENDPOINT = BASE_URL + "updatePassword";
    public static final String DOWNLOAD_MATERI = BASE_URL + "downloadMateri";
    public static final String UPLOAD_TUGAS = BASE_URL + "uploadTugas";
    public static final String TAMPIL_RIWAYAT_PENGUMPULAN_TUGAS = BASE_URL + "tampilRiwayatPengumpulanTugas";



    public static final String LOGIN_GURU_ENDPOINT = BASE_URL + "loginGuru";
    public static final String MATA_PELAJARAN_GURU_ENDPOINT = BASE_URL + "tampilMataPelajaranGuru";
    public static final String MATERI_GURU_ENDPOINT = BASE_URL + "tampilMateriGuru";
    public static final String TAMPIL_TUGAS_DETAIL_MATERI_GURU_ENDPOINT = BASE_URL + "tampilTugasDetailMateriGuru";
    public static final String EDIT_TUGAS_GURU_ENDPOINT = BASE_URL + "editTugasGuru";
    public static final String TAMBAH_TUGAS_GURU_ENDPOINT = BASE_URL + "tambahTugasGuru";
    public static final String TAMPIL_PENGUMPULAN_TUGAS_GURU_ENDPOINT = BASE_URL + "tampilPengumpulanTugasGuru";
    public static final String EDIT_NILAI_TUGAS_GURU_ENDPOINT = BASE_URL + "editNilaiTugasGuru";
    public static final String DOWNLOAD_PENGUMPULAN_TUGAS_GURU_ENDPOINT = BASE_URL + "downloadTugas";
    public static final String API_KEY = "AIzaSyCsS8jv5Mx_cV1EzwMlz21xY5JlqC344Bo";

}
