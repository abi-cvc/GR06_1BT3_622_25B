package com.gestion.mascotas.util;

import com.gestion.mascotas.modelo.entidades.Sugerencia;
import com.gestion.mascotas.modelo.entidades.SugerenciaAlimentacion;
import com.gestion.mascotas.modelo.entidades.SugerenciaEjercicio;
import com.gestion.mascotas.modelo.enums.NivelActividad;
import com.gestion.mascotas.modelo.enums.TipoMascota;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.hibernate.Session;
import org.hibernate.Transaction;

@WebListener
public class DataLoaderListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("===========================================");
        System.out.println("Verificando datos iniciales en la base de datos...");
        System.out.println("===========================================");

        Session session = null;
        Transaction transaction = null;

        try {
            session = HibernateUtil.getSession();
            transaction = session.beginTransaction();

            // Verificar si existen datos en la tabla sugerencias
            Long countSugerencias = session.createQuery(
                    "SELECT COUNT(s) FROM Sugerencia s", Long.class
            ).getSingleResult();

            if (countSugerencias == 0) {
                System.out.println("⚙ Cargando datos iniciales en tabla 'sugerencias'...");
                cargarSugerencias(session);
                System.out.println("✓ Datos de sugerencias cargados correctamente");
            } else {
                System.out.println("✓ Tabla 'sugerencias' ya contiene datos (" + countSugerencias + " registros)");
            }

            // Verificar si existen datos en sugerencias_alimentacion
            Long countAlimentacion = session.createQuery(
                    "SELECT COUNT(sa) FROM SugerenciaAlimentacion sa", Long.class
            ).getSingleResult();

            if (countAlimentacion == 0) {
                System.out.println("⚙ Cargando datos iniciales en tabla 'sugerencias_alimentacion'...");
                cargarSugerenciasAlimentacion(session);
                System.out.println("✓ Datos de alimentación cargados correctamente");
            } else {
                System.out.println("✓ Tabla 'sugerencias_alimentacion' ya contiene datos (" + countAlimentacion + " registros)");
            }

            // Verificar si existen datos en sugerencias_ejercicio
            Long countEjercicio = session.createQuery(
                    "SELECT COUNT(se) FROM SugerenciaEjercicio se", Long.class
            ).getSingleResult();

            if (countEjercicio == 0) {
                System.out.println("⚙ Cargando datos iniciales en tabla 'sugerencias_ejercicio'...");
                cargarSugerenciasEjercicio(session);
                System.out.println("✓ Datos de ejercicio cargados correctamente");
            } else {
                System.out.println("✓ Tabla 'sugerencias_ejercicio' ya contiene datos (" + countEjercicio + " registros)");
            }

            transaction.commit();
            System.out.println("===========================================");
            System.out.println("✓ Carga de datos iniciales completada");
            System.out.println("===========================================");

        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            System.err.println("✗ Error al cargar datos iniciales:");
            e.printStackTrace();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    private void cargarSugerencias(Session session) {
        // Datos de alimentación (1-25)
        crearSugerencia(session, TipoMascota.PERRO, "Labrador Retriever", 2, 5, 20.0, 35.0,
                "Labradores adultos requieren dieta balanceada rica en proteínas y baja en grasa.", "Guía Canina 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Bulldog Francés", 1, 7, 8.0, 14.0,
                "Bulldogs franceses tienden a ganar peso, se recomienda comida controlada en grasa.", "Asociación Veterinaria 2023");

        crearSugerencia(session, TipoMascota.PERRO, "Golden Retriever", 1, 8, 25.0, 35.0,
                "Golden Retrievers necesitan dieta con Omega-3 para el pelo y articulaciones.", "NutriPet 2025");

        crearSugerencia(session, TipoMascota.PERRO, "Chihuahua", 1, 12, 1.0, 3.0,
                "Chihuahuas deben comer pequeñas porciones frecuentes.", "PequeñosCanes 2022");

        crearSugerencia(session, TipoMascota.PERRO, "Beagle", 1, 10, 9.0, 14.0,
                "Los beagles requieren alimento bajo en calorías para evitar obesidad.", "VetWorld 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Pastor Alemán", 2, 10, 25.0, 40.0,
                "Pastores Alemanes requieren comida rica en proteínas para mantener masa muscular.", "GermanShepherdCare");

        crearSugerencia(session, TipoMascota.PERRO, "Pug", 1, 10, 6.0, 10.0,
                "Pugs deben consumir comida hipoalergénica para evitar problemas respiratorios.", "DogCare 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Boxer", 1, 8, 20.0, 35.0,
                "Boxers requieren dieta con antioxidantes para prevenir problemas cardíacos.", "HeartVet 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Dálmata", 2, 10, 18.0, 25.0,
                "Dálmatas deben evitar alimentos ricos en purinas.", "Guía de Salud Animal");

        crearSugerencia(session, TipoMascota.PERRO, "Husky Siberiano", 2, 10, 20.0, 30.0,
                "Huskies deben tener dieta energética rica en proteínas.", "SnowPets 2023");

        crearSugerencia(session, TipoMascota.PERRO, null, 1, 15, 5.0, 50.0,
                "Perros adultos deben tener dieta con balance de proteínas, grasas y carbohidratos.", "GeneralVet 2025");

        crearSugerencia(session, TipoMascota.PERRO, null, 0, 1, 1.0, 15.0,
                "Cachorros requieren alimento con calcio y proteínas.", "PuppyCare 2024");

        crearSugerencia(session, TipoMascota.PERRO, null, 8, 20, 5.0, 50.0,
                "Perros mayores deben tener dieta blanda y baja en grasa.", "SeniorDogCare 2023");

        crearSugerencia(session, TipoMascota.GATO, "Persa", 1, 10, 3.0, 6.0,
                "Gatos Persas deben tener alimento con ácidos grasos esenciales.", "CatCare 2025");

        crearSugerencia(session, TipoMascota.GATO, "Siames", 1, 12, 3.0, 6.0,
                "Siamés requiere dieta rica en taurina y proteínas.", "FelineHealth 2024");

        crearSugerencia(session, TipoMascota.GATO, "Maine Coon", 2, 10, 5.0, 10.0,
                "Maine Coon necesita alto contenido calórico por su tamaño.", "BigCats Vet");

        crearSugerencia(session, TipoMascota.GATO, "Bengala", 1, 10, 4.0, 8.0,
                "Bengala requiere alimentación alta en proteínas y baja en carbohidratos.", "WildCat Guide");

        crearSugerencia(session, TipoMascota.GATO, "Esfinge", 1, 12, 3.0, 5.0,
                "El gato esfinge necesita más calorías para mantener su temperatura corporal.", "VetSkin 2025");

        crearSugerencia(session, TipoMascota.GATO, null, 0, 1, 1.0, 4.0,
                "Gatitos requieren alimento húmedo y rico en calcio.", "KittenCare");

        crearSugerencia(session, TipoMascota.GATO, null, 1, 7, 2.0, 6.0,
                "Gatos adultos deben comer 2-3 veces al día.", "FelineCare 2023");

        crearSugerencia(session, TipoMascota.GATO, null, 7, 20, 2.0, 6.0,
                "Gatos mayores deben consumir alimento con glucosamina y antioxidantes.", "SeniorCat 2024");

        crearSugerencia(session, TipoMascota.GATO, "British Shorthair", 1, 12, 4.0, 8.0,
                "British Shorthair tiende al sobrepeso, dieta ligera recomendada.", "PetBalance 2025");

        crearSugerencia(session, TipoMascota.GATO, "Ragdoll", 1, 10, 4.0, 9.0,
                "Ragdoll requiere dieta rica en proteínas y baja en grasa.", "VetNutrition");

        crearSugerencia(session, TipoMascota.PERRO, "Border Collie", 1, 10, 15.0, 25.0,
                "Border Collie necesita dieta energética para soportar actividad intensa.", "AgilityCan 2023");

        crearSugerencia(session, TipoMascota.GATO, "Abisinio", 1, 12, 3.0, 6.0,
                "Abisinio necesita alto contenido de proteína magra.", "FelineAthletic");

        // Datos de ejercicio (26-50)
        crearSugerencia(session, TipoMascota.PERRO, "Labrador Retriever", 2, 10, 20.0, 35.0,
                "Requiere caminatas diarias de 45–60 minutos y juegos acuáticos.", "DogFitness 2025");

        crearSugerencia(session, TipoMascota.PERRO, "Bulldog Francés", 1, 8, 8.0, 14.0,
                "Ejercicio moderado y paseos cortos para evitar sobrecalentamiento.", "VetFit 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Golden Retriever", 1, 10, 25.0, 35.0,
                "Requiere al menos una hora diaria de actividad moderada.", "GoldenGuide");

        crearSugerencia(session, TipoMascota.PERRO, "Chihuahua", 1, 12, 1.0, 3.0,
                "Juegos cortos en interiores son suficientes.", "TinyDogFit");

        crearSugerencia(session, TipoMascota.PERRO, "Beagle", 1, 10, 9.0, 14.0,
                "Necesita ejercicio diario para evitar ansiedad.", "BeagleWorld");

        crearSugerencia(session, TipoMascota.PERRO, "Pastor Alemán", 2, 10, 25.0, 40.0,
                "Necesita ejercicio intenso diario y entrenamiento mental.", "CanineTrainer");

        crearSugerencia(session, TipoMascota.PERRO, "Pug", 1, 10, 6.0, 10.0,
                "Paseos cortos, evitar calor extremo.", "PugCare 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Boxer", 1, 8, 20.0, 35.0,
                "Ejercicio intenso diario de 45 min.", "MuscleDog 2024");

        crearSugerencia(session, TipoMascota.PERRO, "Husky Siberiano", 2, 10, 20.0, 30.0,
                "Correr y pasear largas distancias a diario.", "SnowDogFit");

        crearSugerencia(session, TipoMascota.PERRO, "Dálmata", 2, 10, 18.0, 25.0,
                "Requiere ejercicio vigoroso para evitar hiperactividad.", "DalmatianCare");

        crearSugerencia(session, TipoMascota.PERRO, null, 0, 1, 1.0, 15.0,
                "Cachorros: juegos suaves y cortos.", "PuppyEnergy");

        crearSugerencia(session, TipoMascota.PERRO, null, 8, 20, 5.0, 50.0,
                "Perros mayores: paseos suaves de 20 minutos.", "SeniorDogFit");

        crearSugerencia(session, TipoMascota.GATO, "Persa", 1, 12, 3.0, 6.0,
                "Juego moderado diario con pelotas o cañas.", "CatFitness");

        crearSugerencia(session, TipoMascota.GATO, "Siames", 1, 12, 3.0, 6.0,
                "Actividades interactivas de 20 minutos diarios.", "FelineActive");

        crearSugerencia(session, TipoMascota.GATO, "Maine Coon", 1, 10, 5.0, 10.0,
                "Necesita espacios grandes y juguetes resistentes.", "BigCatExercise");

        crearSugerencia(session, TipoMascota.GATO, "Bengala", 1, 10, 4.0, 8.0,
                "Requiere actividad intensa y juegos de caza.", "BengalCare");

        crearSugerencia(session, TipoMascota.GATO, "Esfinge", 1, 12, 3.0, 5.0,
                "Ejercicio suave en interiores, evitar frío.", "VetSkin 2024");

        crearSugerencia(session, TipoMascota.GATO, null, 0, 1, 1.0, 3.0,
                "Gatitos: juegos frecuentes y suaves.", "KittenPlay");

        crearSugerencia(session, TipoMascota.GATO, null, 1, 7, 2.0, 6.0,
                "Adultos: 15–30 minutos diarios de juego.", "CatCare");

        crearSugerencia(session, TipoMascota.GATO, null, 7, 20, 2.0, 6.0,
                "Mayores: juegos mentales ligeros.", "SeniorCat");

        crearSugerencia(session, TipoMascota.GATO, "British Shorthair", 1, 12, 4.0, 8.0,
                "Ejercicio moderado con juguetes interactivos.", "PetBalance");

        crearSugerencia(session, TipoMascota.GATO, "Ragdoll", 1, 10, 4.0, 9.0,
                "Prefiere juegos suaves y compañía humana.", "FelineWellness");

        crearSugerencia(session, TipoMascota.PERRO, "Border Collie", 1, 10, 15.0, 25.0,
                "Alta actividad física y mental diaria.", "AgilityCan");

        crearSugerencia(session, TipoMascota.GATO, "Abisinio", 1, 12, 3.0, 6.0,
                "Muy activo, necesita trepar y jugar.", "AthleticCats");

        crearSugerencia(session, TipoMascota.PERRO, null, 1, 15, 5.0, 40.0,
                "Todos los perros deben salir al menos 30 min diarios.", "GeneralVet 2024");
    }

    private void crearSugerencia(Session session, TipoMascota tipo, String raza,
                                 int edadMin, int edadMax, double pesoMin, double pesoMax,
                                 String descripcion, String fuente) {
        Sugerencia sugerencia = new Sugerencia();
        sugerencia.setTipoMascota(tipo);
        sugerencia.setRaza(raza);
        sugerencia.setEdadMin(edadMin);
        sugerencia.setEdadMax(edadMax);
        sugerencia.setPesoMin(pesoMin);
        sugerencia.setPesoMax(pesoMax);
        sugerencia.setDescripcion(descripcion);
        sugerencia.setFuente(fuente);
        session.persist(sugerencia);
    }

    private void cargarSugerenciasAlimentacion(Session session) {
        String[][] datos = {
                {"1", "Pienso balanceado", "2 veces al día", "1200"},
                {"2", "Comida baja en grasa", "2 veces al día", "800"},
                {"3", "Pienso con Omega-3", "2 veces al día", "1000"},
                {"4", "Comida seca", "3 veces al día", "300"},
                {"5", "Comida light", "2 veces al día", "700"},
                {"6", "Pienso de alto rendimiento", "2 veces al día", "1300"},
                {"7", "Pienso hipoalergénico", "2 veces al día", "650"},
                {"8", "Comida seca premium", "2 veces al día", "1200"},
                {"9", "Comida baja en purinas", "2 veces al día", "950"},
                {"10", "Pienso energético", "2 veces al día", "1250"},
                {"11", "Pienso estándar", "2 veces al día", "900"},
                {"12", "Pienso para cachorros", "3 veces al día", "400"},
                {"13", "Comida blanda", "2 veces al día", "600"},
                {"14", "Comida con ácidos grasos", "2 veces al día", "250"},
                {"15", "Comida rica en taurina", "2 veces al día", "300"},
                {"16", "Comida calórica alta", "2 veces al día", "350"},
                {"17", "Comida proteica", "2 veces al día", "400"},
                {"18", "Comida calórica", "3 veces al día", "350"},
                {"19", "Comida húmeda", "3 veces al día", "200"},
                {"20", "Pienso adulto", "2 veces al día", "300"},
                {"21", "Comida con glucosamina", "2 veces al día", "280"},
                {"22", "Comida ligera", "2 veces al día", "270"},
                {"23", "Comida proteica", "2 veces al día", "350"},
                {"24", "Pienso energético", "2 veces al día", "1100"},
                {"25", "Comida rica en proteínas", "2 veces al día", "350"}
        };

        for (String[] dato : datos) {
            SugerenciaAlimentacion sa = new SugerenciaAlimentacion();
            sa.setId(Long.parseLong(dato[0]));
            sa.setTipoComida(dato[1]);
            sa.setFrecuencia(dato[2]);
            sa.setCaloriasRecomendadas(Integer.parseInt(dato[3]));
            session.persist(sa);
        }
    }

    private void cargarSugerenciasEjercicio(Session session) {
        Object[][] datos = {
                {26L, "Caminatas y natación", 60, NivelActividad.ALTA},
                {27L, "Paseos cortos", 20, NivelActividad.BAJA},
                {28L, "Juegos y caminatas", 60, NivelActividad.MEDIA},
                {29L, "Juegos interiores", 15, NivelActividad.BAJA},
                {30L, "Paseos largos", 45, NivelActividad.MEDIA},
                {31L, "Entrenamiento y juegos", 60, NivelActividad.ALTA},
                {32L, "Paseos ligeros", 15, NivelActividad.BAJA},
                {33L, "Ejercicio intenso", 45, NivelActividad.ALTA},
                {34L, "Carrera y paseo", 90, NivelActividad.ALTA},
                {35L, "Ejercicio vigoroso", 60, NivelActividad.ALTA},
                {36L, "Juegos suaves", 10, NivelActividad.BAJA},
                {37L, "Paseos suaves", 20, NivelActividad.BAJA},
                {38L, "Juegos con caña", 15, NivelActividad.MEDIA},
                {39L, "Juegos interactivos", 20, NivelActividad.MEDIA},
                {40L, "Juguetes resistentes", 30, NivelActividad.MEDIA},
                {41L, "Juegos de caza", 25, NivelActividad.ALTA},
                {42L, "Ejercicio interior suave", 20, NivelActividad.BAJA},
                {43L, "Juegos suaves", 15, NivelActividad.BAJA},
                {44L, "Juegos activos", 30, NivelActividad.MEDIA},
                {45L, "Juegos mentales", 15, NivelActividad.BAJA},
                {46L, "Juguetes interactivos", 25, NivelActividad.MEDIA},
                {47L, "Compañía y juego", 20, NivelActividad.BAJA},
                {48L, "Agility y entrenamiento", 60, NivelActividad.ALTA},
                {49L, "Trepar y saltar", 30, NivelActividad.ALTA},
                {50L, "Paseo diario", 30, NivelActividad.MEDIA}
        };

        for (Object[] dato : datos) {
            SugerenciaEjercicio se = new SugerenciaEjercicio();
            se.setId((Long) dato[0]);
            se.setTipoEjercicio((String) dato[1]);
            se.setDuracionMinutos((Integer) dato[2]);
            se.setNivelActividad((NivelActividad) dato[3]);
            session.persist(se);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // No se necesita ninguna acción al destruir el contexto
    }
}