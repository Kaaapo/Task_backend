package com.taskmanager.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "miembros_empresa", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"usuario_id", "empresa_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MiembroEmpresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "empresa_id", nullable = false)
    private Empresa empresa;

    @Column(nullable = false, length = 20)
    private String rol = "MIEMBRO";

    @Column(name = "fecha_union")
    private LocalDateTime fechaUnion;

    @Column(nullable = false)
    private Boolean activo = true;

    @PrePersist
    protected void onCreate() {
        this.fechaUnion = LocalDateTime.now();
        if (this.activo == null) this.activo = true;
        if (this.rol == null) this.rol = "MIEMBRO";
    }
}
