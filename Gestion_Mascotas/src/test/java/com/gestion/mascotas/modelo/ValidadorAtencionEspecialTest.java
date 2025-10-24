package com.gestion.mascotas.modelo;

import com.gestion.mascotas.modelo.entidades.Mascota;
import com.gestion.mascotas.modelo.entidades.Usuario;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import com.gestion.mascotas.util.ValidadorAtencionEspecial;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test unitario para la clase ValidadorAtencionEspecial
 * Siguiendo TDD (Test-Driven Development)
 * 
 * Esta clase valida si una mascota requiere atención especial basándose en:
 * - Edad (cachorros < 1 año o ancianos > 10 años)
 * - Peso (muy bajo < 2kg o muy alto según tipo de mascota)
 */
@DisplayName("Tests Unitarios - ValidadorAtencionEspecial")
class ValidadorAtencionEspecialTest {

    private Mascota mascotaPerro;
    private Mascota mascotaGato;
    private Usuario usuarioTest;

    @BeforeEach
    void setUp() {
        // Configurar usuario de prueba
        usuarioTest = new Usuario();
        usuarioTest.setId(1L);
        usuarioTest.setNombreUsuario("testUser");

        // Configurar mascota perro normal (no requiere atención especial)
        mascotaPerro = new Mascota();
        mascotaPerro.setNombre("Firulais");
        mascotaPerro.setTipo(TipoMascota.PERRO);
        mascotaPerro.setEdad(5); // Edad normal
        mascotaPerro.setPeso(20.0); // Peso normal para perro
        mascotaPerro.setUsuario(usuarioTest);

        // Configurar mascota gato normal (no requiere atención especial)
        mascotaGato = new Mascota();
        mascotaGato.setNombre("Michi");
        mascotaGato.setTipo(TipoMascota.GATO);
        mascotaGato.setEdad(5); // Edad normal
        mascotaGato.setPeso(5.0); // Peso normal para gato
        mascotaGato.setUsuario(usuarioTest);
    }

    @Nested
    @DisplayName("Tests de Validación por Edad")
    class ValidacionPorEdad {

        @Test
        @DisplayName("No requiere atención - Edad normal (5 años)")
        void testEdadNormal_NoRequiereAtencion() {
            mascotaPerro.setEdad(5);
            mascotaPerro.setPeso(20.0); // Peso normal

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Una mascota de 5 años con peso normal NO debe requerir atención especial");
        }

        @Test
        @DisplayName("Requiere atención - Cachorro (< 1 año)")
        void testCachorro_RequiereAtencion() {
            mascotaPerro.setEdad(0); // Cachorro
            mascotaPerro.setPeso(20.0); // Peso normal

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Un cachorro (< 1 año) DEBE requerir atención especial");
        }

