package com.example.elearning_smkn_ngambon.model.guruModel;

public class MateriGuruModel {

    private final int idMateri;
    private final String judulMateri;
    private final String keteranganMateri;

    public MateriGuruModel(int idMateri, String judulMateri, String keteranganMateri) {
        this.idMateri = idMateri;
        this.judulMateri = judulMateri;
        this.keteranganMateri = keteranganMateri;
    }

    public int getIdMateri() {
        return idMateri;
    }

    public String getJudulMateri() {
        return judulMateri;
    }

    public String getKeteranganMateri() {
        return keteranganMateri;
    }
}
