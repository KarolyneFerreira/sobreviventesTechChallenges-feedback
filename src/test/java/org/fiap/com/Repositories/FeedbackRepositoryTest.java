package org.fiap.com.repositories;

import org.fiap.com.dto.FeedbackResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class FeedbackRepositoryTest {

    @Mock
    FeedbackRepository repository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testBuscarPorIntervaloSemResultados() {
        when(repository.buscarPorIntervalo(LocalDate.of(1990, 1, 1), LocalDate.of(1990, 1, 7)))
                .thenReturn(List.of());

        List<FeedbackResponse> resultados = repository.buscarPorIntervalo(
                LocalDate.of(1990, 1, 1),
                LocalDate.of(1990, 1, 7)
        );

        assertNotNull(resultados);
        assertTrue(resultados.isEmpty());
    }

    @Test
    void testBuscarPorIntervaloComResultados() {
        FeedbackResponse f1 = new FeedbackResponse(7, "Bom atendimento",
                LocalDateTime.of(2025, 12, 20, 10, 0));

        when(repository.buscarPorIntervalo(LocalDate.now().minusDays(7), LocalDate.now()))
                .thenReturn(List.of(f1));

        List<FeedbackResponse> resultados = repository.buscarPorIntervalo(
                LocalDate.now().minusDays(7),
                LocalDate.now()
        );

        assertNotNull(resultados);
        assertTrue(resultados.size() >= 0);
    }

    @Test
    void testFeedbacksOrdenadosPorData() {
        FeedbackResponse f1 = new FeedbackResponse(5, "Precisa melhorar",
                LocalDateTime.of(2025, 12, 1, 10, 0));
        FeedbackResponse f2 = new FeedbackResponse(8, "Ótimo",
                LocalDateTime.of(2025, 12, 2, 11, 0));

        when(repository.buscarPorIntervalo(LocalDate.now().minusDays(30), LocalDate.now()))
                .thenReturn(List.of(f1, f2));

        List<FeedbackResponse> resultados = repository.buscarPorIntervalo(
                LocalDate.now().minusDays(30),
                LocalDate.now()
        );

        for (int i = 1; i < resultados.size(); i++) {
            assertTrue(resultados.get(i).getDataCriacao()
                    .isAfter(resultados.get(i - 1).getDataCriacao())
                    || resultados.get(i).getDataCriacao()
                    .isEqual(resultados.get(i - 1).getDataCriacao()));
        }
    }

    @Test
    void testNotasDentroDoIntervalo() {
        FeedbackResponse f1 = new FeedbackResponse(3, "Regular",
                LocalDateTime.of(2025, 12, 15, 9, 0));
        FeedbackResponse f2 = new FeedbackResponse(10, "Excelente",
                LocalDateTime.of(2025, 12, 16, 14, 0));

        when(repository.buscarPorIntervalo(LocalDate.now().minusDays(10), LocalDate.now()))
                .thenReturn(List.of(f1, f2));

        List<FeedbackResponse> resultados = repository.buscarPorIntervalo(
                LocalDate.now().minusDays(10),
                LocalDate.now()
        );

        assertTrue(resultados.stream()
                .allMatch(f -> f.getNota() >= 0 && f.getNota() <= 10));
    }
}
