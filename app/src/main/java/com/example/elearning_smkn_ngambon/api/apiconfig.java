package com.example.elearning_smkn_ngambon.api;

public class apiconfig {

    public static final String URL = "https://17ba-103-47-132-20.ngrok-free.app/";
    public static final String BASE_URL = URL+"api/";

    public static final String LOGIN_ENDPOINT = BASE_URL + "login";
    public static final String MATA_PELAJARAN = BASE_URL + "tampilMataPelajaran";
    public static final String TAMPIL_TUGAS = BASE_URL + "tampilTugas";
    public static final String TAMPIL_MATERI = BASE_URL + "tampilMateri";
    public static final String UBAH_PASSWORD_ENDPOINT = BASE_URL + "updatePassword";
    public static final String DOWNLOAD_MATERI = BASE_URL + "downloadMateri";
    public static final String UPLOAD_TUGAS = BASE_URL + "uploadTugas";


    public static final String API_KEY = "AIzaSyAMMmErV9OgcKjtHynbh-3nqurPIbRu6jU";

}
