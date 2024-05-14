package com.example.elearning_smkn_ngambon.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PengumpulanTugasModel {
    private int idPengumpulan;
    private String lampiranTugas;
    private int idTugas;
    private int idSiswa;
    private int nilai;
    private String createdAt;
    private String updatedAt;

    public PengumpulanTugasModel(int idPengumpulan, String lampiranTugas, int idTugas, int idSiswa, int nilai, String createdAt, String updatedAt) {
        this.idPengumpulan = idPengumpulan;
        this.lampiranTugas = lampiranTugas;
        this.idTugas = idTugas;
        this.idSiswa = idSiswa;
        this.nilai = nilai;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
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

    @Override
    public String toString() {
        return "PengumpulanTugasModel{" +
                "idPengumpulan=" + idPengumpulan +
                ", lampiranTugas='" + lampiranTugas + '\'' +
                ", idTugas=" + idTugas +
                ", idSiswa=" + idSiswa +
                ", nilai=" + nilai +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
