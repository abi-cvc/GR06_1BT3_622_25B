package com.gestion.mascotas.modelo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la clase AnalizadorDatos
 * Siguiendo TDD (Test-Driven Development)
 * 
 * Esta clase analiza mascotas y calcula:
 * - Nivel de actividad recomendado
 * - Calorías diarias recomendadas
 * - Si necesita atención especial
 * - Genera reportes de análisis
 */
@DisplayName("Tests Unitarios - AnalizadorDatos")
class AnalizadorDatosTest {

    private AnalizadorDatos analizador;
    private Mascota mascotaPerro;
    private Mascota mascotaGato;
    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        analizador = new AnalizadorDatos();

        // Configurar usuario de prueba
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombreUsuario("testUser");

        // Configurar mascota perro
        mascotaPerro = new Mascota();
        mascotaPerro.setId(1L);
        mascotaPerro.setNombre("Firulais");
        mascotaPerro.setTipo(TipoMascota.PERRO);
        mascotaPerro.setRaza("Labrador");
        mascotaPerro.setEdad(3);
        mascotaPerro.setPeso(25.0);
        mascotaPerro.setColor("Dorado");
        mascotaPerro.setUsuario(usuarioTest);

        // Configurar mascota gato
        mascotaGato = new Mascota();
        mascotaGato.setId(2L);
        mascotaGato.setNombre("Michi");
        mascotaGato.setTipo(TipoMascota.GATO);
        mascotaGato.setRaza("Persa");
        mascotaGato.setEdad(2);
        mascotaGato.setPeso(4.5);
        mascotaGato.setColor("Blanco");
        mascotaGato.setUsuario(usuarioTest);
    }

    @Nested
    @DisplayName("Tests de Cálculo de Nivel de Actividad")
    class CalculoNivelActividad {

        @Test
        @DisplayName("Perro joven (< 2 años) - Nivel MEDIA")
        void testPerroJoven_NivelMedia() {
            mascotaPerro.setEdad(1);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaPerro);

            assertEquals("MEDIA", nivelActividad,
                    "Un perro joven (< 2 años) debe tener nivel de actividad MEDIA según estrategia");
        }

        @Test
        @DisplayName("Perro adulto (2-7 años) - Nivel ALTA")
        void testPerroAdulto_NivelAlta() {
            mascotaPerro.setEdad(5);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaPerro);

            assertEquals("ALTA", nivelActividad,
                    "Un perro adulto (2-7 años) debe tener nivel de actividad ALTA según estrategia");
        }

        @Test
        @DisplayName("Perro anciano (>= 8 años) - Nivel BAJA")
        void testPerroAnciano_NivelBaja() {
            mascotaPerro.setEdad(10);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaPerro);

            assertEquals("BAJA", nivelActividad,
                    "Un perro anciano (>= 8 años) debe tener nivel de actividad BAJA según estrategia");
        }

        @Test
        @DisplayName("Gato joven/adulto (< 10 años) - Nivel MEDIA")
        void testGatoJoven_NivelMedia() {
            mascotaGato.setEdad(1);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaGato);

            assertEquals("MEDIA", nivelActividad,
                    "Un gato (< 10 años) debe tener nivel de actividad MEDIA según estrategia");
        }

        @Test
        @DisplayName("Gato adulto (< 10 años) - Nivel MEDIA")
        void testGatoAdulto_NivelMedia() {
            mascotaGato.setEdad(5);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaGato);

            assertEquals("MEDIA", nivelActividad,
                    "Un gato adulto (< 10 años) debe tener nivel de actividad MEDIA según estrategia");
        }

        @Test
        @DisplayName("Gato anciano (>= 10 años) - Nivel BAJA")
        void testGatoAnciano_NivelBaja() {
            mascotaGato.setEdad(12);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaGato);

            assertEquals("BAJA", nivelActividad,
                    "Un gato anciano (>= 10 años) debe tener nivel de actividad BAJA según estrategia");
        }

        @Test
        @DisplayName("Edad null - Retorna MEDIA por defecto")
        void testEdadNull_RetornaMedia() {
            mascotaPerro.setEdad(null);

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaPerro);

            assertEquals("MEDIA", nivelActividad,
                    "Si la edad es null, debe retornar nivel MEDIA por defecto");
        }
    }

    @Nested
    @DisplayName("Tests de Cálculo de Calorías Recomendadas")
    class CalculoCaloriasRecomendadas {

        @Test
        @DisplayName("Perro joven (MEDIA actividad) - 30 kcal/kg")
        void testPerroJoven_CaloriasMedias() {
            mascotaPerro.setEdad(1); // MEDIA actividad según estrategia
            mascotaPerro.setPeso(20.0);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);

            assertEquals(600, calorias,
                    "Perro joven con actividad MEDIA: 20kg * 30 kcal/kg = 600 kcal");
        }

        @Test
        @DisplayName("Perro adulto (ALTA actividad) - 35 kcal/kg")
        void testPerroAdulto_CaloriasAltas() {
            mascotaPerro.setEdad(5); // ALTA actividad según estrategia
            mascotaPerro.setPeso(25.0);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);

            assertEquals(875, calorias,
                    "Perro adulto con actividad ALTA: 25kg * 35 kcal/kg = 875 kcal");
        }

        @Test
        @DisplayName("Perro anciano (BAJA actividad) - 25 kcal/kg")
        void testPerroAnciano_CaloriasBajas() {
            mascotaPerro.setEdad(10); // BAJA actividad
            mascotaPerro.setPeso(20.0);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);

            assertEquals(500, calorias,
                    "Perro con actividad BAJA: 20kg * 25 kcal/kg = 500 kcal");
        }

        @Test
        @DisplayName("Gato joven/adulto (MEDIA actividad) - 30 kcal/kg")
        void testGatoJoven_CaloriasMedias() {
            mascotaGato.setEdad(1); // MEDIA actividad según estrategia
            mascotaGato.setPeso(4.0);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaGato);

            assertEquals(120, calorias,
                    "Gato con actividad MEDIA: 4kg * 30 kcal/kg = 120 kcal");
        }

        @Test
        @DisplayName("Gato adulto (MEDIA actividad) - 30 kcal/kg")
        void testGatoAdulto_CaloriasMedias() {
            mascotaGato.setEdad(5); // MEDIA actividad según estrategia
            mascotaGato.setPeso(5.0);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaGato);

            assertEquals(150, calorias,
                    "Gato con actividad MEDIA: 5kg * 30 kcal/kg = 150 kcal");
        }

        @Test
        @DisplayName("Peso null - Retorna null")
        void testPesoNull_RetornaNull() {
            mascotaPerro.setPeso(null);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);

            assertNull(calorias,
                    "Si el peso es null, debe retornar null");
        }

        @Test
        @DisplayName("Peso decimal - Redondea correctamente")
        void testPesoDecimal_RedondeoCorrect() {
            mascotaPerro.setEdad(5); // ALTA actividad = 35 kcal/kg para perros adultos
            mascotaPerro.setPeso(22.5);

            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);

            assertEquals(787, calorias,
                    "22.5kg * 35 kcal/kg = 787.5 → 787 kcal (debe redondear correctamente)");
        }
    }

    @Nested
    @DisplayName("Tests de Validación de Atención Especial")
    class ValidacionAtencionEspecial {

        @Test
        @DisplayName("Mascota normal - No requiere atención especial")
        void testMascotaNormal_NoRequiereAtencion() {
            mascotaPerro.setEdad(5);
            mascotaPerro.setPeso(25.0);

            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertFalse(necesitaAtencion,
                    "Una mascota con edad y peso normales NO debe requerir atención especial");
        }

        @Test
        @DisplayName("Cachorro - Requiere atención especial")
        void testCachorro_RequiereAtencion() {
            mascotaPerro.setEdad(0);
            mascotaPerro.setPeso(5.0);

            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertTrue(necesitaAtencion,
                    "Un cachorro (< 1 año) debe requerir atención especial");
        }

        @Test
        @DisplayName("Mascota anciana - Requiere atención especial")
        void testAnciano_RequiereAtencion() {
            mascotaPerro.setEdad(12);
            mascotaPerro.setPeso(25.0);

            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertTrue(necesitaAtencion,
                    "Una mascota anciana (> 10 años) debe requerir atención especial");
        }

        @Test
        @DisplayName("Peso bajo - Requiere atención especial")
        void testPesoBajo_RequiereAtencion() {
            mascotaPerro.setEdad(5);
            mascotaPerro.setPeso(1.5);

            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertTrue(necesitaAtencion,
                    "Una mascota con peso bajo (< 2kg) debe requerir atención especial");
        }

        @Test
        @DisplayName("Sobrepeso perro - Requiere atención especial")
        void testSobrepesoPerro_RequiereAtencion() {
            mascotaPerro.setEdad(5);
            mascotaPerro.setPeso(55.0);

            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertTrue(necesitaAtencion,
                    "Un perro con sobrepeso (> 50kg) debe requerir atención especial");
        }

        @Test
        @DisplayName("Sobrepeso gato - Requiere atención especial")
        void testSobrepesoGato_RequiereAtencion() {
            mascotaGato.setEdad(5);
            mascotaGato.setPeso(12.0);

            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaGato);

            assertTrue(necesitaAtencion,
                    "Un gato con sobrepeso (> 10kg) debe requerir atención especial");
        }
    }

    @Nested
    @DisplayName("Tests de Generación de Reporte")
    class GeneracionReporte {

        @Test
        @DisplayName("Reporte contiene información básica de la mascota")
        void testReporte_ContieneInfoBasica() {
            String reporte = analizador.generarReporteAnalisis(mascotaPerro);

            assertAll("El reporte debe contener toda la información básica",
                    () -> assertTrue(reporte.contains("Firulais"), "Debe contener el nombre"),
                    () -> assertTrue(reporte.contains("PERRO"), "Debe contener el tipo"),
                    () -> assertTrue(reporte.contains("Labrador"), "Debe contener la raza"),
                    () -> assertTrue(reporte.contains("3 años"), "Debe contener la edad"),
                    () -> assertTrue(reporte.contains("25.0 kg"), "Debe contener el peso")
            );
        }

        @Test
        @DisplayName("Reporte contiene nivel de actividad calculado")
        void testReporte_ContieneNivelActividad() {
            mascotaPerro.setEdad(5); // ALTA actividad para perros adultos

            String reporte = analizador.generarReporteAnalisis(mascotaPerro);

            assertTrue(reporte.contains("Nivel de actividad recomendado: ALTA"),
                    "El reporte debe contener el nivel de actividad calculado");
        }

        @Test
        @DisplayName("Reporte contiene calorías recomendadas")
        void testReporte_ContieneCaloriasRecomendadas() {
            mascotaPerro.setEdad(5); // ALTA = 35 kcal/kg
            mascotaPerro.setPeso(20.0); // 20 * 35 = 700 kcal

            String reporte = analizador.generarReporteAnalisis(mascotaPerro);

            assertTrue(reporte.contains("Calorías diarias recomendadas: 700 kcal"),
                    "El reporte debe contener las calorías recomendadas calculadas");
        }

        @Test
        @DisplayName("Reporte con raza null muestra 'No especificada'")
        void testReporte_RazaNull_MuestraNoEspecificada() {
            mascotaPerro.setRaza(null);

            String reporte = analizador.generarReporteAnalisis(mascotaPerro);

            assertTrue(reporte.contains("Raza: No especificada"),
                    "Si la raza es null, debe mostrar 'No especificada'");
        }

        @Test
        @DisplayName("Reporte tiene formato estructurado")
        void testReporte_TieneFormatoEstructurado() {
            String reporte = analizador.generarReporteAnalisis(mascotaPerro);

            assertAll("El reporte debe tener formato estructurado",
                    () -> assertTrue(reporte.contains("=== REPORTE DE ANÁLISIS ==="),
                            "Debe tener encabezado"),
                    () -> assertTrue(reporte.contains("-----------------------------------------"),
                            "Debe tener separador"),
                    () -> assertTrue(reporte.contains("==========================="),
                            "Debe tener pie de página")
            );
        }
    }

    @Nested
    @DisplayName("Tests de Integración - Casos Completos")
    class CasosCompletos {

        @Test
        @DisplayName("Cachorro con peso bajo - Múltiples validaciones")
        void testCachorroConPesoBajo() {
            mascotaPerro.setEdad(0); // Cachorro (< 2 años) = MEDIA
            mascotaPerro.setPeso(1.5); // Peso bajo

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaPerro);
            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);
            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertAll("Cachorro con peso bajo debe tener características específicas",
                    () -> assertEquals("MEDIA", nivelActividad, "Cachorro debe tener actividad MEDIA"),
                    () -> assertEquals(45, calorias, "1.5kg * 30 kcal/kg = 45 kcal"),
                    () -> assertTrue(necesitaAtencion, "Debe requerir atención especial")
            );
        }

        @Test
        @DisplayName("Perro anciano con sobrepeso - Múltiples validaciones")
        void testPerroAncianoConSobrepeso() {
            mascotaPerro.setEdad(12); // Anciano
            mascotaPerro.setPeso(55.0); // Sobrepeso

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaPerro);
            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaPerro);
            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaPerro);

            assertAll("Perro anciano con sobrepeso debe tener características específicas",
                    () -> assertEquals("BAJA", nivelActividad, "Debe tener actividad BAJA"),
                    () -> assertEquals(1375, calorias, "55kg * 25 kcal/kg = 1375 kcal"),
                    () -> assertTrue(necesitaAtencion, "Debe requerir atención especial")
            );
        }

        @Test
        @DisplayName("Gato adulto normal - Múltiples validaciones")
        void testGatoAdultoNormal() {
            mascotaGato.setEdad(5); // Adulto (< 10) = MEDIA
            mascotaGato.setPeso(5.0); // Normal

            String nivelActividad = analizador.calcularNivelActividadRecomendado(mascotaGato);
            Integer calorias = analizador.calcularCaloriasRecomendadas(mascotaGato);
            boolean necesitaAtencion = analizador.necesitaAtencionEspecial(mascotaGato);

            assertAll("Gato adulto normal debe tener características estándar",
                    () -> assertEquals("MEDIA", nivelActividad, "Debe tener actividad MEDIA"),
                    () -> assertEquals(150, calorias, "5kg * 30 kcal/kg = 150 kcal"),
                    () -> assertFalse(necesitaAtencion, "NO debe requerir atención especial")
            );
        }
    }
}
