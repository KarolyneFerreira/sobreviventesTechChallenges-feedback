package org.fiap.com.Models;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long aulaId;

    private Long cursoId;
    private Long professorId;
    private Integer nota;
    private String comentario;
    private String categoria;
    private Boolean anonimo;
    private LocalDate dataPublicacao;

    // Getters e Setters
    public Long getAulaId() {
        return aulaId;
    }

    public void setAulaId(Long id) {
        this.aulaId = id;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String titulo) {
        this.comentario = titulo;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String conteudo) {
        this.categoria = conteudo;
    }

    public LocalDate getDataPublicacao() {
        return dataPublicacao;
    }

    public void setDataPublicacao() {
        this.dataPublicacao = dataPublicacao.now();
    }

    public Long getCursoId() {
        return cursoId;
    }

    public void setCursoId(Long cursoId) {
        this.cursoId = cursoId;
    }

    public Long getProfessorId() {
        return professorId;
    }

    public void setProfessorId(Long professorId) {
        this.professorId = professorId;
    }

    public Integer getNota() {
        return nota;
    }

    public void setNota(Integer nota) {
        this.nota = nota;
    }

    public Boolean getAnonimo() {
        return anonimo;
    }

    public void setAnonimo(Boolean anonimo) {
        this.anonimo = anonimo;
    }
}