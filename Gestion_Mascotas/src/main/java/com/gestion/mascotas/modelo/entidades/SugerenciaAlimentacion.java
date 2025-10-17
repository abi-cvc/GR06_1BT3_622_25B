package com.gestion.mascotas.modelo.entidades;

import jakarta.persistence.*;

@Entity
@Table(name = "sugerencias_alimentacion")
@PrimaryKeyJoinColumn(name = "id")
public class SugerenciaAlimentacion extends Sugerencia {

    @Column(name = "tipo_comida", length = 100)
    private String tipoComida;

    @Column(length = 100)
    private String frecuencia;

    @Column(name = "calorias_recomendadas")
    private Integer caloriasRecomendadas;

    // Getters y Setters
    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public Integer getCaloriasRecomendadas() {
        return caloriasRecomendadas;
    }

    public void setCaloriasRecomendadas(Integer caloriasRecomendadas) {
        this.caloriasRecomendadas = caloriasRecomendadas;
    }

    @Override
    public String toString() {
        return "SugerenciaAlimentacion{" +
                super.toString() +
                ", tipoComida='" + tipoComida + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                ", caloriasRecomendadas=" + caloriasRecomendadas +
                '}';
    }
}