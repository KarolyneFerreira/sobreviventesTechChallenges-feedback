package org.fiap.com.repositories;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.fiap.com.dto.FeedbackResponse;
import org.fiap.com.exception.FeedbackNotFoundException;
import org.jboss.logging.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository {

    private static final Logger LOG = Logger.getLogger(FeedbackRepository.class);

    private final String url;
    private final String user;
    private final String password;

    @Inject
    public FeedbackRepository(
            @ConfigProperty(name = "db.url") String url,
            @ConfigProperty(name = "db.user") String user,
            @ConfigProperty(name = "db.password") String password) {
        this.url = url;
        this.user = user;
        this.password = password;
    }

    public List<FeedbackResponse> buscarPorIntervalo(LocalDate inicio, LocalDate fim) {

        List<FeedbackResponse> feedbacks = new ArrayList<>();

        String sql = """
            SELECT nota, comentario, data_criacao
            FROM feedbacks
            WHERE data_criacao BETWEEN ? AND ?
            ORDER BY data_criacao
        """;

        LOG.infof("🧾 SQL: %s", sql);
        LOG.infof("📅 Intervalo: %s -> %s", inicio, fim);

        try (Connection conn = DriverManager.getConnection(url, user, password);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setTimestamp(1, Timestamp.valueOf(inicio.atStartOfDay()));
            stmt.setTimestamp(2, Timestamp.valueOf(fim.plusDays(1).atStartOfDay()));

            try (ResultSet rs = stmt.executeQuery()) {

                int count = 0;

                while (rs.next()) {
                    count++;

                    feedbacks.add(
                            new FeedbackResponse(
                                    rs.getInt("nota"),
                                    rs.getString("comentario"),
                                    rs.getTimestamp("data_criacao").toLocalDateTime()
                            )
                    );
                }

                LOG.infof("📊 Total retornado do banco: %d", count);
            }

        } catch (SQLException e) {
            LOG.error("❌ Erro ao consultar feedbacks no RDS", e);
            throw new FeedbackNotFoundException("❌ Erro ao consultar feedbacks no RDS");
        }

        return feedbacks;
    }
}
