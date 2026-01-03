package org.fiap.com;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.fiap.com.services.FeedbackService;
import org.jboss.logging.Logger;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;

@ApplicationScoped
public class LambdaHandler implements RequestHandler<Object, String> {

    private static final Logger LOG = Logger.getLogger(LambdaHandler.class);

    private final FeedbackService service;

    @Inject
    public LambdaHandler(FeedbackService service){
        this.service = service;
    }

    @Override
    public String handleRequest(Object input, Context context) {

        LOG.info("🚀 Lambda acionada pelo EventBridge");
        LOG.infof("📥 Payload recebido: %s", input);

        LocalDate hoje = LocalDate.now(ZoneId.of("America/Sao_Paulo"));

        LocalDate inicioSemanaAnterior =
                hoje.minusWeeks(1).with(DayOfWeek.MONDAY);

        LocalDate fimSemanaAnterior =
                hoje.minusWeeks(1).with(DayOfWeek.SUNDAY);

        LOG.infof(
                "📅 Semana anterior -> Início: %s | Fim: %s",
                inicioSemanaAnterior,
                fimSemanaAnterior
        );

        service.gerarCsvSemanal(
                inicioSemanaAnterior,
                fimSemanaAnterior
        );

        LOG.info("✅ Execução da Lambda finalizada com sucesso");

        return "OK";
    }
}
