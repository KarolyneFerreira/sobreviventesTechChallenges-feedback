package org.fiap.com.Dto;

import java.time.LocalDateTime;

public class FeedbackResponse {

    private int nota;
    private String comentario;
    private LocalDateTime dataCriacao;

    public FeedbackResponse(int nota, String comentario, LocalDateTime dataCriacao) {
        this.nota = nota;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao;
    }

    public int getNota() {
        return nota;
    }

    public String getComentario() {
        return comentario;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }
}
