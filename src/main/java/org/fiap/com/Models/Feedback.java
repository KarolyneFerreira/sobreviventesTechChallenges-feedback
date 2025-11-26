package org.fiap.com.Models;

import lombok.Data;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Data
public class Feedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long cursoId;
    private Long professorId;
    private Integer nota;
    private String comentario;
    private String categoria;
    private Boolean anonimo;
    private LocalDate dataPublicacao;

    @PrePersist
    public void prePersist() {
        this.dataPublicacao = LocalDate.now();
    }
}
