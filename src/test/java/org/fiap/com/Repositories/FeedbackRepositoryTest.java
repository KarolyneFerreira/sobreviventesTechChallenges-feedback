package org.fiap.com.Repositories;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.fiap.com.Models.Feedback;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class FeedbackRepositoryTest {

    @Inject
    FeedbackRepository repository;

    @Test
    void testBuscarPorData() {
        // Arrange: cria e persiste alguns feedbacks
        Feedback f1 = new Feedback();
        f1.setNota(8);
        f1.setComentario("Bom atendimento");
        f1.setDataPublicacao();
        repository.persist(f1);

        Feedback f2 = new Feedback();
        f2.setNota(10);
        f2.setComentario("Excelente!");
        f2.setDataPublicacao();
        repository.persist(f2);

        Feedback f3 = new Feedback();
        f3.setNota(5);
        f3.setComentario("Precisa melhorar");
        f3.setDataPublicacao();
        repository.persist(f3);

        // Act: busca apenas dentro do intervalo
        List<Feedback> resultados = repository.buscarPorData(
                LocalDate.now(),
                LocalDate.now()
        );

        // Assert
        assertEquals(2, resultados.size());
        assertTrue(resultados.stream().anyMatch(f -> f.getComentario().equals("Bom atendimento")));
        assertTrue(resultados.stream().anyMatch(f -> f.getComentario().equals("Excelente!")));
    }

    @Test
    void testCalcularMediaPorData() {
        // Act: calcula média no intervalo
        Double media = repository.calcularMediaPorData(
                LocalDate.of(2025, 12, 1),
                LocalDate.of(2025, 12, 7)
        );

        // Assert: média dos feedbacks inseridos acima (8 e 10)
        assertEquals(9.0, media);
    }
}