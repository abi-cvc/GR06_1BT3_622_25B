package com.gestion.mascotas.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recordatorios")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "fecha_hora_recordatorio", nullable = false)
    private LocalDateTime fechaHoraRecordatorio;

    @Column(nullable = false)
    private boolean activo;

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaHoraRecordatorio() {
        return fechaHoraRecordatorio;
    }

    public void setFechaHoraRecordatorio(LocalDateTime fechaHoraRecordatorio) {
        this.fechaHoraRecordatorio = fechaHoraRecordatorio;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    // Métodos genéricos de gestión de recordatorios
    public void configurarRecordatorio(Mascota mascota, String descripcion, LocalDateTime fechaHora) {
        this.setMascota(mascota);
        this.setDescripcion(descripcion);
        this.setFechaHoraRecordatorio(fechaHora);
        this.setActivo(true);
        System.out.println("Recordatorio configurado: " + descripcion + " para " + mascota.getNombre() + " el " + fechaHora);
    }

    public void modificarRecordatorio(String nuevaDescripcion, LocalDateTime nuevaFechaHora, boolean activo) {
        this.setDescripcion(nuevaDescripcion);
        this.setFechaHoraRecordatorio(nuevaFechaHora);
        this.setActivo(activo);
        System.out.println("Recordatorio modificado a: " + nuevaDescripcion + " el " + nuevaFechaHora + " (Activo: " + activo + ")");
    }

    public void eliminarRecordatorio() {
        this.setActivo(false); // Se marca como inactivo en lugar de eliminar físicamente
        System.out.println("Recordatorio eliminado (marcado como inactivo): " + this.getDescripcion());
    }

    @Override
    public String toString() {
        return "Recordatorio{" +
                "id=" + id +
                ", mascota=" + (mascota != null ? mascota.getNombre() : "N/A") +
                ", descripcion='" + descripcion + '\'' +
                ", fechaHoraRecordatorio=" + fechaHoraRecordatorio +
                ", activo=" + activo +
                '}';
    }
}