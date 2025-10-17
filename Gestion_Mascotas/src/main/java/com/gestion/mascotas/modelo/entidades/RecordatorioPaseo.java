package com.gestion.mascotas.modelo.entidades;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "recordatorios_paseo")
@PrimaryKeyJoinColumn(name = "id")
public class RecordatorioPaseo extends Recordatorio {

    @Column(name = "dias_semana", length = 200)
    private String diasSemana; // Ej: "Lunes,Miércoles,Viernes"

    @Column(nullable = false, length = 500)
    private String horarios; // Ej: "07:00,18:00"

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos; // Duración estimada del paseo en minutos

    // Constructor vacío
    public RecordatorioPaseo() {
        super();
    }

    // Constructor con campos específicos
    public RecordatorioPaseo(Mascota mascota, String descripcion, boolean activo,
                             String diasSemana, String horarios, Integer duracionMinutos) {
        super(mascota, descripcion, activo);
        this.diasSemana = diasSemana;
        this.horarios = horarios;
        this.duracionMinutos = duracionMinutos;
    }

    // Getters y Setters
    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public String getHorarios() {
        return horarios;
    }

    public void setHorarios(String horarios) {
        this.horarios = horarios;
    }

    public List<LocalTime> getListaHorarios() {
        if (this.horarios == null || this.horarios.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.stream(this.horarios.split(","))
                .map(String::trim)
                .map(LocalTime::parse)
                .collect(Collectors.toList());
    }

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public List<String> getListaDiasSemana() {
        if (this.diasSemana == null || this.diasSemana.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.diasSemana.split(","));
    }

    public void modificarDetallesPaseo(String diasSemana, String horarios, Integer duracionMinutos) {
        this.setDiasSemana(diasSemana);
        this.setHorarios(horarios);
        this.setDuracionMinutos(duracionMinutos);
        System.out.println("Detalles de paseo actualizados: Días=" + diasSemana +
                ", Horarios=" + horarios + ", Duración=" + duracionMinutos + " min");
    }

    @Override
    public String toString() {
        String mascotaNombre = obtenerNombreMascotaSeguro();

        return "RecordatorioPaseo{" +
                "id=" + getId() +
                ", mascota=" + mascotaNombre +
                ", descripcion='" + getDescripcion() + '\'' +
                ", activo=" + isActivo() +
                ", diasSemana='" + diasSemana + '\'' +
                ", horarios='" + horarios + '\'' +
                ", duracionMinutos=" + duracionMinutos +
                '}';
    }
}