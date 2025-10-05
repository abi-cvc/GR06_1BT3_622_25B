package com.gestion.mascotas.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "recordatorios_alimentacion")
@PrimaryKeyJoinColumn(name = "id") // Une con la tabla Recordatorio
public class RecordatorioAlimentacion extends Recordatorio {

    @Column(nullable = false)
    private String frecuencia; // Ej: "Diario", "Semanal"

    @Column(name = "dias_semana")
    private String diasSemana; // Ej: "Lunes,Miércoles,Viernes"

    @Column(nullable = false)
    private String horarios; // Ej: "08:00,13:00,19:00"

    @Column(name = "tipo_alimento")
    private String tipoAlimento; // Ej: "Pienso seco", "Comida húmeda"

    // Getters y Setters
    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

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

    public String getTipoAlimento() {
        return tipoAlimento;
    }

    public void setTipoAlimento(String tipoAlimento) {
        this.tipoAlimento = tipoAlimento;
    }

    // Sobreescribe el método de configuración para incluir detalles de alimentación
    public void configurarRecordatorio(Mascota mascota, String descripcion, LocalDateTime fechaHora,
                                       String frecuencia, String diasSemana, String horarios, String tipoAlimento) {
        super.configurarRecordatorio(mascota, descripcion, fechaHora);
        this.setFrecuencia(frecuencia);
        this.setDiasSemana(diasSemana);
        this.setHorarios(horarios);
        this.setTipoAlimento(tipoAlimento);
        System.out.println("Recordatorio de Alimentación configurado para " + mascota.getNombre() +
                ": " + descripcion + " (" + frecuencia + " - " + horarios + " - " + tipoAlimento + ")");
    }

    // Sobreescribe el método de modificación
    @Override
    public void modificarRecordatorio(String nuevaDescripcion, LocalDateTime nuevaFechaHora, boolean activo) {
        super.modificarRecordatorio(nuevaDescripcion, nuevaFechaHora, activo);
        System.out.println("Detalles de alimentación modificados para el recordatorio.");
    }

    public void modificarDetallesAlimentacion(String frecuencia, String diasSemana, String horarios, String tipoAlimento) {
        this.setFrecuencia(frecuencia);
        this.setDiasSemana(diasSemana);
        this.setHorarios(horarios);
        this.setTipoAlimento(tipoAlimento);
        System.out.println("Detalles de alimentación actualizados: Frecuencia=" + frecuencia + ", Horarios=" + horarios);
    }

    @Override
    public String toString() {
        return "RecordatorioAlimentacion{" +
                super.toString() +
                ", frecuencia='" + frecuencia + '\'' +
                ", diasSemana='" + diasSemana + '\'' +
                ", horarios='" + horarios + '\'' +
                ", tipoAlimento='" + tipoAlimento + '\'' +
                '}';
    }
}