package com.gestion.mascotas.modelo.entidades;

import com.gestion.mascotas.modelo.enums.TipoMascota;
import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList; // Importar ArrayList

@Entity
@Table(name = "mascotas")
public class Mascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private TipoMascota tipo;

    // Nuevos atributos para Mascota
    private String raza;
    private Integer edad; // En años
    private Double peso; // En kg
    private String color;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    // Relación con vacunas
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Vacuna> vacunas = new ArrayList<>(); // Inicializar para evitar NullPointerException

    // Relación con visitas
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<VisitaVeterinaria> visitas = new ArrayList<>(); // Inicializar

    // NUEVAS RELACIONES: Recordatorios
    @OneToMany(mappedBy = "mascota", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Recordatorio> recordatorios = new ArrayList<>(); // Inicializar


    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public TipoMascota getTipo() { return tipo; }
    public void setTipo(TipoMascota tipo) { this.tipo = tipo; }

    // Getters y Setters para nuevos atributos
    public String getRaza() { return raza; }
    public void setRaza(String raza) { this.raza = raza; }

    public Integer getEdad() { return edad; }
    public void setEdad(Integer edad) { this.edad = edad; }

    public Double getPeso() { return peso; }
    public void setPeso(Double peso) { this.peso = peso; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }

    public List<Vacuna> getVacunas() { return vacunas; }
    public void setVacunas(List<Vacuna> vacunas) { this.vacunas = vacunas; }

    public List<VisitaVeterinaria> getVisitas() { return visitas; }
    public void setVisitas(List<VisitaVeterinaria> visitas) { this.visitas = visitas; }

    // Getters y Setters para nuevas relaciones
    public List<Recordatorio> getRecordatorios() { return recordatorios; }
    public void setRecordatorios(List<Recordatorio> recordatorios) { this.recordatorios = recordatorios; }

    @Override
    public String toString() {
        return "Mascota{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", tipo=" + tipo +
                ", raza='" + raza + '\'' +
                ", edad=" + edad +
                ", peso=" + peso +
                ", color='" + color + '\'' +
                ", usuario=" + (usuario != null ? usuario.getNombreUsuario() : "N/A") +
                '}';
    }
}