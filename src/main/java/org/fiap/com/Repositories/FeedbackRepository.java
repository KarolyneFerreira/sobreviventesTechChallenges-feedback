package org.fiap.com.Repositories;

import jakarta.enterprise.context.ApplicationScoped;
import org.fiap.com.Dto.FeedbackResponse;
import org.jboss.logging.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class FeedbackRepository {

    private static final Logger LOG = Logger.getLogger(FeedbackRepository.class);

    private static final String URL =
            "jdbc:postgresql://feedback-service.cx6ycoocwna7.us-east-2.rds.amazonaws.com:5432/postgres?sslmode=require";

    private static final String USER = "postgres";
    private static final String PASSWORD = "postgres";

    // 🔥 BUSCA REAL POR INTERVALO
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

        try (Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
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
            throw new RuntimeException(e);
        }

        return feedbacks;
    }
}
