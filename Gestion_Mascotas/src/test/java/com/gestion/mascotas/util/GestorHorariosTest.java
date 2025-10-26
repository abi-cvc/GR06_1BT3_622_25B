package com.gestion.mascotas.util;

import org.junit.Test;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.junit.Assert.*;

public class GestorHorariosTest {

    // Tests para extraerHorarios

    @Test
    public void given_valid_horarios_when_extract_then_return_list() {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("horario1", "08:00");
        parametros.put("horario2", "13:00");
        parametros.put("horario3", "18:00");

        Function<String, String> getParameter = parametros::get;

        List<LocalTime> horarios = GestorHorarios.extraerHorarios(3, "horario", getParameter);

        assertEquals("Se debe extraer 3 horarios", 3, horarios.size());
        assertEquals(LocalTime.parse("08:00"), horarios.get(0));
        assertEquals(LocalTime.parse("13:00"), horarios.get(1));
        assertEquals(LocalTime.parse("18:00"), horarios.get(2));
        System.out.println("Test 1: Extracción de horarios válidos");
    }

    @Test
    public void given_single_horario_when_extract_then_return_single_item_list() {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("horarioPaseo1", "07:30");

        Function<String, String> getParameter = parametros::get;

        List<LocalTime> horarios = GestorHorarios.extraerHorarios(1, "horarioPaseo", getParameter);

        assertEquals("Se debe extraer 1 horario", 1, horarios.size());
        assertEquals(LocalTime.parse("07:30"), horarios.get(0));
        System.out.println("Test 2: Extracción de un solo horario");
    }

    @Test
    public void given_empty_horarios_when_extract_then_return_empty_list() {
        Map<String, String> parametros = new HashMap<>();

        Function<String, String> getParameter = parametros::get;

        List<LocalTime> horarios = GestorHorarios.extraerHorarios(3, "horario", getParameter);

        assertTrue("Se debe retornar lista vacía", horarios.isEmpty());
        System.out.println("Test 3: Horarios vacíos");
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_invalid_time_format_when_extract_then_throw_exception() {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("horario1", "25:00"); // Hora inválida

        Function<String, String> getParameter = parametros::get;

        GestorHorarios.extraerHorarios(1, "horario", getParameter);
        System.out.println("Test 4: Formato de hora inválido");
    }

    @Test(expected = IllegalArgumentException.class)
    public void given_malformed_time_when_extract_then_throw_exception() {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("horario1", "abc:def"); // Formato incorrecto

        Function<String, String> getParameter = parametros::get;

        GestorHorarios.extraerHorarios(1, "horario", getParameter);
        System.out.println("Test 5: Tiempo incorrecto");
    }

    @Test
    public void given_partial_horarios_when_extract_then_return_available_only() {
        Map<String, String> parametros = new HashMap<>();
        parametros.put("horario1", "08:00");
        parametros.put("horario3", "18:00"); // Falta horario 2

        Function<String, String> getParameter = parametros::get;

        List<LocalTime> horarios = GestorHorarios.extraerHorarios(3, "horario", getParameter);

        assertEquals("Se debe extraer solo los horarios disponibles", 2, horarios.size());
        System.out.println("Test 6: Horario faltante");
    }

    // Tests para validarHorariosNoVacios

    @Test
    public void given_non_empty_horarios_when_validate_then_return_null() {
        List<LocalTime> horarios = new ArrayList<>();
        horarios.add(LocalTime.parse("08:00"));
        horarios.add(LocalTime.parse("13:00"));

        String result = GestorHorarios.validarHorariosNoVacios(horarios);

        assertNull("Se debe retornar null para horarios no vacíos", result);
        System.out.println("Test 7: Horarios no vacíos");
    }

    @Test
    public void given_empty_horarios_when_validate_then_return_error() {
        List<LocalTime> horarios = new ArrayList<>();

        String result = GestorHorarios.validarHorariosNoVacios(horarios);

        assertEquals("horarios_vacios", result);
        System.out.println("Test 8: Validación de horarios vacíos");
    }

    // Tests para convertirHorariosAString

    @Test
    public void given_multiple_horarios_when_convert_then_return_comma_separated() {
        List<LocalTime> horarios = new ArrayList<>();
        horarios.add(LocalTime.parse("08:00"));
        horarios.add(LocalTime.parse("13:00"));
        horarios.add(LocalTime.parse("18:00"));

        String result = GestorHorarios.convertirHorariosAString(horarios);

        assertEquals("08:00,13:00,18:00", result);
        System.out.println("Test 9: Conversión de múltiples horarios");
    }

    @Test
    public void given_single_horario_when_convert_then_return_single_string() {
        List<LocalTime> horarios = new ArrayList<>();
        horarios.add(LocalTime.parse("09:30"));

        String result = GestorHorarios.convertirHorariosAString(horarios);

        assertEquals("09:30", result);
        System.out.println("Test 10: Conversión de un horario");
    }

    @Test
    public void given_empty_horarios_when_convert_then_return_empty_string() {
        List<LocalTime> horarios = new ArrayList<>();

        String result = GestorHorarios.convertirHorariosAString(horarios);

        assertEquals("", result);
        System.out.println("Test 11: Conversión de lista vacía");
    }

    // Tests para construirDiasSemana

    @Test
    public void given_multiple_dias_when_build_then_return_comma_separated() {
        String[] dias = {"Lunes", "Miércoles", "Viernes"};

        String result = GestorHorarios.construirDiasSemana(dias);

        assertEquals("Lunes,Miércoles,Viernes", result);
        System.out.println("Test 12: Construcción de múltiples días");
    }

    @Test
    public void given_single_dia_when_build_then_return_single_string() {
        String[] dias = {"Sábado"};

        String result = GestorHorarios.construirDiasSemana(dias);

        assertEquals("Sábado", result);
        System.out.println("Test 13: Construcción de un día");
    }

    @Test
    public void given_null_dias_when_build_then_return_empty_string() {
        String result = GestorHorarios.construirDiasSemana(null);

        assertEquals("", result);
        System.out.println("Test 14: Días nulos");
    }

    @Test
    public void given_empty_dias_when_build_then_return_empty_string() {
        String[] dias = {};

        String result = GestorHorarios.construirDiasSemana(dias);

        assertEquals("", result);
        System.out.println("Test 15: Array de días vacío");
    }

    @Test
    public void given_all_week_days_when_build_then_return_all_days() {
        String[] dias = {"Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"};

        String result = GestorHorarios.construirDiasSemana(dias);

        assertEquals("Lunes,Martes,Miércoles,Jueves,Viernes,Sábado,Domingo", result);
        System.out.println("Test 16: Todos los días de la semana");
    }

}