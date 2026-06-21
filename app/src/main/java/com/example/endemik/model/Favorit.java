package com.example.endemik.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "favorit")
public class Favorit {
    //region Fields
    @PrimaryKey
    @NonNull
    private String id;
    
    private String tipe;
    private String nama;
    private String namaLatin;
    private String famili;
    private String genus;
    private String deskripsi;
    private String asal;
    private String sebaran;
    private String foto;
    private String sumberFoto;
    private String vidio;
    private String sumberVidio;
    private String status;
    //endregion

    //region Constructors
    public Favorit() {}

    public Favorit(Endemik endemik) {
        this.id = endemik.getId();
        this.tipe = endemik.getTipe();
        this.nama = endemik.getNama();
        this.namaLatin = endemik.getNamaLatin();
        this.famili = endemik.getFamili();
        this.genus = endemik.getGenus();
        this.deskripsi = endemik.getDeskripsi();
        this.asal = endemik.getAsal();
        this.sebaran = endemik.getSebaran();
        this.foto = endemik.getFoto();
        this.sumberFoto = endemik.getSumberFoto();
        this.vidio = endemik.getVidio();
        this.sumberVidio = endemik.getSumberVidio();
        this.status = endemik.getStatus();
    }
    //endregion

    //region Converters
    public Endemik toEndemik() {
        Endemik endemik = new Endemik();
        endemik.setId(this.id);
        endemik.setTipe(this.tipe);
        endemik.setNama(this.nama);
        endemik.setNamaLatin(this.namaLatin);
        endemik.setFamili(this.famili);
        endemik.setGenus(this.genus);
        endemik.setDeskripsi(this.deskripsi);
        endemik.setAsal(this.asal);
        endemik.setSebaran(this.sebaran);
        endemik.setFoto(this.foto);
        endemik.setSumberFoto(this.sumberFoto);
        endemik.setVidio(this.vidio);
        endemik.setSumberVidio(this.sumberVidio);
        endemik.setStatus(this.status);
        return endemik;
    }
    //endregion

    //region Getters & Setters
    @NonNull
    public String getId() { return id; }
    public void setId(@NonNull String id) { this.id = id; }

    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getNamaLatin() { return namaLatin; }
    public void setNamaLatin(String namaLatin) { this.namaLatin = namaLatin; }

    public String getFamili() { return famili; }
    public void setFamili(String famili) { this.famili = famili; }

    public String getGenus() { return genus; }
    public void setGenus(String genus) { this.genus = genus; }

    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }

    public String getAsal() { return asal; }
    public void setAsal(String asal) { this.asal = asal; }

    public String getSebaran() { return sebaran; }
    public void setSebaran(String sebaran) { this.sebaran = sebaran; }

    public String getFoto() { return foto; }
    public void setFoto(String foto) { this.foto = foto; }

    public String getSumberFoto() { return sumberFoto; }
    public void setSumberFoto(String sumberFoto) { this.sumberFoto = sumberFoto; }

    public String getVidio() { return vidio; }
    public void setVidio(String vidio) { this.vidio = vidio; }

    public String getSumberVidio() { return sumberVidio; }
    public void setSumberVidio(String sumberVidio) { this.sumberVidio = sumberVidio; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    //endregion
}
