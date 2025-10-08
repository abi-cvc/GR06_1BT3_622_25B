package com.gestion.mascotas.modelo;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "recordatorios")
@Inheritance(strategy = InheritanceType.JOINED) // Añadir esta línea
public class Recordatorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(nullable = false)
    private String descripcion;

    @Column(name = "fecha_hora_recordatorio")
    private LocalDateTime fechaHoraRecordatorio; // Puede ser nulo para recordatorios recurrentes

    @Column(nullable = false)
    private boolean activo;

    // Constructor vacío
    public Recordatorio() {
        // Establecer fecha por defecto para recordatorios
        this.fechaHoraRecordatorio = LocalDateTime.now();
    }

    // Constructor con campos obligatorios
    public Recordatorio(Mascota mascota, String descripcion, boolean activo) {
        this.mascota = mascota;
        this.descripcion = descripcion;
        this.activo = activo;
    }

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

    @Override
    public String toString() {
        String mascotaNombre = "null";
        try {
            if (mascota != null) {
                mascotaNombre = mascota.getNombre();
            }
        } catch (Exception e) {
            // Si ocurre LazyInitializationException, no acceder a la mascota
            mascotaNombre = "Mascota (lazy)";
        }

        return "Recordatorio{" +
                "id=" + id +
                ", mascota=" + mascotaNombre +
                ", descripcion='" + descripcion + '\'' +
                ", fechaHoraRecordatorio=" + fechaHoraRecordatorio +
                ", activo=" + activo +
                '}';
    }
}