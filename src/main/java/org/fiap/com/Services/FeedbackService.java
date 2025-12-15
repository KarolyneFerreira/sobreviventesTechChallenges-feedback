package org.fiap.com.Services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.fiap.com.Dto.FeedbackResponse;
import org.fiap.com.Repositories.FeedbackRepository;
import org.jboss.logging.Logger;

import com.opencsv.CSVWriter;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Locale;

@ApplicationScoped
public class FeedbackService {

    private static final Logger LOG = Logger.getLogger(FeedbackService.class);

    private static final String BUCKET = "corp-feedback-relatorios";

    @Inject
    FeedbackRepository repository;

    @Inject
    S3Client s3Client;

    public String gerarCsvSemanal(LocalDate inicio, LocalDate fim) {

        LOG.info("🔍 Buscando feedbacks reais no RDS");

        List<FeedbackResponse> feedbacks =
                repository.buscarPorIntervalo(inicio, fim);

        LOG.infof("📊 Registros recebidos: %d", feedbacks.size());

        String nomeArquivo = String.format(
                "relatorio-feedback-%s-a-%s.csv", inicio, fim
        );

        try (var sw = new java.io.StringWriter();
             var writer = new CSVWriter(sw)) {

            writer.writeNext(new String[]{"Nota", "Comentário", "Data Criação"});

            for (FeedbackResponse f : feedbacks) {
                writer.writeNext(new String[]{
                        String.valueOf(f.getNota()),
                        f.getComentario(),
                        f.getDataCriacao().toString()
                });
            }

            double media = feedbacks.stream()
                    .mapToInt(FeedbackResponse::getNota)
                    .average()
                    .orElse(0.0);

            writer.writeNext(new String[]{""});
            writer.writeNext(new String[]{
                    "Média do Período",
                    String.format(Locale.US, "%.2f", media)
            });

            LOG.infof("📤 Enviando CSV ao S3: %s", nomeArquivo);

            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(BUCKET)
                            .key(nomeArquivo)
                            .build(),
                    RequestBody.fromString(sw.toString())
            );

            return nomeArquivo;

        } catch (IOException e) {
            LOG.error("❌ Erro ao gerar CSV", e);
            throw new RuntimeException(e);
        }
    }
}
