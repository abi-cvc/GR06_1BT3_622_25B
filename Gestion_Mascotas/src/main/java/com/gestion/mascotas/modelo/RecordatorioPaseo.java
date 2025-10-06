package com.gestion.mascotas.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "recordatorios_paseo")
@PrimaryKeyJoinColumn(name = "id") // Une con la tabla Recordatorio
public class RecordatorioPaseo extends Recordatorio {

    @Column(name = "dias_semana")
    private String diasSemana; // Ej: "Lunes,Miércoles,Viernes"

    @Column(nullable = false)
    private String horarios; // Ej: "07:00,18:00"

    @Column(name = "duracion_minutos")
    private Integer duracionMinutos; // Duración estimada del paseo en minutos

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

    public Integer getDuracionMinutos() {
        return duracionMinutos;
    }

    public void setDuracionMinutos(Integer duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }
/*
    // Sobreescribe el método de configuración para incluir detalles del paseo
    public void configurarRecordatorio(Mascota mascota, String descripcion, LocalDateTime fechaHora,
                                       String diasSemana, String horarios, Integer duracionMinutos) {
        super.configurarRecordatorio(mascota, descripcion, fechaHora);
        this.setDiasSemana(diasSemana);
        this.setHorarios(horarios);
        this.setDuracionMinutos(duracionMinutos);
        System.out.println("Recordatorio de Paseo configurado para " + mascota.getNombre() +
                ": " + descripcion + " (" + diasSemana + " - " + horarios + " - " + duracionMinutos + " min)");
    }

    // Sobreescribe el método de modificación
    @Override
    public void modificarRecordatorio(String nuevaDescripcion, LocalDateTime nuevaFechaHora, boolean activo) {
        super.modificarRecordatorio(nuevaDescripcion, nuevaFechaHora, activo);
        System.out.println("Detalles de paseo modificados para el recordatorio.");
    }
*/
    public void modificarDetallesPaseo(String diasSemana, String horarios, Integer duracionMinutos) {
        this.setDiasSemana(diasSemana);
        this.setHorarios(horarios);
        this.setDuracionMinutos(duracionMinutos);
        System.out.println("Detalles de paseo actualizados: Días=" + diasSemana + ", Horarios=" + horarios + ", Duración=" + duracionMinutos + " min");
    }

    @Override
    public String toString() {
        return "RecordatorioPaseo{" +
                super.toString() +
                ", diasSemana='" + diasSemana + '\'' +
                ", horarios='" + horarios + '\'' +
                ", duracionMinutos=" + duracionMinutos +
                '}';
    }
}