package com.example.elearning_smkn_ngambon.api;

public class apiconfig {

    public static final String URL = "https://7f44-103-47-132-33.ngrok-free.app/";
    public static final String BASE_URL = URL+"api/";

    public static final String LOGIN_ENDPOINT = BASE_URL + "login";
    public static final String LOGIN_GURU_ENDPOINT = BASE_URL + "loginGuru";
    public static final String MATA_PELAJARAN = BASE_URL + "tampilMataPelajaran";
    public static final String TAMPIL_TUGAS = BASE_URL + "tampilTugas";
    public static final String TAMPIL_MATERI = BASE_URL + "tampilMateri";
    public static final String UBAH_PASSWORD_ENDPOINT = BASE_URL + "updatePassword";
    public static final String DOWNLOAD_MATERI = BASE_URL + "downloadMateri";
    public static final String UPLOAD_FILE = BASE_URL + "uploadFile";


    public static final String API_KEY = "AIzaSyCsS8jv5Mx_cV1EzwMlz21xY5JlqC344Bo";

}
