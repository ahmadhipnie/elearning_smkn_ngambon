package com.example.elearning_smkn_ngambon.model.guruModel;

public class TugasGuruModel {
    private final int idTugas;
    private final String judulMateri;
    private final String tenggatWaktu;
    private final String keterangan;

    public TugasGuruModel(int idTugas, String judulMateri, String tenggatWaktu, String keterangan) {
        this.idTugas = idTugas;
        this.judulMateri = judulMateri;
        this.tenggatWaktu = tenggatWaktu;
        this.keterangan = keterangan;
    }

    public int getIdTugas() {
        return idTugas;
    }

    public String getJudulMateri() {return judulMateri;}
    public String getTenggatWaktu() {return tenggatWaktu;}

    public String getKeterangan() {
        return keterangan;
    }
}