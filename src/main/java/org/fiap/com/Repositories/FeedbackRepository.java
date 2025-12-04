package org.fiap.com.Repositories;

import org.fiap.com.Models.Feedback;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository {

    public List<Feedback> buscarPorData(LocalDate inicio, LocalDate fim) {
        List<Feedback> feedbacks = new ArrayList<>();

        feedbacks.add(new Feedback(8, "Comentário exemplo dentro do período"));
        feedbacks.add(new Feedback(10, "Outro comentário de teste"));

        System.out.println("⚠️ [FAKE] Retornando lista mockada de feedbacks entre " + inicio + " e " + fim);

        return feedbacks;
    }

    public double calcularMediaPorData(LocalDate inicio, LocalDate fim) {
        List<Feedback> feedbacks = buscarPorData(inicio, fim);

        return feedbacks.stream()
                .mapToInt(Feedback::getNota)
                .average()
                .orElse(0.0);
    }
}
