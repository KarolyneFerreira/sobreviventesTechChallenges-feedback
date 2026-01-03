package org.fiap.com.services;

import org.fiap.com.dto.FeedbackResponse;
import org.fiap.com.repositories.FeedbackRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FeedbackServiceTest {

    @Mock
    FeedbackRepository repository;

    @Mock
    S3Client s3Client;

    FeedbackService service;

    LocalDate inicio;
    LocalDate fim;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        inicio = LocalDate.of(2025, 12, 1);
        fim = LocalDate.of(2025, 12, 7);

        service = new FeedbackService("corp-feedback-relatorios", repository, s3Client);
    }

    @Test
    void testGerarCsvSemanal_EnviaArquivoAoS3() {
        // Arrange
        FeedbackResponse f1 = new FeedbackResponse(5, "Precisa melhorar", LocalDateTime.of(2025, 12, 2, 10, 0));
        FeedbackResponse f2 = new FeedbackResponse(7, "Bom atendimento", LocalDateTime.of(2025, 12, 3, 11, 0));
        FeedbackResponse f3 = new FeedbackResponse(10, "Excelente!", LocalDateTime.of(2025, 12, 4, 12, 0));

        when(repository.buscarPorIntervalo(inicio, fim)).thenReturn(List.of(f1, f2, f3));

        // Act
        service.gerarCsvSemanal(inicio, fim);

        // Assert
        ArgumentCaptor<PutObjectRequest> requestCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> bodyCaptor = ArgumentCaptor.forClass(RequestBody.class);

        verify(s3Client, times(1)).putObject(requestCaptor.capture(), bodyCaptor.capture());

        PutObjectRequest request = requestCaptor.getValue();
        assertTrue(request.key().contains("relatorio-feedback"));
        assertEquals("corp-feedback-relatorios", request.bucket());
    }

    @Test
    void testGerarCsvSemanal_SemFeedbacks() {
        when(repository.buscarPorIntervalo(inicio, fim)).thenReturn(List.of());

        service.gerarCsvSemanal(inicio, fim);

        verify(s3Client, times(1)).putObject(any(PutObjectRequest.class), any(RequestBody.class));
    }

    @Test
    void testGerarCsvSemanal_RepositorioLancaExcecao() {
        when(repository.buscarPorIntervalo(inicio, fim)).thenThrow(new RuntimeException("DB error"));

        assertThrows(RuntimeException.class, () -> service.gerarCsvSemanal(inicio, fim));
    }
}
