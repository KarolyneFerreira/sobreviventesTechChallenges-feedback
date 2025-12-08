package org.fiap.com.Services;


import org.fiap.com.Models.Feedback;
import org.fiap.com.Repositories.FeedbackRepository;

import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;

import com.opencsv.CSVWriter;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@ApplicationScoped
public class FeedbackService {

    private final FeedbackRepository repository = new FeedbackRepository();
    private final S3Client s3Client;

    public FeedbackService() {
        this.s3Client = S3Client.builder()
                .region(Region.US_EAST_1)
                .build();
    }

   private final String bucket = "corp-feedback-relatorios-sem";

    public Map<String, Object> sumarizarPorNotas(LocalDate inicio, LocalDate fim) {
        System.out.println("🔍 Iniciando sumarização de feedbacks...");
        System.out.println("📅 Intervalo: " + inicio + " até " + fim);

        List<Feedback> feedbacks = repository.buscarPorData(inicio, fim);
        System.out.println("📦 Feedbacks encontrados: " + feedbacks.size());

        Map<String, List<String>> grupos = new HashMap<>();
        grupos.put("[0-6]", new ArrayList<>());
        grupos.put("[6-8]", new ArrayList<>());
        grupos.put("[9-10]", new ArrayList<>());

        double soma = 0;
        int contador = 0;

        for (Feedback f : feedbacks) {
            if (f.getNota() == null) {
                System.out.println("⚠️ Feedback sem nota ignorado: " + f.getComentario());
                continue;
            }

            soma += f.getNota();
            contador++;

            if (f.getNota() <= 6) {
                grupos.get("[0-6]").add(f.getComentario());
            } else if (f.getNota() <= 8) {
                grupos.get("[6-8]").add(f.getComentario());
            } else {
                grupos.get("[9-10]").add(f.getComentario());
            }

            System.out.println("📝 Nota: " + f.getNota() + " | Comentário: " + f.getComentario());
        }

        double media = contador > 0 ? soma / contador : 0.0;
        System.out.println("📊 Média calculada: " + media);

        gerarCsv(grupos, inicio, fim, media);

        System.out.println("✅ Sumarização concluída.");

        return Map.of(
                "dataInicio", inicio,
                "dataFim", fim,
                "quantidadeTotal", feedbacks.size(),
                "mediaSemanal", media,
                "comentariosAgrupados", grupos
        );
    }

    public void testarConexaoS3() {
        try {
            String testeKey = "lambda-teste-conexao.txt";
            String conteudo = "Teste de conexão com S3 via Lambda";

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(testeKey)
                            .build(),
                    RequestBody.fromString(conteudo)
            );

            System.out.println("✅ Conexão com S3 funcionando! Arquivo de teste enviado: " + testeKey);
        } catch (Exception e) {
            System.out.println("❌ Erro ao acessar S3: " + e.getMessage());
        }
    }

    public FeedbackRepository getRepository() {
        return repository;
    }

    private void gerarCsv(Map<String, List<String>> grupos, LocalDate inicio, LocalDate fim, double media) {
        String nomeArquivo = String.format("relatorio-feedback-%s-a-%s.csv", inicio, fim);

        // Criar CSV em memória
        try (java.io.StringWriter sw = new java.io.StringWriter();
             CSVWriter writer = new CSVWriter(sw)) {

            // Cabeçalho
            writer.writeNext(new String[]{"Faixa de Notas", "Comentários"});

            // Linhas com comentários agrupados
            for (Map.Entry<String, List<String>> entry : grupos.entrySet()) {
                String faixa = entry.getKey();
                for (String comentario : entry.getValue()) {
                    writer.writeNext(new String[]{faixa, comentario});
                }
            }

            // Linha em branco e média semanal
            writer.writeNext(new String[]{""});
            writer.writeNext(new String[]{"Média da Semana", String.format(Locale.US, "%.2f", media)});

            writer.close();

            System.out.println("Antes do upload no S3...");
            // Envia CSV para S3
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(nomeArquivo)
                            .build(),
                    RequestBody.fromString(sw.toString())
            );
            System.out.println("Depois do upload no S3...");

            System.out.println("✅ CSV enviado para S3 com sucesso: " + nomeArquivo);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao gerar CSV: " + e.getMessage());
        }
    }

}
