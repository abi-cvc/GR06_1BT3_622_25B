package com.gestion.mascotas.util;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;

@WebListener
public class HibernateListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("===========================================");
        System.out.println("Inicializando Sistema de Gestión de Mascotas");
        System.out.println("===========================================");

        try {
            // Inicializar Hibernate
            HibernateUtil.getSessionFactory();
            System.out.println("✓ Hibernate inicializado correctamente");
            System.out.println("✓ Conexión a base de datos establecida");
        } catch (Exception e) {
            System.err.println("✗ Error al inicializar Hibernate:");
            e.printStackTrace();
        }

        System.out.println("===========================================");
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("===========================================");
        System.out.println("Cerrando Sistema de Gestión de Mascotas");
        System.out.println("===========================================");

        try {
            HibernateUtil.shutdown();
            System.out.println("✓ Hibernate cerrado correctamente");
        } catch (Exception e) {
            System.err.println("✗ Error al cerrar Hibernate:");
            e.printStackTrace();
        }

        System.out.println("===========================================");
    }
}