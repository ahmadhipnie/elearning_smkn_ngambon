package com.example.elearning_smkn_ngambon.model;

public class TugasModel {
    private int idTugas;
    private String namaMateri;
    private String tenggatWaktu;
    private String keterangan;

    public TugasModel(int idTugas, String namaMateri, String tenggatWaktu, String keterangan) {
        this.idTugas = idTugas;
        this.namaMateri = namaMateri;
        this.tenggatWaktu = tenggatWaktu;
        this.keterangan = keterangan;
    }

    public int getIdTugas() {
        return idTugas;
    }

    public String getNamaMateri() {return namaMateri;}
    public String getTenggatWaktu() {return tenggatWaktu;}

    public String getKeterangan() {
        return keterangan;
    }
}
