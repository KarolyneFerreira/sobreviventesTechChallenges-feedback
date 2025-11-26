package org.fiap.com.Repositories;

import org.fiap.com.Models.Feedback;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository implements PanacheRepository<Feedback> {

    public List<Feedback> buscarPorData(LocalDate inicio, LocalDate fim) {
        return list("dataPublicacao BETWEEN ?1 AND ?2", inicio, fim);
    }

    public double calcularMediaPorData(LocalDate inicio, LocalDate fim) {
        List<Feedback> feedbacks = buscarPorData(inicio, fim);

        return feedbacks.stream()
                .mapToInt(Feedback::getNota)
                .average()
                .orElse(0.0);
    }

}
