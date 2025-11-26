package org.fiap.com.Repositories;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.util.List;

import org.fiap.com.Models.Feedback;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@QuarkusTest
class FeedbackRepositoryTest {

    @Inject
    FeedbackRepository repository;

//    private Feedback criarFeedback(int nota, LocalDate data) {
//        Feedback fb = new Feedback();
//        fb.setNota(nota);
//        fb.setCursoId(1L);
//        fb.setProfessorId(1L);
//        fb.setComentario("teste");
//        fb.setCategoria("geral");
//        fb.setAnonimo(false);
//
//        // Setando manualmente a data (mesmo com PrePersist para este caso de teste)
//        try {
//            var field = Feedback.class.getDeclaredField("dataPublicacao");
//            field.setAccessible(true);
//            field.set(fb, data);
//        } catch (Exception e) {
//            fail("Erro ao setar dataPublicacao via reflection");
//        }
//
//        return fb;
//    }

//    @Test
//    @Transactional
//    void testBuscarPorData() {
//        Feedback fb1 = criarFeedback(8, LocalDate.of(2024, 1, 10));
//        Feedback fb2 = criarFeedback(9, LocalDate.of(2024, 1, 12));
//        Feedback fb3 = criarFeedback(10, LocalDate.of(2024, 2, 1));
//
//        repository.persist(fb1);
//        repository.persist(fb2);
//        repository.persist(fb3);
//
//        List<Feedback> resultados = repository.buscarPorData(
//                LocalDate.of(2024, 1, 1),
//                LocalDate.of(2024, 1, 31)
//        );
//
//        assertEquals(2, resultados.size());
//        assertTrue(resultados.stream().anyMatch(f -> f.getNota() == 8));
//        assertTrue(resultados.stream().anyMatch(f -> f.getNota() == 9));
//    }
//
//    @Test
//    @Transactional
//    void testCalcularMediaPorData() {
//        Feedback fb1 = criarFeedback(8, LocalDate.of(2024, 1, 10));
//        Feedback fb2 = criarFeedback(10, LocalDate.of(2024, 1, 15));
//
//        repository.persist(fb1);
//        repository.persist(fb2);
//
//        double media = repository.calcularMediaPorData(
//                LocalDate.of(2024, 1, 1),
//                LocalDate.of(2024, 1, 31)
//        );
//
//        assertEquals(9.0, media);
//    }
//
//    @Test
//    @Transactional
//    void testCalcularMediaPorDataSemResultados() {
//        double media = repository.calcularMediaPorData(
//                LocalDate.of(2024, 5, 1),
//                LocalDate.of(2024, 5, 31)
//        );
//
//        assertEquals(0.0, media);
//    }
}
