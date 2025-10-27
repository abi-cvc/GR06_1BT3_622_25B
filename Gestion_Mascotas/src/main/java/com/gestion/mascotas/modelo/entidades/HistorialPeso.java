package com.gestion.mascotas.modelo.entidades;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historial_peso")
public class HistorialPeso {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relación muchos a uno: muchas mediciones pertenecen a una misma mascota
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(nullable = false)
    private double peso;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro;

    @Column(length = 255)
    private String comentarios;

    // Constructores
    public HistorialPeso() {
    }

    public HistorialPeso(Mascota mascota, double peso, LocalDateTime fechaRegistro, String comentarios) {
        this.mascota = mascota;
        this.peso = peso;
        this.fechaRegistro = fechaRegistro;
        this.comentarios = comentarios;
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

    public double getPeso() {
        return peso;
    }
    public void setPeso(double peso) {
        this.peso = peso;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getComentarios() {
        return comentarios;
    }
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    @Override
    public String toString() {
        return "HistorialPeso{" +
                "id=" + id +
                ", peso=" + peso +
                ", fechaRegistro=" + fechaRegistro +
                ", comentarios='" + comentarios + '\'' +
                '}';
    }
}
