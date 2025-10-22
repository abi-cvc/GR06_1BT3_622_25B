package com.gestion.mascotas.logica;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GestorHorarios {

    public static List<LocalTime> extraerHorarios(int numHorarios, String prefijo,
                                                  java.util.function.Function<String, String> getParameter) {
        List<LocalTime> horarios = new ArrayList<>();

        System.out.println("Numero de horarios esperados: " + numHorarios);

        for (int i = 1; i <= numHorarios; i++) {
            String paramName = prefijo + i;
            String horaStr = getParameter.apply(paramName);

            System.out.println("Parametro " + paramName + ": " + horaStr);

            if (horaStr != null && !horaStr.trim().isEmpty()) {
                try {
                    LocalTime hora = LocalTime.parse(horaStr);
                    horarios.add(hora);
                    System.out.println("Hora agregada: " + hora);
                } catch (Exception e) {
                    System.err.println("Error parseando hora: " + horaStr);
                    e.printStackTrace();
                    throw new IllegalArgumentException("formato_hora_invalido");
                }
            }
        }

        System.out.println("Total horarios capturados: " + horarios.size());

        return horarios;
    }

    public static String validarHorariosNoVacios(List<LocalTime> horarios) {
        if (horarios.isEmpty()) {
            System.err.println("ERROR: No se recibieron horarios");
            return "horarios_vacios";
        }
        return null;
    }

    public static String convertirHorariosAString(List<LocalTime> horarios) {
        return horarios.stream()
                .map(LocalTime::toString)
                .collect(Collectors.joining(","));
    }

    public static String construirDiasSemana(String[] diasSeleccionados) {
        String diasSemana = "";
        if (diasSeleccionados != null && diasSeleccionados.length > 0) {
            diasSemana = String.join(",", diasSeleccionados);
            System.out.println("Dias seleccionados: " + diasSemana);
        } else {
            System.out.println("Sin dias especificos - activo todos los dias");
        }
        return diasSemana;
    }
}