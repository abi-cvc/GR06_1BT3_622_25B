package com.gestion.mascotas.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "sugerencias")
@Inheritance(strategy = InheritanceType.JOINED)
public class Sugerencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_mascota", nullable = false, length = 20)
    private TipoMascota tipoMascota;

    @Column(length = 100)
    private String raza;

    @Column(name = "edad_min")
    private Integer edadMin;

    @Column(name = "edad_max")
    private Integer edadMax;

    @Column(name = "peso_min")
    private Double pesoMin;

    @Column(name = "peso_max")
    private Double pesoMax;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String descripcion;

    @Column(length = 255)
    private String fuente;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoMascota getTipoMascota() {
        return tipoMascota;
    }

    public void setTipoMascota(TipoMascota tipoMascota) {
        this.tipoMascota = tipoMascota;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public Integer getEdadMin() {
        return edadMin;
    }

    public void setEdadMin(Integer edadMin) {
        this.edadMin = edadMin;
    }

    public Integer getEdadMax() {
        return edadMax;
    }

    public void setEdadMax(Integer edadMax) {
        this.edadMax = edadMax;
    }

    public Double getPesoMin() {
        return pesoMin;
    }

    public void setPesoMin(Double pesoMin) {
        this.pesoMin = pesoMin;
    }

    public Double getPesoMax() {
        return pesoMax;
    }

    public void setPesoMax(Double pesoMax) {
        this.pesoMax = pesoMax;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFuente() {
        return fuente;
    }

    public void setFuente(String fuente) {
        this.fuente = fuente;
    }

    @Override
    public String toString() {
        return "Sugerencia{" +
                "id=" + id +
                ", tipoMascota=" + tipoMascota +
                ", raza='" + raza + '\'' +
                ", edadMin=" + edadMin +
                ", edadMax=" + edadMax +
                ", pesoMin=" + pesoMin +
                ", pesoMax=" + pesoMax +
                ", descripcion='" + descripcion + '\'' +
                ", fuente='" + fuente + '\'' +
                '}';
    }
}