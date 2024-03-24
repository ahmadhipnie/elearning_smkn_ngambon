package com.example.elearning_smkn_ngambon.model;

public class MataPelajaranModel {
        private int idMapel;
        private String namaMapel;
        private String namaKelas;

        public MataPelajaranModel(int idMapel, String namaMapel, String namaKelas) {
            this.idMapel = idMapel;
            this.namaMapel = namaMapel;
            this.namaKelas = namaKelas;
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
}