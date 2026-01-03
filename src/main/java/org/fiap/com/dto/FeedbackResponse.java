package org.fiap.com.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class FeedbackResponse {

    private final int nota;
    private final String comentario;
    private final LocalDateTime dataCriacao;

    public FeedbackResponse(int nota, String comentario, LocalDateTime dataCriacao) {
        this.nota = nota;
        this.comentario = comentario;
        this.dataCriacao = dataCriacao;
    }

}
