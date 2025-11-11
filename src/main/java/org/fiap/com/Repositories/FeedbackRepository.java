package org.fiap.com.Repositories;

import org.fiap.com.Models.Feedback;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository implements PanacheRepository<Feedback> {

    public List<Feedback> buscarPorData(LocalDate inicio, LocalDate fim) {
        return find("dataPublicacao BETWEEN ?1 AND ?2", inicio, fim).list();
    }

    public Double calcularMediaPorData(LocalDate inicio, LocalDate fim) {
        Object result = getEntityManager()
        .createQuery("SELECT AVG(f.nota) FROM Feedback f WHERE f.dataPublicacao BETWEEN :inicio AND :fim")
        .setParameter("inicio", inicio)
        .setParameter("fim", fim)
        .getSingleResult();

    return result != null ? ((Number) result).doubleValue() : 0.0;
    }

}
