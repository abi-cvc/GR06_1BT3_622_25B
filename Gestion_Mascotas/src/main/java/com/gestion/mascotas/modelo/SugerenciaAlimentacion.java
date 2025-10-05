package com.gestion.mascotas.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import java.time.LocalDate;

@Entity
@Table(name = "sugerencias_alimentacion")
@PrimaryKeyJoinColumn(name = "id") // Une con la tabla Sugerencia
public class SugerenciaAlimentacion extends Sugerencia {

    @Column(name = "tipo_alimento_sugerido")
    private String tipoAlimentoSugerido; // Ej: "Pienso hipoalergénico"

    @Column(name = "cantidad_diaria_gramos")
    private Double cantidadDiariaGramos;

    @Column(nullable = false)
    private String frecuencia; // Ej: "2 veces al día"

    @Column(columnDefinition = "TEXT")
    private String consideraciones; // Ej: "Dividir en porciones pequeñas, evitar premios."

    // Getters y Setters
    public String getTipoAlimentoSugerido() {
        return tipoAlimentoSugerido;
    }

    public void setTipoAlimentoSugerido(String tipoAlimentoSugerido) {
        this.tipoAlimentoSugerido = tipoAlimentoSugerido;
    }

    public Double getCantidadDiariaGramos() {
        return cantidadDiariaGramos;
    }

    public void setCantidadDiariaGramos(Double cantidadDiariaGramos) {
        this.cantidadDiariaGramos = cantidadDiariaGramos;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getConsideraciones() {
        return consideraciones;
    }

    public void setConsideraciones(String consideraciones) {
        this.consideraciones = consideraciones;
    }

    @Override
    public void generarSugerencia(Mascota mascota) {
        setMascota(mascota);
        setFechaGeneracion(LocalDate.now());
        setTitulo("Sugerencia de Alimentación para " + mascota.getNombre());

        // Lógica de ejemplo para generar sugerencia basada en el tipo de mascota
        String contenidoSugerencia = "";
        if (mascota.getTipo() == TipoMascota.PERRO) {
            this.setTipoAlimentoSugerido("Pienso para perros adultos de raza mediana");
            this.setCantidadDiariaGramos(mascota.getPeso() * 30.0); // Ejemplo: 30g por kg de peso
            this.setFrecuencia("2 veces al día");
            this.setConsideraciones("Asegúrate de que siempre tenga agua fresca disponible. Evita darle comida de humanos.");
            contenidoSugerencia = "Basado en el peso y tipo de " + mascota.getNombre() + ", se recomienda un pienso de alta calidad para perros adultos. " +
                    "Cantidad diaria: " + String.format("%.2f", cantidadDiariaGramos) + " gramos, dividido en " + frecuencia + ". " +
                    "Consideraciones: " + consideraciones;
        } else if (mascota.getTipo() == TipoMascota.GATO) {
            this.setTipoAlimentoSugerido("Pienso para gatos esterilizados");
            this.setCantidadDiariaGramos(mascota.getPeso() * 25.0); // Ejemplo: 25g por kg de peso
            this.setFrecuencia("3 veces al día");
            this.setConsideraciones("Los gatos prefieren comer pequeñas porciones a lo largo del día. Mantén su arenero limpio.");
            contenidoSugerencia = "Para " + mascota.getNombre() + ", un gato, se sugiere un pienso específico para gatos esterilizados. " +
                    "Cantidad diaria: " + String.format("%.2f", cantidadDiariaGramos) + " gramos, dividido en " + frecuencia + ". " +
                    "Consideraciones: " + consideraciones;
        } else {
            this.setTipoAlimentoSugerido("Alimento general para mascotas");
            this.setCantidadDiariaGramos(0.0);
            this.setFrecuencia("Según indicaciones del fabricante");
            this.setConsideraciones("Consulta a un veterinario para una dieta específica.");
            contenidoSugerencia = "No se pudo generar una sugerencia específica. Consulta a un veterinario.";
        }
        setContenido(contenidoSugerencia);
        System.out.println("Sugerencia de alimentación generada para " + mascota.getNombre());
    }

    @Override
    public String toString() {
        return "SugerenciaAlimentacion{" +
                super.toString() +
                ", tipoAlimentoSugerido='" + tipoAlimentoSugerido + '\'' +
                ", cantidadDiariaGramos=" + cantidadDiariaGramos +
                ", frecuencia='" + frecuencia + '\'' +
                ", consideraciones='" + consideraciones + '\'' +
                '}';
    }
}