package com.gestion.mascotas.servicio;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import com.gestion.mascotas.util.ValidadorRecordatorio;
import com.gestion.mascotas.util.GestorHorarios;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class RecordatorioPaseoServiceTest {

    private Mascota mascota;
    private Usuario usuario;
    private Usuario otroUsuario;
    private Map<String, String> parametros;

    @Before
    public void setUp() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("Pepito");

        otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setNombreUsuario("Juanita");

        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Firulais");
        mascota.setTipo(TipoMascota.PERRO);
        mascota.setUsuario(usuario);

        parametros = new HashMap<>();
        parametros.put("horarioPaseo1", "07:00");
        parametros.put("horarioPaseo2", "18:00");
    }

    //Tests para validarMascotaId

    @Test
    public void test_validar_mascota_id_valido() {
        String result = ValidadorRecordatorio.validarMascotaId("123");
        assertNull(result);
        System.out.println("Test 1: ID de mascota válido");
    }

    @Test
    public void test_validar_mascota_id_nulo() {
        String result = ValidadorRecordatorio.validarMascotaId(null);
        assertEquals("mascota_id_invalido", result);
        System.out.println("Test 2: ID de mascota nulo");
    }

    @Test
    public void test_validar_mascota_id_vacio() {
        String result = ValidadorRecordatorio.validarMascotaId("   ");
        assertEquals("mascota_id_invalido", result);
        System.out.println("Test 3: ID de mascota vacío");
    }

    //Tests para validarFrecuencia

    @Test
    public void test_validar_frecuencia_valida() {
        String result = ValidadorRecordatorio.validarFrecuencia("2");
        assertNull(result);
        System.out.println("Test 4: Frecuencia válida");
    }

    @Test
    public void test_validar_frecuencia_nula() {
        String result = ValidadorRecordatorio.validarFrecuencia(null);
        assertEquals("frecuencia_vacia", result);
        System.out.println("Test 5: Frecuencia nula");
    }

    @Test
    public void test_validar_frecuencia_vacia() {
        String result = ValidadorRecordatorio.validarFrecuencia("");
        assertEquals("frecuencia_vacia", result);
        System.out.println("Test 6: Frecuencia vacía");
    }

    //Tests para validarDuracion

    @Test
    public void test_validar_duracion_valida() {
        String result = ValidadorRecordatorio.validarDuracion(30);
        assertNull(result);
        System.out.println("Test 7: Duración válida");
    }

    @Test
    public void test_validar_duracion_nula() {
        String result = ValidadorRecordatorio.validarDuracion(null);
        assertEquals("duracion_invalida", result);
        System.out.println("Test 8: Duración nula");
    }

    @Test
    public void test_validar_duracion_cero() {
        String result = ValidadorRecordatorio.validarDuracion(0);
        assertEquals("duracion_invalida", result);
        System.out.println("Test 9: Duración cero");
    }

    @Test
    public void test_validar_duracion_negativa() {
        String result = ValidadorRecordatorio.validarDuracion(-10);
        assertEquals("duracion_invalida", result);
        System.out.println("Test 10: Duración negativa");
    }

    //Tests para validarMascotaExiste

    @Test
    public void test_validar_mascota_existe() {
        String result = ValidadorRecordatorio.validarMascotaExiste(mascota);
        assertNull(result);
        System.out.println("Test 11: Mascota existente");
    }

    @Test
    public void test_validar_mascota_nula() {
        String result = ValidadorRecordatorio.validarMascotaExiste(null);
        assertEquals("mascota_no_encontrada", result);
        System.out.println("Test 12: Mascota nula");
    }

    //Tests para validarPerteneceUsuario

    @Test
    public void test_validar_mascota_pertenece_usuario() {
        String result = ValidadorRecordatorio.validarPerteneceUsuario(mascota, usuario);
        assertNull(result);
        System.out.println("Test 13: Mascota pertenece al usuario");
    }

    @Test
    public void test_validar_mascota_no_pertenece_usuario() {
        String result = ValidadorRecordatorio.validarPerteneceUsuario(mascota, otroUsuario);
        assertEquals("acceso_denegado", result);
        System.out.println("Test 14: Mascota no pertenece al usuario");
    }

    //Tests para validarRecordatorioExiste

    @Test
    public void test_validar_recordatorio_existe() {
        Object recordatorio = new Object();
        String result = ValidadorRecordatorio.validarRecordatorioExiste(recordatorio);
        assertNull(result);
        System.out.println("Test 15: Recordatorio existente");
    }

    @Test
    public void test_validar_recordatorio_nulo() {
        String result = ValidadorRecordatorio.validarRecordatorioExiste(null);
        assertEquals("recordatorio_no_encontrado", result);
        System.out.println("Test 16: Recordatorio nulo");
    }

    //Tests para validarIdRecordatorio

    @Test
    public void test_validar_id_recordatorio_valido() {
        String result = ValidadorRecordatorio.validarIdRecordatorio("456");
        assertNull(result);
        System.out.println("Test 17: ID de recordatorio válido");
    }

    @Test
    public void test_validar_id_recordatorio_nulo() {
        String result = ValidadorRecordatorio.validarIdRecordatorio(null);
        assertEquals("id_recordatorio_invalido", result);
        System.out.println("Test 18: ID de recordatorio nulo");
    }

    @Test
    public void test_validar_id_recordatorio_vacio() {
        String result = ValidadorRecordatorio.validarIdRecordatorio("");
        assertEquals("id_recordatorio_invalido", result);
        System.out.println("Test 19: ID de recordatorio vacío");
    }

    //Tests para GestorHorarios.extraerHorarios

    @Test
    public void test_extraer_horarios_validos() {
        Function<String, String> getParameter = parametros::get;
        List<LocalTime> horarios = GestorHorarios.extraerHorarios(2, "horarioPaseo", getParameter);

        assertEquals(2, horarios.size());
        assertEquals(LocalTime.of(7, 0), horarios.get(0));
        assertEquals(LocalTime.of(18, 0), horarios.get(1));
        System.out.println("Test 20: Extracción de horarios válidos");
    }

    @Test
    public void test_extraer_horarios_un_horario() {
        Map<String, String> params = new HashMap<>();
        params.put("horarioPaseo1", "08:30");
        Function<String, String> getParameter = params::get;

        List<LocalTime> horarios = GestorHorarios.extraerHorarios(1, "horarioPaseo", getParameter);

        assertEquals(1, horarios.size());
        assertEquals(LocalTime.of(8, 30), horarios.get(0));
        System.out.println("Test 21: Extracción de un solo horario");
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_extraer_horarios_formato_invalido() {
        Map<String, String> params = new HashMap<>();
        params.put("horarioPaseo1", "25:00");
        Function<String, String> getParameter = params::get;

        GestorHorarios.extraerHorarios(1, "horarioPaseo", getParameter);
        System.out.println("Test 22: Formato de hora inválido");
    }

    //Tests para GestorHorarios.validarHorariosNoVacios

    @Test
    public void test_validar_horarios_no_vacios_con_datos() {
        List<LocalTime> horarios = new ArrayList<>();
        horarios.add(LocalTime.of(7, 0));

        String result = GestorHorarios.validarHorariosNoVacios(horarios);
        assertNull(result);
        System.out.println("Test 23: Validación de horarios con datos");
    }

    @Test
    public void test_validar_horarios_vacios() {
        List<LocalTime> horarios = new ArrayList<>();

        String result = GestorHorarios.validarHorariosNoVacios(horarios);
        assertEquals("horarios_vacios", result);
        System.out.println("Test 24: Validación de horarios vacíos");
    }

    //Tests para GestorHorarios.convertirHorariosAString

    @Test
    public void test_convertir_horarios_a_string() {
        List<LocalTime> horarios = new ArrayList<>();
        horarios.add(LocalTime.of(7, 0));
        horarios.add(LocalTime.of(18, 30));

        String result = GestorHorarios.convertirHorariosAString(horarios);
        assertEquals("07:00,18:30", result);
        System.out.println("Test 25: Conversión de horarios a string");
    }

    @Test
    public void test_convertir_horarios_a_string_un_horario() {
        List<LocalTime> horarios = new ArrayList<>();
        horarios.add(LocalTime.of(12, 0));

        String result = GestorHorarios.convertirHorariosAString(horarios);
        assertEquals("12:00", result);
        System.out.println("Test 26: Conversión de un solo horario a string");
    }

    @Test
    public void test_convertir_horarios_a_string_vacio() {
        List<LocalTime> horarios = new ArrayList<>();

        String result = GestorHorarios.convertirHorariosAString(horarios);
        assertEquals("", result);
        System.out.println("Test 27: Conversión de horarios vacíos a string");
    }

    //Tests para GestorHorarios.construirDiasSemana

    @Test
    public void test_construir_dias_semana_con_dias() {
        String[] diasSeleccionados = {"Lunes", "Miércoles", "Viernes"};

        String result = GestorHorarios.construirDiasSemana(diasSeleccionados);
        assertEquals("Lunes,Miércoles,Viernes", result);
        System.out.println("Test 28: Construcción de días de semana con datos");
    }

    @Test
    public void test_construir_dias_semana_un_dia() {
        String[] diasSeleccionados = {"Sábado"};

        String result = GestorHorarios.construirDiasSemana(diasSeleccionados);
        assertEquals("Sábado", result);
        System.out.println("Test 29: Construcción con un solo día");
    }

    @Test
    public void test_construir_dias_semana_nulos() {
        String result = GestorHorarios.construirDiasSemana(null);
        assertEquals("", result);
        System.out.println("Test 30: Construcción con días nulos");
    }

    @Test
    public void test_construir_dias_semana_vacio() {
        String[] diasSeleccionados = {};

        String result = GestorHorarios.construirDiasSemana(diasSeleccionados);
        assertEquals("", result);
        System.out.println("Test 31: Construcción con array vacío");
    }
}