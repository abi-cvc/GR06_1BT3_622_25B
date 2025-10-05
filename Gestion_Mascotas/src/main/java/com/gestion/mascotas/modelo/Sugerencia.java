package com.gestion.mascotas.modelo;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "sugerencias")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Sugerencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "mascota_id", nullable = false)
    private Mascota mascota;

    @Column(nullable = false)
    private String titulo;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String contenido;

    @Column(name = "fecha_generacion", nullable = false)
    private LocalDate fechaGeneracion;

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

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public void setFechaGeneracion(LocalDate fechaGeneracion) {
        this.fechaGeneracion = fechaGeneracion;
    }

    // Métodos genéricos de sugerencia
    public abstract void generarSugerencia(Mascota mascota);

    public void mostrarSugerencia() {
        System.out.println("--- Sugerencia para " + (mascota != null ? mascota.getNombre() : "N/A") + " ---");
        System.out.println("Título: " + titulo);
        System.out.println("Fecha: " + fechaGeneracion);
        System.out.println("Contenido: " + contenido);
        System.out.println("------------------------------------");
    }

    @Override
    public String toString() {
        return "Sugerencia{" +
                "id=" + id +
                ", mascota=" + (mascota != null ? mascota.getNombre() : "N/A") +
                ", titulo='" + titulo + '\'' +
                ", contenido='" + contenido + '\'' +
                ", fechaGeneracion=" + fechaGeneracion +
                '}';
    }
}