        @Test
        @DisplayName("Requiere atención - Mascota anciana (> 10 años)")
        void testAnciano_RequiereAtencion() {
            mascotaPerro.setEdad(12); // Anciano
            mascotaPerro.setPeso(20.0); // Peso normal

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Una mascota anciana (> 10 años) DEBE requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Edad en límite inferior (1 año)")
        void testEdadLimiteInferior_NoRequiereAtencion() {
            mascotaPerro.setEdad(1); // Límite inferior
            mascotaPerro.setPeso(20.0);

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Una mascota de exactamente 1 año NO debe requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Edad en límite superior (10 años)")
        void testEdadLimiteSuperior_NoRequiereAtencion() {
            mascotaPerro.setEdad(10); // Límite superior
            mascotaPerro.setPeso(20.0);

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Una mascota de exactamente 10 años NO debe requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Edad null")
        void testEdadNull_NoRequiereAtencion() {
            mascotaPerro.setEdad(null);
            mascotaPerro.setPeso(20.0);

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Si la edad es null, NO debe requerir atención especial");
        }
    }

    @Nested
    @DisplayName("Tests de Validación por Peso - PERRO")
    class ValidacionPorPesoPerro {

        @Test
        @DisplayName("Requiere atención - Perro con peso muy bajo (< 2 kg)")
        void testPerroPesoBajo_RequiereAtencion() {
            mascotaPerro.setEdad(5); // Edad normal
            mascotaPerro.setPeso(1.5); // Peso muy bajo

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Un perro con peso < 2kg DEBE requerir atención especial");
        }

        @Test
        @DisplayName("Requiere atención - Perro con sobrepeso (> 50 kg)")
        void testPerroPesoAlto_RequiereAtencion() {
            mascotaPerro.setEdad(5); // Edad normal
            mascotaPerro.setPeso(55.0); // Peso muy alto

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Un perro con peso > 50kg DEBE requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Perro peso en límite inferior (2 kg)")
        void testPerroPesoLimiteInferior_NoRequiereAtencion() {
            mascotaPerro.setEdad(5);
            mascotaPerro.setPeso(2.0); // Límite inferior

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Un perro con exactamente 2kg NO debe requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Perro peso en límite superior (50 kg)")
        void testPerroPesoLimiteSuperior_NoRequiereAtencion() {
            mascotaPerro.setEdad(5);
            mascotaPerro.setPeso(50.0); // Límite superior

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Un perro con exactamente 50kg NO debe requerir atención especial");
        }
    }

    @Nested
    @DisplayName("Tests de Validación por Peso - GATO")
    class ValidacionPorPesoGato {

        @Test
        @DisplayName("Requiere atención - Gato con peso muy bajo (< 2 kg)")
        void testGatoPesoBajo_RequiereAtencion() {
            mascotaGato.setEdad(5); // Edad normal
            mascotaGato.setPeso(1.5); // Peso muy bajo

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaGato);

            assertTrue(validador.necesitaAtencion(),
                    "Un gato con peso < 2kg DEBE requerir atención especial");
        }

        @Test
        @DisplayName("Requiere atención - Gato con sobrepeso (> 10 kg)")
        void testGatoPesoAlto_RequiereAtencion() {
            mascotaGato.setEdad(5); // Edad normal
            mascotaGato.setPeso(12.0); // Peso muy alto para un gato

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaGato);

            assertTrue(validador.necesitaAtencion(),
                    "Un gato con peso > 10kg DEBE requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Gato peso en límite inferior (2 kg)")
        void testGatoPesoLimiteInferior_NoRequiereAtencion() {
            mascotaGato.setEdad(5);
            mascotaGato.setPeso(2.0); // Límite inferior

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaGato);

            assertFalse(validador.necesitaAtencion(),
                    "Un gato con exactamente 2kg NO debe requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Gato peso en límite superior (10 kg)")
        void testGatoPesoLimiteSuperior_NoRequiereAtencion() {
            mascotaGato.setEdad(5);
            mascotaGato.setPeso(10.0); // Límite superior

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaGato);

            assertFalse(validador.necesitaAtencion(),
                    "Un gato con exactamente 10kg NO debe requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Peso null")
        void testPesoNull_NoRequiereAtencion() {
            mascotaGato.setEdad(5);
            mascotaGato.setPeso(null);

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaGato);

            assertFalse(validador.necesitaAtencion(),
                    "Si el peso es null, NO debe requerir atención especial");
        }
    }

    @Nested
    @DisplayName("Tests de Casos Combinados")
    class CasosCombinados {

        @Test
        @DisplayName("Requiere atención - Edad y peso problemáticos")
        void testEdadYPesoProblematicos_RequiereAtencion() {
            mascotaPerro.setEdad(0); // Cachorro
            mascotaPerro.setPeso(1.0); // Peso bajo

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Una mascota con edad Y peso problemáticos DEBE requerir atención especial");
        }

        @Test
        @DisplayName("Requiere atención - Solo edad problemática")
        void testSoloEdadProblematica_RequiereAtencion() {
            mascotaPerro.setEdad(12); // Anciano
            mascotaPerro.setPeso(20.0); // Peso normal

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Con solo la edad problemática DEBE requerir atención especial");
        }

        @Test
        @DisplayName("Requiere atención - Solo peso problemático")
        void testSoloPesoProblematico_RequiereAtencion() {
            mascotaPerro.setEdad(5); // Edad normal
            mascotaPerro.setPeso(55.0); // Peso alto

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertTrue(validador.necesitaAtencion(),
                    "Con solo el peso problemático DEBE requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Todo normal")
        void testTodoNormal_NoRequiereAtencion() {
            mascotaPerro.setEdad(5); // Edad normal
            mascotaPerro.setPeso(20.0); // Peso normal

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Una mascota con edad y peso normales NO debe requerir atención especial");
        }

        @Test
        @DisplayName("No requiere atención - Edad y peso null")
        void testEdadYPesoNull_NoRequiereAtencion() {
            mascotaPerro.setEdad(null);
            mascotaPerro.setPeso(null);

            ValidadorAtencionEspecial validador = new ValidadorAtencionEspecial(mascotaPerro);

            assertFalse(validador.necesitaAtencion(),
                    "Si edad y peso son null, NO debe requerir atención especial");
        }
    }
}
