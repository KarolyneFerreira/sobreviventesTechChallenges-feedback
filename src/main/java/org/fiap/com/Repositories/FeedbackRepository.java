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

    public void listarTabelas() {
        String url = "jdbc:postgresql://agendamento-db.cfy4w0m60ie2.us-east-1.rds.amazonaws.com:5432/agendamento";
        String usuario = "postgres";
        String senha = "postgres";

        String sql = "SELECT table_name FROM information_schema.tables WHERE table_schema = 'public'";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, usuario, senha);
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            System.out.println("✅ Tabelas encontradas no schema 'public':");

            while (rs.next()) {
                System.out.println("📌 " + rs.getString("table_name"));
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao listar tabelas: " + e.getMessage());
        }
    }

    public void testarSelect(String tabela) {
        String url = "jdbc:postgresql://agendamento-db.cfy4w0m60ie2.us-east-1.rds.amazonaws.com:5432/agendamento";
        String usuario = "postgres";
        String senha = "postgres";

        String sql = "SELECT * FROM " + tabela + " LIMIT 5";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, usuario, senha);
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            System.out.println("✅ SELECT na tabela '" + tabela + "' retornou:");

            int colunas = rs.getMetaData().getColumnCount();

            while (rs.next()) {
                StringBuilder linha = new StringBuilder("➡ ");
                for (int i = 1; i <= colunas; i++) {
                    linha.append(rs.getMetaData().getColumnName(i))
                            .append("=")
                            .append(rs.getString(i))
                            .append(" ");
                }
                System.out.println(linha);
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao consultar tabela '" + tabela + "': " + e.getMessage());
        }
    }

    public void listarSchemas() {
        String url = "jdbc:postgresql://agendamento-db.cfy4w0m60ie2.us-east-1.rds.amazonaws.com:5432/agendamento";
        String usuario = "postgres";
        String senha = "postgres";

        String sql = "SELECT schema_name FROM information_schema.schemata";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, usuario, senha);
             java.sql.PreparedStatement stmt = conn.prepareStatement(sql);
             java.sql.ResultSet rs = stmt.executeQuery()) {

            System.out.println("✅ Schemas encontrados:");

            while (rs.next()) {
                System.out.println("📌 " + rs.getString("schema_name"));
            }

        } catch (Exception e) {
            System.out.println("❌ Erro ao listar schemas: " + e.getMessage());
        }
    }


    public void testarConexaoRDS() {
        String url = "jdbc:postgresql://agendamento-db.cfy4w0m60ie2.us-east-1.rds.amazonaws.com:5432/agendamento";
        String usuario = "postgres";
        String senha = "postgres";

        try (java.sql.Connection conn = java.sql.DriverManager.getConnection(url, usuario, senha)) {
            System.out.println("✅ Conexão com RDS estabelecida com sucesso!");
        } catch (Exception e) {
            System.out.println("❌ Erro ao conectar com RDS: " + e.getMessage());
        }
    }

}
