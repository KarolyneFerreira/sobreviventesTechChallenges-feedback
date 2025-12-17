package org.fiap.com.Services;

import org.fiap.com.Models.Feedback;
import org.fiap.com.Repositories.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    FeedbackRepository repository;

    @Mock
    S3Client s3Client;

    @InjectMocks
    FeedbackService service;

    LocalDate inicio;
    LocalDate fim;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        inicio = LocalDate.of(2025, 12, 1);
        fim = LocalDate.of(2025, 12, 7);
    }

    @Test
    void testSumarizarPorNotas_GeraCsvEEnviaParaS3() {
        // Arrange: cria feedbacks simulados
        Feedback f1 = new Feedback();
        f1.setNota(5);
        f1.setComentario("Precisa melhorar");

        Feedback f2 = new Feedback();
        f2.setNota(7);
        f2.setComentario("Bom atendimento");

        Feedback f3 = new Feedback();
        f3.setNota(10);
        f3.setComentario("Excelente!");

        when(repository.buscarPorData(inicio, fim)).thenReturn(List.of(f1, f2, f3));

        // Act
        Map<String, Object> resultado = service.sumarizarPorNotas(inicio, fim);

        // Assert: verifica agrupamento e média
        assertEquals(3, resultado.get("quantidadeTotal"));
        assertEquals(7.33, (double) resultado.get("mediaSemanal"), 0.01);

        Map<String, List<String>> grupos = (Map<String, List<String>>) resultado.get("comentariosAgrupados");
        assertTrue(grupos.get("[0-6]").contains("Precisa melhorar"));
        assertTrue(grupos.get("[6-8]").contains("Bom atendimento"));
        assertTrue(grupos.get("[9-10]").contains("Excelente!"));

        // Verifica se o CSV foi enviado ao S3
        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> bodyCaptor = ArgumentCaptor.forClass(RequestBody.class);

        verify(s3Client, times(1)).putObject(requestCaptor.capture(), bodyCaptor.capture());

        PutObjectRequest request = requestCaptor.getValue();
        assertEquals("corp-feedback-relatorios-sem", request.bucket());
        assertTrue(request.key().contains("relatorio-feedback"));

        String conteudoCsv = bodyCaptor.getValue().contentStreamProvider().newStream().toString();
        assertNotNull(conteudoCsv);
    }
}