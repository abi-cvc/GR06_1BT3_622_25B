package com.gestion.mascotas.util;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ValidadorRecordatorioTest {

    private Mascota mascota;
    private Usuario usuario;
    private Usuario otroUsuario;

    @Before
    public void setUp() {
        // Configurar usuario propietario
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNombreUsuario("Pepito");

        // Configurar otro usuario
        otroUsuario = new Usuario();
        otroUsuario.setId(2L);
        otroUsuario.setNombreUsuario("Juanita");

        // Configurar mascota
        mascota = new Mascota();
        mascota.setId(1L);
        mascota.setNombre("Firulais");
        mascota.setTipo(TipoMascota.PERRO);
        mascota.setUsuario(usuario);
    }

    // Tests para validarMascotaId

    @Test
    public void given_valid_mascota_id_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarMascotaId("123");
        assertNull("Retorno null para ID válido", result);
        System.out.println("Test 1: ID de mascota válido");
    }

    @Test
    public void given_null_mascota_id_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarMascotaId(null);
        assertEquals("mascota_id_invalido", result);
        System.out.println("Test 2: ID de mascota nulo");
    }

    @Test
    public void given_empty_mascota_id_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarMascotaId("   ");
        assertEquals("mascota_id_invalido", result);
        System.out.println("Test 3: ID de mascota vacío");
    }

    // Tests para validarFrecuencia

    @Test
    public void given_valid_frecuencia_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarFrecuencia("3");
        assertNull("Retorno null para frecuencia válida", result);
        System.out.println("Test 4: Frecuencia válida");
    }

    @Test
    public void given_null_frecuencia_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarFrecuencia(null);
        assertEquals("frecuencia_vacia", result);
        System.out.println("Test 5: Frecuencia nula");
    }

    @Test
    public void given_empty_frecuencia_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarFrecuencia("");
        assertEquals("frecuencia_vacia", result);
        System.out.println("Test 6: Frecuencia vacía");
    }

    // Tests para validarMascotaExiste

    @Test
    public void given_existing_mascota_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarMascotaExiste(mascota);
        assertNull("Retorno null para mascota existente", result);
        System.out.println("Test 7: Mascota existente");
    }

    @Test
    public void given_null_mascota_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarMascotaExiste(null);
        assertEquals("mascota_no_encontrada", result);
        System.out.println("Test 8: Mascota nula");
    }

    // Tests para validarPerteneceUsuario

    @Test
    public void given_mascota_belongs_to_user_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarPerteneceUsuario(mascota, usuario);
        assertNull("Retorno null cuando mascota pertenece al usuario", result);
        System.out.println("Test 9: Mascota pertenece al usuario");
    }

    @Test
    public void given_mascota_not_belongs_to_user_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarPerteneceUsuario(mascota, otroUsuario);
        assertEquals("acceso_denegado", result);
        System.out.println("Test 10: Mascota no pertenece al usuario");
    }

    // Tests para validarTipoAlimento

    @Test
    public void given_valid_tipo_alimento_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarTipoAlimento("Croquetas");
        assertNull("Retorno null para tipo de alimento válido", result);
        System.out.println("Test 11: Tipo de alimento válido");
    }

    @Test
    public void given_null_tipo_alimento_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarTipoAlimento(null);
        assertEquals("tipo_alimento_vacio", result);
        System.out.println("Test 12: Tipo de alimento nulo");
    }

    @Test
    public void given_empty_tipo_alimento_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarTipoAlimento("  ");
        assertEquals("tipo_alimento_vacio", result);
        System.out.println("Test 13: Tipo de alimento vacío");
    }

    // Tests para validarDuracion

    @Test
    public void given_valid_duracion_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarDuracion(30);
        assertNull("Retorno null para duración válida", result);
        System.out.println("Test 14: Duración válida");
    }

    @Test
    public void given_null_duracion_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarDuracion(null);
        assertEquals("duracion_invalida", result);
        System.out.println("Test 15: Duración nula");
    }

    @Test
    public void given_zero_duracion_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarDuracion(0);
        assertEquals("duracion_invalida", result);
        System.out.println("Test 16: Duración cero");
    }

    @Test
    public void given_negative_duracion_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarDuracion(-10);
        assertEquals("duracion_invalida", result);
        System.out.println("Test 17: Duración negativa");
    }

    // Tests para validarRecordatorioExiste

    @Test
    public void given_existing_recordatorio_when_validate_then_return_null() {
        Object recordatorio = new Object();
        String result = ValidadorRecordatorio.validarRecordatorioExiste(recordatorio);
        assertNull("Retorno null para recordatorio existente", result);
        System.out.println("Test 18: Recordatorio existente");
    }

    @Test
    public void given_null_recordatorio_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarRecordatorioExiste(null);
        assertEquals("recordatorio_no_encontrado", result);
        System.out.println("Test 19: Recordatorio nulo");
    }

    // Tests para validarIdRecordatorio

    @Test
    public void given_valid_id_recordatorio_when_validate_then_return_null() {
        String result = ValidadorRecordatorio.validarIdRecordatorio("456");
        assertNull("Retorno null para ID de recordatorio válido", result);
        System.out.println("Test 20: ID de recordatorio válido");
    }

    @Test
    public void given_null_id_recordatorio_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarIdRecordatorio(null);
        assertEquals("id_recordatorio_invalido", result);
        System.out.println("Test 21: ID de recordatorio nulo");
    }

    @Test
    public void given_empty_id_recordatorio_when_validate_then_return_error() {
        String result = ValidadorRecordatorio.validarIdRecordatorio("");
        assertEquals("id_recordatorio_invalido", result);
        System.out.println("Test 22: ID de recordatorio vacío");
    }
    
}