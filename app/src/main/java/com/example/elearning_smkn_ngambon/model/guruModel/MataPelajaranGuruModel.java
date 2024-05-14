package com.example.elearning_smkn_ngambon.model.guruModel;

public class MataPelajaranGuruModel {

    private final int idMapel;
    private final String namaMapel;
    private final String namaKelas;

    private final int idKelas;

    public MataPelajaranGuruModel(int idMapel, String namaMapel, String namaKelas, int idKelas) {
        this.idMapel = idMapel;
        this.namaMapel = namaMapel;
        this.namaKelas = namaKelas;
        this.idKelas = idKelas;
    }

    public int getIdMapel() {
        return idMapel;
    }

    public String getNamaMapel() {
        return namaMapel;
    }

    public String getNamaKelas() {
        return namaKelas;
    }

    public int getIdKelas() {return idKelas;}
}
