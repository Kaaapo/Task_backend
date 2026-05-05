package com.taskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
@SQLDelete(sql = "UPDATE proyectos SET deleted_at = NOW() WHERE id = ?")
@SQLRestriction("deleted_at IS NULL")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Proyecto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String nombre;

    @Column(length = 1000)
    private String descripcion;

    @Column(length = 20, unique = true)
    private String codigo;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "tipo_proyecto_id", nullable = false)
    private TipoProyecto tipoProyecto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "estado_id", nullable = false)
    private Estado estado;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creador_id")
    private Usuario creador;

    @Column(length = 20)
    private String prioridad;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin_estimada")
    private LocalDate fechaFinEstimada;

    @Column(name = "fecha_fin_real")
    private LocalDate fechaFinReal;

    @Column(precision = 5, scale = 2)
    private BigDecimal progreso = BigDecimal.ZERO;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @PrePersist
    protected void onCreate() {
        this.fechaCreacion = LocalDateTime.now();
        if (this.progreso == null) this.progreso = BigDecimal.ZERO;
    }
}
