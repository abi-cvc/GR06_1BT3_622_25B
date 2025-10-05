package com.gestion.mascotas.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "sugerencias_ejercicio")
@PrimaryKeyJoinColumn(name = "id") // Une con la tabla Sugerencia
public class SugerenciaEjercicio extends Sugerencia {

    @Column(name = "tipo_actividad")
    private String tipoActividad; // Ej: "Paseos", "Juegos interactivos"

    @Column(name = "duracion_minima_minutos")
    private Integer duracionMinimaMinutos;

    @Column(nullable = false)
    private String intensidad; // Ej: "Moderada", "Alta"

    @Column(name = "frecuencia_semanal")
    private String frecuenciaSemanal; // Ej: "Diaria", "3-4 veces por semana"

    @Column(columnDefinition = "TEXT")
    private String precauciones; // Ej: "Evitar horas de calor, revisar almohadillas."

    // Getters y Setters
    public String getTipoActividad() {
        return tipoActividad;
    }

    public void setTipoActividad(String tipoActividad) {
        this.tipoActividad = tipoActividad;
    }

    public Integer getDuracionMinimaMinutos() {
        return duracionMinimaMinutos;
    }

    public void setDuracionMinimaMinutos(Integer duracionMinimaMinutos) {
        this.duracionMinimaMinutos = duracionMinimaMinutos;
    }

    public String getIntensidad() {
        return intensidad;
    }

    public void setIntensidad(String intensidad) {
        this.intensidad = intensidad;
    }

    public String getFrecuenciaSemanal() {
        return frecuenciaSemanal;
    }

    public void setFrecuenciaSemanal(String frecuenciaSemanal) {
        this.frecuenciaSemanal = frecuenciaSemanal;
    }

    public String getPrecauciones() {
        return precauciones;
    }

    public void setPrecauciones(String precauciones) {
        this.precauciones = precauciones;
    }

    @Override
    public void generarSugerencia(Mascota mascota) {
        setMascota(mascota);
        setFechaGeneracion(LocalDate.now());
        setTitulo("Sugerencia de Ejercicio para " + mascota.getNombre());

        // Lógica de ejemplo para generar sugerencia basada en el tipo de mascota y edad
        String contenidoSugerencia = "";
        if (mascota.getTipo() == TipoMascota.PERRO) {
            if (mascota.getEdad() < 2) { // Cachorro
                this.setTipoActividad("Juegos de socialización y paseos cortos");
                this.setDuracionMinimaMinutos(30);
                this.setIntensidad("Baja a Moderada");
                this.setFrecuenciaSemanal("Diaria (varias veces al día)");
                this.setPrecauciones("Evitar ejercicio excesivo en cachorros para proteger sus articulaciones. Supervisión constante.");
            } else if (mascota.getEdad() < 8) { // Adulto
                this.setTipoActividad("Paseos largos, correr, juegos de buscar");
                this.setDuracionMinimaMinutos(60);
                this.setIntensidad("Moderada a Alta");
                this.setFrecuenciaSemanal("Diaria");
                this.setPrecauciones("Asegurarse de que el perro esté hidratado. Adaptar la intensidad al clima.");
            } else { // Senior
                this.setTipoActividad("Paseos suaves y cortos, natación");
                this.setDuracionMinimaMinutos(20);
                this.setIntensidad("Baja");
                this.setFrecuenciaSemanal("Diaria");
                this.setPrecauciones("Monitorear signos de fatiga o dolor. Consultar al veterinario para un plan adecuado.");
            }
            contenidoSugerencia = "Para " + mascota.getNombre() + ", un perro, se recomienda " + tipoActividad + ". " +
                    "Duración mínima: " + duracionMinimaMinutos + " minutos con intensidad " + intensidad + ", " +
                    frecuenciaSemanal + ". " +
                    "Precauciones: " + precauciones;
        } else if (mascota.getTipo() == TipoMascota.GATO) {
            this.setTipoActividad("Juegos interactivos con juguetes, rascadores");
            this.setDuracionMinimaMinutos(15);
            this.setIntensidad("Baja a Moderada");
            this.setFrecuenciaSemanal("Diaria (varias sesiones cortas)");
            this.setPrecauciones("Asegurarse de que los juguetes sean seguros. Fomentar el juego para evitar el sedentarismo.");
            contenidoSugerencia = "Para " + mascota.getNombre() + ", un gato, se sugiere " + tipoActividad + ". " +
                    "Duración mínima: " + duracionMinimaMinutos + " minutos con intensidad " + intensidad + ", " +
                    frecuenciaSemanal + ". " +
                    "Precauciones: " + precauciones;
        } else {
            this.setTipoActividad("Actividad general");
            this.setDuracionMinimaMinutos(0);
            this.setIntensidad("N/A");
            this.setFrecuenciaSemanal("N/A");
            this.setPrecauciones("Consulta a un veterinario para un plan de ejercicio específico.");
            contenidoSugerencia = "No se pudo generar una sugerencia específica. Consulta a un veterinario.";
        }
        setContenido(contenidoSugerencia);
        System.out.println("Sugerencia de ejercicio generada para " + mascota.getNombre());
    }

    @Override
    public String toString() {
        return "SugerenciaEjercicio{" +
                super.toString() +
                ", tipoActividad='" + tipoActividad + '\'' +
                ", duracionMinimaMinutos=" + duracionMinimaMinutos +
                ", intensidad='" + intensidad + '\'' +
                ", frecuenciaSemanal='" + frecuenciaSemanal + '\'' +
                ", precauciones='" + precauciones + '\'' +
                '}';
    }
}