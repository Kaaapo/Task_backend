package com.taskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Entidad TipoProyecto
 * Representa los tipos de proyecto disponibles
 */
@Entity
@Table(name = "tipos_proyecto")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoProyecto {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(length = 500)
    private String descripcion;
    
    @Column(length = 7)
    private String color;

    @Column(length = 50)
    private String icono;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;
}
