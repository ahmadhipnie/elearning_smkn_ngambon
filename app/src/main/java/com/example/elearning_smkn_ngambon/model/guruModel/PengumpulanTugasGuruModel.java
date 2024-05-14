package com.example.elearning_smkn_ngambon.model.guruModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PengumpulanTugasGuruModel {

    private final int idPengumpulan;
    private final String lampiranTugas;
    private final int idTugas;
    private final int idSiswa;
    private final int nilai;
    private final String createdAt;
    private final String updatedAt;
    private final String namaSiswa;
    private final String judulMateri;

    public PengumpulanTugasGuruModel(int idPengumpulan, String lampiranTugas, int idTugas, int idSiswa, int nilai, String createdAt, String updatedAt, String namaSiswa, String judulMateri) {
        this.idPengumpulan = idPengumpulan;
        this.lampiranTugas = lampiranTugas;
        this.idTugas = idTugas;
        this.idSiswa = idSiswa;
        this.nilai = nilai;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.namaSiswa = namaSiswa;
        this.judulMateri = judulMateri;
    }

    public int getIdPengumpulan() {
        return idPengumpulan;
    }

    public String getLampiranTugas() {
        return lampiranTugas;
    }

    public int getIdTugas() {
        return idTugas;
    }

    public int getIdSiswa() {
        return idSiswa;
    }

    public int getNilai() {
        return nilai;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getNamaSiswa() {
        return namaSiswa;
    }

    public String getJudulMateri() {return judulMateri;}

    public String getFormattedCreatedAt() {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date = inputFormat.parse(createdAt);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return createdAt; // Return the original value if parsing fails
        }
    }
}
