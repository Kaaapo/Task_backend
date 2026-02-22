package com.taskmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Aplicación principal de Task Manager Backend
 * 
 * @author Task Manager Team
 * @version 1.0
 */
@SpringBootApplication
public class TaskManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(TaskManagerApplication.class, args);
        System.out.println("===========================================");
        System.out.println("Task Manager Backend iniciado correctamente");
        System.out.println("API disponible en: http://localhost:8080/api");
        System.out.println("===========================================");
    }
}
