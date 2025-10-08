package com.gestion.mascotas.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "sugerencias_ejercicio")
@PrimaryKeyJoinColumn(name = "id")
public class SugerenciaEjercicio extends Sugerencia {

    @Column(name = "tipo_ejercicio", length = 100)
    private String tipoEjercicio;

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos;

    @Enumerated(EnumType.STRING)
    @Column(name = "nivel_actividad", length = 30)
    private NivelActividad nivelActividad;

    // Getters y Setters
    public String getTipoEjercicio() {
        return tipoEjercicio;
    }

    public void setTipoEjercicio(String tipoEjercicio) {
        this.tipoEjercicio = tipoEjercicio;
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public NivelActividad getNivelActividad() {
        return nivelActividad;
    }

    public void setNivelActividad(NivelActividad nivelActividad) {
        this.nivelActividad = nivelActividad;
    }

    @Override
    public String toString() {
        return "SugerenciaEjercicio{" +
                super.toString() +
                ", tipoEjercicio='" + tipoEjercicio + '\'' +
                ", duracionMinutos=" + duracionMinutos +
                ", nivelActividad=" + nivelActividad +
                '}';
    }
}