package com.gestion.mascotas.modelo;

import jakarta.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "recordatorios_alimentacion")
@PrimaryKeyJoinColumn(name = "id")
public class RecordatorioAlimentacion extends Recordatorio {

    @Column(nullable = false)
    private String frecuencia; // Ej: "1", "2", "3"

    @Column(nullable = false, length = 500)
    private String horarios; // Horas separadas por coma, ej: "08:00,13:00,18:00"

    @Column(name = "tipo_alimento", length = 100)
    private String tipoAlimento;

    @Column(name = "dias_semana", length = 200)
    private String diasSemana; // Días de la semana separados por coma, ej: "Lunes,Miércoles,Viernes"

    // Constructor vacío
    public RecordatorioAlimentacion() {
        super();
    }

    // Constructor con campos específicos de alimentación
    public RecordatorioAlimentacion(Mascota mascota, String descripcion, boolean activo,
                                    String frecuencia, String horarios, String tipoAlimento, String diasSemana) {
        super(mascota, descripcion, activo);
        this.frecuencia = frecuencia;
        this.horarios = horarios;
        this.tipoAlimento = tipoAlimento;
        this.diasSemana = diasSemana;
    }

    // Getters y Setters
    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
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

    public String getTipoAlimento() {
        return tipoAlimento;
    }

    public void setTipoAlimento(String tipoAlimento) {
        this.tipoAlimento = tipoAlimento;
    }

    public String getDiasSemana() {
        return diasSemana;
    }

    public void setDiasSemana(String diasSemana) {
        this.diasSemana = diasSemana;
    }

    public List<String> getListaDiasSemana() {
        if (this.diasSemana == null || this.diasSemana.isEmpty()) {
            return new ArrayList<>();
        }
        return Arrays.asList(this.diasSemana.split(","));
    }

    @Override
    public String toString() {
        String mascotaNombre = obtenerNombreMascotaSeguro();

        return "RecordatorioAlimentacion{" +
                "id=" + getId() +
                ", mascota=" + mascotaNombre +
                ", descripcion='" + getDescripcion() + '\'' +
                ", activo=" + isActivo() +
                ", frecuencia='" + frecuencia + '\'' +
                ", horarios='" + horarios + '\'' +
                ", tipoAlimento='" + tipoAlimento + '\'' +
                ", diasSemana='" + diasSemana + '\'' +
                '}';
    }

}